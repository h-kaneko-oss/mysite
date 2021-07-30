/*
 * TOadrCapacity.java
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

import java.sql.Date;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

/**
 * OpenADR契約容量テーブルを表すクラス. <br>
 * <b>【注意】</b> 特になし.
 *
 * @author naya
 */
public final class TOadrCapacity {

    /** リソースID */
    private String resourceid = null;

    /** 対象月 */
    private Date targetmonth = null;

    /** 契約容量 */
    private Double capacity = null;

    /**
     * コンストラクタ. <br>
     * <b>【注意】</b> 特になし.
     */
    public TOadrCapacity() {
    }

    /**
     * オブジェクトの文字列表現を返すメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @return このオブジェクトの文字列表現
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return new ReflectionToStringBuilder(this).toString();
    }

    /**
     * リソースIDを取得するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @return リソースID
     */
    public String getResourceid() {
        return resourceid;
    }

    /**
     * リソースIDを設定するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param resourceid リソースID
     */
    public void setResourceid(String resourceid) {
        this.resourceid = resourceid;
    }

    /**
     * 対象月を取得するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @return 対象月
     */
    public Date getTargetmonth() {
        return targetmonth;
    }

    /**
     * 対象月を設定するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param targetmonth 対象月
     */
    public void setTargetmonth(Date targetmonth) {
        this.targetmonth = targetmonth;
    }

    /**
     * 契約容量を取得するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @return 契約容量
     */
    public Double getCapacity() {
        return capacity;
    }

    /**
     * 契約容量を設定するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param capacity 契約容量
     */
    public void setCapacity(Double capacity) {
        this.capacity = capacity;
    }

}
