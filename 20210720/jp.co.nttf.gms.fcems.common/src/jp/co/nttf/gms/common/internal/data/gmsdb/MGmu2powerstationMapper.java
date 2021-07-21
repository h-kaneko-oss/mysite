/*
 * MGmu2powerstationMapper.java
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

import java.util.List;

/**
 *
 * MGmu2powerstationテーブルのMapperインタフェース. <br>
 * <b>【注意】</b> 特になし.
 *
 * @author ninomiya
 */
public interface MGmu2powerstationMapper {

    /**
     *
     * 発電所IDに紐付くGMU-発電所を取得. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param powerstationid 発電所ID
     * @return GMU-発電所リスト
     */
    List<MGmu2powerstation> getGmu2PowerstationListByPowerId(int powerstationid);

}
