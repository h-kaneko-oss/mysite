/*
 * TMaintenanceSchedule.java
 * Created on 2016/09/08
 * RCSID: $Id$
 *
 * (C) Copyright NTT-F 2016 All rights reserved.
 *
 * This software is furnished under a contract and use, duplication, disclosure
 * and all other uses are restricted to the rights specified in the written
 * contract and memorandum between the contractor and NTT-F.
 */
package jp.co.nttf.gms.common.internal.data.gmsdb;

import java.sql.Time;
import java.sql.Timestamp;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

/**
 * メンテナンススケジュールテーブルを表すクラス. <br>
 * <b>【注意】</b> 特になし.
 *
 * @author naya
 */
public final class TMaintenanceSchedule implements Cloneable {

    /** モード：DR拒否 */
    public static final int MODE_DR_DENY = 300;
    /** モード：設定反映 */
    public static final int MODE_DISTRIBUTION = 100;
    /** モード：制御テスト */
    public static final int MODE_DISTRIBUTION_TEST = 101;
    /** モード：FW再起動 */
    public static final int MODE_FW_REBOOT = 200;
    /** モード：一括登録 */
    public static final int MODE_BULK_UPDATE = 102;
    /** モード：接点連携制御 */
    public static final int MODE_STATUSNOTICE = 103;
    /** モード：再エネ制御 */
    public static final int MODE_RE_ENERGY = 104;

    /** ステータス：通常 */
    public static final short STATUS_ENABLE = 0;
    /** ステータス：即時 */
    public static final short STATUS_RAPID = 1;
    /** ステータス：無効 */
    public static final short STATUS_DISABLE = -1;

    /** スケジュールID. */
    private Integer scheduleid;

    /** GMU-ID. */
    private String gmuid;

    /** カレンダーID. */
    private Integer calendarid;

    /** モードNo. */
    private Integer modeno;

    /** 開始日時. */
    private Timestamp starttime;

    /** 終了日時. */
    private Timestamp endtime;

    /** 繰り返し曜日. */
    private String repeatweeks;

    /** 繰り返し時刻. */
    private Time repeattime;

    /** 繰り返し開始日. */
    private Timestamp repeatstart;

    /** 繰り返し終了日. */
    private Timestamp repeatend;

    /** ステータス. */
    private Short status;

    /** 更新日時. */
    private Timestamp updatetime;

    // original

    /** 配信結果. */
    private Short resultStatus;


    /**
     * コンストラクタ.
     */
    public TMaintenanceSchedule() {
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
     * クローン生成メソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @return 複製したオブジェクト
     * @see java.lang.Object#clone()
     */
    @Override
    public Object clone() {
        TMaintenanceSchedule ret = null;
        try {
            ret = (TMaintenanceSchedule) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return ret;
    }

    /**
     * スケジュールID を設定します.
     *
     * @param scheduleid
     *            スケジュールID
     */
    public void setScheduleid(Integer scheduleid) {
        this.scheduleid = scheduleid;
    }

    /**
     * スケジュールID を取得します.
     *
     * @return スケジュールID
     */
    public Integer getScheduleid() {
        return this.scheduleid;
    }

    /**
     * GMU-ID を設定します.
     *
     * @param gmuid
     *            GMU-ID
     */
    public void setGmuid(String gmuid) {
        this.gmuid = gmuid;
    }

    /**
     * GMU-ID を取得します.
     *
     * @return GMU-ID
     */
    public String getGmuid() {
        return this.gmuid;
    }

    /**
     * カレンダーID を設定します.
     *
     * @param calendarid
     *            カレンダーID
     */
    public void setCalendarid(Integer calendarid) {
        this.calendarid = calendarid;
    }

    /**
     * カレンダーID を取得します.
     *
     * @return カレンダーID
     */
    public Integer getCalendarid() {
        return this.calendarid;
    }

    /**
     * モードNo を設定します.
     *
     * @param modeno
     *            モードNo
     */
    public void setModeno(Integer modeno) {
        this.modeno = modeno;
    }

    /**
     * モードNo を取得します.
     *
     * @return モードNo
     */
    public Integer getModeno() {
        return this.modeno;
    }

    /**
     * 開始日時 を設定します.
     *
     * @param starttime
     *            開始日時
     */
    public void setStarttime(Timestamp starttime) {
        this.starttime = starttime;
    }

    /**
     * 開始日時 を取得します.
     *
     * @return 開始日時
     */
    public Timestamp getStarttime() {
        return this.starttime;
    }

    /**
     * 終了日時 を設定します.
     *
     * @param endtime
     *            終了日時
     */
    public void setEndtime(Timestamp endtime) {
        this.endtime = endtime;
    }

    /**
     * 終了日時 を取得します.
     *
     * @return 終了日時
     */
    public Timestamp getEndtime() {
        return this.endtime;
    }

    /**
     * 繰り返し曜日 を設定します.
     *
     * @param repeatweeks
     *            繰り返し曜日
     */
    public void setRepeatweeks(String repeatweeks) {
        this.repeatweeks = repeatweeks;
    }

    /**
     * 繰り返し曜日 を取得します.
     *
     * @return 繰り返し曜日
     */
    public String getRepeatweeks() {
        return this.repeatweeks;
    }

    /**
     * 繰り返し時刻 を設定します.
     *
     * @param repeattime
     *            繰り返し時刻
     */
    public void setRepeattime(Time repeattime) {
        this.repeattime = repeattime;
    }

    /**
     * 繰り返し時刻 を取得します.
     *
     * @return 繰り返し時刻
     */
    public Time getRepeattime() {
        return this.repeattime;
    }

    /**
     * 繰り返し開始日 を設定します.
     *
     * @param repeatstart
     *            繰り返し開始日
     */
    public void setRepeatstart(Timestamp repeatstart) {
        this.repeatstart = repeatstart;
    }

    /**
     * 繰り返し開始日 を取得します.
     *
     * @return 繰り返し開始日
     */
    public Timestamp getRepeatstart() {
        return this.repeatstart;
    }

    /**
     * 繰り返し終了日 を設定します.
     *
     * @param repeatend
     *            繰り返し終了日
     */
    public void setRepeatend(Timestamp repeatend) {
        this.repeatend = repeatend;
    }

    /**
     * 繰り返し終了日 を取得します.
     *
     * @return 繰り返し終了日
     */
    public Timestamp getRepeatend() {
        return this.repeatend;
    }

    /**
     * ステータス を設定します.
     *
     * @param status
     *            ステータス
     */
    public void setStatus(Short status) {
        this.status = status;
    }

    /**
     * ステータス を取得します.
     *
     * @return ステータス
     */
    public Short getStatus() {
        return this.status;
    }

    /**
     * 更新日時 を設定します.
     *
     * @param updatetime
     *            更新日時
     */
    public void setUpdatetime(Timestamp updatetime) {
        this.updatetime = updatetime;
    }

    /**
     * 更新日時 を取得します.
     *
     * @return 更新日時
     */
    public Timestamp getUpdatetime() {
        return this.updatetime;
    }

    /**
     * 配信結果を取得するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @return 配信結果
     */
    public Short getResultStatus() {
        return resultStatus;
    }

    /**
     * 配信結果を設定するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param resultStatus 配信結果
     */
    public void setResultStatus(Short resultStatus) {
        this.resultStatus = resultStatus;
    }

}
