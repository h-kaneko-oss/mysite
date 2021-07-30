/*
 * AppUtil.java
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

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.KeyManager;
import javax.net.ssl.TrustManager;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import org.dom4j.Document;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import jp.co.nttf.gms.common.internal.communication.ConnectionInfo;
import jp.co.nttf.gms.common.internal.communication.ConnectionInfo.ProxyInfo;
import jp.co.nttf.gms.common.internal.communication.GmuSSLUtil;
import jp.co.nttf.gms.common.internal.data.KeyValueInfo;


/**
 * ユーティリティークラス. <br>
 * <b>【注意】</b> 特になし.
 *
 * @author naya
 */
public final class AppUtil {

    /** ロガー */
    private static final AppLogger LOGGER = new AppLogger(AppUtil.class);

    /** 期間(ISO 8601)の正規表現 */
    private static final String REGEX_ISO8601_DURATION =
            "(\\+|\\-)?P(((\\d+)Y)?((\\d+)M)?((\\d+)D)?T?((\\d+)H)?((\\d+)M)?((\\d+)S)?)|((\\d+)W)";

    /** 正規表現のグループインデックス:符号 */
    private static final int GROUP_INDEX_SIGN = 1;
    /** 正規表現のグループインデックス:年 */
    private static final int GROUP_INDEX_YEAR = 4;
    /** 正規表現のグループインデックス:年 */
    private static final int GROUP_INDEX_MONTH = 6;
    /** 正規表現のグループインデックス:年 */
    private static final int GROUP_INDEX_DATE = 8;
    /** 正規表現のグループインデックス:年 */
    private static final int GROUP_INDEX_HOUR = 10;
    /** 正規表現のグループインデックス:年 */
    private static final int GROUP_INDEX_MINUTE = 12;
    /** 正規表現のグループインデックス:年 */
    private static final int GROUP_INDEX_SECOND = 14;
    /** 正規表現のグループインデックス:年 */
    private static final int GROUP_INDEX_WEEK = 16;

    /** 週の日数 */
    private static final int DAY_OF_WEEK = 7;

    /**
     * コンストラクタ.
     * <br>
     * このクラスのインスタンスが生成されることを防ぐ役割を持つ。
     * <b>【注意】</b>
     * 特になし.
     */
    private AppUtil() {
    }

    /**
     * Empty判定処理.
     * <br>
     * 引数の文字列がnullか空文字の場合trueを、そうでない場合falseを返却します。
     * <b>【注意】</b>
     * 特になし.
     * @param target 検査対象文字列
     * @return true(検査対象文字列がnullもしくは空文字の場合)/false(trueでない場合)
     */
    public static boolean isEmpty(String target) {
        return (null == target || target.length() <= 0);
    }

    /**
     * KeyValueInfoのリストをマップに変換するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param list KeyValueInfoのリスト
     * @return マップ
     */
    public static Map<String, String> list2Map(List<KeyValueInfo> list) {
        Map<String, String> ret = new HashMap<String, String>();
        for (KeyValueInfo info : list) {
            ret.put(info.getKey(), info.getValue());
        }
        return ret;
    }

    /**
     * 文字列をデリミタで分割しリストに格納するメソッド. <br>
     * <b>【注意】</b> clazzに指定可能なクラスはString,Integer,Double.
     *
     * @param <T> 格納型
     * @param arrayString 配列文字列
     * @param delimiter デリミタ
     * @param clazz 格納型クラス
     * @return 変換後のリスト
     */
    @SuppressWarnings("unchecked")
    public static <T> List<T> toList(String arrayString, String delimiter, Class<T> clazz) {

        List<T> list = new ArrayList<T>();

        // 全体の空文字チェック
        if (!StringUtils.isEmpty(arrayString)) {

            // デリミタで分割
            String[] array = arrayString.split(delimiter, -1);

            // 配列の数だけループ
            for (String elm : array) {

                // クラスに合った格納を実施
                if (clazz == String.class) {
                    list.add((T) elm);
                } else if (clazz == Integer.class) {
                    list.add((T) Integer.valueOf(elm));
                } else if (clazz == Double.class) {
                    list.add((T) Double.valueOf(elm));
                } else {
                    throw new IllegalArgumentException(clazz.getClass() + " is not support.");
                }
            }
        }
        return list;
    }

    /**
     * 2重の配列文字列をマップに変換するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param arrayString 2重の配列文字列
     * @param outerDelim 外側のデリミタ
     * @param innerDelim 内側のデリミタ
     * @return 変換後のマップ
     */
    public static Map<String, String> doubleArray2Map(String arrayString, String outerDelim, String innerDelim) {

        Map<String, String> map = new LinkedHashMap<String, String>();

        // 全体の空文字チェック
        if (!StringUtils.isEmpty(arrayString)) {

            // 外側のデリミタで分割
            String[] outerArray = arrayString.split(outerDelim, -1);

            // 外側配列の数だけループ
            for (String outerElm : outerArray) {
                // 空文字チェック
                if (!StringUtils.isEmpty(outerElm)) {
                    // 内側のデリミタで分割
                    String[] innerArray = outerElm.split(innerDelim, -1);
                    // 要素数をチェック
                    if (1 < innerArray.length) {
                        // 要素数が2以上ならマップに格納
                        map.put(innerArray[0], innerArray[1]);
                    }
                }

            } // 外側配列ループEnd
        }
        return map;
    }

    /**
     * 指定した日時に加算するメソッド.
     * <br>
     * <b>【注意】</b>
     * 特になし.
     * @param date 日時
     * @param field 加算するフィールド
     * @param amount 加算する値(減算する場合はマイナス)
     * @return 加算後の日時
     */
    public static Timestamp addDate(Date date, int field, int amount) {
        // カレンダー型に変換
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        // 加算
        calendar.add(field, amount);
        // Timestamp型に変換して返却
        return new Timestamp(calendar.getTimeInMillis());
    }

    /**
     * 日時の差分を求め分で返却するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param fromDate 開始日時
     * @param toDate 終了日時
     * @return 差分[分]
     */
    public static long getDiffMinutes(Date fromDate, Date toDate) {
        long  diffMin = 0;
        if (fromDate != null && toDate != null) {
            // ミリ秒を取得し差を求める
            long fromDateTime = fromDate.getTime();
            long toDateTime = toDate.getTime();
            // 経過ミリ秒 / (1000ミリ秒 x 60秒) ※端数切り捨て
            diffMin = (long) ((toDateTime - fromDateTime)
                    / (CommonConstants.INT_THOUSAND
                            * CommonConstants.SECONDS_OF_MINUTE));
        }
        return diffMin;
    }

    /**
     * 指定した日時に期間を加算するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param date 日時(nullを指定した場合は現在時刻)
     * @param duration 期間を表すISO8601文字列
     * @return 加算後の日時
     * @throws ParseException 期間のパースに失敗した場合
     */
    public static Timestamp addDuration(Date date, String duration) throws ParseException {
        // カレンダー型に変換
        Calendar calendar = Calendar.getInstance();
        if (null != date) {
            calendar.setTime(date);
        }
        int sign = 1;

        // 期間をパース
        Pattern p = Pattern.compile(REGEX_ISO8601_DURATION);
        Matcher m = p.matcher(duration);

        // 正規表現にマッチしなかったら例外をスロー
        if (!m.find()) {
            throw new ParseException("Failed to parse duration.[duration=" + duration + "]", 0);
        }

        // 正規表現にマッチしたらグループの数だけループ
        for (int i = GROUP_INDEX_SIGN; i <= m.groupCount(); i++) {
            String val = m.group(i);
            if (null != val) {
                // 値がある場合のみ分岐
                switch (i) {
                case GROUP_INDEX_SIGN:
                    // 符号
                    if ("-".equals(val)) {
                        // マイナスが指定されていたら符号を負にセット
                        sign = -1;
                    }
                    break;
                case GROUP_INDEX_YEAR:
                    // 年
                    calendar.add(Calendar.YEAR, sign * Integer.parseInt(val));
                    break;
                case GROUP_INDEX_MONTH:
                    // 月
                    calendar.add(Calendar.MONTH, sign * Integer.parseInt(val));
                    break;
                case GROUP_INDEX_DATE:
                    // 日
                    calendar.add(Calendar.DATE, sign * Integer.parseInt(val));
                    break;
                case GROUP_INDEX_HOUR:
                    // 時
                    calendar.add(Calendar.HOUR, sign * Integer.parseInt(val));
                    break;
                case GROUP_INDEX_MINUTE:
                    // 分
                    calendar.add(Calendar.MINUTE, sign * Integer.parseInt(val));
                    break;
                case GROUP_INDEX_SECOND:
                    // 秒
                    calendar.add(Calendar.SECOND, sign * Integer.parseInt(val));
                    break;
                case GROUP_INDEX_WEEK:
                    // 週(7日)
                    calendar.add(Calendar.DATE, sign * Integer.parseInt(val) * DAY_OF_WEEK);
                    break;
                default:
                    break;
                }
            }
        }

        // Timestamp型に変換して返却
        return new Timestamp(calendar.getTimeInMillis());
    }

    /**
     * 例外がSQLExceptionならログを出力するメソッド.
     * <br>
     * <b>【注意】</b>
     * 特になし.
     * @param t 例外クラス
     * @param logger ロガー
     */
    public static void putSqlErrorLog(Throwable t, AppLogger logger) {
        Throwable cause = t.getCause();
        if (null != cause && cause instanceof SQLException) {
            // 原因がSQLExceptionだった場合
            SQLException e = (SQLException) cause;
            // ERRORログ出力
            String msg = MessageUtil.getMessage(CommonMessageID.ACOM001,
                    e.getErrorCode(), e.getSQLState());
            logger.error(msg, e);
        }
    }

    /**
     * <p>
     * 指定された時間をinterval（分）で指定されている分単位に丸めます。<br>
     * 秒も丸められ、0秒になります。<br>
     * 丸められた新しいTimestampオブジェクトを返します。<br>
     * intervalは0より大きい整数である必要があります。<br>
     * intervalが0以下の場合、またはsrcがnullの場合、nullを返却します。
     * </p>
     * @param src 丸めの対象時間
     * @param interval インターバル（分）
     * @return 丸められた新しいTimestamp
     */
    public static Timestamp roundMinute(Date src, int interval) {
        Timestamp ret = null;

        if (interval > 0 && src != null) {
            Calendar currentDate = Calendar.getInstance();
            currentDate.setTime(src);
            int minute = currentDate.get(Calendar.MINUTE);
            minute = minute - (minute % interval);
            currentDate.set(Calendar.MINUTE, minute);
            currentDate.set(Calendar.SECOND, 0);
            currentDate.set(Calendar.MILLISECOND, 0);
            ret = new Timestamp(currentDate.getTimeInMillis());
        }

        return ret;
    }

    /**
     * <p>
     * 指定された時間を年月日までに丸め、新しいTimestampオブジェクトを返却する。<br>
     * srcがnullの場合、nullを返却します。
     * </p>
     * @param src 丸めの対象時間
     * @return 丸められた新しいTimestamp
     */
    public static Timestamp roundDay(Date src) {
        Timestamp ret = null;

        if (src != null) {
            Calendar currentDate = Calendar.getInstance();
            currentDate.setTime(src);
            currentDate.set(Calendar.HOUR_OF_DAY, 0);
            currentDate.set(Calendar.MINUTE, 0);
            currentDate.set(Calendar.SECOND, 0);
            currentDate.set(Calendar.MILLISECOND, 0);
            ret = new Timestamp(currentDate.getTimeInMillis());
        }

        return ret;
    }

    /**
     * ファイルをロードしてバイト配列に変換するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param filePath ファイルパス
     * @return バイト配列
     * @throws IOException 処理中に異常が発生した場合
     */
    public static byte[] readFileToByte(String filePath) throws IOException {
        final int readSize = 256;
        byte[] b = new byte[readSize];
        FileInputStream fis = null;
        ByteArrayOutputStream baos = null;
        try {
            fis = new FileInputStream(filePath);
            baos = new ByteArrayOutputStream();
            int data;
            while (0 < (data = fis.read(b))) {
                baos.write(b, 0, data);
            }
        } finally {
            if (null != baos) {
                baos.close();
            }
            if (null != fis) {
                fis.close();
            }
        }
        return baos.toByteArray();
    }

    /**
     * 日時をGMTに変換するメソッド. <br>
     * <b>【注意】</b> 出力例:[1970-11-28T21:30:45Z]
     *
     * @param date 日時
     * @return 変換後の日付
     */
    public static String getGMTDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        return sdf.format(date).toUpperCase();
    }

    /**
     * 日時をISO8601に変換するメソッド. <br>
     * <b>【注意】</b> 出力例:[1970-11-28T21:30:45+09:00]
     *
     * @param date 日時
     * @return 変換後の日付
     */
    public static String getISO8601Date(Date date) {
        FastDateFormat fdf = FastDateFormat.getInstance("yyyy-MM-dd'T'HH:mm:ssZZ");
        return fdf.format(date);
    }

    /**
     * 日時文字列をパースしてTimestampに変換するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param dateStr 日時文字列
     * @return 変換後の日時
     * @throws ParseException 変換に失敗した場合
     */
    public static Timestamp parseGMTDate(String dateStr) throws ParseException {
        String formatStr = null;
        if (0 <= dateStr.indexOf('.')) {
            // ドットを含む場合はミリ秒あり
            formatStr = "yyyy-MM-dd'T'HH:mm:ss.S'Z'";
        } else {
            // ドットが無ければミリ秒なし
            formatStr = "yyyy-MM-dd'T'HH:mm:ss'Z'";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(formatStr);
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        Date d = sdf.parse(dateStr);
        return new Timestamp(d.getTime());
    }

    /**
     * Documentを文字列として出力するメソッド。
     * @param document Documentオブジェクト
     * @param charset 文字コード
     * @return XML文字列
     */
    public static String toXML(Document document, String charset) {

        // Writerを生成
        StringWriter sw = new StringWriter();

        XMLWriter xmlWriter = null;
        try {
            // フォーマットを設定
            OutputFormat format = OutputFormat.createPrettyPrint();
            format.setEncoding(charset);
            format.setNewLineAfterDeclaration(false);

            // 書込み実行
            xmlWriter = new XMLWriter(sw, format);
            xmlWriter.write(document);

        } catch (IOException e) {
            // DEBUGログ（Throwableキャッチ）
            if (LOGGER.isCatchedEnabled()) {
                LOGGER.catched("toXML",
                        e.getClass().getName() + "." + e.getMessage(), e);
            }
        } finally {
            if (xmlWriter != null) {
                try {
                    xmlWriter.close();
                } catch (IOException e) {
                    // DEBUGログ（Throwableキャッチ）
                    if (LOGGER.isCatchedEnabled()) {
                        LOGGER.catched("toXML",
                                e.getClass().getName() + "." + e.getMessage(), e);
                    }
                }
            }
        }
        return sw.toString();
    }

    /**
     * オブジェクトがnullだったら別の文字に差し替えるメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param input オブジェクト
     * @param nullValue 差し替える文字列
     * @return 変換後の文字列
     */
    public static String replaceNull(Object input, String nullValue) {
        if (null != input) {
            return String.valueOf(input);
        }
        return nullValue;
    }

    /**
     * 接続情報を生成して返却するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param prefix 接続情報の接頭辞
     * @param resourceId リソースID(指定しない場合はnull)
     * @return コネクション生成情報
     * @throws Exception 処理中に問題が発生した場合
     */
    public static ConnectionInfo getConnectionInfo(String prefix, String resourceId) throws Exception {

        Configuration gmsConfig = CommonInfo.getInstance().getGmsConfig();

        ConnectionInfo info = new ConnectionInfo();

        // URL
        String url = gmsConfig.getString(prefix + CommonConstants.CONNECTION_SUFFIX_URL);
        if (!StringUtils.isEmpty(resourceId)) {
            // リソースIDの指定があった場合
            String tmpUrl = gmsConfig.getString(prefix + CommonConstants.CONNECTION_SUFFIX_URL
                            + CommonConstants.PERIOD + resourceId);
            if (!StringUtils.isEmpty(tmpUrl)) {
                // リソースID指定のキーが存在する場合のみURLとしてセット
                url = tmpUrl;
            }
        }
        info.setServerUrl(url);

        // BASIC認証ユーザ
        info.setAuthUser(gmsConfig
                .getString(prefix + CommonConstants.CONNECTION_SUFFIX_BASIC_USER));
        // BASIC認証パスワード
        info.setAuthPassword(gmsConfig
                .getString(prefix + CommonConstants.CONNECTION_SUFFIX_BASIC_PASSWD));

        // プロキシ使用可否
        info.setProxyEnable(gmsConfig
                .getBoolean(prefix + CommonConstants.CONNECTION_SUFFIX_PROXY_ENABLE));
        if (info.isProxyEnable()) {
            // プロキシが有効な場合
            ProxyInfo proxyInfo = new ProxyInfo();
            proxyInfo.setHost(gmsConfig
                    .getString(prefix + CommonConstants.CONNECTION_SUFFIX_PROXY_HOST));
            proxyInfo.setPort(gmsConfig
                    .getInt(prefix + CommonConstants.CONNECTION_SUFFIX_PROXY_PORT));
            proxyInfo.setAuthUser(gmsConfig
                    .getString(prefix + CommonConstants.CONNECTION_SUFFIX_PROXY_USER));
            proxyInfo.setAuthPassword(gmsConfig
                    .getString(prefix + CommonConstants.CONNECTION_SUFFIX_PROXY_PASSWD));
            info.setProxyInfo(proxyInfo);
        }

        // 接続タイムアウト
        info.setHttpConnetionTimeoutMsec(gmsConfig
                .getInt(prefix + CommonConstants.CONNECTION_SUFFIX_CONNECTION_TIMEOUT));
        // リトライ回数
        info.setMaxRetryCount(gmsConfig
                .getInt(prefix + CommonConstants.CONNECTION_SUFFIX_RETRY_COUNT));
        // リトライ間隔
        info.setRetryIntervalSecond(gmsConfig
                .getInt(prefix + CommonConstants.CONNECTION_SUFFIX_RETRY_INTERVAL));
        // SSL使用可否
        info.setSslModeOn(gmsConfig
                .getBoolean(prefix + CommonConstants.CONNECTION_SUFFIX_SSL_ENABLE));
        if (info.isSslModeOn()) {
            // SSLが有効な場合

            // キーストアのファイルパス
            String keystorePath = gmsConfig
                    .getString(CommonConstants.PROPERTY_SSL_KEYSTORE_PATH);
            // キーストアのパスワード
            String keystorePasswd = gmsConfig
                    .getString(CommonConstants.PROPERTY_SSL_KEYSTORE_PASSWD);
            // 秘密鍵のパスワード
            String privatePasswd = gmsConfig
                    .getString(CommonConstants.PROPERTY_SSL_PRIVATE_PASSWD);
            // ロードしてセット
            byte[] keystoreFile = AppUtil.readFileToByte(keystorePath);
            KeyManager[] keyManager = GmuSSLUtil.getKeyManagers(keystoreFile,
                    keystorePasswd, privatePasswd);
            info.setKeyManager(keyManager);

            // キーストアのファイルパス
            String truststorePath = gmsConfig
                    .getString(CommonConstants.PROPERTY_SSL_TRUSTSTORE_PATH);
            // トラストストアのパスワード
            String truststorePasswd = gmsConfig
                    .getString(CommonConstants.PROPERTY_SSL_TRUSTSTORE_PASSWD);
            // ロードしてセット
            byte[] truststoreFile = AppUtil.readFileToByte(truststorePath);
            TrustManager[] trustManager = GmuSSLUtil.getTrustManagers(
                    truststoreFile, truststorePasswd);
            info.setTrustManager(trustManager);
        }

        return info;
    }

    /**
     * 接続情報を生成して返却するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param prefix 接続情報の接頭辞
     * @return コネクション生成情報
     * @throws Exception 処理中に問題が発生した場合
     */
    public static ConnectionInfo getConnectionInfo(String prefix) throws Exception {
        return getConnectionInfo(prefix, null);
    }

}
