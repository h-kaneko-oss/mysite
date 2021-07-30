/*
 * TDemandForecastError.java
 * Created on 2021/07/20
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
public final class TDemandForecastError {

    /** 需要発電計測ポイントID */
    private Integer d_g_location_id = null;

    /** 実施時刻 */
    private Timestamp calc_time = null;

      /** データ時刻 */
    private Timestamp time = null;

    /** 地上気圧 */
    private Double surface_pressure = null;

    /** 予測積算値 */
    private Double integrated_forecast_val = null;

    /** 実績積算値 */
    private Double integrated_actual_val = null;

    /** RMSE */
    private Double rmse = null;

    /** 誤差率 */
    private Double error_rate = null;

    /**
     * コンストラクタ. <br>
     * <b>【注意】</b> 特になし.
     */
    public TDemandForecastError() {
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
     * 実施時刻を取得するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @return 実施時刻
     */
    public Timestamp getCalcTime() {
        return calc_time;
    }

    /**
     * 実施時刻を設定するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param calc_time 実施時刻
     */
    public void setCalcTime(Timestamp calc_time) {
        this.calc_time = calc_time;
    }

    /**
     * データ時刻を取得するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @return データ時刻
     */
    public Timestamp getTime() {
        return time;
    }

    /**
     * データ時刻を設定するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param time データ時刻
     */
    public void setTime(Timestamp time) {
        this.time = time;
    }

    /**
     * 予測積算値を取得するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @return 予測積算値
     */
    public Double getIntegratedForecastVal() {
        return integrated_forecast_val;
    }

    /**
     * 予測積算値を設定するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param integrated_forecast_val 予測積算値
     */
    public void setIntegratedForecastVal(Double integrated_forecast_val) {
        this.integrated_forecast_val = integrated_forecast_val;
    }

    /**
     * 実績積算値を取得するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @return 実績積算値
     */
    public Double getIntegratedActualVal() {
        return integrated_actual_val;
    }

    /**
     * 実績積算値を設定するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param integrated_actual_val 実績積算値
     */
    public void setIntegratedActualVal(Double integrated_actual_val) {
        this.integrated_actual_val = integrated_actual_val;
    }

    /**
     * RMSEを取得するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @return RMSE
     */
    public Double getRmse() {
        return rmse;
    }

    /**
     * RMSEを設定するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param rmse RMSE
     */
    public void setRmse(Double rmse) {
        this.rmse = rmse;
    }

    /**
     * 誤差率を取得するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @return 誤差率
     */
    public Double getErrorRate() {
        return error_rate;
    }

    /**
     * 誤差率を設定するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param error_rate 誤差率
     */
    public void setErrorRate(Double error_rate) {
        this.error_rate = error_rate;
    }
}
