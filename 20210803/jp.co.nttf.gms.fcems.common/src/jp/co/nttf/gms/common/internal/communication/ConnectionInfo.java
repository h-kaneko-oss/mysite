/*
 * ConnectionInfo.java
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

import javax.net.ssl.KeyManager;
import javax.net.ssl.TrustManager;

/**
 * コネクション情報を保持するクラス. <br>
 * <b>【注意】</b> 特になし.
 *
 * @author naya
 */
public final class ConnectionInfo {

    /** コネクションタイムアウト(単位：ミリ秒) */
    private int httpConnetionTimeoutMsec;

    /** ソケットタイムアウト(単位：ミリ秒) */
    private int httpSocketTimeoutMsec;

    /** GMUID */
    private String gmuId;

    /** 接続先URL */
    private String serverUrl;

    /** Basic認証ユーザ */
    private String authUser;

    /** Basic認証パスワード */
    private String authPassword;

    /** プロキシ有効無効フラグ */
    private boolean isProxyEnable;

    /** プロキシ情報 */
    private ProxyInfo proxyInfo;

    /** 最大リトライ回数 */
    private int maxRetryCount;

    /** リトライ間隔(単位：秒) */
    private int retryIntervalSecond;

    /** SSL有効無効フラグ */
    private boolean isSslModeOn;

    /** 送信実行最大待機時間(単位：秒) */
    private int maxStandbyTime;

    /** キーストア */
    private KeyManager[] keyManager = new KeyManager[]{};

    /** トラストストア */
    private TrustManager[] trustManager = new TrustManager[]{};

    /**
     * httpConnetionTimeoutMsecを取得するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @return httpConnetionTimeoutMsec
     */
    public int getHttpConnetionTimeoutMsec() {
        return httpConnetionTimeoutMsec;
    }

    /**
     * httpConnetionTimeoutMsecを設定するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param httpConnetionTimeoutMsec コネクションタイムアウト
     */
    public void setHttpConnetionTimeoutMsec(int httpConnetionTimeoutMsec) {
        this.httpConnetionTimeoutMsec = httpConnetionTimeoutMsec;
    }

    /**
     * httpSocketTimeoutMsecを取得するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @return httpSocketTimeoutMsec ソケットタイムアウト
     */
    public int getHttpSocketTimeoutMsec() {
        return httpSocketTimeoutMsec;
    }

    /**
     * httpSocketTimeoutMsecを設定するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param httpSocketTimeoutMsec httpソケットタイムアウト
     */
    public void setHttpSocketTimeoutMsec(int httpSocketTimeoutMsec) {
        this.httpSocketTimeoutMsec = httpSocketTimeoutMsec;
    }

    /**
     * gmuIdを取得するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @return gmuId GMUID
     */
    public String getGmuId() {
        return gmuId;
    }

    /**
     * gmuIdを設定するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param gmuId GMUID
     */
    public void setGmuId(String gmuId) {
        this.gmuId = gmuId;
    }

    /**
     * serverUrlを取得するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @return serverUrl 接続先サーバURL
     */
    public String getServerUrl() {
        return serverUrl;
    }

    /**
     * serverUrlを設定するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param serverUrl 接続先サーバURL
     */
    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    /**
     * authUserを取得するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @return authUser ユーザ名
     */
    public String getAuthUser() {
        return authUser;
    }

    /**
     * authUserを設定するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param authUser ユーザ名
     */
    public void setAuthUser(String authUser) {
        this.authUser = authUser;
    }

    /**
     * authPasswordを取得するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @return authPassword
     */
    public String getAuthPassword() {
        return authPassword;
    }

    /**
     * authPasswordを設定するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param authPassword パスワード
     */
    public void setAuthPassword(String authPassword) {
        this.authPassword = authPassword;
    }

    /**
     * isProxyEnableを取得するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @return isProxyEnable
     */
    public boolean isProxyEnable() {
        return isProxyEnable;
    }

    /**
     * isProxyEnableを設定するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param isProxyEnable プロキシ可否フラグ
     */
    public void setProxyEnable(boolean isProxyEnable) {
        this.isProxyEnable = isProxyEnable;
    }

    /**
     * proxyInfoを取得するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @return proxyInfo
     */
    public ProxyInfo getProxyInfo() {
        return proxyInfo;
    }

    /**
     * proxyInfoを設定するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param proxyInfo プロキシ情報クラス
     */
    public void setProxyInfo(ProxyInfo proxyInfo) {
        this.proxyInfo = proxyInfo;
    }

    /**
     * maxRetryCountを取得するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @return maxRetryCount
     */
    public int getMaxRetryCount() {
        return maxRetryCount;
    }

    /**
     * maxRetryCountを設定するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param maxRetryCount リトライ関数
     */
    public void setMaxRetryCount(int maxRetryCount) {
        this.maxRetryCount = maxRetryCount;
    }

    /**
     * retryIntervalSecondを取得するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @return retryIntervalSecond
     */
    public int getRetryIntervalSecond() {
        return retryIntervalSecond;
    }

    /**
     * retryIntervalSecondを設定するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param retryIntervalSecond リトライ間隔
     */
    public void setRetryIntervalSecond(int retryIntervalSecond) {
        this.retryIntervalSecond = retryIntervalSecond;
    }

    /**
     * isSslModeOnを取得するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @return isSslModeOn
     */
    public boolean isSslModeOn() {
        return isSslModeOn;
    }

    /**
     * isSslModeOnを設定するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param isSslModeOn SSLモード可否フラグ
     */
    public void setSslModeOn(boolean isSslModeOn) {
        this.isSslModeOn = isSslModeOn;
    }

    /**
     * maxStandbyTimeを取得するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @return maxStandbyTime
     */
    public int getMaxStandbyTime() {
        return maxStandbyTime;
    }

    /**
     * maxStandbyTimeを設定するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param maxStandbyTime 待機時間
     */
    public void setMaxStandbyTime(int maxStandbyTime) {
        this.maxStandbyTime = maxStandbyTime;
    }

    /**
     * keyManagerを取得するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @return keyManager
     */
    public KeyManager[] getKeyManager() {
        return keyManager.clone();
    }

    /**
     * keyManagerを設定するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param keyManager キーマネージャー
     */
    public void setKeyManager(KeyManager[] keyManager) {
        this.keyManager = keyManager.clone();
    }

    /**
     * trustManagerを取得するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @return trustManager
     */
    public TrustManager[] getTrustManager() {
        return trustManager.clone();
    }

    /**
     * trustManagerを設定するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param trustManager トラストマネージャ
     */
    public void setTrustManager(TrustManager[] trustManager) {
        this.trustManager = trustManager.clone();
    }

    /**
     * 文字列表現を返すメソッド. <br>
     * <b>【注意】</b> 特になし。
     *
     * @return オブジェクトの文字列表現
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        final String tab = "    ";

        StringBuilder buffer = new StringBuilder();

        buffer.append("ConnectionInfo ( ");
        buffer.append(super.toString());
        buffer.append(tab).append("httpConnetionTimeoutMsec = ").append(this.httpConnetionTimeoutMsec);
        buffer.append(tab).append("httpSocketTimeoutMsec = ").append(this.httpSocketTimeoutMsec);
        buffer.append(tab).append("gmuId = ").append(this.gmuId);
        buffer.append(tab).append("serverUrl = ").append(this.serverUrl);
        buffer.append(tab).append("authUser = ").append(this.authUser);
        buffer.append(tab).append("authPassword = ").append(this.authPassword);
        buffer.append(tab).append("isProxyEnable = ").append(this.isProxyEnable);
        buffer.append(tab).append("proxyInfo = ").append(this.proxyInfo);
        buffer.append(tab).append("maxRetryCount = ").append(this.maxRetryCount);
        buffer.append(tab).append("retryIntervalSecond = ").append(this.retryIntervalSecond);
        buffer.append(tab).append("isSslModeOn = ").append(this.isSslModeOn);
        buffer.append(" )");

        return buffer.toString();
    }

    /**
     * プロキシ情報を保持するクラス. <br>
     * <b>【注意】</b> 特になし.
     *
     * @author naya
     */
    public static final class ProxyInfo {

        /** ホスト */
        private String host;

        /** ポート */
        private int port;

        /** 認証ユーザ */
        private String authUser;

        /** 認証パスワード */
        private String authPassword;

        /**
         * hostを取得するメソッド. <br>
         * <b>【注意】</b> 特になし.
         *
         * @return host
         */
        public String getHost() {
            return host;
        }

        /**
         * hostを設定するメソッド. <br>
         * <b>【注意】</b> 特になし.
         *
         * @param host プロキシホスト
         */
        public void setHost(String host) {
            this.host = host;
        }

        /**
         * portを取得するメソッド. <br>
         * <b>【注意】</b> 特になし.
         *
         * @return port
         */
        public int getPort() {
            return port;
        }

        /**
         * portを設定するメソッド. <br>
         * <b>【注意】</b> 特になし.
         *
         * @param port プロキシポート
         */
        public void setPort(int port) {
            this.port = port;
        }

        /**
         * authUserを取得するメソッド. <br>
         * <b>【注意】</b> 特になし.
         *
         * @return authUser
         */
        public String getAuthUser() {
            return authUser;
        }

        /**
         * authUserを設定するメソッド. <br>
         * <b>【注意】</b> 特になし.
         *
         * @param authUser プロキシユーザ
         */
        public void setAuthUser(String authUser) {
            this.authUser = authUser;
        }

        /**
         * authPasswordを取得するメソッド. <br>
         * <b>【注意】</b> 特になし.
         *
         * @return authPassword
         */
        public String getAuthPassword() {
            return authPassword;
        }

        /**
         * authPasswordを設定するメソッド. <br>
         * <b>【注意】</b> 特になし.
         *
         * @param authPassword プロキシパスワード
         */
        public void setAuthPassword(String authPassword) {
            this.authPassword = authPassword;
        }

        /**
         * 文字列表現を返すメソッド. <br>
         * <b>【注意】</b> 特になし。
         *
         * @return オブジェクトの文字列表現
         * @see java.lang.Object#toString()
         */
        @Override
        public String toString() {
            final String tab = "    ";

            StringBuilder buffer = new StringBuilder();

            buffer.append("ProxyInfo ( ");
            buffer.append(super.toString());
            buffer.append(tab).append("host = ").append(this.host);
            buffer.append(tab).append("port = ").append(this.port);
            buffer.append(tab).append("authUser = ").append(this.authUser);
            buffer.append(tab).append("authPassword = ").append(
                    this.authPassword);
            buffer.append(" )");

            return buffer.toString();
        }
    }
}
