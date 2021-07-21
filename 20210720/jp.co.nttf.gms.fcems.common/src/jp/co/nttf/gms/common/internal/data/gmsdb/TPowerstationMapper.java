/*
 * TPowerstationMapper.java
 * Created on 2017/10/18
 * RCSID: $Id$
 *
 * (C) Copyright NTT-F 2017 All rights reserved.
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
 *
 * TPowerstationクラスのMapperインタフェース. <br>
 * <b>【注意】</b> 特になし.
 *
 * @author ninomiya
 */
public interface TPowerstationMapper {

    /**
     *
     * 発電所一覧取得. <br>
     * <b>【注意】</b> 特になし.
     *
     * @return 発電所一覧
     */
    List<TPowerstation> getPowerstationList();

    /**
     *
     * 最終確認時間を更新する. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param powerstationid 発電所ID
     * @param executeDate 最終確認時間
     */
    void updateLastCheckTime(@Param("powerstationid")int powerstationid,
            @Param("executeDate")Timestamp executeDate);

}
