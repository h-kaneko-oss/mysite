/*
 * ExtendedLogger.java
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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 拡張ロガークラス. <br>
 * <b>【注意】</b> 特になし.
 *
 * @author naya
 */
public final class AppLogger implements
                            Log {

    /** LOG_HEADER：METHOD_IN */
    private static final String LOG_HEADER_METHOD_IN = "Access:";

    /** LOG_HEADER：CATCHED */
    private static final String LOG_HEADER_CATCHED = "Catched:";

    /** LOG_HEADER：METHOD_INVOKE */
    private static final String LOG_HEADER_METHOD_INVOKE = "Method invoke:";

    /** LOG_HEADER：METHOD_RESULT */
    private static final String LOG_HEADER_METHOD_RESULT = "Method result:";

    /** LOG_HEADER：SYNCHRO_OUTER */
    private static final String LOG_HEADER_SYNCHRO_OUTER = "Synchronized block-OUTER:";

    /** LOG_HEADER：SYNCHRO_INNER_TOP */
    private static final String LOG_HEADER_SYNCHRO_INNER_TOP = "Synchronized block-INNER-TOP:";

    /** LOG_HEADER：SYNCHRO_INNER_BOTTOM */
    private static final String LOG_HEADER_SYNCHRO_INNER_BOTTOM = "Synchronized block-INNER-BOTTOM:";

    /** LOG_HEADER：WAIT_BEFORE */
    private static final String LOG_HEADER_WAIT_BEFORE = "Wait-BEFORE:";

    /** LOG_HEADER：WAIT_AFTER */
    private static final String LOG_HEADER_WAIT_AFTER = "Wait-AFTER:";

    /** LOG_HEADER：SLEEP_BEFORE */
    private static final String LOG_HEADER_SLEEP_BEFORE = "Sleep-BEFORE:";

    /** LOG_HEADER：SLEEP_AFTER */
    private static final String LOG_HEADER_SLEEP_AFTER = "Sleep-AFTER:";

    /** LOG_HEADER：THREAD_BEGIN */
    private static final String LOG_HEADER_THREAD_BEGIN = "Thread(Runnable)-BEGIN:";

    /** LOG_HEADER：THREAD_END */
    private static final String LOG_HEADER_THREAD_END = "Thread(Runnable)-END:";

    /** LOG_LABEL：METHOD */
    private static final String LOG_LABEL_METHOD = " method:";

    /** LOG_LABEL：PARAMETERS */
    private static final String LOG_LABEL_PARAMETERS = " parameters:";

    /** LOG_LABEL：MONITOR */
    private static final String LOG_LABEL_MONITOR = " monitor:";

    /** LOG_LABEL：THREAD */
    private static final String LOG_LABEL_THREAD = " thread:";

    /** LOG_LABEL：RESULT */
    private static final String LOG_LABEL_RESULT = " result:";

    /** LOG_LABEL：ERROR MESSAGE */
    private static final String LOG_LABEL_ERROR_MESSAGE = " errormessage:";

    /** LOG_LABEL：CAUSE */
    private static final String LOG_LABEL_CAUSE = " cause:";

    /** TOKEN_CLASS_TO_METHOD */
    private static final String TOKEN_CLASS_TO_METHOD = "#";

    /** Logのインスタンス */
    private final Log logger;

    /** クラス名称 */
    private final String usingClassName;

    /**
     * コンストラクタ. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param usingClass 使用クラス
     */
    public AppLogger(Class<?> usingClass) {
        this.logger = LogFactory.getLog(usingClass);
        this.usingClassName = usingClass.getName();
    }

    /**
     * コンストラクタ. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param usingClass 使用クラス
     * @param logger Logのインスタンス
     */
    public AppLogger(Class<?> usingClass, Log logger) {
        this.logger = logger;
        this.usingClassName = usingClass.getName();
    }

    /**
     * fatalログ出力. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param obj 出力内容
     * @param throwable 例外
     * @see org.apache.commons.logging.Log#fatal(java.lang.Object,
     *      java.lang.Throwable)
     */
    @Override
    public void fatal(Object obj, Throwable throwable) {
        if (!this.isFatalEnabled()) {
            return;
        }
        this.logger.fatal(obj, throwable);
    }

    /**
     * fatalログ出力. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param obj 出力内容
     * @see org.apache.commons.logging.Log#fatal(java.lang.Object)
     */
    @Override
    public void fatal(Object obj) {
        if (!this.isFatalEnabled()) {
            return;
        }
        this.logger.fatal(obj);
    }

    /**
     * errorログ出力. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param obj 出力内容
     * @param throwable 例外
     * @see org.apache.commons.logging.Log#error(java.lang.Object,
     *      java.lang.Throwable)
     */
    @Override
    public void error(Object obj, Throwable throwable) {
        if (!this.isErrorEnabled()) {
            return;
        }
        this.logger.error(obj, throwable);
    }

    /**
     * errorログ出力. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param obj 出力内容
     * @see org.apache.commons.logging.Log#error(java.lang.Object)
     */
    @Override
    public void error(Object obj) {
        if (!this.isErrorEnabled()) {
            return;
        }
        this.logger.error(obj);
    }

    /**
     * warnログ出力. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param obj 出力内容
     * @param throwable 例外
     * @see org.apache.commons.logging.Log#warn(java.lang.Object,
     *      java.lang.Throwable)
     */
    @Override
    public void warn(Object obj, Throwable throwable) {
        if (!this.isWarnEnabled()) {
            return;
        }
        this.logger.warn(obj, throwable);
    }

    /**
     * warnログ出力. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param obj 出力内容
     * @see org.apache.commons.logging.Log#warn(java.lang.Object)
     */
    @Override
    public void warn(Object obj) {
        if (!this.isWarnEnabled()) {
            return;
        }
        this.logger.warn(obj);
    }

    /**
     * infoログ出力. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param obj 出力内容
     * @param throwable 例外
     * @see org.apache.commons.logging.Log#info(java.lang.Object,
     *      java.lang.Throwable)
     */
    @Override
    public void info(Object obj, Throwable throwable) {
        if (!this.isInfoEnabled()) {
            return;
        }
        this.logger.info(obj, throwable);
    }

    /**
     * infoログ出力. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param obj 出力内容
     * @see org.apache.commons.logging.Log#info(java.lang.Object)
     */
    @Override
    public void info(Object obj) {
        if (!this.isInfoEnabled()) {
            return;
        }
        this.logger.info(obj);
    }

    /**
     * debugログ出力. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param obj 出力内容
     * @param throwable 例外
     * @see org.apache.commons.logging.Log#debug(java.lang.Object,
     *      java.lang.Throwable)
     */
    @Override
    public void debug(Object obj, Throwable throwable) {
        if (!this.isDebugEnabled()) {
            return;
        }
        this.logger.debug(obj, throwable);
    }

    /**
     * debugログ出力. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param obj 出力内容
     * @see org.apache.commons.logging.Log#debug(java.lang.Object)
     */
    @Override
    public void debug(Object obj) {
        if (!this.isDebugEnabled()) {
            return;
        }
        this.logger.debug(obj);
    }

    /**
     * traceログ出力. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param obj 出力内容
     * @param throwable 例外
     * @see org.apache.commons.logging.Log#trace(java.lang.Object,
     *      java.lang.Throwable)
     */
    @Override
    public void trace(Object obj, Throwable throwable) {
        if (!this.isTraceEnabled()) {
            return;
        }
        this.logger.trace(obj, throwable);
    }

    /**
     * traceログ出力. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param obj 出力内容
     * @see org.apache.commons.logging.Log#trace(java.lang.Object)
     */
    @Override
    public void trace(Object obj) {
        if (!this.isTraceEnabled()) {
            return;
        }
        this.logger.trace(obj);
    }

    /**
     * fatalログ有効チェック. <br>
     * <b>【注意】</b> 特になし.
     *
     * @return 有効な場合true、無効な場合false
     * @see org.apache.commons.logging.Log#isFatalEnabled()
     */
    @Override
    public boolean isFatalEnabled() {
        return this.logger.isFatalEnabled();
    }

    /**
     * errorログ有効チェック. <br>
     * <b>【注意】</b> 特になし.
     *
     * @return 有効な場合true、無効な場合false
     * @see org.apache.commons.logging.Log#isErrorEnabled()
     */
    @Override
    public boolean isErrorEnabled() {
        return this.logger.isErrorEnabled();
    }

    /**
     * warnログ有効チェック. <br>
     * <b>【注意】</b> 特になし.
     *
     * @return 有効な場合true、無効な場合false
     * @see org.apache.commons.logging.Log#isWarnEnabled()
     */
    @Override
    public boolean isWarnEnabled() {
        return this.logger.isWarnEnabled();
    }

    /**
     * infoログ有効チェック. <br>
     * <b>【注意】</b> 特になし.
     *
     * @return 有効な場合true、無効な場合false
     * @see org.apache.commons.logging.Log#isInfoEnabled()
     */
    @Override
    public boolean isInfoEnabled() {
        return this.logger.isInfoEnabled();
    }

    /**
     * debugログ有効チェック. <br>
     * <b>【注意】</b> 特になし.
     *
     * @return 有効な場合true、無効な場合false
     * @see org.apache.commons.logging.Log#isDebugEnabled()
     */
    @Override
    public boolean isDebugEnabled() {
        return this.logger.isDebugEnabled();
    }

    /**
     * traceログ有効チェック. <br>
     * <b>【注意】</b> 特になし.
     *
     * @return 有効な場合true、無効な場合false
     * @see org.apache.commons.logging.Log#isTraceEnabled()
     */
    @Override
    public boolean isTraceEnabled() {
        return this.logger.isTraceEnabled();
    }

    /**
     * DEBUGログ有効判定（ルールNo.1用）. <br>
     * <b>【注意】</b> 特になし.
     *
     * @return 有効な場合true、無効な場合false
     */
    public boolean isMethodInEnabled() {

        return this.isDebugEnabled();
    }

    /**
     * DEBUGログ記録（ルールNo.1用）. <br>
     * ルールNo.1. privateメソッド以外のメソッドおよびコンストラクタの先頭で記録. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param methodName メソッド名称
     */
    public void methodIn(String methodName) {
        this.methodIn(methodName, null);
    }

    /**
     * DEBUGログ記録（ルールNo.1用）. <br>
     * privateメソッド以外のメソッドおよびコンストラクタの先頭で記録. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param methodName メソッド名称
     * @param paramString パラメータ内容
     */
    public void methodIn(String methodName, String paramString) {

        // 該当ログが無効の場合は出力しない
        if (!this.isMethodInEnabled()) {
            return;
        }

        StringBuffer buffer = new StringBuffer();

        buffer.append(LOG_HEADER_METHOD_IN);
        buffer.append(LOG_LABEL_METHOD);
        buffer.append(this.usingClassName);
        buffer.append(TOKEN_CLASS_TO_METHOD);
        buffer.append(methodName);
        if (null != paramString) {
            buffer.append(LOG_LABEL_PARAMETERS);
            buffer.append(paramString);
        }
        // ログ出力
        this.debug(buffer.toString());
    }

    /**
     * DEBUGログ有効判定（ルールNo.2用）. <br>
     * <b>【注意】</b> 特になし.
     *
     * @return 有効な場合true、無効な場合false
     */
    public boolean isCatchedEnabled() {

        return this.isDebugEnabled();
    }

    /**
     * DEBUGログ記録（ルールNo.2用）. <br>
     * Throwableキャッチ時にログを記録. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param methodName メソッド名称
     * @param cause 理由
     * @param throwable 例外オブジェクト
     */
    public void catched(String methodName, String cause, Throwable throwable) {

        // 該当ログが無効の場合は出力しない
        if (!this.isCatchedEnabled()) {
            return;
        }

        StringBuffer buffer = new StringBuffer();

        buffer.append(LOG_HEADER_CATCHED);
        buffer.append(LOG_LABEL_METHOD);
        buffer.append(this.usingClassName);
        buffer.append(TOKEN_CLASS_TO_METHOD);
        buffer.append(methodName);
        buffer.append(LOG_LABEL_ERROR_MESSAGE);
        buffer.append(cause);
        buffer.append(LOG_LABEL_CAUSE);
        buffer.append(throwable.getMessage());

        // ログ出力
        this.debug(buffer.toString(), throwable);

    }

    /**
     * DEBUGログ有効判定（ルールNo.3/4/5用）. <br>
     * <b>【注意】</b> 本メソッドは本ロガーの内部で使用するのみとし、ロギングコードを記述する部分では使用してはならない.
     *
     * @return 有効な場合true、無効な場合false
     */
    private boolean isSynchronizedBlockEnabled() {

        return this.isDebugEnabled();
    }

    /**
     * DEBUGログ記録（ルールNo.3用）. <br>
     * synchronizedブロックに入る直前でDEBUGログを記録. <br>
     * <b>【注意】</b> 現在実行中のスレッドの情報は内部で取得します.
     *
     * @param methodName メソッド名称
     * @param objectForLock モニターオブジェクト
     */
    public void outerSynchronizedBlock(String methodName, Object objectForLock) {
        this.loggedForSynchronizedBlock(LOG_HEADER_SYNCHRO_OUTER, methodName,
                objectForLock);
    }

    /**
     * DEBUGログ記録（ルールNo.4用）. <br>
     * synchronizedブロック内の先頭でDEBUGログを記録. <br>
     * <b>【注意】</b> 現在実行中のスレッドの情報は内部で取得します.
     *
     * @param methodName メソッド名称
     * @param objectForLock モニターオブジェクト
     */
    public void innerTopSynchronizedBlock(String methodName,
            Object objectForLock) {
        this.loggedForSynchronizedBlock(LOG_HEADER_SYNCHRO_INNER_TOP,
                methodName, objectForLock);
    }

    /**
     * DEBUGログ記録（ルールNo.5用）. <br>
     * synchronizedブロック内の末尾でDEBUGログを記録. <br>
     * <b>【注意】</b> 現在実行中のスレッドの情報は内部で取得します.
     *
     * @param methodName メソッド名称
     * @param objectForLock 同期用のモニターオブジェクト
     */
    public void innerBottomSynchronizedBlock(String methodName,
            Object objectForLock) {
        this.loggedForSynchronizedBlock(LOG_HEADER_SYNCHRO_INNER_BOTTOM,
                methodName, objectForLock);
    }

    /**
     * DEBUGログ記録（ルールNo.3/4/5用）. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param headerLabel ヘッダのラベル
     * @param methodName メソッド名称
     * @param objectForLock 同期用のモニターオブジェクト
     */
    private void loggedForSynchronizedBlock(String headerLabel,
            String methodName, Object objectForLock) {

        // 該当ログが無効の場合は出力しない
        if (!this.isSynchronizedBlockEnabled()) {
            return;
        }

        StringBuffer buffer = new StringBuffer();
        buffer.append(headerLabel);
        buffer.append(LOG_LABEL_METHOD);
        buffer.append(this.usingClassName);
        buffer.append(TOKEN_CLASS_TO_METHOD);
        buffer.append(methodName);
        buffer.append(LOG_LABEL_MONITOR);
        buffer.append(createMonitorObjectLabel(objectForLock));
        buffer.append(LOG_LABEL_THREAD);
        buffer.append(Thread.currentThread().getName());

        this.debug(buffer.toString());
    }

    /**
     * DEBUGログ有効判定（ルールNo.6/7用）. <br>
     * <b>【注意】</b> 本メソッドは本ロガーの内部で使用するのみとし、ロギングコードを記述する部分では使用してはならない.
     *
     * @return 有効な場合true、無効な場合false
     */
    private boolean isWaitEnabled() {

        return this.isDebugEnabled();
    }

    /**
     * DEBUGログ記録（ルールNo.6用）. <br>
     * Object#waitメソッドの呼び出し直前でDEBUGログを記録. <br>
     * <b>【注意】</b> 現在実行中のスレッドの情報は内部で取得します。
     *
     * @param methodName メソッド名称
     * @param objectForLock 同期用のモニターオブジェクト
     */
    public void beforeWait(String methodName, Object objectForLock) {
        this.loggedForWait(LOG_HEADER_WAIT_BEFORE, methodName, objectForLock);
    }

    /**
     * DEBUGログ記録（ルールNo.7用）. <br>
     * Object#waitメソッドの呼び出し直後でDEBUGログを記録. <br>
     * <b>【注意】</b> 現在実行中のスレッドの情報は内部で取得します。
     *
     * @param methodName メソッド名称
     * @param objectForLock 同期用のモニターオブジェクト
     */
    public void afterWait(String methodName, Object objectForLock) {
        this.loggedForWait(LOG_HEADER_WAIT_AFTER, methodName, objectForLock);
    }

    /**
     * DEBUGログ記録（ルールNo.6/7用）. <br>
     * <b>【注意】</b> 現在実行中のスレッドの情報は内部で取得します。
     *
     * @param headerLabel ヘッダラベル
     * @param methodName メソッド名称
     * @param objectForLock 同期用のモニターオブジェクト
     */
    private void loggedForWait(String headerLabel, String methodName,
            Object objectForLock) {

        // 該当ログが無効の場合は出力しない
        if (!this.isWaitEnabled()) {
            return;
        }

        StringBuffer buffer = new StringBuffer();
        buffer.append(headerLabel);
        buffer.append(LOG_LABEL_METHOD);
        buffer.append(this.usingClassName);
        buffer.append(TOKEN_CLASS_TO_METHOD);
        buffer.append(methodName);
        buffer.append(LOG_LABEL_MONITOR);
        buffer.append(createMonitorObjectLabel(objectForLock));
        buffer.append(LOG_LABEL_THREAD);
        buffer.append(Thread.currentThread().getName());

        this.debug(buffer.toString());
    }

    /**
     * モニターオブジェクトラベル生成. <br>
     * パラメータで与えられたモニターオブジェクトのラベルを生成する. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param objectForLock 同期用のモニターオブジェクト
     * @return 生成したラベル
     */
    private static String createMonitorObjectLabel(Object objectForLock) {

        // パラメータがnullの場合は、例外をスロー
        if (objectForLock == null) {
            throw new IllegalArgumentException("Parameter is null.");
        }

        // ラベルを生成してリターン
        return objectForLock.getClass().getName() + '@'
                + Integer.toHexString(objectForLock.hashCode());
    }

    /**
     * DEBUGログ有効判定（ルールNo.8/9用）. <br>
     * <b>【注意】</b> 本メソッドは本ロガーの内部で使用するのみとし、ロギングコードを記述する部分では使用してはならない.
     *
     * @return 有効な場合true、無効な場合false
     */
    private boolean isSleepEnabled() {

        return this.isDebugEnabled();
    }

    /**
     * DEBUGログ記録（ルールNo.8用）. <br>
     * Thread#sleepメソッドの呼び出し直前でDEBUGログを記録. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param methodName メソッド名称
     */
    public void beforeSleep(String methodName) {
        this.loggedForSleep(LOG_HEADER_SLEEP_BEFORE, methodName);
    }

    /**
     * DEBUGログ記録（ルールNo.9用）. <br>
     * Thread#sleepメソッドの呼び出し直後でDEBUGログを記録. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param methodName メソッド名称
     */
    public void afterSleep(String methodName) {
        this.loggedForSleep(LOG_HEADER_SLEEP_AFTER, methodName);
    }

    /**
     * DEBUGログ記録（ルールNo.8/9用）. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param headerLabel ヘッダーラベル
     * @param methodName メソッド名称
     */
    private void loggedForSleep(String headerLabel, String methodName) {

        // 該当ログが無効の場合は出力しない
        if (!this.isSleepEnabled()) {
            return;
        }

        StringBuffer buffer = new StringBuffer();
        buffer.append(headerLabel);
        buffer.append(LOG_LABEL_METHOD);
        buffer.append(this.usingClassName);
        buffer.append(TOKEN_CLASS_TO_METHOD);
        buffer.append(methodName);
        buffer.append(LOG_LABEL_THREAD);
        buffer.append(Thread.currentThread().getName());

        this.debug(buffer.toString());

    }

    /**
     * DEBUGログ有効判定（ルールNo.10/11用）. <br>
     * <b>【注意】</b> 本メソッドは本ロガーの内部で使用するのみとし、ロギングコードを記述する部分では使用してはならない.
     *
     * @return 有効な場合true、無効な場合false
     */
    private boolean isThreadRunEnabled() {

        return this.isDebugEnabled();
    }

    /**
     * DEBUGログ記録（ルールNo.10用）. <br>
     * Runnable#runメソッドの先頭でDEBUGログを記録. <br>
     * <b>【注意】</b> 特になし.
     */
    public void threadRunBegin() {
        this.loggedForThread(LOG_HEADER_THREAD_BEGIN);
    }

    /**
     * DEBUGログ記録（ルールNo.11用）. <br>
     * Runnable#runメソッドの末尾でDEBUGログを記録. <br>
     * <b>【注意】</b> 特になし.
     */
    public void threadRunEnd() {
        this.loggedForThread(LOG_HEADER_THREAD_END);
    }

    /**
     * DEBUGログ記録（ルールNo.10/11用）. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param headerLabel ヘッダラベル
     */
    private void loggedForThread(String headerLabel) {

        // 該当ログが無効の場合は出力しない
        if (!this.isThreadRunEnabled()) {
            return;
        }

        StringBuffer buffer = new StringBuffer();
        buffer.append(headerLabel);
        buffer.append(this.usingClassName);
        buffer.append(LOG_LABEL_THREAD);
        buffer.append(Thread.currentThread().getName());

        this.debug(buffer.toString());

    }

    /**
     * DEBUGログ有効判定（ルールNo.12用）. <br>
     * <b>【注意】</b> 特になし.
     *
     * @return 有効な場合true、無効な場合false
     */
    public boolean isServiceMethodInvokeEnabled() {

        return this.isDebugEnabled();
    }

    /**
     * DEBUGログ記録（ルールNo.12用）. <br>
     * サービス呼び出しの直前でDEBUGログを記録. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param serviceClazz 呼び出すサービスクラス
     * @param methodName 呼び出すメソッド名称
     */
    public void serviceMethodInvoke(Class<?> serviceClazz, String methodName) {
        this.serviceMethodInvoke(serviceClazz, methodName, null);
    }

    /**
     * DEBUGログ記録（ルールNo.12用）. <br>
     * サービス呼び出しの直前でDEBUGログを記録. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param serviceClazz 呼び出すサービスクラス
     * @param methodName 呼び出すメソッド名称
     * @param paramString 呼び出し時に渡すパラメータ
     */
    public void serviceMethodInvoke(Class<?> serviceClazz, String methodName,
            String paramString) {

        // 該当ログが無効の場合は出力しない
        if (!this.isServiceMethodInvokeEnabled()) {
            return;
        }

        StringBuffer buffer = new StringBuffer();
        buffer.append(LOG_HEADER_METHOD_INVOKE);
        buffer.append(LOG_LABEL_METHOD);
        if (serviceClazz != null) {
            buffer.append(serviceClazz.getName());
            buffer.append(TOKEN_CLASS_TO_METHOD);
        }
        buffer.append(methodName);
        buffer.append(LOG_LABEL_PARAMETERS);
        buffer.append(paramString);

        this.debug(buffer.toString());
    }

    /**
     * DEBUGログ有効判定（ルールNo.13用）. <br>
     * <b>【注意】</b> 特になし.
     *
     * @return 有効な場合true、無効な場合false
     */
    public boolean isServiceMethodResultEnabled() {

        return this.isDebugEnabled();
    }

    /**
     * DEBUGログ記録（ルールNo.13用）. <br>
     * サービス呼び出しの直後でDEBUGログを記録. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param serviceClazz 呼び出したサービスクラス
     * @param methodName 呼び出したメソッド名称
     */
    public void serviceMethodResult(Class<?> serviceClazz, String methodName) {
        this.serviceMethodResult(serviceClazz, methodName, null);
    }

    /**
     * DEBUGログ記録（ルールNo.13用）. <br>
     * サービス呼び出しの直後でDEBUGログを記録. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param serviceClazz 呼び出したサービスクラス
     * @param methodName 呼び出したメソッド名称
     * @param returnObject 戻り値
     */
    public void serviceMethodResult(Class<?> serviceClazz, String methodName,
            Object returnObject) {

        // 該当ログが無効の場合は出力しない
        if (!this.isServiceMethodResultEnabled()) {
            return;
        }

        StringBuffer buffer = new StringBuffer();
        buffer.append(LOG_HEADER_METHOD_RESULT);
        buffer.append(LOG_LABEL_METHOD);
        if (serviceClazz != null) {
            buffer.append(serviceClazz.getName());
            buffer.append(TOKEN_CLASS_TO_METHOD);
        }
        buffer.append(methodName);
        buffer.append(LOG_LABEL_RESULT);
        buffer.append(returnObject);

        this.debug(buffer.toString());

    }

}
