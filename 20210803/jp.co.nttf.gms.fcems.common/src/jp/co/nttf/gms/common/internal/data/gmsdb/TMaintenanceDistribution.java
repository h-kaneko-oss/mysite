/*
 * TMaintenanceDistribution.java
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
 * t_maintenance_distributionテーブルを表すクラス. <br>
 * <b>【注意】</b> 特になし.
 *
 * @author ninomiya
 */
public class TMaintenanceDistribution {

    /** モード：接点連携制御 */
    public static final int MODE_STATUSNOTICE = 103;
    /** モード：再エネ制御 */
    public static final int MODE_RE_ENERGY = 104;
    /** ステータス：配信待ち */
    public static final int STATUS_SEND_WAIT = 0;

    /** 配信ID */
    private Integer distributionid;
    /** スケジュールID */
    private Integer scheduleid;
    /** GMU-ID */
    private String gmuid;
    /** モードNo */
    private Integer modeno;
    /** 開始日時 */
    private Timestamp starttime;
    /** ステータス */
    private Integer status;
    /** 作成日時 */
    private Timestamp createtime;
    /** 更新日時 */
    private Timestamp updatetime;

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
     * distributionidを取得するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @return distributionid
     */
    public Integer getDistributionid() {
        return distributionid;
    }

    /**
     * distributionidを設定するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param distributionid 配信ID
     */
    public void setDistributionid(Integer distributionid) {
        this.distributionid = distributionid;
    }

    /**
     * scheduleidを取得するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @return scheduleid
     */
    public Integer getScheduleid() {
        return scheduleid;
    }

    /**
     * scheduleidを設定するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param scheduleid スケジュールID
     */
    public void setScheduleid(Integer scheduleid) {
        this.scheduleid = scheduleid;
    }

    /**
     * gmuidを取得するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @return gmuid
     */
    public String getGmuid() {
        return gmuid;
    }

    /**
     * gmuidを設定するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param gmuid GMU-ID
     */
    public void setGmuid(String gmuid) {
        this.gmuid = gmuid;
    }

    /**
     * modenoを取得するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @return modeno
     */
    public Integer getModeno() {
        return modeno;
    }

    /**
     * modenoを設定するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param modeno モードNo
     */
    public void setModeno(Integer modeno) {
        this.modeno = modeno;
    }

    /**
     * starttimeを取得するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @return starttime
     */
    public Timestamp getStarttime() {
        return starttime;
    }

    /**
     * starttimeを設定するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param starttime 開始日時
     */
    public void setStarttime(Timestamp starttime) {
        this.starttime = starttime;
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
     * createtimeを取得するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @return createtime
     */
    public Timestamp getCreatetime() {
        return createtime;
    }

    /**
     * createtimeを設定するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param createtime 作成日時
     */
    public void setCreatetime(Timestamp createtime) {
        this.createtime = createtime;
    }

    /**
     * updatetimeを取得するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @return updatetime
     */
    public Timestamp getUpdatetime() {
        return updatetime;
    }

    /**
     * updatetimeを設定するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param updatetime 更新日時
     */
    public void setUpdatetime(Timestamp updatetime) {
        this.updatetime = updatetime;
    }

}
