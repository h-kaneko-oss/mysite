/*
 * TSimulationResultMapper.java
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
import java.util.List;

import org.apache.ibatis.annotations.Param;

/**
 * TSimulationResultクラスのMapperインタフェース. <br>
 * <b>【注意】</b> 特になし.
 *
 * @author sekiya
 */
public interface TSimulationResultMapper {

    /**
     * レコードの挿入を行うメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param paramObject パラメータオブジェクト
     * @return 挿入件数
     */
    int insert(Object paramObject);

    /**
     * レコードの更新を行うメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param paramObject パラメータオブジェクト
     */
    //void updateResult(@Param("dgLocationId")int dgLocationId,
    //        @Param("simulationTime")Timestamp simulationTime,
    //        @Param("scheduleId")int scheduleId);
    void updateResult(Object paramObject);

    /**
     *
     * 全件取得する. <br>
     * <b>【注意】</b> 特になし.
     *
     * @return シミュレーション情報
     */
    List<TSimulationResult> selectAll();

    /**
     *
     * 最新のデータを取得する. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param paramObject パラメータオブジェクト
     * @return シミュレーション情報
     */
    TSimulationResult selectLatest(Object paramObject);

    /**
     *
     * 特定のシミュレーションにおけるデータを取得する. <br>
     * <b>【注意】</b> 特になし.
     *
     * @return 取得結果
     */
    TSimulationResult getDataWithSimTime(@Param("dgLocationId") int dgLocationId,
            @Param("simulationTime") Timestamp simulationTime);
}
