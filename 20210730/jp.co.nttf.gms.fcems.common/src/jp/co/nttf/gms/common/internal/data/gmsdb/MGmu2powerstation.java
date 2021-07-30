/*
 * MGmu2powerstation.java
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

import java.sql.Time;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

/**
 *
 * m_gmu2powerstationテーブルを表すクラス. <br>
 * <b>【注意】</b> 特になし.
 *
 * @author ninomiya
 */
public class MGmu2powerstation {

    /** GMU-ID */
    private String gmuid;
    /** 発電所ID */
    private Integer powerstationid;
    /** 制御時刻 */
    private Time controltime;

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
     * gmuidを取得するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @return gmuid
     */
    public String getGmuid() {
        return gmuid;
    }

    /**
     * gmuidを設定するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param gmuid GMU-ID
     */
    public void setGmuid(String gmuid) {
        this.gmuid = gmuid;
    }

    /**
     * powerstationidを取得するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @return powerstationid
     */
    public Integer getPowerstationid() {
        return powerstationid;
    }

    /**
     * powerstationidを設定するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param powerstationid 発電所ID
     */
    public void setPowerstationid(Integer powerstationid) {
        this.powerstationid = powerstationid;
    }

    /**
     * controltimeを取得するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @return controltime
     */
    public Time getControltime() {
        return controltime;
    }

    /**
     * controltimeを設定するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param controltime 制御時刻
     */
    public void setControltime(Time controltime) {
        this.controltime = controltime;
    }

}
