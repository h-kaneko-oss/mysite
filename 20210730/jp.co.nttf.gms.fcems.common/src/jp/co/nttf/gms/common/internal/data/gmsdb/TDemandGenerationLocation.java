/*
 * TDemandGenerationLocation.java
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

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

/**
 * 気象位置情報テーブルを表すクラス. <br>
 * <b>【注意】</b> 特になし.
 *
 * @author sekiya
 */
public final class TDemandGenerationLocation {

    /** 需要発電計測ポイントID */
    private Integer d_g_location_id = null;

    /** 地上面位置情報ID */
    private Integer g_location_id = null;

    /** 気圧面位置情報ID */
    private Integer p_location_id = null;

    /** 需要計測ポイントリストID */
    private Integer demand_measurepoint_list_id = null;

    /** 発電計測ポイントリストID */
    private Integer generation_measurepoint_list_id = null;

    /** GMU-ID */
    private String gmu_id = null;

    /** SoC設定変更要否設定 */
    private Integer soc_mod_setting = null;

    /**
     * コンストラクタ. <br>
     * <b>【注意】</b> 特になし.
     */
    public TDemandGenerationLocation() {
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
     * 地上面位置情報IDを取得するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @return 地上面位置情報ID
     */
    public Integer getGLocationId() {
        return g_location_id;
    }

    /**
     * 地上面位置情報IDを設定するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param g_location_id 地上面位置情報ID
     */
    public void setGLocationId(Integer g_location_id) {
        this.g_location_id = g_location_id;
    }

    /**
     * 気圧面位置情報IDを取得するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @return 気圧面位置情報ID
     */
    public Integer getPLocationId() {
        return p_location_id;
    }

    /**
     * 気圧面位置情報IDを設定するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param p_location_id 地上面位置情報ID
     */
    public void setPLocationId(Integer p_location_id) {
        this.p_location_id = p_location_id;
    }

    /**
     * 需要計測ポイントリストIDを取得するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @return 需要計測ポイントリストID
     */
    public Integer getDemandMeasurepointListId() {
        return demand_measurepoint_list_id;
    }

    /**
     * 需要計測ポイントリストIDを設定するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param demand_measurepoint_list_id 需要計測ポイントリストID
     */
    public void setDemandMeasurepointListId(Integer demand_measurepoint_list_id) {
        this.demand_measurepoint_list_id = demand_measurepoint_list_id;
    }

    /**
     * 発電計測ポイントリストIDを取得するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @return 発電計測ポイントリストID
     */
    public Integer getGenerationMeasurepointListId() {
        return generation_measurepoint_list_id;
    }

    /**
     * 発電計測ポイントリストIDを設定するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param generation_measurepoint_list_id 発電計測ポイントリストID
     */
    public void setGenerationMeasurepointListId(Integer generation_measurepoint_list_id) {
        this.generation_measurepoint_list_id = generation_measurepoint_list_id;
    }

    /**
     * GMU-IDを取得するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @return GMU-ID
     */
    public String getGmuId() {
        return gmu_id;
    }

    /**
     * GMU-IDを設定するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param gmu_id GMU-ID
     */
    public void setGmuId(String gmu_id) {
        this.gmu_id = gmu_id;
    }

    /**
     * SoC設定変更要否設定を取得するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @return SoC設定変更要否設定
     */
    public Integer getSocModSetting() {
        return soc_mod_setting;
    }

    /**
     * SoC設定変更要否設定を設定するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param soc_mod_setting SoC設定変更要否設定
     */
    public void setSocModSetting(Integer soc_mod_setting) {
        this.soc_mod_setting = soc_mod_setting;
    }
}
