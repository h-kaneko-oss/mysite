/*
 * GmsConfig.java
 * Created on 2014/12/09
 * RCSID: $Id$
 *
 * (C) Copyright NTT-F 2014 All rights reserved.
 *
 * This software is furnished under a contract and use, duplication, disclosure
 * and all other uses are restricted to the rights specified in the written
 * contract and memorandum between the contractor and NTT-F.
 */
package jp.co.nttf.gms.common.internal;

import java.util.List;
import java.util.Map;

import jp.co.nttf.gms.common.internal.data.KeyValueInfo;

/**
 *  GMS設定情報クラス. <br>
 * <b>【注意】</b> 特になし.
 *
 * @author naya
 */
public final class GmsConfig extends Configuration {

    /** プロパティのMap */
    private Map<String, String> configMap;


    /**
     * コンストラクタ. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param list 設定情報のリスト
     */
    public GmsConfig(List<KeyValueInfo> list) {
        this.configMap = AppUtil.list2Map(list);
    }

    /**
     * {@inheritDoc}
     *
     * @see jp.co.nttf.gms.common.internal.Configuration#getValue(java.lang.String)
     */
    @Override
    protected String getValue(String key) {
        return this.configMap.get(key);
    }

}
