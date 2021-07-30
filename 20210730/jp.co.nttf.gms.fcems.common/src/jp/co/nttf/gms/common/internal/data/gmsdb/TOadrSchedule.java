/*
 * TOadrSchedule.java
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
 * OpenADRスケジュール管理テーブルを表すクラス. <br>
 * <b>【注意】</b> 特になし.
 *
 * @author naya
 */
public class TOadrSchedule {

    /** ステータス：キャンセル */
    public static final short STATUS_CANCEL = -1;
    /** ステータス：有効 */
    public static final short STATUS_ENABLE = 0;
    /** ステータス：無効 */
    public static final short STATUS_DISABLE = 1;

    /** スケジュールID */
    private Integer scheduleid;
    /** モードNo */
    private Integer modeno;
    /** 開始日時 */
    private Timestamp starttime;
    /** 実施期間 */
    private Integer duration;
    /** リソースID(エリア情報) */
    private String resourceid;
    /** ステータス */
    private Short status;
    /** DRグループID */
    private Integer drgroupid;
    /** イベントID */
    private String eventid;
    /** ペイロード */
    private Double payload;
    /** 補正係数 */
    private Double factor;
    /** DR達成目標 */
    private Double target;
    /** DR達成率 */
    private Double achievement;
    /** 親スケジュールID */
    private Integer parentid;
    /** 作成日時 */
    private Timestamp createtime;
    /** 更新日時 */
    private Timestamp updatetime;
    /** パラメータ更新日時 */
    private Timestamp parameteruptime;

    // オリジナル

    /** 終了日時 */
    private Timestamp endtime;

    /**
     * コンストラクタ. <br>
     * <b>【注意】</b> 特になし.
     */
    public TOadrSchedule() {
    }

    /**
     * 実施期間を追加するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param duration 実施期間
     */
    public final void addDuration(Integer duration) {
        if (null != duration) {
            // パラメータがnullでなければ処理
            if (null != this.duration) {
                // フィールドがnullで無ければ加算
                this.duration += duration;
            } else {
                // フィールドがnullの場合はパラメータの値を採用
                this.duration = duration;
            }
        }
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
    public final Integer getScheduleid() {
        return scheduleid;
    }

    /**
     * スケジュールIDを設定するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param scheduleid スケジュールID
     */
    public final void setScheduleid(Integer scheduleid) {
        this.scheduleid = scheduleid;
    }

    /**
     * モードNoを取得するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @return モードNo
     */
    public final Integer getModeno() {
        return modeno;
    }

    /**
     * モードNoを設定するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param modeno モードNo
     */
    public final void setModeno(Integer modeno) {
        this.modeno = modeno;
    }

    /**
     * 開始日時を取得するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @return 開始日時
     */
    public final Timestamp getStarttime() {
        return starttime;
    }

    /**
     * 開始日時を設定するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param starttime 開始日時
     */
    public final void setStarttime(Timestamp starttime) {
        this.starttime = starttime;
    }

    /**
     * 実施期間を取得するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @return 実施期間
     */
    public final Integer getDuration() {
        return duration;
    }

    /**
     * 実施期間を設定するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param duration 実施期間
     */
    public final void setDuration(Integer duration) {
        this.duration = duration;
    }

    /**
     * リソースIDを取得するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @return リソースID
     */
    public final String getResourceid() {
        return resourceid;
    }

    /**
     * リソースIDを設定するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param resourceid リソースID
     */
    public final void setResourceid(String resourceid) {
        this.resourceid = resourceid;
    }

    /**
     * ステータスを取得するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @return ステータス
     */
    public final Short getStatus() {
        return status;
    }

    /**
     * ステータスを設定するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param status ステータス
     */
    public final void setStatus(Short status) {
        this.status = status;
    }

    /**
     * DRグループIDを取得するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @return DRグループID
     */
    public final Integer getDrgroupid() {
        return drgroupid;
    }

    /**
     * DRグループIDを設定するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param drgroupid DRグループID
     */
    public final void setDrgroupid(Integer drgroupid) {
        this.drgroupid = drgroupid;
    }

    /**
     * イベントIDを取得するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @return イベントID
     */
    public final String getEventid() {
        return eventid;
    }

    /**
     * イベントIDを設定するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param eventid イベントID
     */
    public final void setEventid(String eventid) {
        this.eventid = eventid;
    }

    /**
     * ペイロードを取得するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @return ペイロード
     */
    public final Double getPayload() {
        return payload;
    }

    /**
     * ペイロードを設定するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param payload ペイロード
     */
    public final void setPayload(Double payload) {
        this.payload = payload;
    }

    /**
     * 補正係数を取得するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @return 補正係数
     */
    public final Double getFactor() {
        return factor;
    }

    /**
     * 補正係数を設定するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param factor 補正係数
     */
    public final void setFactor(Double factor) {
        this.factor = factor;
    }

    /**
     * DR達成目標を取得するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @return DR達成目標
     */
    public final Double getTarget() {
        return target;
    }

    /**
     * DR達成目標を設定するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param target DR達成目標
     */
    public final void setTarget(Double target) {
        this.target = target;
    }

    /**
     * DR達成率を取得するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @return DR達成率
     */
    public final Double getAchievement() {
        return achievement;
    }

    /**
     * DR達成率を設定するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param achievement DR達成率
     */
    public final void setAchievement(Double achievement) {
        this.achievement = achievement;
    }

    /**
     * 親スケジュールIDを取得するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @return 親スケジュールID
     */
    public final Integer getParentid() {
        return parentid;
    }

    /**
     * 親スケジュールIDを設定するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param parentid 親スケジュールID
     */
    public final void setParentid(Integer parentid) {
        this.parentid = parentid;
    }

    /**
     * 作成日時を取得するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @return 作成日時
     */
    public final Timestamp getCreatetime() {
        return createtime;
    }

    /**
     * 作成日時を設定するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param createtime 作成日時
     */
    public final void setCreatetime(Timestamp createtime) {
        this.createtime = createtime;
    }

    /**
     * 更新日時を取得するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @return 更新日時
     */
    public final Timestamp getUpdatetime() {
        return updatetime;
    }

    /**
     * 更新日時を設定するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param updatetime 更新日時
     */
    public final void setUpdatetime(Timestamp updatetime) {
        this.updatetime = updatetime;
    }

    /**
     * パラメータ更新日時を取得するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @return パラメータ更新日時
     */
    public final Timestamp getParameteruptime() {
        return parameteruptime;
    }

    /**
     * パラメータ更新日時を設定するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param parameteruptime パラメータ更新日時
     */
    public final void setParameteruptime(Timestamp parameteruptime) {
        this.parameteruptime = parameteruptime;
    }

    /**
     * 終了日時を取得するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @return 終了日時
     */
    public final Timestamp getEndtime() {
        return endtime;
    }

    /**
     * 終了日時を設定するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param endtime 終了日時
     */
    public final void setEndtime(Timestamp endtime) {
        this.endtime = endtime;
    }

}
