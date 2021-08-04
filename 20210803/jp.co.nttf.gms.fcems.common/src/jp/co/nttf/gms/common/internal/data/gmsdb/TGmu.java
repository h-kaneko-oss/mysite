/*
 * TGmu.java
 * Created on 2017/12/07
 * RCSID: $Id$
 *
 * (C) Copyright NTT-F 2017 All rights reserved.
 *
 * This software is furnished under a contract and use, duplication, disclosure
 * and all other uses are restricted to the rights specified in the written
 * contract and memorandum between the contractor and NTT-F.
 */
package jp.co.nttf.gms.common.internal.data.gmsdb;

import java.util.Date;

/**
 * TGmuを表すクラス. <br>
 * <b>【注意】</b> 特になし.
 *
 * @author ninomiya
 */
public final class TGmu {

    /** GMU-ID */
    private String userid;

    /** 接続用パスワード */
    private String passwd;

    /** データ更新間隔 */
    private Integer interval;

    /** 計測データ受信時刻 */
    private Date recvtime;

    /** 記事欄 */
    private String note;

    /** IPアドレス */
    private String ipaddr;

    /** ステータス通知メールフィルタフラグ */
    private Integer smailfilterflg;

    /** ステータス通知メール文字列 */
    private String smailfilterword;

    /** ステータス通知メールフィルタ時間 */
    private Integer smailfiltertime;

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
     * 接続用パスワードを取得するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @return 接続用パスワード
     */
    public String getPasswd() {
        return passwd;
    }

    /**
     * 接続用パスワードを設定するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param passwd 接続用パスワード
     */
    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    /**
     * データ更新間隔を取得するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @return データ更新間隔
     */
    public Integer getInterval() {
        return interval;
    }

    /**
     * データ更新間隔を設定するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param interval データ更新間隔
     */
    public void setInterval(Integer interval) {
        this.interval = interval;
    }

    /**
     * 計測データ受信時刻を取得するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @return 計測データ受信時刻
     */
    public Date getRecvtime() {
        return recvtime;
    }

    /**
     * 計測データ受信時刻を設定するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param recvtime 計測データ受信時刻
     */
    public void setRecvtime(Date recvtime) {
        this.recvtime = recvtime;
    }

    /**
     * 記事欄を取得するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @return 記事欄
     */
    public String getNote() {
        return note;
    }

    /**
     * 記事欄を設定するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param note 記事欄
     */
    public void setNote(String note) {
        this.note = note;
    }

    /**
     * IPアドレスを取得するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @return IPアドレス
     */
    public String getIpaddr() {
        return ipaddr;
    }

    /**
     * IPアドレスを設定するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param ipaddr IPアドレス
     */
    public void setIpaddr(String ipaddr) {
        this.ipaddr = ipaddr;
    }

    /**
     * ステータス通知メールフィルタフラグを取得するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @return ステータス通知メールフィルタフラグ
     */
    public Integer getSmailfilterflg() {
        return smailfilterflg;
    }

    /**
     * ステータス通知メールフィルタフラグを設定するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param smailfilterflg ステータス通知メールフィルタフラグ
     */
    public void setSmailfilterflg(Integer smailfilterflg) {
        this.smailfilterflg = smailfilterflg;
    }

    /**
     * ステータス通知メール文字列を取得するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @return ステータス通知メール文字列
     */
    public String getSmailfilterword() {
        return smailfilterword;
    }

    /**
     * ステータス通知メール文字列を設定するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param smailfilterword ステータス通知メール文字列
     */
    public void setSmailfilterword(String smailfilterword) {
        this.smailfilterword = smailfilterword;
    }

    /**
     * ステータス通知メールフィルタ時間を取得するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @return ステータス通知メールフィルタ時間
     */
    public Integer getSmailfiltertime() {
        return smailfiltertime;
    }

    /**
     * ステータス通知メールフィルタ時間を設定するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param smailfiltertime ステータス通知メールフィルタ時間
     */
    public void setSmailfiltertime(Integer smailfiltertime) {
        this.smailfiltertime = smailfiltertime;
    }

}
