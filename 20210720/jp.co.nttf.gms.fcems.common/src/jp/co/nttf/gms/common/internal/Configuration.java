/*
 * Configuration.java
 * Created on 2014/12/09
 * RCSID: $Id$
 *
 * (C) Copyright NTT-F 2014 All rights reserved.
 *
 * This software is furnished under a contract and use, duplication, disclosure
 * and all other uses are restricted to the rights specified in the written
 * contract and memorandum between the contractor and NTT-F.
 */
package jp.co.nttf.gms.common.internal;

/**
 * 設定情報の抽象クラス. <br>
 * <b>【注意】</b> 特になし.
 *
 * @author naya
 */
public abstract class Configuration {

    /**
     * キーに対応する値を取得するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param key キー
     * @return 値
     */
    protected abstract String getValue(String key);

    /**
     * 指定した設定内容を文字列として取得するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param key キー
     * @return 設定内容
     */
    public final String getString(String key) {
        return this.getValue(key);
    }

    /**
     * 指定した設定内容を整数として取得するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param key キー
     * @return 設定内容
     */
    public final int getInt(String key) {
        String value = this.getValue(key);
        return Integer.parseInt(value);
    }

    /**
     * 指定した設定内容をdouble型として取得するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param key キー
     * @return 設定内容
     */
    public final double getDouble(String key) {
        String value = this.getValue(key);
        return Double.parseDouble(value);
    }

    /**
     * 指定した設定内容を真偽値として取得するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param key キー
     * @return 設定内容
     */
    public final boolean getBoolean(String key) {
        String value = this.getValue(key);
        return Boolean.parseBoolean(value);
    }

}
