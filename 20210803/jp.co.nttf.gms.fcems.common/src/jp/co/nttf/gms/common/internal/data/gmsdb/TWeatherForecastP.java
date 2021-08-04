/*
 * TWeatherForecastP.java
 * Created on 2021/07/13
 * RCSID: $Id$
 *
 * (C) Copyright NTT-F 2021 All rights reserved.
 *
 * This software is furnished under a contract and use, duplication, disclosure
 * and all other uses are restricted to the rights specified in the written
 * contract and memorandum between the contractor and NTT-F.
 */
package jp.co.nttf.gms.common.internal.data.gmsdb;

import java.sql.Timestamp;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

/**
 * 気象位置情報テーブルを表すクラス. <br>
 * <b>【注意】</b> 特になし.
 *
 * @author sekiya
 */
public final class TWeatherForecastP {

    /** 配信時刻 */
    private Timestamp distribution_time = null;

    /** 位置情報ID */
    private Integer location_id = null;

    /** 気圧面 */
    private Integer pressure_level = null;

    /** 予報時刻 */
    private Timestamp forecast_time = null;

    /** ジオポテンシャル高度 */
    private Double geopotential_height = null;

    /** 風ベクトルU */
    private Double u_wind = null;

    /** 風ベクトルV */
    private Double v_wind = null;

    /** 気温 */
    private Float temperature = null;

    /** 相対湿度 */
    private Float relative_humidity = null;

    /** 上昇流 */
    private Double updraft = null;

    /**
     * コンストラクタ. <br>
     * <b>【注意】</b> 特になし.
     */
    public TWeatherForecastP() {
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
     * 配信時刻を取得するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @return 配信時刻
     */
    public Timestamp getDistributionTime() {
        return distribution_time;
    }

    /**
     * 配信時刻を設定するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param distribution_time 配信時刻
     */
    public void setDistributionTime(Timestamp distribution_time) {
        this.distribution_time = distribution_time;
    }

    /**
     * 位置情報IDを取得するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @return 位置情報ID
     */
    public Integer getLocationId() {
        return location_id;
    }

    /**
     * 位置情報IDを設定するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param location_id 位置情報ID
     */
    public void setLocationId(Integer location_id) {
        this.location_id = location_id;
    }

    /**
     * 気圧面を取得するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @return 気圧面
     */
    public Integer getPressureLevel() {
        return pressure_level;
    }

    /**
     * 気圧面を設定するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param pressure_level 気圧面
     */
    public void setPressureLevel(Integer pressure_level) {
        this.pressure_level = pressure_level;
    }

  /**
     * 予報時刻を取得するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @return 予報時刻
     */
    public Timestamp getForecastTime() {
        return forecast_time;
    }

    /**
     * 予報時刻を設定するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param forecast_time 予報時刻
     */
    public void setForecastTime(Timestamp forecast_time) {
        this.forecast_time = forecast_time;
    }

    /**
     * ジオポテンシャル高度を取得するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @return ジオポテンシャル高度
     */
    public Double getGeopotentialHeight() {
        return geopotential_height;
    }

    /**
     * ジオポテンシャル高度を設定するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param geopotential_height ジオポテンシャル高度
     */
    public void setGeopotentialHeight(Double geopotential_height) {
        this.geopotential_height = geopotential_height;
    }

    /**
     * 風ベクトルUを取得するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @return 風ベクトルU
     */
    public Double getUWind() {
        return u_wind;
    }

    /**
     * 風ベクトルUを設定するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param u_wind 風ベクトルU
     */
    public void setUWind(Double u_wind) {
        this.u_wind = u_wind;
    }

    /**
     * 風ベクトルVを取得するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @return 風ベクトルV
     */
    public Double getVWind() {
        return v_wind;
    }

    /**
     * 風ベクトルVを設定するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param v_wind 風ベクトルV
     */
    public void setVWind(Double v_wind) {
        this.v_wind = v_wind;
    }

    /**
     * 気温を取得するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @return 気温
     */
    public Float getTemperature() {
        return temperature;
    }

    /**
     * 気温を設定するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param temperature 気温
     */
    public void setTemperature(Float temperature) {
        this.temperature = temperature;
    }

    /**
     * 相対湿度を取得するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @return 相対湿度
     */
    public Float getRelativeHumidity() {
        return relative_humidity;
    }

    /**
     * 相対湿度を設定するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param relative_humidity 相対湿度
     */
    public void setRelativeHumidity(Float relative_humidity) {
        this.relative_humidity = relative_humidity;
    }

    /**
     * 上昇流を取得するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @return 上昇流
     */
    public Double getUpdraft() {
        return updraft;
    }

    /**
     * 上昇流を設定するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param updraft 上昇流
     */
    public void setUpdraft(Double updraft) {
        this.updraft = updraft;
    }
}
