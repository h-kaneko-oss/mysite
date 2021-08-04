/*
 * TOadrTransitions.java
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

import java.sql.Timestamp;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

/**
 * OpenADR状態遷移テーブルを表すクラス. <br>
 * <b>【注意】</b> 特になし.
 *
 * @author naya
 */
public final class TOadrTransitions {

    /** スケジュールID */
    private Integer scheduleid = null;

    /** 対象日時 */
    private Timestamp targettime = null;

    /** 放電計画値 */
    private Double plannedvalue = null;

    /** 推定DR達成率 */
    private Double achievement = null;

    /**
     * コンストラクタ. <br>
     * <b>【注意】</b> 特になし.
     */
    public TOadrTransitions() {
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
     * スケジュールIDを取得するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @return スケジュールID
     */
    public Integer getScheduleid() {
        return scheduleid;
    }

    /**
     * スケジュールIDを設定するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param scheduleid スケジュールID
     */
    public void setScheduleid(Integer scheduleid) {
        this.scheduleid = scheduleid;
    }

    /**
     * 対象日時を取得するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @return 対象日時
     */
    public Timestamp getTargettime() {
        return this.targettime;
    }

    /**
     * 対象日時を設定するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param targettime 対象日時
     */
    public void setTargettime(Timestamp targettime) {
        this.targettime = targettime;
    }

    /**
     * 放電計画値を取得するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @return 放電計画値
     */
    public Double getPlannedvalue() {
        return this.plannedvalue;
    }

    /**
     * 放電計画値を設定するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param plannedvalue 放電計画値
     */
    public void setPlannedvalue(Double plannedvalue) {
        this.plannedvalue = plannedvalue;
    }

    /**
     * 推定DR達成率を取得するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @return 推定DR達成率
     */
    public Double getAchievement() {
        return this.achievement;
    }

    /**
     * 推定DR達成率を設定するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param achievement 推定DR達成率
     */
    public void setAchievement(Double achievement) {
        this.achievement = achievement;
    }

}
