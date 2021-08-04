/*
 * MailSendConnectInfo.java
 * Created on 2019/01/22
 * RCSID: $Id$
 *
 * (C) Copyright NTT-F 2019 All rights reserved.
 *
 * This software is furnished under a contract and use, duplication, disclosure
 * and all other uses are restricted to the rights specified in the written
 * contract and memorandum between the contractor and NTT-F.
 */
package jp.co.nttf.gms.common.internal.mail;

/**
 * メール送信に必要な接続情報を格納するクラス. <br>
 * <b>【注意】</b> 特になし.
 *
 * @author sunnet
 */
public class MailSendConnectInfo {

    /**
     * プロトコル名。
     */
    private String protocol = null;

    /**
     * ホスト名。
     */
    private String host = null;

    /**
     * POP3ポート番号(-1を設定するとデフォルトのポートを使用する)。
     */
    private int pop3Port = -1;

    /**
     * SMTPポート番号（-1を指定するとデフォルトのポートを使用する）。
     */
    private int smtpPort = -1;

    /**
     * タイムアウト時間（-1を指定するとデフォルトのポートを使用する）。
     */
    private int timeout = -1;

    /**
     * ユーザ名。
     */
    private String user = null;

    /**
     * パスワード。
     */
    private String pass = null;

    /**
     * 送信元のメールアドレス。
     */
    private String from = null;

    /**
     * 送信元の名称。
     */
    private String sourceName = null;

    /**
     * <p>
     * コンストラクタ。
     * </p>
     */
    public MailSendConnectInfo() {
    }

    /**
     * <p>
     * プロトコルを取得。
     * </p>
     * 
     * @return protocol
     */
    public String getProtocol() {
        return protocol;
    }

    /**
     * <p>
     * プロトコルを設定。
     * </p>
     * 
     * @param aProtocol プロトコル
     */
    public void setProtocol(String aProtocol) {
        this.protocol = aProtocol;
    }

    /**
     * <p>
     * メールサーバの設定。
     * </p>
     * 
     * @param aHost メールサーバ名
     */
    public void setHost(String aHost) {
        host = aHost;
    }

    /**
     * <p>
     * メールサーバ名を返す。
     * </p>
     * 
     * @return host
     */
    public String getHost() {
        return host;
    }

    /**
     * <p>
     * ポート番号の設定。
     * </p>
     * 
     * @param aPort ポート番号
     */
    public void setPop3Port(int aPort) {
        pop3Port = aPort;
    }

    /**
     * <p>
     * ポート番号を返す。
     * </p>
     * 
     * @return pop3Port
     */
    public int getPop3Port() {
        return pop3Port;
    }

    /**
     * <p>
     * ユーザ名の設定。
     * </p>
     * 
     * @param aUser ユーザ名
     */
    public void setUser(String aUser) {
        user = aUser;
    }

    /**
     * <p>
     * ユーザ名を返す。
     * </p>
     * 
     * @return user
     */
    public String getUser() {
        return user;
    }

    /**
     * <p>
     * パスワードの設定。
     * </p>
     * 
     * @param aPass パスワード
     */
    public void setPass(String aPass) {
        pass = aPass;
    }

    /**
     * <p>
     * パスワードを返す。
     * </p>
     * 
     * @return pass
     */
    public String getPass() {
        return pass;
    }

    /**
     * <p>
     * 送信元のメールアドレス設定。
     * </p>
     * 
     * @param aFrom 送信元のメールアドレス
     */
    public void setFrom(String aFrom) {
        from = aFrom;
    }

    /**
     * <p>
     * 送信元のアドレスを返す。
     * </p>
     * 
     * @return from
     */
    public String getFrom() {
        return from;
    }

    /**
     * <p>
     * SMTPのポート番号を取得。
     * </p>
     * 
     * @return smtpPortを戻します。
     */
    public int getSmtpPort() {
        return smtpPort;
    }

    /**
     * <p>
     * SMTPのポート番号を設定。
     * </p>
     * 
     * @param smtpPort smtpPortを設定。
     */
    public void setSmtpPort(int smtpPort) {
        this.smtpPort = smtpPort;
    }

    /**
     * <p>
     * 送信元の名称を取得。
     * </p>
     * 
     * @return 送信元の名称を戻します。
     */
    public String getSourceName() {
        return sourceName;
    }

    /**
     * <p>
     * 送信元の名称を設定。
     * </p>
     * 
     * @param sourceName 送信元の名称
     */
    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    /**
     * <p>
     * タイムアウト時間を取得。
     * </p>
     * 
     * @return タイムアウト時間を戻します。
     */
    public int getTimeout() {
        return timeout;
    }

    /**
     * <p>
     * タイムアウト時間を設定。
     * </p>
     * 
     * @param timeout タイムアウト時間
     */
    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }
}
