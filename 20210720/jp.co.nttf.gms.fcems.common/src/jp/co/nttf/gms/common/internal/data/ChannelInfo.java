/*
 * ChannelInfo.java
 * Created on 2015/07/22
 * RCSID: $Id$
 *
 * (C) Copyright NTT-F 2015 All rights reserved.
 *
 * This software is furnished under a contract and use, duplication, disclosure
 * and all other uses are restricted to the rights specified in the written
 * contract and memorandum between the contractor and NTT-F.
 */
package jp.co.nttf.gms.common.internal.data;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

/**
 * チャンネル情報を表すクラス. <br>
 * <b>【注意】</b> 特になし.
 *
 * @author naya
 */
public final class ChannelInfo {

    /** 計測ポイントID */
    private Integer measurepointid;
    /** 配線仕様ID */
    private Integer wiringid;
    /** 最大積算値 */
    private Double maxmultiplicationvalue;

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
     * 計測ポイントIDを取得するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @return 計測ポイントID
     */
    public Integer getMeasurepointid() {
        return measurepointid;
    }

    /**
     * 計測ポイントIDを設定するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param measurepointid 計測ポイントID
     */
    public void setMeasurepointid(Integer measurepointid) {
        this.measurepointid = measurepointid;
    }

    /**
     * 配線仕様IDを取得するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @return 配線仕様ID
     */
    public Integer getWiringid() {
        return wiringid;
    }

    /**
     * 配線仕様IDを設定するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param wiringid 配線仕様ID
     */
    public void setWiringid(Integer wiringid) {
        this.wiringid = wiringid;
    }

    /**
     * 最大積算値を取得するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @return 最大積算値
     */
    public Double getMaxmultiplicationvalue() {
        return maxmultiplicationvalue;
    }

    /**
     * 最大積算値を設定するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param maxmultiplicationvalue 最大積算値
     */
    public void setMaxmultiplicationvalue(Double maxmultiplicationvalue) {
        this.maxmultiplicationvalue = maxmultiplicationvalue;
    }

}
