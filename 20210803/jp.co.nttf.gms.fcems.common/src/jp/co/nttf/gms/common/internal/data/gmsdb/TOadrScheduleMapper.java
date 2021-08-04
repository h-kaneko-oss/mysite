/*
 * TOadrScheduleMapper.java
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

import jp.co.nttf.gms.common.internal.data.ControlInfo;

import org.apache.ibatis.annotations.Param;

/**
 * TOadrScheduleクラスのMapperインタフェース. <br>
 * <b>【注意】</b> 特になし.
 *
 * @author naya
 */
public interface TOadrScheduleMapper {

    /**
     * 既存レコードを無効にするメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param paramObject パラメータオブジェクト
     * @return 更新件数
     */
    int disableAll(Object paramObject);

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
     * @return 挿入件数
     */
    int update(Object paramObject);

    /**
     * レコードを取得するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param paramObject パラメータオブジェクト
     * @return 取得結果
     */
    List<TOadrSchedule> select(Object paramObject);

    /**
     * 対象日直近のモードNoを取得するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param paramObject パラメータオブジェクト
     * @return 対象日直近のモードNo(存在しない場合はnull)
     */
    Integer selectNearnessMode(Object paramObject);

    /**
     * 同時刻開始スケジュールを取得するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param paramObject パラメータオブジェクト
     * @return 取得結果
     */
    TOadrSchedule selectSameStartSchedule(Object paramObject);

    /**
     * 対象日に有効なスケジュールのリストを取得するメソッド. <br>
     * <b>【注意】</b> モードNo＞0のスケジュールのみを取得. <br>
     * また、手動DR,分散DRを除く.
     *
     * @param paramObject パラメータオブジェクト
     * @return 取得結果
     */
    List<TOadrSchedule> selectActiveScheduleList(Object paramObject);

    /**
     * 制御種別（CEMSDR)の情報を取得するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param startTime 開始時間
     * @return 取得結果
     */
    List<ControlInfo> selectControlInfoList(
            @Param("startTime") Timestamp startTime);
}
