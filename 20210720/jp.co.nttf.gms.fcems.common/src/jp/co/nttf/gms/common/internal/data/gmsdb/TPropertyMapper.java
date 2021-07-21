/*
 * TPropertyMapper.java
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

import java.util.List;

import jp.co.nttf.gms.common.internal.data.KeyValueInfo;

/**
 * 設定情報テーブル用Mapperインタフェース. <br>
 * <b>【注意】</b> 特になし.
 *
 * @author naya
 */
public interface TPropertyMapper {

    /**
     * 全レコードを取得するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @return 取得結果のリスト
     */
    List<KeyValueInfo> selectAll();

}
