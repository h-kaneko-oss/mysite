/*
 * CommonConstants.java
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
 * 共通定数インタフェース. <br>
 * <b>【注意】</b> 特になし.
 *
 * @author naya
 */
public interface CommonConstants {

    /** メッセージリソースファイル名 */
    String MESSAGE_RESOURCE_NAME = "message";

    /** MyBatisリソースファイル名 */
    String MYBATIS_RESOURCE_NAME = "mybatis-config.xml";

    /** DB環境名：gmsdb */
    String ENVIRONMENT_GMSDB = "gmsdb";

    /** レポートのContent-Type */
    String REPORT_CONTENTTYPE = "application/xml";


    /** 接続情報の接頭辞：VTN */
    String CONNECTION_PREFIX_VTN = "fcems.vtn";
    /** 接続情報の接頭辞：VEN(イベント) */
    String CONNECTION_PREFIX_VEN_EVENT = "fcems.ven.event";
    /** 接続情報の接頭辞：VEN(レポート) */
    String CONNECTION_PREFIX_VEN_REPORT = "fcems.ven.report";

    /** 接続情報の接尾辞：URL */
    String CONNECTION_SUFFIX_URL = ".url";
    /** 接続情報の接尾辞：BASIC認証ユーザ */
    String CONNECTION_SUFFIX_BASIC_USER = ".basic.user";
    /** 接続情報の接尾辞：BASIC認証パスワード */
    String CONNECTION_SUFFIX_BASIC_PASSWD = ".basic.passwd";
    /** 接続情報の接尾辞：プロキシ使用可否 */
    String CONNECTION_SUFFIX_PROXY_ENABLE = ".proxy.enable";
    /** 接続情報の接尾辞：プロキシホスト */
    String CONNECTION_SUFFIX_PROXY_HOST = ".proxy.host";
    /** 接続情報の接尾辞：プロキシポート */
    String CONNECTION_SUFFIX_PROXY_PORT = ".proxy.port";
    /** 接続情報の接尾辞：プロキシユーザ */
    String CONNECTION_SUFFIX_PROXY_USER = ".proxy.user";
    /** 接続情報の接尾辞：プロキシパスワード */
    String CONNECTION_SUFFIX_PROXY_PASSWD = ".proxy.passwd";
    /** 接続情報の接尾辞：接続タイムアウト */
    String CONNECTION_SUFFIX_CONNECTION_TIMEOUT = ".connection.timeout";
    /** 接続情報の接尾辞：リトライ回数 */
    String CONNECTION_SUFFIX_RETRY_COUNT = ".retry.count";
    /** 接続情報の接尾辞：リトライ間隔 */
    String CONNECTION_SUFFIX_RETRY_INTERVAL = ".retry.interval";
    /** 接続情報の接尾辞：SSL使用可否 */
    String CONNECTION_SUFFIX_SSL_ENABLE = ".ssl.enable";

    /** プロパティキー：キーストアのファイルパス */
    String PROPERTY_SSL_KEYSTORE_PATH = "fcems.ssl.keystore.path";
    /** プロパティキー：キーストアのパスワード */
    String PROPERTY_SSL_KEYSTORE_PASSWD = "fcems.ssl.keystore.passwd";
    /** プロパティキー：秘密鍵のパスワード */
    String PROPERTY_SSL_PRIVATE_PASSWD = "fcems.ssl.private.passwd";
    /** プロパティキー：トラストストアのファイルパス */
    String PROPERTY_SSL_TRUSTSTORE_PATH = "fcems.ssl.truststore.path";
    /** プロパティキー：トラストストアのパスワード */
    String PROPERTY_SSL_TRUSTSTORE_PASSWD = "fcems.ssl.truststore.passwd";


    /** プロパティキー：テーブルスペース使用可否 */
    String PROPERTY_GAT_USE_TABLESPACE = "gat.use.tablespace";
    /** プロパティキー：テーブル分割使用可否 */
    String PROPERTY_GAT_USE_TABLEDIVIDE = "gat.use.tabledivide";

    /** DR高度制御実施可否[true|false] */
    String PROPERTY_FCEMS_ADVANCED_CONTROL_ENABLE = "fcems.advanced.control.enable";

    /** GMSプロパティキー：汎用計測用積算リセット値 */
    String GMS_PROPERTY_GAT_GENERAL_RESET_VALUE = "gat.general.reset.value";

    /** テーブルスペースを使用する場合の接頭文字列 */
    String GMSDB_TABLESPACE_PREFIX = "gmsdb";

    /** IDセパレータ */
    String ID_SEPARATOR = "-";

    /** カンマ */
    String COMMA = ",";
    /** ピリオド */
    String PERIOD = ".";
    /** 改行(LF) */
    String LF = "\n";

    /** 100を表す定数 */
    int INT_HUNDRED = 100;
    /** 1000を表す定数 */
    int INT_THOUSAND = 1000;

    /** 1分の秒数 */
    int SECONDS_OF_MINUTE = 60;
    /** 1時間の分数 */
    int MINUTES_OF_HOUR = 60;

}
