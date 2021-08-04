/*
 * TOadrDistributionMapper.java
 * Created on 2016/01/23
 * RCSID: $Id$
 *
 * (C) Copyright NTT-F 2016 All rights reserved.
 *
 * This software is furnished under a contract and use, duplication, disclosure
 * and all other uses are restricted to the rights specified in the written
 * contract and memorandum between the contractor and NTT-F.
 */
package jp.co.nttf.gms.common.internal.data.gmsdb;

import java.util.List;

import org.apache.ibatis.annotations.Param;


/**
 * m_gmu2resourceidテーブルのMapperインタフェース. <br>
 * <b>【注意】</b> 特になし.
 *
 * @author naya
 */
public interface MGmu2resourceidMapper {

    /**
     * 計測ポイントIDから対応するリソースIDを取得するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param measurepointId 計測ポイントID
     * @return 対象のリソースID、レコードが存在しなければnull
     */
    String selectByMeasurepoint(@Param("measurepointId") Integer measurepointId);

    /**
     * 対象のGMU-IDを取得するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param resourceid リソースID
     * @return 対象GMU-IDのリスト
     */
    List<String> selectGmuId(@Param("resourceid") String resourceid);

}
