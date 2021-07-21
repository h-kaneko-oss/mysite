/*
 * TGenerationMeasurepointList.java
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
 * 発電計測ポイントリストテーブルを表すクラス. <br>
 * <b>【注意】</b> 特になし.
 *
 * @author sekiya
 */
public final class TGenerationMeasurepointList {

    /** 発電計測ポイントリストID */
    private Integer generation_measurepoint_list_id = null;

    /** 発電計測ポイントID */
    private Integer[] generation_measurepoint_id = null;

    /**
     * コンストラクタ. <br>
     * <b>【注意】</b> 特になし.
     */
    public TGenerationMeasurepointList() {
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
     * 発電計測ポイントリストIDを取得するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @return 発電発電計測ポイントID
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
     * 発電計測ポイントIDを取得するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @return 発電計測ポイントID
     */
    public Integer[] getGenerationMeasurepointId() {
        return generation_measurepoint_id;
    }

    /**
     * 発電計測ポイントIDを設定するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param generation_measurepoint_id 発電計測ポイントID
     */
    public void setGenerationMeasurepointId(Integer[] generation_measurepoint_id) {
        this.generation_measurepoint_id = generation_measurepoint_id;
    }
}
