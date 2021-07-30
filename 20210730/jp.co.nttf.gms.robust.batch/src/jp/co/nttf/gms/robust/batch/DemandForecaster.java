/*
 * DemandForecaster.java
 * Created on 2021/07/14
 * RCSID: $Id$
 *
 * (C) Copyright NTT-F 2021 All rights reserved.
 *
 * This software is furnished under a contract and use, duplication, disclosure
 * and all other uses are restricted to the rights specified in the written
 * contract and memorandum between the contractor and NTT-F.
 */
package jp.co.nttf.gms.robust.batch;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.ibatis.session.SqlSession;

import jp.co.nttf.gms.common.internal.AppLogger;
import jp.co.nttf.gms.common.internal.CommonConstants;
import jp.co.nttf.gms.common.internal.CommonInfo;
import jp.co.nttf.gms.common.internal.MessageUtil;
import jp.co.nttf.gms.common.internal.data.gmsdb.TDemandForecast;
import jp.co.nttf.gms.common.internal.data.gmsdb.TDemandForecastMapper;
import jp.co.nttf.gms.common.internal.data.gmsdb.TDemandGenerationLocation;
import jp.co.nttf.gms.common.internal.data.gmsdb.TDemandGenerationLocationMapper;
import jp.co.nttf.gms.common.internal.data.gmsdb.TDemandMeasurepointList;
import jp.co.nttf.gms.common.internal.data.gmsdb.TDemandMeasurepointListMapper;
import jp.co.nttf.gms.common.internal.data.gmsdb.TExtractvalue;
import jp.co.nttf.gms.common.internal.data.gmsdb.TExtractvalueMapper;
import jp.co.nttf.gms.robust.batch.internal.AppConstants;
import jp.co.nttf.gms.robust.batch.internal.CommonBatch;
import jp.co.nttf.gms.robust.batch.internal.MessageID;

/**
 * 需要予測値の算出を行うバッチクラス. <br>
 * <b>【注意】</b> 特になし.
 *
 * @author sekiya
 */
public final class DemandForecaster extends CommonBatch {
    /** 共通情報 */
    protected CommonInfo comInfo = CommonInfo.getInstance();

    /** ロガー */
    private static final AppLogger LOGGER = new AppLogger(DemandForecaster.class);

    /**
     * メインメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param args コマンドライン引数(args[0]:処理日時【必須】[YYYYmmddHHMM])
     * @throws Exception  処理中に問題が発生した場合
     */
    public static void main(String[] args) throws Exception {
        // メッセージプロパティをロード
        MessageUtil.setResourceBundle(ResourceBundle
                .getBundle(AppConstants.MESSAGE_RESOURCE_NAME));

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(MessageUtil.getMessage(MessageID.IROB018));
        }

        // 引数の取得
        if (args.length == 0) {
            LOGGER.error(MessageUtil.getMessage(MessageID.EROB002, args.length));
            throw new Exception();
        }
        String argDate = args[0];
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(MessageUtil.getMessage(MessageID.IROB020, argDate));
        }

        try {
            // 処理を実行
            DemandForecaster reporter = new DemandForecaster();
            reporter.execute(argDate);

        } catch (Exception e) {
            // ERRORログ出力
            LOGGER.error(MessageUtil.getMessage(MessageID.ACOM000), e);
            throw e;
        }

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(MessageUtil.getMessage(MessageID.IROB019));
        }
    }

    /**
     * 実行メソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param argDate メインバッチから渡された基準となる処理日時
     * @throws Exception 処理中に問題が発生した場合
     */
    private void execute(String argDate) throws Exception {
        // 初期化処理
        this.init();

        // 予測設定ファイルの読み込み
        ResourceBundle demandBundle = ResourceBundle.getBundle(AppConstants.F_RESOURCE_NAME);

        // 需要発電計測ポイント情報のリストを取得
        List<TDemandGenerationLocation> listDGLocation = this.getDGLocationList();

        // 需要発電計測ポイント情報毎に実施
        for (TDemandGenerationLocation dgLocation : listDGLocation) {
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info(MessageUtil.getMessage(MessageID.IROB035, dgLocation.getDGLocationId()));
            }

            // 需要発電計測ポイント情報の需要計測ポイントリストIDに対応する需要計測ポイントリストを取得
            TDemandMeasurepointList listDemandMeasurepointId = this.getDemandMeasurepointId(dgLocation.getDemandMeasurepointListId());

            double[] listSumMeasurevalues = new double[AppConstants.D_1WEEK_EXTRACTDATA_COUNT];

            // 需要計測ポイントリスト内の計測ポイントID毎に実施
            for (int measurepointId : listDemandMeasurepointId.getDemandMeasurepointId()) {
                if (LOGGER.isInfoEnabled()) {
                    LOGGER.info(MessageUtil.getMessage(MessageID.IROB036, measurepointId));
                }

                // 予測設定のデータ確認リトライ回数分繰り返し
                int cntMax = Integer.valueOf(demandBundle.getString(AppConstants.F_RESOURCE_KEY_RETRYCOUNT));
                for (int i=0; i<cntMax; i++) {
                    // t_extractvalueテーブルの最新のデータの日時が、処理日時の30分前以降であればOK
                    if(this.isExistExtractvalue(argDate, measurepointId)) {
                        break;
                    }

                    if (i == cntMax-1) {
                        LOGGER.error(MessageUtil.getMessage(MessageID.EROB003));
                        throw(new Exception());
                    }
                    Thread.sleep(Long.valueOf(demandBundle.getString(AppConstants.F_RESOURCE_KEY_RETRYINTERVAL)) * 1000);
                }

                // t_extractvalueテーブルから1週間分の需要電力の実績値を取得
                List<TExtractvalue> listExtractvalues = this.getDemandExtractData(argDate, measurepointId);

                // 取得したデータ数が想定した1週間分のデータ数と一致した場合
                if (listExtractvalues.size() == AppConstants.D_1WEEK_EXTRACTDATA_COUNT) {
                    // 需要発電計測ポイント単位で加算
                    for (int i=0; i<AppConstants.D_1WEEK_EXTRACTDATA_COUNT; i++) {
                        listSumMeasurevalues[i] += listExtractvalues.get(i).getMeasurevalue();
                    }
                } else {
                    LOGGER.error(MessageUtil.getMessage(MessageID.EROB004, listExtractvalues.size(), AppConstants.D_1WEEK_EXTRACTDATA_COUNT));
                    throw(new Exception());
                }
            }

            // 予測値の算出
            double[] forecastValues = this.calcForecastValue(listSumMeasurevalues, demandBundle);
            // 予測値のDB登録
            this.storeForecastData(dgLocation.getDGLocationId(), argDate, forecastValues);
        }
    }

    /**
     * 需要発電計測ポイント情報の全件取得を行うメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @return 需要発電計測ポイント情報のリスト
     * @throws Exception 処理中に問題が発生した場合
     */
    private List<TDemandGenerationLocation> getDGLocationList() throws Exception{
        List<TDemandGenerationLocation> list = new ArrayList<TDemandGenerationLocation>();

        SqlSession session = null;

        try {
            // DBセッションを取得
            session = this.comInfo.getSqlSessionFactory(
                    CommonConstants.ENVIRONMENT_GMSDB).openSession();

            // SQL実行
            TDemandGenerationLocationMapper mapper = session.getMapper(TDemandGenerationLocationMapper.class);
            list = mapper.selectAll();
        } finally {
            // セッションをクローズ
            if (null != session) {
                session.close();
            }
        }
        return(list);
    }

    /**
     * 需要計測ポイントリストテーブルのDB検索を行うメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param demandMeasurepointListId 需要計測ポイントリストID
     * @return 需要計測ポイントリストテーブルの検索結果
     * @throws Exception 処理中に問題が発生した場合
     */
    private TDemandMeasurepointList getDemandMeasurepointId(Integer demandMeasurepointListId) throws Exception {
        TDemandMeasurepointList list = null;

        SqlSession session = null;

        try {
            // DBセッションを取得
            session = this.comInfo.getSqlSessionFactory(
                    CommonConstants.ENVIRONMENT_GMSDB).openSession();

            // SQL実行
            TDemandMeasurepointListMapper mapper = session.getMapper(TDemandMeasurepointListMapper.class);
            list = mapper.selectByListId(demandMeasurepointListId);
        } finally {
            // セッションをクローズ
            if (null != session) {
                session.close();
            }
        }
        return(list);
    }

    /**
     * 抽出データテーブルのDB検索を行うメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param execDate 処理日時(これより前の1週間分のデータを検索)
     * @param demandMeasurepointId 需要計測ポイントID
     * @return 抽出データテーブルの検索結果
     * @throws Exception 処理中に問題が発生した場合
     */
    private boolean isExistExtractvalue(String execDate, Integer demandMeasurepointId) throws Exception {
        boolean result = false;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
        TExtractvalue param = new TExtractvalue();
        param.setMeasurepointid(demandMeasurepointId);
        param.setMeasuretime(new Timestamp(sdf.parse(execDate).getTime() - AppConstants.ROBUST_1WEEK_IN_MILLISEC));
        param.setTotalflag(AppConstants.ROBUST_TOTALFLAG_DEMAND);

        SqlSession session = null;

        try {
            // DBセッションを取得
            session = this.comInfo.getSqlSessionFactory(
                    CommonConstants.ENVIRONMENT_GMSDB).openSession();

            // SQL実行
            TExtractvalueMapper mapper = session.getMapper(TExtractvalueMapper.class);
            TExtractvalue data = mapper.selectLatest(param);

            // 最新のデータ日時が処理日時の30分前と同じか新しい
            if (data.getMeasuretime().getTime() >= sdf.parse(execDate).getTime() - AppConstants.ROBUST_30MIN_IN_MILLISEC) {
                result = true;
            }
        } finally {
            // セッションをクローズ
            if (null != session) {
                session.close();
            }
        }

        return(result);
    }

    /**
     * 抽出データテーブルのDB検索を行うメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param execDate 処理日時(これより前の1週間分のデータを検索)
     * @param demandMeasurepointId 需要計測ポイントID
     * @return 抽出データテーブルの検索結果
     * @throws Exception 処理中に問題が発生した場合
     */
    private List<TExtractvalue> getDemandExtractData(String execDate, Integer demandMeasurepointId) throws Exception {
        List<TExtractvalue> list = null;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
        Timestamp startTS  = new Timestamp(sdf.parse(execDate).getTime() - AppConstants.ROBUST_1WEEK_IN_MILLISEC);
        Timestamp endTS  = new Timestamp(sdf.parse(execDate).getTime() - AppConstants.ROBUST_30MIN_IN_MILLISEC);
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(MessageUtil.getMessage(MessageID.IROB037, sdf.format(startTS)));
            LOGGER.info(MessageUtil.getMessage(MessageID.IROB038, sdf.format(endTS)));
        }

        SqlSession session = null;

        try {
            // DBセッションを取得
            session = this.comInfo.getSqlSessionFactory(
                    CommonConstants.ENVIRONMENT_GMSDB).openSession();

            // SQL実行
            TExtractvalueMapper mapper = session.getMapper(TExtractvalueMapper.class);
            list = mapper.getRangeData(demandMeasurepointId, startTS, endTS, 2);
        } finally {
            // セッションをクローズ
            if (null != session) {
                session.close();
            }
        }

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(MessageUtil.getMessage(MessageID.IROB039, list.size()));
            LOGGER.info(MessageUtil.getMessage(MessageID.IROB040, sdf.format(list.get(0).getMeasuretime())));
            LOGGER.info(MessageUtil.getMessage(MessageID.IROB041, sdf.format(list.get(list.size()-1).getMeasuretime())));
        }

        return(list);
    }

    /**
     * 需要予測の算出を行うメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param extractValues t_extractvalueテーブルから取得したデータ
     * @param demandBundle 予測設定ファイル情報
     * @return 需要電力(予測)の算出結果
     * @throws Exception 処理中に問題が発生した場合
     */
    private double[] calcForecastValue(double[] measureValues, ResourceBundle demandBundle) throws Exception {
        // 第1係数
        double coe1 = Double.valueOf(demandBundle.getString(AppConstants.F_RESOURCE_KEY_COEFFICIENT1));
        // 第2係数
        double coe2 = Double.valueOf(demandBundle.getString(AppConstants.F_RESOURCE_KEY_COEFFICIENT2));
        // 第3係数
        double coe3 = Double.valueOf(demandBundle.getString(AppConstants.F_RESOURCE_KEY_COEFFICIENT3));
        // 第4係数
        double coe4 = Double.valueOf(demandBundle.getString(AppConstants.F_RESOURCE_KEY_COEFFICIENT4));
        // 第5係数
        double coe5 = Double.valueOf(demandBundle.getString(AppConstants.F_RESOURCE_KEY_COEFFICIENT5));
        // 第6係数
        double coe6 = Double.valueOf(demandBundle.getString(AppConstants.F_RESOURCE_KEY_COEFFICIENT6));
        // 第7係数
        double coe7 = Double.valueOf(demandBundle.getString(AppConstants.F_RESOURCE_KEY_COEFFICIENT7));
        // 予測ステップ数
        int steps = Integer.valueOf(demandBundle.getString(AppConstants.F_RESOURCE_KEY_STEPS));

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(MessageUtil.getMessage(MessageID.IROB025, coe1, coe2, coe3, coe4, coe5));
            LOGGER.info(MessageUtil.getMessage(MessageID.IROB026, coe6, coe7));
            LOGGER.info(MessageUtil.getMessage(MessageID.IROB027, steps));
        }

        // 予測結果配列
        double[] results = new double[steps];

        for (int i=0; i<steps; i++) {
            double result = 0;
            result += coe1 * measureValues[i+steps*6];
            result += coe2 * measureValues[i+steps*5];
            result += coe3 * measureValues[i+steps*4];
            result += coe4 * measureValues[i+steps*3];
            result += coe5 * measureValues[i+steps*2];
            result += coe6 * measureValues[i+steps*1];
            result += coe7 * measureValues[i+steps*0];
            result /= coe1 + coe2 + coe3 + coe4 + coe5 + coe6 + coe7;

            if (LOGGER.isInfoEnabled()) {
                LOGGER.info(MessageUtil.getMessage(MessageID.IROB028, result));
            }

            results[i] = result;
        }
        return(results);
    }

    /**
     * 需要予測のDB登録を行うメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param dgLocationId DB登録対象の需要発電計測ポイントID
     * @param execDate DB登録対象の処理日時(予測実施時刻)
     * @param extractValues t_extractvalueテーブルから取得したデータ(予測データ時刻の算出元として使用)
     * @param forecastValues DB登録対象の需要電力(予測値)
     * @throws Exception 処理中に問題が発生した場合
     */
    private void storeForecastData(int dgLocationId, String execDate, double[] forecastValues) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
        Timestamp calcTS  = new Timestamp(sdf.parse(execDate).getTime());

        for (int i=0; i<24 * 2; i++) {
            // 処理日時の30分後から24時間分が予測日時に該当
            Timestamp foreTS = new Timestamp(sdf.parse(execDate).getTime() + (i+1) * AppConstants.ROBUST_30MIN_IN_MILLISEC);

            // 登録対象のパラメータをセット
            TDemandForecast param = new TDemandForecast();
            // 需要発電計測ポイントID
            param.setDGLocationId(dgLocationId);
            // 予測実施時刻を設定
            param.setCalcTime(calcTS);
            // 予測データ時刻を設定
            param.setForecastTime(foreTS);
            // 需要電力(予測)を設定
            param.setDemandForecastValue(forecastValues[i]);

            if (LOGGER.isInfoEnabled()) {
                LOGGER.info(MessageUtil.getMessage(MessageID.IROB023));
            }

            SqlSession session = null;

            try {
                // DBセッションを取得
                session = this.comInfo.getSqlSessionFactory(
                        CommonConstants.ENVIRONMENT_GMSDB).openSession();

                // SQL実行
                TDemandForecastMapper mapper = session.getMapper(TDemandForecastMapper.class);
                mapper.insert(param);

                // コミット
                session.commit();
                if (LOGGER.isInfoEnabled()) {
                    LOGGER.info(MessageUtil.getMessage(MessageID.IROB024));
                }
            } finally {
                // セッションをクローズ
                if (null != session) {
                    session.close();
                }
            }
        }
    }
}
