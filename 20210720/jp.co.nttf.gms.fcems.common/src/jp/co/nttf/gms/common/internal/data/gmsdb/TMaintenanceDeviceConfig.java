/*
 * TMaintenanceDeviceConfig.java
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

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

/**
 *
 * t_maintenance_device_configテーブルを表すクラス. <br>
 * <b>【注意】</b> 特になし.
 *
 * @author ninomiya
 */
public class TMaintenanceDeviceConfig {

    /** 設定ID */
    private Integer configid;
    /** スケジュールID */
    private Integer scheduleid;
    /** デバイス名 */
    private String devicename;
    /** 配信ID */
    private Integer distributionid;
    /** プロパティ */
    private String property;

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
     * configidを取得するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @return configid
     */
    public Integer getConfigid() {
        return configid;
    }

    /**
     * configidを設定するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param configid 設定ID
     */
    public void setConfigid(Integer configid) {
        this.configid = configid;
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
     * devicenameを取得するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @return devicename
     */
    public String getDevicename() {
        return devicename;
    }

    /**
     * devicenameを設定するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param devicename デバイス名
     */
    public void setDevicename(String devicename) {
        this.devicename = devicename;
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
     * propertyを取得するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @return property
     */
    public String getProperty() {
        return property;
    }

    /**
     * propertyを設定するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param property プロパティ
     */
    public void setProperty(String property) {
        this.property = property;
    }

}
