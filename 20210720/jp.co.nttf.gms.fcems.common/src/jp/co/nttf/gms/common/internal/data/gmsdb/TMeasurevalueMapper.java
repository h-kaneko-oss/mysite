/*
 * TMeasurevalueMapper.java
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
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import jp.co.nttf.gms.common.internal.data.ChannelInfo;


/**
 * TMeasurevalueクラスのMapperインタフェース. <br>
 * <b>【注意】</b> 特になし.
 *
 * @author naya
 */
public interface TMeasurevalueMapper {

    /**
     * 指定された期間の電力を取得するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param paramObject パラメータオブジェクト
     * @return 電力
     */
    Double selectPower(Object paramObject);

    /**
     * 指定された期間の電力量を取得するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param paramObject パラメータオブジェクト
     * @return 計測値Map("energy":電力量, "multipy":乗率, "max":最大積算値)
     */
    Map<String, Double> selectEnergy(Object paramObject);

    /**
     * 指定した条件に一致する計測データを取得するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param measurepointId 計測ポイントID
     * @param from 開始日時
     * @param to 終了日時
     * @param vidx 電力量のインデックス
     * @param midx 乗率のインデックス
     * @param margin 検索期間の前後余白[秒]
     * @return TMeasurevalueのリスト
     */
    List<TMeasurevalue> findByGmuIdAndPeriod(
            @Param("measurepointId") int measurepointId,
            @Param("from") Timestamp from,
            @Param("to") Timestamp to,
            @Param("vidx") int vidx,
            @Param("midx") int midx,
            @Param("margin") int margin
            );

    /**
     * 指定したGMU-ID配下の計測ポイント毎のチャンネルインデックスを取得するメソッド. <br>
     * 配線仕様に応じた電力量(vidx)および乗率(midx)のチャンネルインデックスと、<br>
     * 計測器型番に応じた最大積算値(maxmultiplicationvalue)を取得する. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param gmuid GMU-ID
     * @param idList 対象とする計測ポイントIDのリスト
     * @return 電力量および乗率のチャンネルインデックス情報
     */
    List<ChannelInfo> findEChannelIndexByGmuId(@Param("gmuid") String gmuid,
            @Param("idList") List<Integer> idList);

    /**
     * 指定したGMUの計測データ受信時刻を取得するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param gmuId GMU-ID
     * @return 計測データ受信時刻
     */
    Date selectLastMeasuretime(@Param("gmuId") String gmuId);

}
