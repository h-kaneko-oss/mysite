/*
 * TOadrDistribution.java
 * Created on 2015/07/13
 * RCSID: $Id$
 *
 * (C) Copyright NTT-F 2015 All rights reserved.
 *
 * This software is furnished under a contract and use, duplication, disclosure
 * and all other uses are restricted to the rights specified in the written
 * contract and memorandum between the contractor and NTT-F.
 */
package jp.co.nttf.gms.common.internal.data.gmsdb;

import java.sql.Timestamp;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

/**
 * OpenADR配信管理テーブルを表すクラス. <br>
 * <b>【注意】</b> 特になし.
 *
 * @author naya
 */
public final class TOadrDistribution extends TOadrSchedule {

    /** スケジュールID：強制配信 */
    public static final int SCHEDULEID_FORCED = -1;

    /** ステータス：未配信 */
    public static final short STATUS_UNDISTRIBUTED = 0;
    /** ステータス：配信済 */
    public static final short STATUS_DISTRIBUTED = 1;
    /** ステータス：反映成功 */
    public static final short STATUS_DISTRIBUTION_SUCCESS = 2;
    /** ステータス：反映失敗 */
    public static final short STATUS_DISTRIBUTION_FAILED = 3;

    /** GMU-ID */
    private String userid;
    /** リクエストID */
    private String requestid;
    /** リクエスト日時 */
    private Timestamp requesttime;
    /** 更新情報 */
    private Integer modificationnum;
    /** ベースライン */
    private Double baseline;
    /** 優先度 */
    private Integer priority;
    /** 指示値 */
    private Double instruction;

    /**
     * コンストラクタ. <br>
     * <b>【注意】</b> 特になし.
     */
    public TOadrDistribution() {
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
     * GMU-IDを取得するメソッド.
     * <br>
     * <b>【注意】</b>
     * 特になし.
     *
     * @return GMU-ID
     */
    public String getUserid() {
        return userid;
    }

    /**
     * GMU-IDを設定するメソッド.
     * <br>
     * <b>【注意】</b>
     * 特になし.
     *
     * @param userid GMU-ID
     */
    public void setUserid(String userid) {
        this.userid = userid;
    }

    /**
     * リクエストIDを取得するメソッド.
     * <br>
     * <b>【注意】</b>
     * 特になし.
     *
     * @return リクエストID
     */
    public String getRequestid() {
        return requestid;
    }

    /**
     * リクエストIDを設定するメソッド.
     * <br>
     * <b>【注意】</b>
     * 特になし.
     *
     * @param requestid リクエストID
     */
    public void setRequestid(String requestid) {
        this.requestid = requestid;
    }

    /**
     * リクエスト日時を取得するメソッド.
     * <br>
     * <b>【注意】</b>
     * 特になし.
     *
     * @return リクエスト日時
     */
    public Timestamp getRequesttime() {
        return requesttime;
    }

    /**
     * リクエスト日時を設定するメソッド.
     * <br>
     * <b>【注意】</b>
     * 特になし.
     *
     * @param requesttime リクエスト日時
     */
    public void setRequesttime(Timestamp requesttime) {
        this.requesttime = requesttime;
    }

    /**
     * 更新情報を取得するメソッド.
     * <br>
     * <b>【注意】</b>
     * 特になし.
     *
     * @return 更新情報
     */
    public Integer getModificationnum() {
        return modificationnum;
    }

    /**
     * 更新情報を設定するメソッド.
     * <br>
     * <b>【注意】</b>
     * 特になし.
     *
     * @param modificationnum 更新情報
     */
    public void setModificationnum(Integer modificationnum) {
        this.modificationnum = modificationnum;
    }

    /**
     * ベースラインを取得するメソッド.
     * <br>
     * <b>【注意】</b>
     * 特になし.
     *
     * @return ベースライン
     */
    public Double getBaseline() {
        return baseline;
    }

    /**
     * ベースラインを設定するメソッド.
     * <br>
     * <b>【注意】</b>
     * 特になし.
     *
     * @param baseline ベースライン
     */
    public void setBaseline(Double baseline) {
        this.baseline = baseline;
    }

    /**
     * 優先度を取得するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @return 優先度
     */
    public Integer getPriority() {
        return priority;
    }

    /**
     * 優先度を設定するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param priority 優先度
     */
    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    /**
     * 指示値を取得するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @return 指示値
     */
    public Double getInstruction() {
        return instruction;
    }

    /**
     * 指示値を設定するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param instruction 指示値
     */
    public void setInstruction(Double instruction) {
        this.instruction = instruction;
    }

}
