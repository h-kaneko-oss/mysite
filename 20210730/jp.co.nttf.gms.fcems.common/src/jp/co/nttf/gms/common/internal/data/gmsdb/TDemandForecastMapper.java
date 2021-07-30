/*
 * TDemandForecastMapper.java
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
import java.util.List;

import org.apache.ibatis.annotations.Param;

/**
 * TDemandForecastクラスのMapperインタフェース. <br>
 * <b>【注意】</b> 特になし.
 *
 * @author sekiya
 */
public interface TDemandForecastMapper {

    /**
     * レコードの挿入を行うメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param paramObject パラメータオブジェクト
     * @return 挿入件数
     */
    int insert(Object paramObject);

    /**
     *
     * 全件取得する. <br>
     * <b>【注意】</b> 特になし.
     *
     * @return 需要予測
     */
    List<TDemandForecast> selectAll();

    /**
     *
     * 特定の配信における登録件数を取得する. <br>
     * <b>【注意】</b> 特になし.
     *
     * @return 指定された実施日時で登録されているデータ件数
     */
    Integer countDataInCalc(@Param("calcTime") Timestamp calcTime);

    /**
     *
     * 指定時間の需要予測のデータを取得. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param d_g_location_id 需要発電計測ポイントID
     * @param forecast_time 指定時間
     * @return 取得結果
     */
    TDemandForecast getDataWithForecastTime(@Param("d_g_location_id") int d_g_location_id,
            @Param("forecastTime") Timestamp forecastTime);

    /**
     *
     * 特定の配信におけるデータを取得する. <br>
     * <b>【注意】</b> 特になし.
     *
     * @return 取得結果
     */
    List<TDemandForecast> getDataWithCalcTime(@Param("d_g_location_id") int d_g_location_id,
            @Param("calcTime") Timestamp calcTime);
}
