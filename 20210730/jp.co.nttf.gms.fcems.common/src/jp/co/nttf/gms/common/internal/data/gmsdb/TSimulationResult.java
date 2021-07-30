/*
 * TSimulationResult.java
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
public final class TSimulationResult {

    /** 需要発電計測ポイントID */
    private Integer d_g_location_id = null;

    /** シミュレーション時刻 */
    private Timestamp simulation_time = null;

    /** SoC変換要否 */
    private Integer soc_mod_need = null;

    /** スケジュールID */
    private Integer schedule_id = null;

    /** 運用下限変更値 */
    private Double soc_min = null;

    /** 自家消費率 */
    private Float r_isrand = null;

    /** バックアップ最低所要電力量 */
    private Double w_r = null;

    /**
     * コンストラクタ. <br>
     * <b>【注意】</b> 特になし.
     */
    public TSimulationResult() {
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
     * SoC変更要否を取得するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @return SoC変更要否
     */
    public Integer getSocModNeed() {
        return soc_mod_need;
    }

    /**
     * SoC変更要否を設定するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param soc_mod_need SoC変更要否
     */
    public void setSocModNeed(Integer soc_mod_need) {
        this.soc_mod_need = soc_mod_need;
    }

    /**
     * スケジュールIDを取得するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @return スケジュールID
     */
    public Integer getScheduleId() {
        return schedule_id;
    }

    /**
     * スケジュールIDを設定するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param schedule_id スケジュールID
     */
    public void setScheduleId(Integer schedule_id) {
        this.schedule_id = schedule_id;
    }

    /**
     * 運用下限変更値を取得するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @return 運用下限変更値
     */
    public Double getSocMin() {
        return soc_min;
    }

    /**
     * 運用下限変更値を設定するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param soc_min 運用下限変更値
     */
    public void setSocMin(Double soc_min) {
        this.soc_min = soc_min;
    }

    /**
     * 自家消費率を取得するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @return 自家消費率
     */
    public Float getRIsrand() {
        return r_isrand;
    }

    /**
     * 自家消費率を設定するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param r_isrand 自家消費率
     */
    public void setRIsrand(Float r_isrand) {
        this.r_isrand = r_isrand;
    }

    /**
     * バックアップ最低所要電力量を取得するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @return バックアップ最低所要電力量
     */
    public Double getWR() {
        return w_r;
    }

    /**
     * バックアップ最低所要電力量を設定するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param w_r バックアップ最低所要電力量
     */
    public void setWR(Double w_r) {
        this.w_r = w_r;
    }
}
