/*
 * TMeasurevalue.java
 * Created on 2014/12/09
 * RCSID: $Id$
 *
 * (C) Copyright NTT-F 2014 All rights reserved.
 *
 * This software is furnished under a contract and use, duplication, disclosure
 * and all other uses are restricted to the rights specified in the written
 * contract and memorandum between the contractor and NTT-F.
 */
package jp.co.nttf.gms.common.internal.data.gmsdb;

import java.sql.Timestamp;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

/**
 * 計測データテーブルを表すクラス. <br>
 * <b>【注意】</b> 特になし.
 *
 * @author naya
 */
public final class TMeasurevalue {

    /** 計測ポイントID */
    private Integer measurepointid = null;
    /** 計測時間 */
    private Timestamp measuretime = null;
    /** CH */
    private String[] ch = null;

    // オリジナル

    /** 計測時間(開始) */
    private Timestamp measuretimeStart;
    /** 計測時間(終了) */
    private Timestamp measuretimeEnd;
    /** 電力量文字列 */
    private String electric;
    /** 乗率文字列 */
    private String multi;

    /**
     * コンストラクタ. <br>
     * <b>【注意】</b> 特になし.
     */
    public TMeasurevalue() {
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
     * 計測ポイントIDを取得するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @return 計測ポイントID
     */
    public Integer getMeasurepointid() {
        return this.measurepointid;
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
     * 計測時間を取得するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @return 計測時間
     */
    public Timestamp getMeasuretime() {
        return this.measuretime;
    }

    /**
     * 計測時間を設定するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param measuretime 計測時間
     */
    public void setMeasuretime(Timestamp measuretime) {
        this.measuretime = measuretime;
    }

    /**
     * CHを取得するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @return CH
     */
    public String[] getCh() {
        return this.ch;
    }

    /**
     * CHを設定するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param ch CH
     */
    public void setCh(String[] ch) {
        this.ch = ch;
    }

    /**
     * 計測時間(開始)を取得するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @return 計測時間(開始)
     */
    public Timestamp getMeasuretimeStart() {
        return measuretimeStart;
    }

    /**
     * 計測時間(開始)を設定するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param measuretimeStart 計測時間(開始)
     */
    public void setMeasuretimeStart(Timestamp measuretimeStart) {
        this.measuretimeStart = measuretimeStart;
    }

    /**
     * 計測時間(終了)を取得するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @return 計測時間(終了)
     */
    public Timestamp getMeasuretimeEnd() {
        return measuretimeEnd;
    }

    /**
     * 計測時間(終了)を設定するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param measuretimeEnd 計測時間(終了)
     */
    public void setMeasuretimeEnd(Timestamp measuretimeEnd) {
        this.measuretimeEnd = measuretimeEnd;
    }

    /**
     * 電力量文字列を取得するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @return 電力量文字列
     */
    public String getElectric() {
        return electric;
    }

    /**
     * 電力量文字列を設定するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param electric 電力量文字列
     */
    public void setElectric(String electric) {
        this.electric = electric;
    }

    /**
     * 乗率文字列を取得するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @return 乗率文字列
     */
    public String getMulti() {
        return multi;
    }

    /**
     * 乗率文字列を設定するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param multi 乗率文字列
     */
    public void setMulti(String multi) {
        this.multi = multi;
    }

}
