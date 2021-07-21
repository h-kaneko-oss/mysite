/*
 * TDemandGroup.java
 * Created on 2015/03/06
 * RCSID: $Id$
 *
 * (C) Copyright NTT-F 2015 All rights reserved.
 *
 * This software is furnished under a contract and use, duplication, disclosure
 * and all other uses are restricted to the rights specified in the written
 * contract and memorandum between the contractor and NTT-F.
 */
package jp.co.nttf.gms.common.internal.data.gmsdb;

import java.sql.Timestamp;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

/**
 * DR情報テーブルを表すクラス. <br>
 * <b>【注意】</b> 特になし.
 *
 * @author naya
 */
public final class TDemandInfo {

    /** ステータス：有効 */
    public static final int STATUSFLAG_ENABLE = 0;
    /** ステータス：無効(中止) */
    public static final int STATUSFLAG_DISABLE = 1;

    /** DR-ID */
    private Integer drid;
    /** DRグループID */
    private Integer drgroupid;
    /** 開始時間 */
    private Timestamp starttime;
    /** 終了時間 */
    private Timestamp endtime;
    /** 状態フラグ */
    private Integer statusflag = STATUSFLAG_ENABLE;
    /** 記事欄 */
    private String note;
    /** 登録日 */
    private Timestamp registdate;
    /** 登録者 */
    private String registuser;
    /** DRデータ */
    private String drcsv;

    /**
     * コンストラクタ. <br>
     * <b>【注意】</b> 特になし.
     */
    public TDemandInfo() {
    }

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
     * DR-IDを取得するメソッド.
     * <br>
     * <b>【注意】</b>
     * 特になし.
     *
     * @return DR-ID
     */
    public Integer getDrid() {
        return drid;
    }

    /**
     * DR-IDを設定するメソッド.
     * <br>
     * <b>【注意】</b>
     * 特になし.
     *
     * @param drid DR-ID
     */
    public void setDrid(Integer drid) {
        this.drid = drid;
    }

    /**
     * DRグループIDを取得するメソッド.
     * <br>
     * <b>【注意】</b>
     * 特になし.
     *
     * @return DRグループID
     */
    public Integer getDrgroupid() {
        return drgroupid;
    }

    /**
     * DRグループIDを設定するメソッド.
     * <br>
     * <b>【注意】</b>
     * 特になし.
     *
     * @param drgroupid DRグループID
     */
    public void setDrgroupid(Integer drgroupid) {
        this.drgroupid = drgroupid;
    }

    /**
     * 開始時間を取得するメソッド.
     * <br>
     * <b>【注意】</b>
     * 特になし.
     *
     * @return 開始時間
     */
    public Timestamp getStarttime() {
        return starttime;
    }

    /**
     * 開始時間を設定するメソッド.
     * <br>
     * <b>【注意】</b>
     * 特になし.
     *
     * @param starttime 開始時間
     */
    public void setStarttime(Timestamp starttime) {
        this.starttime = starttime;
    }

    /**
     * 終了時間を取得するメソッド.
     * <br>
     * <b>【注意】</b>
     * 特になし.
     *
     * @return 終了時間
     */
    public Timestamp getEndtime() {
        return endtime;
    }

    /**
     * 終了時間を設定するメソッド.
     * <br>
     * <b>【注意】</b>
     * 特になし.
     *
     * @param endtime 終了時間
     */
    public void setEndtime(Timestamp endtime) {
        this.endtime = endtime;
    }

    /**
     * 状態フラグを取得するメソッド.
     * <br>
     * <b>【注意】</b>
     * 特になし.
     *
     * @return 状態フラグ
     */
    public Integer getStatusflag() {
        return statusflag;
    }

    /**
     * 状態フラグを設定するメソッド.
     * <br>
     * <b>【注意】</b>
     * 特になし.
     *
     * @param statusflag 状態フラグ
     */
    public void setStatusflag(Integer statusflag) {
        this.statusflag = statusflag;
    }

    /**
     * 記事欄を取得するメソッド.
     * <br>
     * <b>【注意】</b>
     * 特になし.
     *
     * @return 記事欄
     */
    public String getNote() {
        return note;
    }

    /**
     * 記事欄を設定するメソッド.
     * <br>
     * <b>【注意】</b>
     * 特になし.
     *
     * @param note 記事欄
     */
    public void setNote(String note) {
        this.note = note;
    }

    /**
     * 登録日を取得するメソッド.
     * <br>
     * <b>【注意】</b>
     * 特になし.
     *
     * @return 登録日
     */
    public Timestamp getRegistdate() {
        return registdate;
    }

    /**
     * 登録日を設定するメソッド.
     * <br>
     * <b>【注意】</b>
     * 特になし.
     *
     * @param registdate 登録日
     */
    public void setRegistdate(Timestamp registdate) {
        this.registdate = registdate;
    }

    /**
     * 登録者を取得するメソッド.
     * <br>
     * <b>【注意】</b>
     * 特になし.
     *
     * @return 登録者
     */
    public String getRegistuser() {
        return registuser;
    }

    /**
     * 登録者を設定するメソッド.
     * <br>
     * <b>【注意】</b>
     * 特になし.
     *
     * @param registuser 登録者
     */
    public void setRegistuser(String registuser) {
        this.registuser = registuser;
    }

    /**
     * DRデータを取得するメソッド.
     * <br>
     * <b>【注意】</b>
     * 特になし.
     *
     * @return DRデータ
     */
    public String getDrcsv() {
        return drcsv;
    }

    /**
     * DRデータを設定するメソッド.
     * <br>
     * <b>【注意】</b>
     * 特になし.
     *
     * @param drcsv DRデータ
     */
    public void setDrcsv(String drcsv) {
        this.drcsv = drcsv;
    }

}
