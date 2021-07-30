/*
 * MailSendContents.java
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
 * メールの内容（サブジェクト、ボディ、文字コード）を格納するクラス. <br>
 * <b>【注意】</b> 特になし.
 *
 * @author sunnet
 */
public class MailSendContents {

    /**
     * ボディ。
     */
    private String body = null;

    /**
     * サブジェクト。
     */
    private String subject = null;

    /**
     * 変換に使う文字コード。
     */
    private String charCode = null;

    /**
     * <p>
     * コンストラクタ。
     * </p>
     */
    public MailSendContents() {
    }

    /**
     * <p>
     * サブジェクトの設定。
     * </p>
     * 
     * @param aSubject サブジェクト
     */
    public void setSubject(String aSubject) {
        subject = aSubject;
    }

    /**
     * <p>
     * サブジェクトを返す。
     * </p>
     * 
     * @return subject
     */
    public String getSubject() {
        return subject;
    }

    /**
     * <p>
     * 文字コードを取得。
     * </p>
     * 
     * @return charCode
     */
    public String getCharCode() {
        return charCode;
    }

    /**
     * <p>
     * 文字コードを設定。
     * </p>
     * 
     * @param aCharCode 文字コード
     */
    public void setCharCode(String aCharCode) {
        this.charCode = aCharCode;
    }

    /**
     * <p>
     * ボディを取得。
     * </p>
     * 
     * @return body
     */
    public String getBody() {
        return body;
    }

    /**
     * <p>
     * ボディを設定。
     * </p>
     * 
     * @param aBody ボディ。
     */
    public void setBody(String aBody) {
        this.body = aBody;
    }
}
