/*
 * TStatusnoticePoint.java
 * Created on 2017/10/18
 * RCSID: $Id$
 *
 * (C) Copyright NTT-F 2017 All rights reserved.
 *
 * This software is furnished under a contract and use, duplication, disclosure
 * and all other uses are restricted to the rights specified in the written
 * contract and memorandum between the contractor and NTT-F.
 */
package jp.co.nttf.gms.common.internal.data.gmsdb;

import java.sql.Timestamp;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

/**
 *
 * t_statusnotice_pointテーブルを表すクラス. <br>
 * <b>【注意】</b> 特になし.
 *
 * @author ninomiya
 */
public class TStatusnoticePoint {

    /** ステータス：ON */
    public static final int STATUS_ON = 1;

    /** メッセージNo */
    private Integer messageno;
    /** 計測ポイントID */
    private Integer measurepointid;
    /** 日時時間 */
    private Timestamp noticetime;
    /** 確認時間 */
    private Timestamp confirmtime;
    /** ステータス */
    private Integer status;
    /** 確認者 */
    private String confirmuser;
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
     * messagenoを取得するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @return messageno
     */
    public Integer getMessageno() {
        return messageno;
    }

    /**
     * messagenoを設定するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param messageno メッセージNo
     */
    public void setMessageno(Integer messageno) {
        this.messageno = messageno;
    }

    /**
     * measurepointidを取得するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @return measurepointid
     */
    public Integer getMeasurepointid() {
        return measurepointid;
    }

    /**
     * measurepointidを設定するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param measurepointid 計測ポイントID
     */
    public void setMeasurepointid(Integer measurepointid) {
        this.measurepointid = measurepointid;
    }

    /**
     * noticetimeを取得するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @return noticetime
     */
    public Timestamp getNoticetime() {
        return noticetime;
    }

    /**
     * noticetimeを設定するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param noticetime 日時時間
     */
    public void setNoticetime(Timestamp noticetime) {
        this.noticetime = noticetime;
    }

    /**
     * confirmtimeを取得するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @return confirmtime
     */
    public Timestamp getConfirmtime() {
        return confirmtime;
    }

    /**
     * confirmtimeを設定するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param confirmtime 確認時間
     */
    public void setConfirmtime(Timestamp confirmtime) {
        this.confirmtime = confirmtime;
    }

    /**
     * statusを取得するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @return status
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * statusを設定するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param status ステータス
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * confirmuserを取得するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @return confirmuser
     */
    public String getConfirmuser() {
        return confirmuser;
    }

    /**
     * confirmuserを設定するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param confirmuser 確認者
     */
    public void setConfirmuser(String confirmuser) {
        this.confirmuser = confirmuser;
    }

    /**
     * noteを取得するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @return note
     */
    public String getNote() {
        return note;
    }

    /**
     * noteを設定するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param note 記事欄
     */
    public void setNote(String note) {
        this.note = note;
    }

}
