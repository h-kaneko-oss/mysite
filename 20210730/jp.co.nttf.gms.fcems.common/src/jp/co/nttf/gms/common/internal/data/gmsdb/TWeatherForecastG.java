/*
 * TWeatherForecastG.java
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

import java.sql.Timestamp;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

/**
 * 気象位置情報テーブルを表すクラス. <br>
 * <b>【注意】</b> 特になし.
 *
 * @author sekiya
 */
public final class TWeatherForecastG {

    /** 配信時刻 */
    private Timestamp distribution_time = null;

    /** 位置情報ID */
    private Integer location_id = null;

    /** 予報時刻 */
    private Timestamp forecast_time = null;

    /** 海面更正気圧 */
    private Double sea_level_pressure = null;

    /** 地上気圧 */
    private Double surface_pressure = null;

    /** 風ベクトルU */
    private Double u_wind = null;

    /** 風ベクトルV */
    private Double v_wind = null;

    /** 気温 */
    private Float temperature = null;

    /** 相対湿度 */
    private Float relative_humidity = null;

    /** 降水量 */
    private Double precipitation = null;

    /** 全雲量 */
    private Float total_cloud_cover = null;

    /** 上層雲量 */
    private Float high_cloud_cover = null;

    /** 中層雲量 */
    private Float medium_cloud_cover = null;

    /** 下層雲量 */
    private Float low_cloud_cover = null;

    /** 日射量 */
    private Double sunshine = null;

    /**
     * コンストラクタ. <br>
     * <b>【注意】</b> 特になし.
     */
    public TWeatherForecastG() {
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
     * 海面更正気圧を取得するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @return 海面更正気圧
     */
    public Double getSeaLevelPressure() {
        return sea_level_pressure;
    }

    /**
     * 海面更正気圧を設定するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param sea_level_pressure 海面更正気圧
     */
    public void setSeaLevelPressure(Double sea_level_pressure) {
        this.sea_level_pressure = sea_level_pressure;
    }

    /**
     * 地上気圧を取得するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @return 地上気圧
     */
    public Double getSurfacePressure() {
        return surface_pressure;
    }

    /**
     * 地上気圧を設定するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param surface_pressure 地上気圧
     */
    public void setSurfacePressure(Double surface_pressure) {
        this.surface_pressure = surface_pressure;
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
     * 降水量を取得するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @return 降水量
     */
    public Double getPrecipitation() {
        return precipitation;
    }

    /**
     * 降水量を設定するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param precipitation 降水量
     */
    public void setPrecipitation(Double precipitation) {
        this.precipitation = precipitation;
    }

    /**
     * 全雲量を取得するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @return 全雲量
     */
    public Float getTotalCloudCover() {
        return total_cloud_cover;
    }

    /**
     * 全雲量を設定するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param total_cloud_cover 全雲量
     */
    public void setTotalCloudCover(Float total_cloud_cover) {
        this.total_cloud_cover = total_cloud_cover;
    }

    /**
     * 上層雲量を取得するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @return 上層雲量
     */
    public Float getHighCloudCover() {
        return high_cloud_cover;
    }

    /**
     * 上層雲量を設定するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param high_cloud_cover 全雲量
     */
    public void setHighCloudCover(Float high_cloud_cover) {
        this.high_cloud_cover = high_cloud_cover;
    }

    /**
     * 中層雲量を取得するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @return 中層雲量
     */
    public Float getMediumCloudCover() {
        return medium_cloud_cover;
    }

    /**
     * 中層雲量を設定するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param medium_cloud_cover 中層雲量
     */
    public void setMediumCloudCover(Float medium_cloud_cover) {
        this.medium_cloud_cover = medium_cloud_cover;
    }

    /**
     * 下層雲量を取得するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @return 下層雲量
     */
    public Float getLowCloudCover() {
        return low_cloud_cover;
    }

    /**
     * 下層雲量を設定するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param low_cloud_cover 下層雲量
     */
    public void setLowCloudCover(Float low_cloud_cover) {
        this.low_cloud_cover = low_cloud_cover;
    }

    /**
     * 日射量を取得するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @return 日射量
     */
    public Double getSunshine() {
        return sunshine;
    }

    /**
     * 日射量を設定するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param sunshine 日射量
     */
    public void setSunshine(Double sunshine) {
        this.sunshine = sunshine;
    }
}
