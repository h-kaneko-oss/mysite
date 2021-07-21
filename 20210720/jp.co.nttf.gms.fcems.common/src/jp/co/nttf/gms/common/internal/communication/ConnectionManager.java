/*
 * ConnectionManager.java
 * Created on 2014/12/09
 * RCSID: $Id$
 *
 * (C) Copyright NTT-F 2014 All rights reserved.
 *
 * This software is furnished under a contract and use, duplication, disclosure
 * and all other uses are restricted to the rights specified in the written
 * contract and memorandum between the contractor and NTT-F.
 */
package jp.co.nttf.gms.common.internal.communication;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HostConfiguration;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpConnectionManager;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpState;
import org.apache.commons.httpclient.SimpleHttpConnectionManager;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.commons.httpclient.protocol.Protocol;

import jp.co.nttf.gms.common.internal.AppLogger;
import jp.co.nttf.gms.common.internal.AppUtil;
import jp.co.nttf.gms.common.internal.CommonConstants;
import jp.co.nttf.gms.common.internal.CommonMessageID;
import jp.co.nttf.gms.common.internal.MessageUtil;

/**
 * 送信実行クラス. <br>
 * GMSへの送信を実行する。<br>
 * <b>【注意】</b> 特になし.
 *
 * @author naya
 */
public final class ConnectionManager {

    /** ロガー */
    private static final AppLogger LOGGER = new AppLogger(ConnectionManager.class);

    /** レスポンスコード：処理を継続する */
    private static final int[] RESPONSE_FINISH = {200};

    /** プロトコル：HTTPS */
    private static final String PROTOCOL_HTTPS = "https";

    /** プロトコル指定：HTTP */
    private static final String PROTOCOL_SETTING_HTTP = "http://";

    /** プロトコル指定：HTTPS */
    private static final String PROTOCOL_SETTING_HTTPS = "https://";

    /** デフォルトポート番号：HTTPS */
    private static final int DEFAULT_PORT_HTTPS = 443;

    /** デフォルトポート番号：HTTP */
    private static final int DEFAULT_PORT_HTTP = 80;

    /** コネクション情報 */
    private final ConnectionInfo connectionInfo;

    /** アクセス先URL */
    private final URL serverUrl;

    /**
     * コンストラクタ. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param connectionInfo 通信情報
     * @throws Exception インスタンス生成に失敗した場合
     */
    public ConnectionManager(ConnectionInfo connectionInfo)
            throws Exception {

        this.connectionInfo = connectionInfo;
        this.serverUrl = createUrlObject(connectionInfo.getServerUrl());
    }

    /**
     * コンストラクタ. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param connectionInfo 通信情報
     * @param serverUrl ダウンロードファイルのパス
     * @throws Exception インスタンス生成に失敗した場合
     */
    public ConnectionManager(ConnectionInfo connectionInfo, String serverUrl)
            throws Exception {

        this.connectionInfo = connectionInfo;
        this.serverUrl = createUrlObject(serverUrl);
    }

    /**
     * 送信実行メソッド. <br>
     * GMSへの送信処理を実行する。<br>
     * <b>【注意】</b> 特になし.
     *
     * @param method 送信情報で初期化されたメソッドオブジェクト
     * @throws Exception 送信に失敗した場合
     */
    public void sendExecute(HttpMethod method) throws Exception {

        // DEBUGログ（メソッドの先頭）
        if (LOGGER.isMethodInEnabled()) {
            LOGGER.methodIn("sendExecute", "method=" + method);
        }

        // 送信リトライ回数を初期化
        int sendRetryCount = 0;

        // HttpClient生成
        HttpClient httpClient = this.createHttpClient();

        // 送信処理ループ
        while (true) {

            int status = 0;

            try {
                // メソッドにパスを設定
                method.setPath(this.serverUrl.getPath());

                // メソッドに認証ありを設定
                method.setDoAuthentication(true);

                // 送信実行
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("execute method start. method=<" + method + ">");
                }
                status = httpClient.executeMethod(method);
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("execute method end. status=<" + status + ">");
                }

                // レスポンスコードが、「処理を継続する」の場合
                if (this.checkContainStatus(RESPONSE_FINISH, status)) {
                    // 正常ならループを抜ける
                    break;
                }

                // 異常なHTTPレスポンスコードを取得した場合はERRORログ出力
                LOGGER.error(MessageUtil.getMessage(
                        CommonMessageID.ECOM001, String.valueOf(status)));

            } catch (IOException e) {
                // 送信処理中に例外が発生した場合はERRORログ出力
                LOGGER.error(MessageUtil.getMessage(CommonMessageID.ECOM000), e);
            }

            // リトライチェック
            if (!this.isRetry(sendRetryCount, method)) {
                // リトライオーバーの場合は例外をスロー
                throw new Exception(MessageUtil.getMessage(
                        CommonMessageID.ECOM002, String.valueOf(sendRetryCount)));
            }
            sendRetryCount++;
            continue;
        }

    }

    /**
     * リトライを行うかを判定するメソッド.<br>
     * リトライをおこなう場合はリトライ間隔分待機する。 <br>
     * <b>【注意】</b> 特になし.
     *
     * @param sendRetryCount 現在のリトライ回数
     * @param method 送信情報で初期化されたメソッドオブジェクト
     * @return リトライを行う場合、trueを返す。
     * @throws InterruptedException 割り込みが発生した場合
     */
    private boolean isRetry(int sendRetryCount, HttpMethod method)
            throws InterruptedException {

        if (this.connectionInfo.getMaxRetryCount() <= sendRetryCount) {
            return false;
        }

        // リトライ回数がまだ上限に達していない場合
        // リトライ間隔分待機する
        long sleepTime = this.connectionInfo.getRetryIntervalSecond() * CommonConstants.INT_THOUSAND;
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("wait execute method start. sleepTime=<" + sleepTime + ">");
        }

        Thread.sleep(sleepTime);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("wait execute method end. sleepTime=<" + sleepTime + ">");
        }

        return true;
    }

    /**
     * createHttpClientメソッド. <br>
     * 接続に必要な情報をセットしてHttpClientオブジェクトを生成する。<br>
     * <b>【注意】</b> 特になし.
     *
     * @return 生成したHttpClientオブジェクト
     * @throws Exception HttpClientの生成に失敗した場合
     */
    private HttpClient createHttpClient() throws Exception {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("method start.");
        }

        // -------------------------------------------------
        // HttpClientオブジェクト作成
        // -------------------------------------------------
        SimpleHttpConnectionManager manager = new SimpleHttpConnectionManager(true);
        HttpClient newHttpClient = new HttpClient(manager);

        HttpClientParams clientParams = newHttpClient.getParams();
        HostConfiguration hostConfiguration = newHttpClient.getHostConfiguration();
        HttpState state = newHttpClient.getState();
        HttpConnectionManager connectionManager = newHttpClient.getHttpConnectionManager();
        this.setHostConfiguration(hostConfiguration);

        // -------------------------------------------------
        // Basic認証情報設定
        // -------------------------------------------------
        if (!AppUtil.isEmpty(this.connectionInfo.getAuthUser())) {
            // ユーザの設定があった場合のみセット
            clientParams.setAuthenticationPreemptive(true);
            AuthScope authScope = new AuthScope(hostConfiguration.getHost(), hostConfiguration.getPort());
            Credentials credentials = new UsernamePasswordCredentials(
                    this.connectionInfo.getAuthUser(),
                    this.connectionInfo.getAuthPassword());
            state.setCredentials(authScope, credentials);
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Credentials set. authScope=<" + authScope + "> credentials=<" + credentials + ">");
            }
        }

        // -------------------------------------------------
        // タイムアウト情報設定
        // -------------------------------------------------
        HttpConnectionManagerParams params = new HttpConnectionManagerParams();
        params.setConnectionTimeout(this.connectionInfo.getHttpConnetionTimeoutMsec());
        params.setSoTimeout(this.connectionInfo.getHttpConnetionTimeoutMsec());
        connectionManager.setParams(params);

        // -------------------------------------------------
        // プロキシ情報設定
        // -------------------------------------------------
        if (this.connectionInfo.isProxyEnable()) {

            String proxyHost = this.connectionInfo.getProxyInfo().getHost();
            int proxyPort = this.connectionInfo.getProxyInfo().getPort();

            // プロキシサーバ情報の設定
            hostConfiguration.setProxy(proxyHost, proxyPort);
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Proxy host set. proxyHost=<" + proxyHost + "> proxyPort=<" + proxyPort + ">");
            }

            // プロキシサーバ認証情報の設定
            String proxyUsername = this.connectionInfo.getProxyInfo().getAuthUser();
            String proxyPassword = this.connectionInfo.getProxyInfo().getAuthPassword();

            if (proxyUsername != null && proxyUsername.length() > 0) {
                Credentials proxyCredentials = new UsernamePasswordCredentials(proxyUsername, proxyPassword);
                AuthScope proxyAuthScope = new AuthScope(proxyHost, proxyPort);

                state.setProxyCredentials(proxyAuthScope, proxyCredentials);
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("Proxy credentials set. proxyAuthScope=<" + proxyAuthScope
                            + "> proxyCredentials=<" + proxyCredentials + ">");
                }
            }

        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("method end. result=<" + newHttpClient + ">");
        }

        return newHttpClient;

    }

    /**
     * getHostConfigurationメソッド. <br>
     * GMSに接続するHostConfigurationを取得する.<br>
     * <b>【注意】</b> 特になし.
     *
     * @param hostConfiguration 設定対象のHostConfigurationオブジェクト
     * @throws Exception SSL処理で問題が発生した場合
     */
    private void setHostConfiguration(HostConfiguration hostConfiguration)
            throws Exception {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("method start. hostConfiguration=<" + hostConfiguration + ">");
        }

        int port = this.serverUrl.getPort();

        if (this.connectionInfo.isSslModeOn()) {
            // SSL接続する場合

            if (this.serverUrl.getPort() == -1) {
                // ポート指定がない場合、WellKnownポートを使用
                port = DEFAULT_PORT_HTTPS;
            }

            // プロトコルを生成
            Protocol protocol = new Protocol(PROTOCOL_HTTPS, GmuSSLUtil
                    .getProtocolSocketFactory(connectionInfo.getKeyManager(),
                            connectionInfo.getTrustManager()), port);

            // ホスト・ポート・プロトコルを設定
            hostConfiguration.setHost(this.serverUrl.getHost(), port, protocol);

        } else {
            // SSL接続しない場合

            if (this.serverUrl.getPort() == -1) {
                // ポート指定がない場合、WellKnownポートを使用
                port = DEFAULT_PORT_HTTP;
            }

            // ホスト・ポート・プロトコルを設定
            hostConfiguration.setHost(this.serverUrl.getHost(), port);
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("method end.");
        }

    }

    /**
     * HTTPスターテスチェックメソッド. <br>
     * 対象のHTTPステータスに含まれるかどうかを判定する。<br>
     * <b>【注意】</b> 特になし.
     *
     * @param targetArray 対象のHTTPステータス
     * @param status 受信したステータス
     * @return 含まれる場合はtrue、含まれない場合はfalse
     */
    private boolean checkContainStatus(int[] targetArray, int status) {

        boolean ret = false;

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("method start. targetArray=<"
                    + Arrays.toString(targetArray) + "> status=<" + status + ">");
        }

        for (int i = 0; i < targetArray.length; i++) {
            if (targetArray[i] == status) {
                ret = true;
                break;
            }
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("method end. result=<" + ret + ">");
        }
        return ret;
    }

    /**
     * URLオブジェクトを生成するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param urlString URLの文字列表現
     * @return 生成されたURLオブジェクト、URL以上の場合はnull
     * @throws MalformedURLException URLオブジェクトの生成に失敗した場合
     */
    private URL createUrlObject(String urlString) throws MalformedURLException {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("method start. urlString=<" + urlString + ">");
        }

        URL urlObject;
        if (urlString.startsWith(PROTOCOL_SETTING_HTTP)
                || urlString.startsWith(PROTOCOL_SETTING_HTTPS)) {
            // プロトコル指定されている場合はそのまま使用
            urlObject = new URL(urlString);
        } else {
            // プロトコル指定されていない場合は仮に補完
            urlObject = new URL(PROTOCOL_SETTING_HTTP + urlString);
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("method end. result=<" + urlObject + ">");
        }
        return urlObject;

    }

}
