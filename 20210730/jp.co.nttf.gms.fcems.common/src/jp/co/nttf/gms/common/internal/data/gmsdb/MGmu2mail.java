/*
 * MGmu2mail.java
 * Created on 2019/01/22
 * RCSID: $Id$
 *
 * (C) Copyright NTT-F 2019 All rights reserved.
 *
 * This software is furnished under a contract and use, duplication, disclosure
 * and all other uses are restricted to the rights specified in the written
 * contract and memorandum between the contractor and NTT-F.
 */
package jp.co.nttf.gms.common.internal.data.gmsdb;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

/**
 * m_gmu2mailテーブルを表すクラス. <br>
 * <b>【注意】</b> 特になし.
 *
 * @author sunnet
 */
public class MGmu2mail {
    /** 送信する */
    public static final int DR_SEND_FLAG_TRANSMIT = 1;

    /** 送信しない */
    public static final int DR_SEND_FLAG_DONT_TRANSMIT = 0;

    /** GMU-ID */
    private String userid;

    /** No */
    private Integer no;

    /** 名前 */
    private String name;

    /** メールアドレス */
    private String mailaddress;

    /** 制御種別（CEMSDR)送信フラグ */
    private Integer cemsdrsend;

    /** 制御種別（設定反映)送信フラグ */
    private Integer settingsend;

    /** 制御種別（制御テスト)送信フラグ */
    private Integer testsend;

    /** 制御種別（一括登録)送信フラグ */
    private Integer bulksend;

    /** 制御種別（接点連携制御)送信フラグ */
    private Integer pointsend;

    /** 制御種別（再エネ制御)送信フラグ */
    private Integer reenergysend;

    /** 制御種別（FW再起動)送信フラグ */
    private Integer fwsend;

    /** 制御種別（DR拒否)送信フラグ */
    private Integer refusalsend;

    /** 記事欄 */
    private String note;

    /**
     * オブジェクトの文字列表現を返すメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @return このオブジェクトの文字列表現
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return new ReflectionToStringBuilder(this).toString();
    }

    /**
     * GMU-IDを取得するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @return GMU-ID
     */
    public String getUserid() {
        return userid;
    }

    /**
     * GMU-IDを設定するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param userid GMU-ID
     */
    public void setUserid(String userid) {
        this.userid = userid;
    }

    /**
     * Noを取得するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @return No
     */
    public Integer getNo() {
        return no;
    }

    /**
     * Noを設定するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param no No
     */
    public void setNo(Integer no) {
        this.no = no;
    }

    /**
     * 名前を取得するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @return 名前
     */
    public String getName() {
        return name;
    }

    /**
     * 名前を設定するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param name 名前
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * メールアドレスを取得するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @return メールアドレス
     */
    public String getMailaddress() {
        return mailaddress;
    }

    /**
     * メールアドレスを設定するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param mailaddress メールアドレス
     */
    public void setMailaddress(String mailaddress) {
        this.mailaddress = mailaddress;
    }

    /**
     * 制御種別（CEMSDR)送信フラグを取得するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @return 制御種別（CEMSDR)送信フラグ
     */
    public Integer getCemsdrsend() {
        return cemsdrsend;
    }

    /**
     * 制御種別（CEMSDR)送信フラグを設定するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param cemsdrsend 制御種別（CEMSDR)送信フラグ
     */
    public void setCemsdrsend(Integer cemsdrsend) {
        this.cemsdrsend = cemsdrsend;
    }

    /**
     * 制御種別（設定反映)送信フラグを取得するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @return 制御種別（設定反映)送信フラグ
     */
    public Integer getSettingsend() {
        return settingsend;
    }

    /**
     * 制御種別（設定反映)送信フラグを設定するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param settingsend 制御種別（設定反映)送信フラグ
     */
    public void setSettingsend(Integer settingsend) {
        this.settingsend = settingsend;
    }

    /**
     * 制御種別（制御テスト)送信フラグを取得するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @return 制御種別（制御テスト)送信フラグ
     */
    public Integer getTestsend() {
        return testsend;
    }

    /**
     * 制御種別（制御テスト)送信フラグを設定するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param testsend 制御種別（制御テスト)送信フラグ
     */
    public void setTestsend(Integer testsend) {
        this.testsend = testsend;
    }

    /**
     * 制御種別（一括登録)送信フラグを取得するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @return 制御種別（一括登録)送信フラグ
     */
    public Integer getBulksend() {
        return bulksend;
    }

    /**
     * 制御種別（一括登録)送信フラグを設定するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param bulksend 制御種別（一括登録)送信フラグ
     */
    public void setBulksend(Integer bulksend) {
        this.bulksend = bulksend;
    }

    /**
     * 制御種別（接点連携制御)送信フラグを取得するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @return 制御種別（接点連携制御)送信フラグ
     */
    public Integer getPointsend() {
        return pointsend;
    }

    /**
     * 制御種別（接点連携制御)送信フラグを設定するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param pointsend 制御種別（接点連携制御)送信フラグ
     */
    public void setPointsend(Integer pointsend) {
        this.pointsend = pointsend;
    }

    /**
     * 制御種別（再エネ制御)送信フラグを取得するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @return 制御種別（再エネ制御)送信フラグ
     */
    public Integer getReenergysend() {
        return reenergysend;
    }

    /**
     * 制御種別（再エネ制御)送信フラグを設定するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param reenergysend 制御種別（再エネ制御)送信フラグ
     */
    public void setReenergysend(Integer reenergysend) {
        this.reenergysend = reenergysend;
    }

    /**
     * 制御種別（FW再起動)送信フラグを取得するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @return 制御種別（FW再起動)送信フラグ
     */
    public Integer getFwsend() {
        return fwsend;
    }

    /**
     * 制御種別（FW再起動)送信フラグを設定するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param fwsend 制御種別（FW再起動)送信フラグ
     */
    public void setFwsend(Integer fwsend) {
        this.fwsend = fwsend;
    }

    /**
     * 制御種別（DR拒否)送信フラグを取得するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @return 制御種別（DR拒否)送信フラグ
     */
    public Integer getRefusalsend() {
        return refusalsend;
    }

    /**
     * 制御種別（DR拒否)送信フラグを設定するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param refusalsend 制御種別（DR拒否)送信フラグ
     */
    public void setRefusalsend(Integer refusalsend) {
        this.refusalsend = refusalsend;
    }

    /**
     * 記事欄を取得するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @return 記事欄
     */
    public String getNote() {
        return note;
    }

    /**
     * 記事欄を設定するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param note 記事欄
     */
    public void setNote(String note) {
        this.note = note;
    }
}
