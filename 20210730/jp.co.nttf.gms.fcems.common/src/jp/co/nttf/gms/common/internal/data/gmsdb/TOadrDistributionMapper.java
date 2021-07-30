/*
 * TOadrDistributionMapper.java
 * Created on 2015/07/13
 * RCSID: $Id$
 *
 * (C) Copyright NTT-F 2015 All rights reserved.
 *
 * This software is furnished under a contract and use, duplication, disclosure
 * and all other uses are restricted to the rights specified in the written
 * contract and memorandum between the contractor and NTT-F.
 */
package jp.co.nttf.gms.common.internal.data.gmsdb;

import java.util.List;
import java.util.Set;

import org.apache.ibatis.annotations.Param;

/**
 * TOadrDistributionクラスのMapperインタフェース. <br>
 * <b>【注意】</b> 特になし.
 *
 * @author naya
 */
public interface TOadrDistributionMapper {

    /**
     * 指定された配信管理情報を取得するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param paramObject パラメータオブジェクト
     * @return 取得結果のリスト
     */
    List<TOadrDistribution> select(Object paramObject);

    /**
     * 指定されたリソースIDに該当する配信用GMU-IDを取得するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param resourceid リソースID
     * @param gmuSet 配信用GMU-IDのセット
     * @return 取得結果のリスト
     */
    List<String> selectDistributionGmu(@Param("resourceid") String resourceid,
            @Param("gmuSet") Set<String> gmuSet);

    /**
     * 指定されたスケジュールに対する未挿入のレコード挿入を行うメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param paramObject パラメータオブジェクト
     * @return 挿入件数
     */
    int insertUnregistered(Object paramObject);

    /**
     * 算出用GMUかつ未挿入であるレコードの挿入を行うメソッド. <br>
     * <b>【注意】</b> DR配信の対象外となるようステータスを「配信成功」で挿入する.
     *
     * @param paramObject パラメータオブジェクト
     * @return 挿入件数
     */
    int insertMeasurementGmu(Object paramObject);

    /**
     * レコードの更新を行うメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param paramObject パラメータオブジェクト
     * @return 更新件数
     */
    int update(Object paramObject);

    /**
     * 指示値の保存を行うメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param paramObject パラメータオブジェクト
     * @return 更新件数
     */
    int saveInstructions(Object paramObject);

    /**
     * 直近の指示値をコピーするメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param paramObject パラメータオブジェクト
     * @return 更新件数
     */
    int copyInstructions(Object paramObject);

}
