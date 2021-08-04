/*
 * TMaintenanceDistributionMapper.java
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

/**
 *
 * TMaintenanceDistributionクラスのMapperインタフェース. <br>
 * 詳細説明文（オプション）. <br>
 * <b>【注意】</b> 特になし.
 *
 * @author ninomiya
 */
public interface TMaintenanceDistributionMapper {

    /**
     *
     * メンテナンス配信管理を登録する. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param obj パラメータオブジェクト
     */
    void insertDistribution(Object obj);

}
