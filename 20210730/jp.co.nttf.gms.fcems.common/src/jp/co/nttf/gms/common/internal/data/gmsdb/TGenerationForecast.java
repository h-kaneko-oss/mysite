/*
 * TGenerationForecast.java
 * Created on 2021/07/14
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
 * 需要予測テーブルを表すクラス. <br>
 * <b>【注意】</b> 特になし.
 *
 * @author sekiya
 */
public final class TGenerationForecast {

    /** 需要発電計測ポイントID */
    private Integer d_g_location_id = null;

    /** 予測実施時刻 */
    private Timestamp calc_time = null;

    /** 予測データ時刻 */
    private Timestamp forecast_time = null;

    /** 発電量予測 */
    private Double generation_forecast_value = null;

    /**
     * コンストラクタ. <br>
     * <b>【注意】</b> 特になし.
     */
    public TGenerationForecast() {
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
     * 需要発電計測ポイントIDを取得するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @return 需要発電計測ポイントID
     */
    public Integer getDGLocationId() {
        return d_g_location_id;
    }

    /**
     * 需要発電計測ポイントIDを設定するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param d_g_location_id 需要発電計測ポイントID
     */
    public void setDGLocationId(Integer d_g_location_id) {
        this.d_g_location_id = d_g_location_id;
    }

    /**
     * 予測実施時刻を取得するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @return 予測実施時刻
     */
    public Timestamp getCalcTime() {
        return calc_time;
    }

    /**
     * 予測実施時刻を設定するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param calc_time 予測実施時刻
     */
    public void setCalcTime(Timestamp calc_time) {
        this.calc_time = calc_time;
    }

    /**
     * 予測データ時刻を取得するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @return 予測データ時刻
     */
    public Timestamp getForecastTime() {
        return forecast_time;
    }

    /**
     * 予測データ時刻を設定するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param forecast_time 予測データ時刻
     */
    public void setForecastTime(Timestamp forecast_time) {
        this.forecast_time = forecast_time;
    }

    /**
     * 発電量予測を取得するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @return 発電量予測
     */
    public Double getGenerationForecastValue() {
        return generation_forecast_value;
    }

    /**
     * 発電量予測を設定するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param generation_forecast_value 発電量予測
     */
    public void setGenerationForecastValue(Double generation_forecast_value) {
        this.generation_forecast_value = generation_forecast_value;
    }
}
