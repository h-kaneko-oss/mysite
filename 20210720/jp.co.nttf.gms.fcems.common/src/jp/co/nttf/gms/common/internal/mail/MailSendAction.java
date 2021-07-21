/*
 * MailSendAction.java
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

import java.util.Date;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import jp.co.nttf.gms.common.internal.AppLogger;
import jp.co.nttf.gms.common.internal.data.gmsdb.MGmu2mail;

import org.apache.commons.lang3.StringUtils;

/**
 * 与えられたパラメータでメールを作成し送信する。<br>
 * このクラスはJavaMail1.4.4(mail.jar)を使用する。<br>
 * このクラスでサポートする、メール送信プロトコルは「SMTP」と「Pop before SMTP」「SMTPS」です。<br>
 *
 * @author sunnet
 */
public final class MailSendAction {

    /**
     * プロトコル名称(Pop before SMTP)。
     */
    public static final String POP_BEFORE_SMTP = "pop before smtp";

    /**
     * プロトコル名称(SMTP)。
     */
    public static final String SMTP = "smtp";

    /**
     * プロトコル名称(POP3)。
     */
    public static final String POP3 = "pop3";

    /**
     * プロトコル名称(SMTPS)。
     */
    public static final String SMTPS = "smtps";

    /** ロガー */
    private static final AppLogger LOGGER = new AppLogger(MailSendAction.class);

    /**
     * セッションオブジェクト。
     */
    private Session session;

    /**
     * トランスポートオブジェクト。
     */
    private Transport transport;

    /**
     * <p>
     * デフォルトコンストラクタ。
     * </p>
     */
    public MailSendAction() {
    }

    /**
     * <p>
     * 受け取ったパラメータで、Textタイプメールを作成し、BCCで送信する。
     * </p>
     * 
     * @param msConnect 送信情報
     * @param msContent 送信内容
     * @param mail 送信先のメールアドレス
     * @throws Exception メールの送信に関する例外
     */
    public void simpleSenderBcc(MailSendConnectInfo msConnect,
            MailSendContents msContent, MGmu2mail mail) throws Exception {
        try {
            createConnect(msConnect);
            MimeMessage msg = new MimeMessage(session);
            InternetAddress ia = new InternetAddress(msConnect.getFrom(),
                    msConnect.getSourceName());
            LOGGER.debug("送信元メールアドレス：" + msConnect.getFrom() + ",送信元名称："
                    + msConnect.getSourceName());
            msg.setFrom(ia);
            msg.setSubject(msContent.getSubject(), msContent.getCharCode());
            msg.setText(msContent.getBody(), msContent.getCharCode());

            // 送信日時の設定
            msg.setSentDate(new Date());
            msg.saveChanges();

            // ユーザが空白で設定した場合、「null」を使う
            String userName = StringUtils.defaultIfEmpty(msConnect.getUser(),
                    null);
            // パスワードが空白で設定した場合、「null」を使う
            String password = StringUtils.defaultIfEmpty(msConnect.getPass(),
                    null);
            // メールサーバに接続する
            transport.connect(msConnect.getHost(), msConnect.getSmtpPort(),
                    userName, password);

            // 送信をアドレスが入っている分だけ繰り返す
            Address mailAddress = new InternetAddress(mail.getMailaddress(),
                    mail.getName());
            msg.setRecipient(Message.RecipientType.BCC, mailAddress);
            // メールを送信する
            transport.sendMessage(msg, new Address[] {mailAddress});
            LOGGER.debug("送信先メールアドレス：" + mail.getMailaddress());
        } finally {
            // メールサーバから切断する
            if (transport != null && transport.isConnected()) {
                transport.close();
            }
        }
    }

    /**
     * <p>
     * セッションとトランスポートを生成する。
     * </p>
     * 
     * @param msConnect 接続に関数情報
     * @throws MessagingException 接続ができない場合、指定のプロトコルでない場合
     */
    private void createConnect(MailSendConnectInfo msConnect)
            throws MessagingException {
        Properties prop = new Properties();

        if (POP_BEFORE_SMTP.equalsIgnoreCase(msConnect.getProtocol())) {
            prop.put("mail.pop3.connectiontimeout", msConnect.getTimeout());
            Session popSession = Session.getInstance(prop, null);
            Store store = popSession.getStore(POP3);
            store.connect(msConnect.getHost(), msConnect.getPop3Port(),
                    msConnect.getUser(), msConnect.getPass());
            store.close();
            session = Session.getInstance(prop, null);
            transport = session.getTransport(SMTP);
        } else if (SMTP.equalsIgnoreCase(msConnect.getProtocol())) {
            prop.put("mail.smtp.connectiontimeout", msConnect.getTimeout());
            session = Session.getInstance(prop, null);
            transport = session.getTransport(SMTP);
        } else if (SMTPS.equalsIgnoreCase(msConnect.getProtocol())) {
            prop.put("mail.smtps.connectiontimeout", msConnect.getTimeout());
            session = Session.getInstance(prop, null);
            transport = session.getTransport(SMTPS);
        } else {
            throw new NoSuchProviderException();
        }
    }
}
