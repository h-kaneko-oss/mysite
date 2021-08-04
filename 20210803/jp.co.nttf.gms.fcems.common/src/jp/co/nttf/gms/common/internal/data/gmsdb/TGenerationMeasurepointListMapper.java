/*
 * TGenerationMeasurepointListMapper.java
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

import java.util.List;

import org.apache.ibatis.annotations.Param;

/**
 * TGenerationMeasurepointListMapperクラスのMapperインタフェース. <br>
 * <b>【注意】</b> 特になし.
 *
 * @author sekiya
 */
public interface TGenerationMeasurepointListMapper {

    /**
     *
     * 全件取得する. <br>
     * <b>【注意】</b> 特になし.
     *
     * @return 発電計測ポイントリスト
     */
    List<TGenerationMeasurepointList> selectAll();

    /**
     *
     * 発電計測ポイントリストIDに対応する発電計測ポイントIDを取得する. <br>
     * <b>【注意】</b> 特になし.
     *
     * @return 発電計測ポイントリスト
     */
    TGenerationMeasurepointList selectByListId(@Param("generationMeasurepointListId") Integer generationMeasurepointListId);

}
