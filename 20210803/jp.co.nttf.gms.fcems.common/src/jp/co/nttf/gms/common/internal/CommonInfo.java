/*
 * CommonInfo.java
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

import java.util.HashMap;
import java.util.Map;

import org.apache.ibatis.session.SqlSessionFactory;

/**
 * 共通情報クラス. <br>
 * <b>【注意】</b> 特になし.
 *
 * @author naya
 */
public final class CommonInfo {

    /** 共通情報インスタンス */
    private static final CommonInfo INSTANCE = new CommonInfo();

    /** SqlSessionFactoryのマップ */
    private Map<String, SqlSessionFactory> sqlSessionFactoryMap = null;

    /** GMS設定情報 */
    private Configuration gmsConfig = null;

    /**
     * コンストラクタ. <br>
     * このクラスのインスタンスが生成されることを防ぐ役割を持つ. <br>
     * <b>【注意】</b> 特になし.
     */
    private CommonInfo() {
        // マップを初期化
        this.sqlSessionFactoryMap = new HashMap<String, SqlSessionFactory>();
    }

    /**
     * 共通情報インスタンス取得メソッド. <br>
     * <b>【注意】</b> インスタンスはコンテキスト内で一つとなる.
     *
     * @return 共通情報インスタンス
     */
    public static CommonInfo getInstance() {
        return INSTANCE;
    }

    /**
     * SqlSessionFactoryを設定するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param environment 環境名
     * @param sqlSessionFactory SqlSessionFactory
     */
    public void setSqlSessionFactory(String environment,
            SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactoryMap.put(environment, sqlSessionFactory);
    }

    /**
     * SqlSessionFactoryを取得するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param environment 環境名
     * @return SqlSessionFactory
     */
    public SqlSessionFactory getSqlSessionFactory(String environment) {
        return this.sqlSessionFactoryMap.get(environment);
    }

    /**
     * GMS設定情報を設定するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param gmsConfig GMS設定情報
     */
    public void setGmsConfig(GmsConfig gmsConfig) {
        this.gmsConfig = gmsConfig;
    }

    /**
     * GMS設定情報を取得するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @return GMS設定情報
     */
    public Configuration getGmsConfig() {
        return this.gmsConfig;
    }

}
