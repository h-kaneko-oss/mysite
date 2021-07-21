/*
 * TDemandGroup.java
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

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

/**
 * DRグループテーブルを表すクラス. <br>
 * <b>【注意】</b> 特になし.
 *
 * @author naya
 */
public final class TDemandGroup {

    /** DRグループID */
    private Integer drgroupid;
    /** DRグループ名 */
    private String name;
    /** リソースID(エリア情報) */
    private String[] resourceid;

    /**
     * コンストラクタ. <br>
     * <b>【注意】</b> 特になし.
     */
    public TDemandGroup() {
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
     * DRグループIDを取得するメソッド.
     * <br>
     * <b>【注意】</b>
     * 特になし.
     *
     * @return DRグループID
     */
    public Integer getDrgroupid() {
        return drgroupid;
    }

    /**
     * DRグループIDを設定するメソッド.
     * <br>
     * <b>【注意】</b>
     * 特になし.
     *
     * @param drgroupid DRグループID
     */
    public void setDrgroupid(Integer drgroupid) {
        this.drgroupid = drgroupid;
    }

    /**
     * DRグループ名を取得するメソッド.
     * <br>
     * <b>【注意】</b>
     * 特になし.
     *
     * @return DRグループ名
     */
    public String getName() {
        return name;
    }

    /**
     * DRグループ名を設定するメソッド.
     * <br>
     * <b>【注意】</b>
     * 特になし.
     *
     * @param name DRグループ名
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * リソースIDを取得するメソッド.
     * <br>
     * <b>【注意】</b>
     * 特になし.
     *
     * @return リソースID
     */
    public String[] getResourceid() {
        return resourceid;
    }

    /**
     * リソースIDを設定するメソッド.
     * <br>
     * <b>【注意】</b>
     * 特になし.
     *
     * @param resourceid リソースID
     */
    public void setResourceid(String[] resourceid) {
        this.resourceid = resourceid;
    }

}
