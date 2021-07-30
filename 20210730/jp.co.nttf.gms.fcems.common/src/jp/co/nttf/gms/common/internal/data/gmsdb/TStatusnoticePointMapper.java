/*
 * TStatusnoticePointMapper.java
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

import org.apache.ibatis.annotations.Param;

/**
 *
 * TStatusnoticePointクラスのMapperインタフェース. <br>
 * <b>【注意】</b> 特になし.
 *
 * @author ninomiya
 */
public interface TStatusnoticePointMapper {

    /**
     *
     * 指定したメッセージNo間のうち最大メッセージNoの接点入力情報を1件取得する. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param measurepointid 計測ポイントID
     * @param minmsgno 最少メッセージNo
     * @param maxmsgno 最大メッセージNo
     * @return 接点入力情報
     */
    TStatusnoticePoint getStatusnoticePointForMsgNo(
            @Param("measurepointid") int measurepointid,
            @Param("minmsgno") int minmsgno,
            @Param("maxmsgno") int maxmsgno);

    /**
     *
     * 最新のメッセージNoを取得する. <br>
     * <b>【注意】</b> 特になし.
     *
     * @return メッセージNo
     */
    Integer getLastMessageNo();

}
