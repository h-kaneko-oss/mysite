/*
 * MGmu2mailMapper.java
 * Created on 2019/01/22
 * RCSID: $Id$
 *
 * (C) Copyright NTT-F 2019 All rights reserved.
 *
 * This software is furnished under a contract and use, duplication, disclosure
 * and all other uses are restricted to the rights specified in the written
 * contract and memorandum between the contractor and NTT-F.
 */
package jp.co.nttf.gms.common.internal.data.gmsdb;

import java.util.List;

/**
 * MGmu2mailテーブルのMapperインタフェース. <br>
 * <b>【注意】</b> 特になし.
 *
 * @author sunnet
 */
public interface MGmu2mailMapper {
    /**
     * DR発動時メール送信先アドレス管理を取得. <br>
     * GMU-IDは必須<br>
     * <b>【注意】</b> 特になし.
     *
     * @param paramObject パラメータオブジェクト
     * @return 取得結果
     */
    List<MGmu2mail> getGmu2mailList(Object paramObject);
}
