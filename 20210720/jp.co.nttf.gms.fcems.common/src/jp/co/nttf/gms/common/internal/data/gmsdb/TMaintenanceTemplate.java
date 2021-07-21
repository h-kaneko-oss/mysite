/*
 * TMaintenanceTemplate.java
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
 * t_maintenance_templateテーブルを表すクラス. <br>
 * <b>【注意】</b> 特になし.
 *
 * @author ninomiya
 */
public class TMaintenanceTemplate {

    /** GMU-ID */
    private String gmuid;
    /** 制御レベル */
    private Integer level;
    /** デバイス名 */
    private String devicename;
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
     * levelを取得するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @return level
     */
    public Integer getLevel() {
        return level;
    }

    /**
     * levelを設定するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param level 制御レベル
     */
    public void setLevel(Integer level) {
        this.level = level;
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
