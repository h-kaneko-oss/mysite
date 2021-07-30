/*
 * ControlInfo.java
 * Created on 2019/01/22
 * RCSID: $Id$
 *
 * (C) Copyright NTT-F 2019 All rights reserved.
 *
 * This software is furnished under a contract and use, duplication, disclosure
 * and all other uses are restricted to the rights specified in the written
 * contract and memorandum between the contractor and NTT-F.
 */
package jp.co.nttf.gms.common.internal.data;

import java.util.Date;
import java.util.HashMap;

/**
 * 制御情報クラス. <br>
 * <b>【注意】</b> 特になし.
 *
 * @author sunnet
 */
public final class ControlInfo {

    /** 制御種別「CEMSDR」 */
    public static final int CEMSDR = 0;

    /** 制御種別「設定反映」（モードNoと一致） */
    public static final int SETTING = 100;

    /** 制御種別「制御テスト」（モードNoと一致） */
    public static final int TEST = 101;

    /** 制御種別「一括登録」（モードNoと一致） */
    public static final int BULK_UPDATE = 102;

    /** 制御種別「接点連携制御」（モードNoと一致） */
    public static final int STATUSNOTICE = 103;

    /** 制御種別「再エネ制御」（モードNoと一致） */
    public static final int RE_ENERGY = 104;

    /** 制御種別「FW再起動」（モードNoと一致） */
    public static final int FW_REBOOT = 200;

    /** 制御種別「DR拒否」（モードNoと一致） */
    public static final int DR_DENY = 300;

    /** 制御種別のマップ. */
    private static final HashMap<Integer, String> NAME_MAP = new HashMap<Integer, String>() {
        /** シリアルID. */
        private static final long serialVersionUID = 1L;
        {
            put(CEMSDR, "CEMSDR");
            put(SETTING, "設定反映");
            put(TEST, "制御テスト");
            put(BULK_UPDATE, "一括登録");
            put(STATUSNOTICE, "接点連携制御");
            put(RE_ENERGY, "再エネ制御");
            put(FW_REBOOT, "FW再起動");
            put(DR_DENY, "DR拒否");
        }
    };

    /** GMU-ID */
    private String userid;

    /** GMU名称 */
    private String note;

    /** 開始時間 */
    private Date starttime;

    /** 終了時間 */
    private Date endtime;

    /** レベル */
    private Integer level;

    /** 制御種別 */
    private int controlType;

    /**
     * GMU-IDを取得するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @return GMU-ID
     */
    public String getUserid() {
        return userid;
    }

    /**
     * GMU-IDを設定するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param userid GMU-ID
     */
    public void setUserid(String userid) {
        this.userid = userid;
    }

    /**
     * GMU名称を取得するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @return GMU名称
     */
    public String getNote() {
        return note;
    }

    /**
     * GMU名称を設定するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param note GMU名称
     */
    public void setNote(String note) {
        this.note = note;
    }

    /**
     * 開始時間を取得するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @return 開始時間
     */
    public Date getStarttime() {
        return starttime;
    }

    /**
     * 開始時間を設定するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param starttime 開始時間
     */
    public void setStarttime(Date starttime) {
        this.starttime = starttime;
    }

    /**
     * 終了時間を取得するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @return 終了時間
     */
    public Date getEndtime() {
        return endtime;
    }

    /**
     * 終了時間を設定するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param endtime 終了時間
     */
    public void setEndtime(Date endtime) {
        this.endtime = endtime;
    }

    /**
     * レベルを取得するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @return レベル
     */
    public Integer getLevel() {
        return level;
    }

    /**
     * レベルを設定するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param level レベル
     */
    public void setLevel(Integer level) {
        this.level = level;
    }

    /**
     * 制御種別を取得するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @return 制御種別
     */
    public int getControlType() {
        return controlType;
    }

    /**
     * 制御種別を設定するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param controlType 制御種別
     */
    public void setControlType(int controlType) {
        this.controlType = controlType;
    }

    /**
     * 制御種別名称を取得するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @return 制御種別名称
     */
    public String getControlTypeName() {
        return NAME_MAP.get(controlType);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ControlInfo) {
            ControlInfo temp = (ControlInfo) obj;
            if (this.userid.equals(temp.getUserid())
                    && this.note.equals(temp.getNote())
                    && this.starttime.compareTo(temp.getStarttime()) == 0
                    && this.endtime.compareTo(temp.getEndtime()) == 0
                    && ((this.level == null && temp.getLevel() == null) || (this.level != null
                            && temp.getLevel() != null && this.level == temp
                            .getLevel()))) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        int levelHashCode = 0;
        if (this.level != null) {
            levelHashCode = this.level.hashCode();
        }
        return (this.userid.hashCode() + this.note.hashCode()
                + this.starttime.hashCode() + this.endtime.hashCode() + levelHashCode);
    }
}
