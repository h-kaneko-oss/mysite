/*
 * TDemandMeasurepointList.java
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
 * 需要計測ポイントリストテーブルを表すクラス. <br>
 * <b>【注意】</b> 特になし.
 *
 * @author sekiya
 */
public final class TDemandMeasurepointList {

    /** 需要計測ポイントリストID */
    private Integer demand_measurepoint_list_id = null;

    /** 需要計測ポイントID */
    private Integer[] demand_measurepoint_id = null;

    /**
     * コンストラクタ. <br>
     * <b>【注意】</b> 特になし.
     */
    public TDemandMeasurepointList() {
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
     * 需要計測ポイントリストIDを取得するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @return 需要発電計測ポイントID
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
     * 需要計測ポイントIDを取得するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @return 需要計測ポイントID
     */
    public Integer[] getDemandMeasurepointId() {
        return demand_measurepoint_id;
    }

    /**
     * 需要計測ポイントIDを設定するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param demand_measurepoint_id 需要計測ポイントID
     */
    public void setDemandMeasurepointId(Integer[] demand_measurepoint_id) {
        this.demand_measurepoint_id = demand_measurepoint_id;
    }
}
