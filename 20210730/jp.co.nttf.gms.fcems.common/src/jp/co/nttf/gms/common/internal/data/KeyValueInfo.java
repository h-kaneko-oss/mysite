/*
 * KeyValueInfo.java
 * Created on 2014/12/09
 * RCSID: $Id$
 *
 * (C) Copyright NTT-F 2014 All rights reserved.
 *
 * This software is furnished under a contract and use, duplication, disclosure
 * and all other uses are restricted to the rights specified in the written
 * contract and memorandum between the contractor and NTT-F.
 */
package jp.co.nttf.gms.common.internal.data;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;


/**
 * キーバリューを保持するクラス. <br>
 * <b>【注意】</b> 特になし.
 *
 * @author naya
 */
public final class KeyValueInfo {

    /** キー */
    private String key;

    /** 値 */
    private String value;


    /**
     * コンストラクタ. <br>
     * <b>【注意】</b> 特になし.
     */
    public KeyValueInfo() {
    }

    /**
     * コンストラクタ. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param key キー
     * @param value 値
     */
    public KeyValueInfo(String key, String value) {
        this.key = key;
        this.value = value;
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
     * キーを取得するメソッド.
     * <br>
     * <b>【注意】</b>
     * 特になし.
     *
     * @return キー
     */
    public String getKey() {
        return this.key;
    }

    /**
     * キーを設定するメソッド.
     * <br>
     * <b>【注意】</b>
     * 特になし.
     *
     * @param key キー
     */
    public void setKey(String key) {
        this.key = key;
    }

    /**
     * 値を取得するメソッド.
     * <br>
     * <b>【注意】</b>
     * 特になし.
     *
     * @return 値
     */
    public String getValue() {
        return this.value;
    }

    /**
     * 値を設定するメソッド.
     * <br>
     * <b>【注意】</b>
     * 特になし.
     *
     * @param value 値
     */
    public void setValue(String value) {
        this.value = value;
    }

}
