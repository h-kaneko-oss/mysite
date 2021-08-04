/*
 * TOadrCapacityMapper.java
 * Created on 2016/09/07
 * RCSID: $Id$
 *
 * (C) Copyright NTT-F 2016 All rights reserved.
 *
 * This software is furnished under a contract and use, duplication, disclosure
 * and all other uses are restricted to the rights specified in the written
 * contract and memorandum between the contractor and NTT-F.
 */
package jp.co.nttf.gms.common.internal.data.gmsdb;

/**
 * TOadrCapacityクラスのMapperインタフェース. <br>
 * <b>【注意】</b> 特になし.
 *
 * @author naya
 */
public interface TOadrCapacityMapper {

    /**
     * レコードを取得するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param paramObject パラメータオブジェクト
     * @return 取得結果
     */
    TOadrCapacity selectOne(Object paramObject);

}
