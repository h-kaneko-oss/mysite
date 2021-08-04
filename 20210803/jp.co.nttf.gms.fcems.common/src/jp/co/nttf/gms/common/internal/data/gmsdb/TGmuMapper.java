/*
 * TGmuMapper.java
 * Created on 2015/07/22
 * RCSID: $Id$
 *
 * (C) Copyright NTT-F 2015 All rights reserved.
 *
 * This software is furnished under a contract and use, duplication, disclosure
 * and all other uses are restricted to the rights specified in the written
 * contract and memorandum between the contractor and NTT-F.
 */
package jp.co.nttf.gms.common.internal.data.gmsdb;

import java.util.Date;

import org.apache.ibatis.annotations.Param;

/**
 * TGmuクラスのMapperインタフェース. <br>
 * <b>【注意】</b> 特になし.
 *
 * @author naya
 */
public interface TGmuMapper {

    /**
     * 指定したGMUの計測データ受信時刻を取得するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param gmuId GMU-ID
     * @return 計測データ受信時刻
     */
    Date selectRecvtime(@Param("gmuId") String gmuId);

    /**
     *
     * 指定したGMU-IDの情報を取得する. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param gmuId GMU-ID
     * @return GMU-ID情報
     */
    TGmu getTargetGmuId(@Param("gmuId") String gmuId);

}
