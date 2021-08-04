/*
 * EncodingFilter.java
 * Created on 2014/12/09
 * RCSID: $Id$
 *
 * (C) Copyright NTT-F 2014 All rights reserved.
 *
 * This software is furnished under a contract and use, duplication, disclosure
 * and all other uses are restricted to the rights specified in the written
 * contract and memorandum between the contractor and NTT-F.
 */
package jp.co.nttf.gms.common.internal.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import jp.co.nttf.gms.common.internal.AppLogger;

/**
 * エンコーディングフィルタ. <br>
 * <b>【注意】</b> 特になし.
 *
 * @author naya
 */
public final class EncodingFilter implements Filter {

    /** ロガー */
    private static final AppLogger LOGGER = new AppLogger(EncodingFilter.class);

    /** 文字エンコーディング */
    private String encoding = null;


    /**
     * フィルタ初期化メソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param fConfig フィルタ設定
     * @throws ServletException 処理中に異常が発生した場合
     * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
     */
    @Override
    public void init(FilterConfig fConfig) throws ServletException {
        // 初期化パラメータから文字エンコーディングを取得
        this.encoding = fConfig.getInitParameter("encoding");
    }

    /**
     * フィルタ処理を行うメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param request HTTPリクエスト
     * @param response HTTPレスポンス
     * @param chain フィルタチェーン
     * @throws ServletException 処理中に異常が発生した場合
     * @throws IOException 処理中に入出力例外が発生した場合
     * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest,
     *      javax.servlet.ServletResponse, javax.servlet.FilterChain)
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain) throws ServletException, IOException {

        // DEBUGログ（メソッドの先頭）
        if (LOGGER.isMethodInEnabled()) {
            LOGGER.methodIn("doFilter", "request=" + request + ", response="
                    + response + ", chain=" + chain);
        }

        // リクエストの文字エンコーディングを設定
        request.setCharacterEncoding(this.encoding);

        // 次のフィルタを実行
        chain.doFilter(request, response);
    }

    /**
     * サービスの終了を伝えるメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @see javax.servlet.Filter#destroy()
     */
    @Override
    public void destroy() {
        // 何もしない
    }

}
