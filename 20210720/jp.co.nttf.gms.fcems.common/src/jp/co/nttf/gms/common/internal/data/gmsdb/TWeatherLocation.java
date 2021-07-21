/*
 * TWeatherLocation.java
 * Created on 2021/07/12
 * RCSID: $Id$
 *
 * (C) Copyright NTT-F 2021 All rights reserved.
 *
 * This software is furnished under a contract and use, duplication, disclosure
 * and all other uses are restricted to the rights specified in the written
 * contract and memorandum between the contractor and NTT-F.
 */
package jp.co.nttf.gms.common.internal.data.gmsdb;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

/**
 * 気象位置情報テーブルを表すクラス. <br>
 * <b>【注意】</b> 特になし.
 *
 * @author sekiya
 */
public final class TWeatherLocation {

    /** 位置情報ID */
    private Integer location_id = null;

    /** データ種別 */
    private Integer data_type = null;

    /** 緯度方向Index */
    private Integer latitude_index = null;

    /** 経度方向Index */
    private Integer longitude_index = null;

    /**
     * コンストラクタ. <br>
     * <b>【注意】</b> 特になし.
     */
    public TWeatherLocation() {
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
     * 位置情報IDを取得するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @return リソースID
     */
    public Integer getLocationId() {
        return location_id;
    }

    /**
     * 位置情報IDを設定するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param location_id リソースID
     */
    public void setLocationId(Integer location_id) {
        this.location_id = location_id;
    }

    /**
     * データ種別を取得するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @return データ種別
     */
    public Integer getDataType() {
        return data_type;
    }

    /**
     * データ種別を設定するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param data_type データ種別
     */
    public void setDataType(Integer data_type) {
        this.data_type = data_type;
    }

    /**
     * 緯度方向Indexを取得するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @return 緯度方向Index
     */
    public Integer getLatitudeIndex() {
        return latitude_index;
    }

    /**
     * 緯度方向Indexを設定するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param latitude_index 緯度方向Index
     */
    public void setLatitudeIndex(Integer latitude_index) {
        this.latitude_index = latitude_index;
    }

    /**
     * 経度方向Indexを取得するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @return 経度方向Index
     */
    public Integer getLongitudeIndex() {
        return longitude_index;
    }

    /**
     * 経度方向Indexを設定するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param longitude_index 経度方向Index
     */
    public void setLongitudeIndex(Integer longitude_index) {
        this.longitude_index = longitude_index;
    }
}
