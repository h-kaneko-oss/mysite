/*
 * MessageUtil.java
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

import java.text.MessageFormat;
import java.util.ResourceBundle;

/**
 * メッセージユーティリティクラス. <br>
 * <b>【注意】</b> 特になし.
 *
 * @author naya
 */
public class MessageUtil {

    /** messageIdとmessageの区切り文字 */
    private static final String MESSAGE_DELIMITER = ", ";

    /** リソースバンドル */
    private static ResourceBundle resourceBundle;


    /**
     * コンストラクタ. <br>
     * このクラスのインスタンスが生成されることを防ぐ役割を持つ.<br>
     * <b>【注意】</b> 特になし.
     */
    private MessageUtil() {
    }

    /**
     * リソースバンドルを設定するメソッド. <br>
     * <b>【注意】</b> 本クラスを使用する場合はあらかじめ本メソッドにてリソースバンドルを登録しなければならない.
     *
     * @param resourceBundle リソースバンドル
     */
    public static void setResourceBundle(ResourceBundle resourceBundle) {
        MessageUtil.resourceBundle = resourceBundle;
    }

    /**
     * getMessageメソッド.
     * <br>
     * 指定されたメッセージIDに対応するメッセージ内容を取得する.<br>
     * その際、指定されたパラメータでメッセージ内容の指定部分を置換する.<br>
     * 【処理詳細】<br>
     * 01.メッセージ設定ファイルから、メッセージ内容を取得する(Properties#get)<br>
     *   01-01.メッセージが取得できなかった場合は、実行時例外をスローする<br>
     * 02.取得したメッセージ内容の中に埋め込まれている変換用パラメータ部分を置換する(MessageFormat#format)<br>
     * 03.置換後メッセージを返す<br>
     * <b>【注意】</b>
     * 特になし.
     * @param messageId メッセージID
     * @param parameter 置換パラメータ
     * @return 置換されたメッセージ内容
     */
    public static String getMessageWithoutId(String messageId, Object[] parameter) {
        // メッセージ設定ファイルから、メッセージ内容を取得
        String message = MessageUtil.resourceBundle.getString(messageId);
        // 取得したメッセージ内容の中に埋め込まれている変換用パラメータ部分を置換
        if (null != parameter) {
            message = MessageFormat.format(message, parameter);
        }
        // 置換後のメッセージを返却
        return message;
    }

    /**
     * getMessageメソッド.
     * <br>
     * 指定されたメッセージIDに対応するメッセージ内容を取得する.<br>
     * その際、指定されたパラメータでメッセージ内容の指定部分を置換する.<br>
     * 【処理詳細】<br>
     * 01.置換されたメッセージを取得する(getMessageWithoutId())<br>
     * 02.メッセージIDと置換後メッセージを連結して返す<br>
     * <b>【注意】</b>
     * 特になし.
     * @param messageId メッセージID
     * @param parameter 置換パラメータ
     * @return 置換されたメッセージ内容
     */
    public static String getMessage(String messageId, Object[] parameter) {
        // メッセージIDと置換後メッセージを連結して返却
        return messageId + MESSAGE_DELIMITER
                + MessageUtil.getMessageWithoutId(messageId, parameter);
    }

    /**
     * １つの置換パラメータ指定可能なメッセージ文字列取得メソッド.
     * <br>
     * 指定されたメッセージIDと、置換パラメータを配列し化た値で、
     * {@link MessageUtil#getMessage(String, Object[])}を実行し、メッセージ文字列を取得する.
     * <br>
     * <b>【注意】</b>
     * 特になし.
     * @param messageId メッセージID
     * @param param1 置換パラメータ1
     * @return メッセージ文字列
     */
    public static String getMessage(String messageId, Object param1) {
        return MessageUtil.getMessage(messageId, new Object[]{param1});
    }

    /**
     * ２つの置換パラメータを指定可能なメッセージ文字列取得メソッド.
     * <br>
     * 指定されたメッセージIDと、置換パラメータを配列化した値で、
     * {@link MessageUtil#getMessage(String, Object[])}を実行し、メッセージ文字列を取得する.
     * <br>
     * <b>【注意】</b>
     * 特になし.
     * @param messageId メッセージID
     * @param param1 置換パラメータ1
     * @param param2 置換パラメータ2
     * @return メッセージ文字列
     */
    public static String getMessage(String messageId, Object param1, Object param2) {
        return MessageUtil.getMessage(messageId, new Object[]{param1, param2});
    }

    /**
     * ３つの置換パラメータを指定可能なメッセージ文字列取得メソッド.
     * <br>
     * 指定されたメッセージIDと、置換パラメータを配列化した値で、
     * {@link MessageUtil#getMessage(String, Object[])}を実行し、メッセージ文字列を取得する.
     * <br>
     * <b>【注意】</b>
     * 特になし.
     * @param messageId メッセージID
     * @param param1 置換パラメータ1
     * @param param2 置換パラメータ2
     * @param param3 置換パラメータ3
     * @return メッセージ文字列
     */
    public static String getMessage(String messageId, Object param1, Object param2, Object param3) {
        return MessageUtil.getMessage(messageId, new Object[]{param1, param2, param3});
    }

    /**
     * ４つの置換パラメータを指定可能なメッセージ文字列取得メソッド.
     * <br>
     * 指定されたメッセージIDと、置換パラメータを配列化した値で、
     * {@link MessageUtil#getMessage(String, Object[])}を実行し、メッセージ文字列を取得する.
     * <br>
     * <b>【注意】</b>
     * 特になし.
     * @param messageId メッセージID
     * @param param1 置換パラメータ1
     * @param param2 置換パラメータ2
     * @param param3 置換パラメータ3
     * @param param4 置換パラメータ4
     * @return メッセージ文字列
     */
    public static String getMessage(String messageId, Object param1, Object param2, Object param3, Object param4) {
        return MessageUtil.getMessage(messageId, new Object[]{param1, param2, param3, param4});
    }

    /**
     * ５つの置換パラメータを指定可能なメッセージ文字列取得メソッド.
     * <br>
     * 指定されたメッセージIDと、置換パラメータを配列化した値で、
     * {@link MessageUtil#getMessage(String, Object[])}を実行し、メッセージ文字列を取得する.
     * <br>
     * <b>【注意】</b>
     * 特になし.
     * @param messageId メッセージID
     * @param param1 置換パラメータ1
     * @param param2 置換パラメータ2
     * @param param3 置換パラメータ3
     * @param param4 置換パラメータ4
     * @param param5 置換パラメータ5
     * @return メッセージ文字列
     */
    public static String getMessage(String messageId, Object param1, Object param2, Object param3, Object param4,
            Object param5) {
        return MessageUtil.getMessage(messageId, new Object[]{param1, param2, param3, param4, param5});
    }

    /**
     * 置換パラメータ指定無しのメッセージ文字列取得メソッド.
     * <br>
     * 指定されたメッセージIDと、置換パラメータをnullとして、
     * {@link MessageUtil#getMessage(String, Object[])}を実行し、メッセージ文字列を取得する.
     * <br>
     * <b>【注意】</b>
     * 特になし.
     * @param messageId メッセージID
     * @return メッセージ文字列
     */
    public static String getMessage(String messageId) {
        return MessageUtil.getMessage(messageId, null);
    }

    /**
     * １つの置換パラメータ指定可能なメッセージ文字列（メッセージIDを含まない）取得メソッド.
     * <br>
     * 指定されたメッセージIDと、置換パラメータを配列化した値で、
     * {@link MessageUtil#getMessageWithoutId(String, Object[])}を実行し、メッセージ文字列を取得する.
     * <br>
     * <b>【注意】</b>
     * 特になし.
     * @param messageId メッセージID
     * @param param1 置換パラメータ1
     * @return メッセージ文字列（メッセージIDを含まない）
     */
    public static String getMessageWithoutId(String messageId, Object param1) {
        return MessageUtil.getMessageWithoutId(messageId, new Object[]{param1});
    }

    /**
     * 置換パラメータ指定無しのメッセージ文字列（メッセージIDを含まない）取得メソッド.
     * <br>
     * 指定されたメッセージIDと、置換パラメータをnullとして、
     * {@link MessageUtil#getMessageWithoutId(String, Object[])}を実行し、メッセージ文字列を取得する.
     * <br>
     * <b>【注意】</b>
     * 特になし.
     * @param messageId メッセージID
     * @return メッセージ文字列（メッセージIDを含まない）
     */
    public static String getMessageWithoutId(String messageId) {
        return MessageUtil.getMessageWithoutId(messageId, null);
    }

}
