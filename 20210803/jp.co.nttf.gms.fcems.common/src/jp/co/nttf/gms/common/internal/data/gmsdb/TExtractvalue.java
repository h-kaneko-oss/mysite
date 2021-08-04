/*
 * TExtractvalue.java
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
 * 抽出計測データテーブルを表すクラス. <br>
 * <b>【注意】</b> 特になし.
 *
 * @author naya
 */
public final class TExtractvalue {

    /** 積算フラグ：瞬時値データ */
    public static final int TOTALFLAG_INSTANTANEOUS = 0;
    /** 積算フラグ：積算値データ */
    public static final int TOTALFLAG_INTEGRATION = 1;
    /** 積算フラグ：デマンド電力 */
    public static final int TOTALFLAG_DEMAND = 2;
    /** 積算フラグ：需要予測 */
    public static final int TOTALFLAG_CUSTOMER = 3;
    /** 積算フラグ：発電予測 */
    public static final int TOTALFLAG_GENERATOR = 4;

    /** テーブル名 */
    private String tableName = "t_extractvalue";

    /** 計測ポイントID */
    private Integer measurepointid;
    /** 計測時間 */
    private Timestamp measuretime;
    /** 計測値 */
    private Double measurevalue;
    /** 積算フラグ */
    private Integer totalflag = TOTALFLAG_INSTANTANEOUS;
    /** 平均数 */
    private Short numofavrg = 1;

    // オリジナル

    /** 計測時間(終了) */
    private Timestamp measuretimeEnd;
    /** オプション値(調達量) */
    private Double optionalvalue;
    /** GMU-ID */
    private String gmuId;

    /**
     * コンストラクタ. <br>
     * <b>【注意】</b> 特になし.
     */
    public TExtractvalue() {
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
     * テーブル名を取得するメソッド.
     * <br>
     * <b>【注意】</b>
     * 特になし.
     *
     * @return テーブル名
     */
    public String getTableName() {
        return this.tableName;
    }

    /**
     * テーブル名を設定するメソッド.
     * <br>
     * <b>【注意】</b>
     * 特になし.
     *
     * @param tableName テーブル名
     */
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    /**
     * 計測ポイントIDを取得するメソッド.
     * <br>
     * <b>【注意】</b>
     * 特になし.
     *
     * @return 計測ポイントID
     */
    public Integer getMeasurepointid() {
        return this.measurepointid;
    }

    /**
     * 計測ポイントIDを設定するメソッド.
     * <br>
     * <b>【注意】</b>
     * 特になし.
     *
     * @param measurepointid 計測ポイントID
     */
    public void setMeasurepointid(Integer measurepointid) {
        this.measurepointid = measurepointid;
    }

    /**
     * 計測時間を取得するメソッド.
     * <br>
     * <b>【注意】</b>
     * 特になし.
     *
     * @return 計測時間
     */
    public Timestamp getMeasuretime() {
        return this.measuretime;
    }

    /**
     * 計測時間を設定するメソッド.
     * <br>
     * <b>【注意】</b>
     * 特になし.
     *
     * @param measuretime 計測時間
     */
    public void setMeasuretime(Timestamp measuretime) {
        this.measuretime = measuretime;
    }

    /**
     * 計測値を取得するメソッド.
     * <br>
     * <b>【注意】</b>
     * 特になし.
     *
     * @return 計測値
     */
    public Double getMeasurevalue() {
        return this.measurevalue;
    }

    /**
     * 計測値を設定するメソッド.
     * <br>
     * <b>【注意】</b>
     * 特になし.
     *
     * @param measurevalue 計測値
     */
    public void setMeasurevalue(Double measurevalue) {
        this.measurevalue = measurevalue;
    }

    /**
     * 積算フラグを取得するメソッド.
     * <br>
     * <b>【注意】</b>
     * 特になし.
     *
     * @return 積算フラグ
     */
    public Integer getTotalflag() {
        return this.totalflag;
    }

    /**
     * 積算フラグを設定するメソッド.
     * <br>
     * <b>【注意】</b>
     * 特になし.
     *
     * @param totalflag 積算フラグ
     */
    public void setTotalflag(Integer totalflag) {
        this.totalflag = totalflag;
    }

    /**
     * 平均数を取得するメソッド.
     * <br>
     * <b>【注意】</b>
     * 特になし.
     *
     * @return 平均数
     */
    public Short getNumofavrg() {
        return this.numofavrg;
    }

    /**
     * 平均数を設定するメソッド.
     * <br>
     * <b>【注意】</b>
     * 特になし.
     *
     * @param numofavrg 平均数
     */
    public void setNumofavrg(Short numofavrg) {
        this.numofavrg = numofavrg;
    }

    /**
     * 計測時間(終了)を取得するメソッド.
     * <br>
     * <b>【注意】</b>
     * 特になし.
     *
     * @return 計測時間(終了)
     */
    public Timestamp getMeasuretimeEnd() {
        return this.measuretimeEnd;
    }

    /**
     * 計測時間(終了)を設定するメソッド.
     * <br>
     * <b>【注意】</b>
     * 特になし.
     *
     * @param measuretimeEnd 計測時間(終了)
     */
    public void setMeasuretimeEnd(Timestamp measuretimeEnd) {
        this.measuretimeEnd = measuretimeEnd;
    }

    /**
     * オプション値を取得するメソッド.
     * <br>
     * <b>【注意】</b>
     * 特になし.
     *
     * @return オプション値
     */
    public Double getOptionalvalue() {
        return optionalvalue;
    }

    /**
     * オプション値を設定するメソッド.
     * <br>
     * <b>【注意】</b>
     * 特になし.
     *
     * @param optionalvalue オプション値
     */
    public void setOptionalvalue(Double optionalvalue) {
        this.optionalvalue = optionalvalue;
    }

    /**
     * GMU-IDを取得するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @return GMU-ID
     */
    public String getGmuId() {
        return gmuId;
    }

    /**
     * GMU-IDを設定するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param gmuId GMU-ID
     */
    public void setGmuId(String gmuId) {
        this.gmuId = gmuId;
    }

}
