/*
 * TDemandInfoMapper.java
 * Created on 2015/03/06
 * RCSID: $Id$
 *
 * (C) Copyright NTT-F 2015 All rights reserved.
 *
 * This software is furnished under a contract and use, duplication, disclosure
 * and all other uses are restricted to the rights specified in the written
 * contract and memorandum between the contractor and NTT-F.
 */
package jp.co.nttf.gms.common.internal.data.gmsdb;


/**
 * TOadrScheduleクラスのMapperインタフェース. <br>
 * <b>【注意】</b> 特になし.
 *
 * @author naya
 */
public interface TDemandInfoMapper {

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

}
