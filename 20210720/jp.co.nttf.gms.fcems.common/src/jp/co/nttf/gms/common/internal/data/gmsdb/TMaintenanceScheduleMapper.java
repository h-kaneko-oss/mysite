/*
 * TMaintenanceScheduleMapper.java
 * Created on 2016/09/08
 * RCSID: $Id$
 *
 * (C) Copyright NTT-F 2016 All rights reserved.
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
 * TMaintenanceScheduleクラスのMapperインタフェース. <br>
 * <b>【注意】</b> 特になし.
 *
 * @author naya
 */
public interface TMaintenanceScheduleMapper {

    /**
     * メンテナンススケジュールの取得を行うメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param paramObject パラメータオブジェクト
     * @return 取得結果
     */
    List<TMaintenanceSchedule> selectMaintenance(Object paramObject);

    /**
     * メンテナンススケジュールを登録. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param obj パラメータオブジェクト
     */
    void insertMaintenance(Object obj);

    /**
     * 指定期間内の一括登録スケジュールを無効化するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param resourceid リソースID
     * @param param パラメータオブジェクト
     */
    void disableByBulkSchedule(@Param("resourceid") String resourceid,
            @Param("param") TMaintenanceSchedule param);

    /**
     * 「繰り返し開始日」が指定されるデータを取得する. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param startTime 開始時間
     * @param modeNo モードNo
     * @return 「繰り返し開始日」が指定されるデータ
     */
    List<ControlInfo> selectRepeatnessControlInfoList(
            @Param("startTime") Timestamp startTime, @Param("modeNo") int modeNo);

    /**
     * 「繰り返し開始日」が指定されるデータを取得する. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param week システム時間の曜日
     * @param startTime 開始時間
     * @param modeNo モードNo
     * @return 「繰り返し開始日」が指定されるデータ
     */
    List<ControlInfo> selectRepeatedControlInfoList(@Param("week") int week,
            @Param("startTime") Timestamp startTime, @Param("modeNo") int modeNo);
}
