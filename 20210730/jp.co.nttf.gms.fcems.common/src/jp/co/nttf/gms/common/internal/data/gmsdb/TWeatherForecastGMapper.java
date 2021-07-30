/*
 * TWeatherForecastGMapper.java
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
import java.util.List;

import org.apache.ibatis.annotations.Param;

/**
 * TWeatherForecastGクラスのMapperインタフェース. <br>
 * <b>【注意】</b> 特になし.
 *
 * @author sekiya
 */
public interface TWeatherForecastGMapper {

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
     * @return 気象情報 (地上面)
     */
    List<TWeatherForecastG> selectAll();

    /**
     *
     * 特定の配信における登録件数を取得する. <br>
     * <b>【注意】</b> 特になし.
     *
     * @return 指定された配信日時で登録されているデータ件数
     */
    Integer countDataInDistribution(@Param("distributionTime") Timestamp distributionTime);

    /**
     *
     * 指定時間の気象位置情報のデータを取得. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param locationid 気象位置情報ID
     * @param forecast_time 指定時間
     * @return 取得結果
     */
    TWeatherForecastG getDataWithForecastTime(@Param("locationId") int locationId,
            @Param("forecastTime") Timestamp forecastTime);
}
