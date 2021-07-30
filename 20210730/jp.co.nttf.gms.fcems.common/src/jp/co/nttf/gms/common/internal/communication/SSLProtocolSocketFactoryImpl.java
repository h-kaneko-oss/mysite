/*
 * SSLProtocolSocketFactoryImpl.java
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
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

import org.apache.commons.httpclient.params.HttpConnectionParams;
import org.apache.commons.httpclient.protocol.SecureProtocolSocketFactory;

import jp.co.nttf.gms.common.internal.AppLogger;

/**
 * SSLProtocolSocketFactoryImplクラス. <br>
 * ProtocolSocketFactoryの実装クラスであり、GmuSSLUtilクラスで生成される. <br>
 * <b>【注意】</b> 特になし.
 *
 * @author naya
 */
public final class SSLProtocolSocketFactoryImpl implements
                                               SecureProtocolSocketFactory {

    /** ロガー */
    private static final AppLogger LOGGER = new AppLogger(SSLProtocolSocketFactoryImpl.class);

    /** ソケット */
    private final SSLSocketFactory socketFactory;

    /**
     * コンストラクタ. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param keyManager KeyManager
     * @param trustManager TrustManager
     * @throws Exception SocketFactoryが生成できなかった場合
     */
    public SSLProtocolSocketFactoryImpl(KeyManager[] keyManager,
            TrustManager[] trustManager) throws Exception {
        this.socketFactory = GmuSSLUtil.getSSLSocketFactory(keyManager,
                trustManager);
    }

    /**
     * createSocketメソッド. <br>
     * ソケットを生成する。<br>
     * <b>【注意】</b> 特になし.
     *
     * @param host 接続先ホスト
     * @param port 接続先ポート
     * @param localAddress ローカルアドレス
     * @param localPort ローカルポート
     * @param params HttpConnectionパラメータ
     * @return 作成したSocket
     * @throws IOException 以下のケースの場合に例外をスローする.
     *         <ul>
     *         <li>キーストアデータに入出力または形式の問題があった場合
     *         <li>ホストが見つからない場合
     *         <li>接続がタイムアウトした場合
     *         </ul>
     * @see org.apache.commons.httpclient.protocol.ProtocolSocketFactory#createSocket(java.lang.String,
     *      int, java.net.InetAddress, int,
     *      org.apache.commons.httpclient.params.HttpConnectionParams)
     */
    @Override
    public Socket createSocket(String host, int port, InetAddress localAddress,
            int localPort, HttpConnectionParams params) throws IOException {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("method start. host=<" + host + "> port=<" + port
                    + "> localAddress=<" + localAddress + "> localPort=<"
                    + localPort + "> params=<" + params + ">");
        }

        Socket socket = this.socketFactory.createSocket();

        InetSocketAddress clientInetSocketAddress = new InetSocketAddress(
                localAddress, localPort);
        InetSocketAddress serverInetSocketAddress = new InetSocketAddress(host,
                port);

        int connectionTimeout = params.getConnectionTimeout();
        int soTimeout = params.getSoTimeout();

        socket.setSoTimeout(soTimeout);
        socket.bind(clientInetSocketAddress);
        socket.connect(serverInetSocketAddress, connectionTimeout);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("method end. result=<" + socket + ">");
        }
        return socket;

    }

    /**
     * createSocketメソッド. <br>
     * ソケットを生成する。<br>
     * <b>【注意】</b> 特になし.
     *
     * @param host 接続先ホスト
     * @param port 接続先ポート
     * @param clientHost クライアントアドレス
     * @param clientPort ローカルポート
     * @return 作成したSocket
     * @throws IOException 以下のケースの場合に例外をスローする.
     *         <ul>
     *         <li>キーストアデータに入出力または形式の問題があった場合
     *         <li>ホストが見つからない場合
     *         </ul>
     * @see org.apache.commons.httpclient.protocol.ProtocolSocketFactory#createSocket(java.lang.String,
     *      int, java.net.InetAddress, int)
     */
    @Override
    public Socket createSocket(String host, int port, InetAddress clientHost,
            int clientPort) throws IOException {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("method start. host=<" + host + "> port=<" + port
                    + "> clientHost=<" + clientHost + "> clientPort=<"
                    + clientPort + ">");
        }

        Socket socket = this.socketFactory.createSocket(host, port, clientHost,
                clientPort);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("method end. result=<" + socket + ">");
        }
        return socket;

    }

    /**
     * createSocketメソッド. <br>
     * ソケットを生成する。<br>
     * <b>【注意】</b> 特になし.
     *
     * @param host 接続先ホスト
     * @param port 接続先ポート
     * @return 作成したSocket
     * @throws IOException 以下のケースの場合に例外をスローする.
     *         <ul>
     *         <li>キーストアデータに入出力または形式の問題があった場合
     *         <li>ホストが見つからない場合
     *         </ul>
     * @see org.apache.commons.httpclient.protocol.ProtocolSocketFactory#createSocket(java.lang.String,
     *      int)
     */
    @Override
    public Socket createSocket(String host, int port) throws IOException {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("method start. host=<" + host + "> port=<" + port + ">");
        }

        Socket socket = this.socketFactory.createSocket(host, port);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("method end. result=<" + socket + ">");
        }
        return socket;

    }

    /**
     * createSocketメソッド. <br>
     * ソケットを生成する。<br>
     * <b>【注意】</b> 特になし.
     *
     * @param socket ソケット
     * @param host 接続先ホスト
     * @param port 接続先ポート
     * @param autoClose 自動Close
     * @return 生成したSocket
     * @throws IOException 以下のケースの場合に例外をスローする.
     *         <ul>
     *         <li>キーストアデータに入出力または形式の問題があった場合
     *         <li>ホストが見つからない場合
     *         </ul>
     * @see org.apache.commons.httpclient.protocol.SecureProtocolSocketFactory#createSocket(java.net.Socket,
     *      java.lang.String, int, boolean)
     */
    @Override
    public Socket createSocket(Socket socket, String host, int port,
            boolean autoClose) throws IOException {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("method start. socket=<" + socket + "> host=<" + host
                    + "> port=<" + port + "> autoClose=<" + autoClose + ">");
        }

        Socket newSocket = this.socketFactory.createSocket(socket, host, port,
                autoClose);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("method end. result=<" + socket + ">");
        }
        return newSocket;

    }
}
