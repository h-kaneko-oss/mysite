/*
 * TMaintenanceTemplateMapper.java
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

import org.apache.ibatis.annotations.Param;

/**
 *
 * TMaintenanceTemplateクラスのMapperインタフェース. <br>
 * <b>【注意】</b> 特になし.
 *
 * @author ninomiya
 */
public interface TMaintenanceTemplateMapper {

    /**
     *
     * 指定したGMU-ID、レベル、デバイスに紐付くテンプレートを取得. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param gmuid FMU-ID
     * @param level レベル
     * @param deviceName デバイス名
     * @return メンテナンステンプレートリスト
     */
    List<TMaintenanceTemplate> getTemplateByGmuIdLevel(
            @Param("gmuid")String gmuid,
            @Param("level")int level,
            @Param("deviceName")List<String> deviceName);

}
