/*
 * TSimulationIntermediate.java
 * Created on 2021/07/16
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
 * 充放電シミュレーション情報テーブルを表すクラス. <br>
 * <b>【注意】</b> 特になし.
 *
 * @author sekiya
 */
public final class TSimulationIntermediate {

    /** 需要発電計測ポイントID */
    private Integer d_g_location_id = null;

    /** シミュレーション時刻 */
    private Timestamp simulation_time = null;

    /** 時系列Index */
    private Integer time_series_index = null;

    /** 蓄電池のSoC */
    private Float soc = null;

    /** 系統電力 */
    private Double w_u = null;

    /** 充放電電力 */
    private Double w_b = null;

    /** 過放電状態 */
    private Integer s_over = null;

    /** 双方向AC/DCの運転負荷率 */
    private Float lf_acdc = null;

    /** MPPTユニットの運転負荷率 */
    private Float lf_dcdc = null;

    /**
     * コンストラクタ. <br>
     * <b>【注意】</b> 特になし.
     */
    public TSimulationIntermediate() {
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
     * シミュレーション時刻を取得するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @return シミュレーション時刻
     */
    public Timestamp getSimulationTime() {
        return simulation_time;
    }

    /**
     * シミュレーション時刻を設定するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param simulation_time シミュレーション時刻
     */
    public void setSimulationTime(Timestamp simulation_time) {
        this.simulation_time = simulation_time;
    }

    /**
     * 時系列Indexを取得するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @return 時系列Index
     */
    public Integer getTimeSeriesIndex() {
        return time_series_index;
    }

    /**
     * 時系列Indexを設定するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param time_series_index 時系列Index
     */
    public void setTimeSeriesIndex(Integer time_series_index) {
        this.time_series_index = time_series_index;
    }

    /**
     * 蓄電池のSoCを取得するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @return 蓄電池のSoC
     */
    public Float getSoc() {
        return soc;
    }

    /**
     * 蓄電池のSoCを設定するメソッド. <br>
     * 蓄電池のSoCを設定するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param soc 蓄電池のSoC
     */
    public void setSoc(Float soc) {
        this.soc = soc;
    }

    /**
     * 系統電力を取得するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @return 系統電力
     */
    public Double getWU() {
        return w_u;
    }

    /**
     * 系統電力を設定するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param w_u 系統電力
     */
    public void setWU(Double w_u) {
        this.w_u = w_u;
    }

    /**
     * 充放電電力を取得するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @return 充放電電力
     */
    public Double getWB() {
        return w_b;
    }

    /**
     * 充放電電力を設定するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param w_b 充放電電力
     */
    public void setWB(Double w_b) {
        this.w_b = w_b;
    }

    /**
     * 過放電状態を取得するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @return 過放電状態
     */
    public Integer getSOver() {
        return s_over;
    }

    /**
     * 過放電状態を設定するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param s_over 過放電状態
     */
    public void setSOver(Integer s_over) {
        this.s_over = s_over;
    }

    /**
     * 双方向AC/DCの運転負荷率を取得するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @return 双方向AC/DCの運転負荷率
     */
    public Float getLfAcdc() {
        return lf_acdc;
    }

    /**
     * 双方向AC/DCの運転負荷率を設定するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param lf_acdc 双方向AC/DCの運転負荷率
     */
    public void setLfAcdc(Float lf_acdc) {
        this.lf_acdc = lf_acdc;
    }

    /**
     * MPPTユニットの運転負荷率を取得するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @return MPPTユニットの運転負荷率
     */
    public Float getLfDcdc() {
        return lf_dcdc;
    }

    /**
     * MPPTユニットの運転負荷率を設定するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param lf_dcdc MPPTユニットの運転負荷率
     */
    public void setLfDcdc(Float lf_dcdc) {
        this.lf_dcdc = lf_dcdc;
    }
}
