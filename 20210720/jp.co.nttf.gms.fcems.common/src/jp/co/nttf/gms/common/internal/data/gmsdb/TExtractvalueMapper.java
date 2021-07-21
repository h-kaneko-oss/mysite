/*
 * TExtractvalueMapper.java
 * Created on 2014/12/09
 * RCSID: $Id$
 *
 * (C) Copyright NTT-F 2014 All rights reserved.
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
 * TExtractvalueクラスのMapperインタフェース. <br>
 * <b>【注意】</b> 特になし.
 *
 * @author naya
 */
public interface TExtractvalueMapper {

    /**
     * 指定された日時の需要予測値を取得するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param paramObject パラメータオブジェクト
     * @return 取得結果
     */
    List<TExtractvalue> selectCustomerPrediction(Object paramObject);

    /**
     * 指定された日時の発電予測値を取得するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param paramObject パラメータオブジェクト
     * @return 取得結果
     */
    List<TExtractvalue> selectGeneratorPrediction(Object paramObject);

    /**
     * 指定された期間の需要予測値を取得するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param gmuList GMU-IDのリスト
     * @param mpList 計測ポイントIDのリスト
     * @param paramObject パラメータオブジェクト
     * @return 取得結果
     */
    List<TExtractvalue> selectPrediction(@Param("gmuList") List<String> gmuList,
            @Param("mpList") List<Integer> mpList,
            @Param("param") Object paramObject);

    /**
     * 指定された計測ポイントの最新値を取得するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param paramObject パラメータオブジェクト
     * @return 取得結果
     */
    TExtractvalue selectLatest(Object paramObject);

    /**
     *
     * 発電所に紐付く計測ポイントの指定時間の電力量合計値を取得. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param powerstationid 発電所ID
     * @param startDate 指定時間
     * @return 計測値合計
     */
    Double getElectricPowerValue(@Param("powerstationid") int powerstationid,
            @Param("startDate") Timestamp startDate);

    /**
     *
     * 指定範囲時間内の指定計測ポイント、指定積算フラグのデータを取得. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param measurepointid 計測ポイントID
     * @param startDate 指定時間(開始)
     * @param endDate 指定時間(終了)
     * @param totalflag 積算フラグ
     * @return 取得結果
     */
    List<TExtractvalue> getRangeData(@Param("measurepointid") int measurepointid,
            @Param("startDate") Timestamp startDate,
            @Param("endDate") Timestamp endDate,
            @Param("totalflag") int totalflag);

}
