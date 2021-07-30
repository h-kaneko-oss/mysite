/*
 * TPowerstation.java
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
import java.sql.Timestamp;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

/**
 *
 * t_powerstationテーブルを表すクラス. <br>
 * <b>【注意】</b> 特になし.
 *
 * @author ninomiya
 */
public class TPowerstation {

    /** 発電所ID */
    private Integer powerstationid;
    /** 名称 */
    private String name;
    /** 判定開始時刻 */
    private Time judgestarttime;
    /** 判定終了時刻 */
    private Time judgeendtime;
    /** 閾値1 */
    private double threshold1;
    /** 閾値2 */
    private double threshold2;
    /** 閾値3 */
    private double threshold3;
    /** 最終確認時間 */
    private Timestamp lastchecktime;

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
     * nameを取得するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * nameを設定するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param name 名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * judgestarttimeを取得するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @return judgestarttime
     */
    public Time getJudgestarttime() {
        return judgestarttime;
    }

    /**
     * judgestarttimeを設定するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param judgestarttime 判定開始時刻
     */
    public void setJudgestarttime(Time judgestarttime) {
        this.judgestarttime = judgestarttime;
    }

    /**
     * judgeendtimeを取得するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @return judgeendtime
     */
    public Time getJudgeendtime() {
        return judgeendtime;
    }

    /**
     * judgeendtimeを設定するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param judgeendtime 判定終了時刻
     */
    public void setJudgeendtime(Time judgeendtime) {
        this.judgeendtime = judgeendtime;
    }

    /**
     * threshold1を取得するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @return threshold1
     */
    public double getThreshold1() {
        return threshold1;
    }

    /**
     * threshold1を設定するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param threshold1 閾値1
     */
    public void setThreshold1(double threshold1) {
        this.threshold1 = threshold1;
    }

    /**
     * threshold2を取得するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @return threshold2
     */
    public double getThreshold2() {
        return threshold2;
    }

    /**
     * threshold2を設定するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param threshold2 閾値2
     */
    public void setThreshold2(double threshold2) {
        this.threshold2 = threshold2;
    }

    /**
     * threshold3を取得するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @return threshold3
     */
    public double getThreshold3() {
        return threshold3;
    }

    /**
     * threshold3を設定するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param threshold3 閾値3
     */
    public void setThreshold3(double threshold3) {
        this.threshold3 = threshold3;
    }

    /**
     * lastchecktimeを取得するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @return lastchecktime
     */
    public Timestamp getLastchecktime() {
        return lastchecktime;
    }

    /**
     * lastchecktimeを設定するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param lastchecktime 最終確認時間
     */
    public void setLastchecktime(Timestamp lastchecktime) {
        this.lastchecktime = lastchecktime;
    }

}
