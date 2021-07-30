/*
 * GmuSSLUtil.java
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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.util.Arrays;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

import org.apache.commons.httpclient.protocol.ProtocolSocketFactory;

import jp.co.nttf.gms.common.internal.AppLogger;

/**
 * GmuSSLUtilクラス. <br>
 * SSLを利用して接続を行うための、ユーティリティクラス. <br>
 * <b>【注意】</b> 特になし.
 *
 * @author naya
 */
public final class GmuSSLUtil {

    /** ロガー */
    private static final AppLogger LOGGER = new AppLogger(GmuSSLUtil.class);

    /**
     * コンストラクタ. <br>
     * <b>【注意】</b> このクラスのインスタンスが生成されることを防ぐ役割を持つ。
     */
    private GmuSSLUtil() {
    }

    /**
     * ProtocolSocketFactoryを取得するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param keyManager KeyManager
     * @param trustManager TrustManager
     * @return ProtocolSocketFactory
     * @throws Exception SocketFactoryが生成できなかった場合
     */
    public static ProtocolSocketFactory getProtocolSocketFactory(
            KeyManager[] keyManager, TrustManager[] trustManager)
            throws Exception {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("method start. keyManager=<" + Arrays.toString(keyManager)
                    + "> trustManager=<" + Arrays.toString(trustManager) + ">");
        }

        ProtocolSocketFactory result = new SSLProtocolSocketFactoryImpl(
                keyManager, trustManager);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("method end. result=<" + result + ">");
        }
        return result;
    }

    /**
     * SSLSocketFactoryを取得するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param keyManager KeyManager
     * @param trustManager TrustManager
     * @return SSLSocketFactory
     * @throws Exception SocketFactoryが生成できなかった場合
     */
    public static SSLSocketFactory getSSLSocketFactory(KeyManager[] keyManager,
            TrustManager[] trustManager) throws Exception {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("method start. keyManager=<" + Arrays.toString(keyManager)
                    + "> trustManager=<" + Arrays.toString(trustManager) + ">");
        }

        SSLContext sslContext = SSLContext.getInstance("SSL");
        sslContext.init(keyManager, trustManager, null);

        // 用意したcontextからSSLSocketFactoryを生成
        SSLSocketFactory factory = sslContext.getSocketFactory();

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("method end. result=<" + factory + ">");
        }
        return factory;
    }

    /**
     * KeyManagerを取得するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param storeData キーストアデータ
     * @param password キーストアのパスワード
     * @param keyPassword 秘密鍵パスワード
     * @return 取得したKeyManagerの配列
     * @throws GeneralSecurityException キーストアの読込みに失敗した場合
     * @throws IOException キーストアの読込みに失敗した場合
     */
    public static KeyManager[] getKeyManagers(byte[] storeData, String password, String keyPassword)
            throws GeneralSecurityException, IOException {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("method start. storeData=<" + Arrays.toString(storeData) + ">");
        }

        // キーストアを取得
        final KeyStore keyStore = getKeyStore(storeData, password.toCharArray());

        // キーストアが取得できなかった場合は例外をスロー
        if (keyStore == null) {
            throw new GeneralSecurityException("Could not get Keystore.");
        }

        // KeyManagerFactoryを初期化し、KeyManagerを取得
        final String algorithm = KeyManagerFactory.getDefaultAlgorithm();
        final KeyManagerFactory kmf = KeyManagerFactory.getInstance(algorithm);
        kmf.init(keyStore, keyPassword.toCharArray());

        KeyManager[] result = kmf.getKeyManagers();

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("method end. result=<" + Arrays.toString(result) + ">");
        }
        return result;

    }

    /**
     * TrustManagerを取得するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param storeData トラストストアデータ
     * @param password トラストストアのパスワード
     * @return 取得したTrustManagerの配列
     * @throws GeneralSecurityException キーストアの読込みに失敗した場合.
     * @throws IOException キーストアの読込みに失敗した場合.
     */
    public static TrustManager[] getTrustManagers(byte[] storeData, String password)
            throws GeneralSecurityException, IOException {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("method start. storeData=<" + Arrays.toString(storeData) + ">");
        }

        // キーストアを取得
        KeyStore keyStore = getKeyStore(storeData, password.toCharArray());

        // デフォルトアルゴリズム取得
        final String algorithm = TrustManagerFactory.getDefaultAlgorithm();

        // TrustManagerFactroyを初期化しTrustManagerを取得
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(algorithm);
        tmf.init(keyStore);

        TrustManager[] result = tmf.getTrustManagers();

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("method end. result=<" + Arrays.toString(result) + ">");
        }
        return result;

    }

    /**
     * KeyStoreを取得するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param storeData トラストストアデータ
     * @param passwd KeyStoreのパスワード
     * @return 取得したキーストア
     * @throws GeneralSecurityException キーストアの読込みに失敗した場合
     * @throws IOException キーストアの読込みに失敗した場合
     */
    private static KeyStore getKeyStore(final byte[] storeData,
            final char[] passwd) throws GeneralSecurityException, IOException {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("method start. storeData=<" + Arrays.toString(storeData)
                    + "> passwd=<" + new String(passwd) + ">");
        }

        // ファイル名がnullでない場合
        InputStream in = new ByteArrayInputStream(storeData);
        try {
            // キーストアインスタンスの取得
            KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());

            // キーストアのload
            keystore.load(in, passwd);

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("method end. result=<" + keystore + ">");
            }
            return keystore;

        } finally {
            if (in != null) {
                in.close();
            }
        }
    }
}
