/*
 * ErrInfoCalculator.java
 * Created on 2021/07/19
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
import jp.co.nttf.gms.common.internal.data.gmsdb.TDemandForecastError;
import jp.co.nttf.gms.common.internal.data.gmsdb.TDemandForecastErrorMapper;
import jp.co.nttf.gms.common.internal.data.gmsdb.TDemandForecastMapper;
import jp.co.nttf.gms.common.internal.data.gmsdb.TDemandGenerationLocation;
import jp.co.nttf.gms.common.internal.data.gmsdb.TDemandGenerationLocationMapper;
import jp.co.nttf.gms.common.internal.data.gmsdb.TDemandMeasurepointList;
import jp.co.nttf.gms.common.internal.data.gmsdb.TDemandMeasurepointListMapper;
import jp.co.nttf.gms.common.internal.data.gmsdb.TExtractvalue;
import jp.co.nttf.gms.common.internal.data.gmsdb.TExtractvalueMapper;
import jp.co.nttf.gms.common.internal.data.gmsdb.TGenerationForecast;
import jp.co.nttf.gms.common.internal.data.gmsdb.TGenerationForecastError;
import jp.co.nttf.gms.common.internal.data.gmsdb.TGenerationForecastErrorMapper;
import jp.co.nttf.gms.common.internal.data.gmsdb.TGenerationForecastMapper;
import jp.co.nttf.gms.common.internal.data.gmsdb.TGenerationMeasurepointList;
import jp.co.nttf.gms.common.internal.data.gmsdb.TGenerationMeasurepointListMapper;
import jp.co.nttf.gms.robust.batch.internal.AppConstants;
import jp.co.nttf.gms.robust.batch.internal.CommonBatch;
import jp.co.nttf.gms.robust.batch.internal.MessageID;

/**
 * 予測誤差の算出を行うバッチクラス. <br>
 * <b>【注意】</b> 特になし.
 *
 * @author sekiya
 */
public final class ErrInfoCalculator extends CommonBatch {
    /** 共通情報 */
    protected CommonInfo comInfo = CommonInfo.getInstance();

    /** ロガー */
    private static final AppLogger LOGGER = new AppLogger(ErrInfoCalculator.class);

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
            LOGGER.info(MessageUtil.getMessage(MessageID.IROB073));
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
            ErrInfoCalculator reporter = new ErrInfoCalculator();
            reporter.execute(argDate);

        } catch (Exception e) {
            // ERRORログ出力
            LOGGER.error(MessageUtil.getMessage(MessageID.ACOM000), e);
            throw e;
        }

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(MessageUtil.getMessage(MessageID.IROB074));
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

        // 需要発電計測ポイント情報のリストを取得
        List<TDemandGenerationLocation> listDGLocation = this.getDGLocationList();

        // 需要発電計測ポイント情報毎に実施
        for (TDemandGenerationLocation dgLocation : listDGLocation) {
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info(MessageUtil.getMessage(MessageID.IROB035, dgLocation.getDGLocationId()));
            }

            // 需要発電計測ポイント情報の需要計測ポイントリストIDに対応する需要計測ポイントリストを取得
            TDemandMeasurepointList listDemandMeasurepointId = this.getDemandMeasurepointId(dgLocation.getGenerationMeasurepointListId());

            double[] arrayDMeasurevalues = new double[AppConstants.E_24HR_INPUTDATA_COUNT];
            double[] arrayDForecastvalues = new double[AppConstants.E_24HR_INPUTDATA_COUNT];
            double[] arrayGMeasurevalues = new double[AppConstants.E_24HR_INPUTDATA_COUNT];
            double[] arrayGForecastvalues = new double[AppConstants.E_24HR_INPUTDATA_COUNT];

            // 需要計測ポイントID毎に実施
            for (int demandMeasurepointId : listDemandMeasurepointId.getDemandMeasurepointId()) {
                if (LOGGER.isInfoEnabled()) {
                    LOGGER.info(MessageUtil.getMessage(MessageID.IROB036, demandMeasurepointId));
                }

                // 需要実績をt_extractvalueテーブルから取得
                List<TExtractvalue> listDExtractvalues = this.getDemandExtractData(argDate, demandMeasurepointId);

                // 取得したデータ数が想定した24時間分のデータ数と一致した場合
                if (listDExtractvalues.size() == AppConstants.E_24HR_EXTRACTDATA_COUNT) {
                    // 需要発電計測ポイント単位で加算
                    for (int i=0; i<AppConstants.E_24HR_EXTRACTDATA_COUNT; i++) {

                        // 実績は1時間値を30分値に変換
                        arrayDMeasurevalues[i] += listDExtractvalues.get(i).getMeasurevalue() * AppConstants.ROBUST_30MIN_IN_HOUR;
                    }
                } else {
                    LOGGER.error(MessageUtil.getMessage(MessageID.EROB004, listDExtractvalues.size(), AppConstants.E_24HR_EXTRACTDATA_COUNT));
                    throw(new Exception());
                }
            }
            List<TDemandForecast> listDForecastvalues = this.getDemandForecast(dgLocation.getDGLocationId(), argDate);

            // 取得したデータ数が想定した24時間分のデータ数と一致した場合
            if (listDForecastvalues.size() == AppConstants.E_24HR_EXTRACTDATA_COUNT) {

                for (int i=0; i<AppConstants.E_24HR_EXTRACTDATA_COUNT; i++) {
                    arrayDForecastvalues[i] = listDForecastvalues.get(i).getDemandForecastValue();
                }
            } else {
                LOGGER.error(MessageUtil.getMessage(MessageID.EROB004, listDForecastvalues.size(), AppConstants.E_24HR_EXTRACTDATA_COUNT));
                throw(new Exception());
            }

            // 需要予測の誤差の算出
            List<TDemandForecastError> listDemandForecastErr = this.calcDemandErrInfo(dgLocation.getDGLocationId(), argDate, arrayDMeasurevalues, arrayDForecastvalues);

            // 需要発電計測ポイント情報の発電計測ポイントリストIDに対応する発電計測ポイントリストを取得
            TGenerationMeasurepointList listGenerationMeasurepointId = this.getGenerationMeasurepointId(dgLocation.getGenerationMeasurepointListId());

            // 発電計測ポイントリスト内の発電量の計測ポイントIDを取得
            int generationMeasurepointId = listGenerationMeasurepointId.getGenerationMeasurepointId()[AppConstants.BT_GENERATION_DATA_ORDER];

            if (LOGGER.isInfoEnabled()) {
                LOGGER.info(MessageUtil.getMessage(MessageID.IROB036, generationMeasurepointId));
            }

            // 発電実績をt_extractvalueテーブルから取得
            List<TExtractvalue> listGExtractvalues = this.getGenerationExtractData(argDate, generationMeasurepointId);
            // 発電予測をt_generation_forecastテーブルから取得
            List<TGenerationForecast> listGForecastvalues = this.getGenerationForecast(dgLocation.getDGLocationId(), argDate);

            // 取得したデータ数が想定した24時間分のデータ数と一致した場合
            if (listGExtractvalues.size() == AppConstants.E_24HR_EXTRACTDATA_COUNT
                    && listGForecastvalues.size() == AppConstants.E_24HR_EXTRACTDATA_COUNT) {

                for (int i=0; i<AppConstants.E_24HR_EXTRACTDATA_COUNT; i++) {
                    // 実績は1時間値を30分値に変換
                    arrayGMeasurevalues[i] = listGExtractvalues.get(i).getMeasurevalue() * AppConstants.ROBUST_30MIN_IN_HOUR;
                    arrayGForecastvalues[i] = listGForecastvalues.get(i).getGenerationForecastValue();
                }
            } else {
                LOGGER.error(MessageUtil.getMessage(MessageID.EROB004, listGExtractvalues.size(), AppConstants.E_24HR_EXTRACTDATA_COUNT));
                throw(new Exception());
            }

            // 発電予測の誤差の算出
            List<TGenerationForecastError> listGenerationForecastErr = this.calcGenerationErrInfo(dgLocation.getDGLocationId(), argDate, arrayGMeasurevalues, arrayGForecastvalues);

            // DB登録はまとめて実施 (どちらかだけが登録される状況を極力割ける)
            // 需要予測誤差のDB登録
            this.storeDemandErrInfo(listDemandForecastErr);
            // 発電予測誤差のDB登録
            this.storeGenerationErrInfo(listGenerationForecastErr);
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
     * 発電計測ポイントリストテーブルのDB検索を行うメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param generationMeasurepointListId 発電計測ポイントリストID
     * @return 発電計測ポイントリストテーブルの検索結果
     * @throws Exception 処理中に問題が発生した場合
     */
    private TGenerationMeasurepointList getGenerationMeasurepointId(Integer generationMeasurepointListId) throws Exception {
        TGenerationMeasurepointList list = null;

        SqlSession session = null;

        try {
            // DBセッションを取得
            session = this.comInfo.getSqlSessionFactory(
                    CommonConstants.ENVIRONMENT_GMSDB).openSession();

            // SQL実行
            TGenerationMeasurepointListMapper mapper = session.getMapper(TGenerationMeasurepointListMapper.class);
            list = mapper.selectByListId(generationMeasurepointListId);
        } finally {
            // セッションをクローズ
            if (null != session) {
                session.close();
            }
        }
        return(list);
    }

    /**
     * 抽出データテーブルにて需要実績値のDB検索を行うメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param execDate 処理日時(これより1時間前から遡って24時間分のデータを検索)
     * @param demandMeasurepointId 需要計測ポイントID
     * @return 抽出データテーブルの検索結果
     * @throws Exception 処理中に問題が発生した場合
     */
    private List<TExtractvalue> getDemandExtractData(String execDate, Integer demandMeasurepointId) throws Exception {
        List<TExtractvalue> list = null;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
        // startTSへの指定としては24時間+30分前で24時間分
        Timestamp startTS  = new Timestamp(sdf.parse(execDate).getTime() - AppConstants.ROBUST_30MIN_IN_MILLISEC - AppConstants.ROBUST_24HR_IN_MILLISEC);
        Timestamp endTS  = new Timestamp(sdf.parse(execDate).getTime() - AppConstants.ROBUST_1HR_IN_MILLISEC);
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

        //System.out.println("Demand Extract");
        //System.out.println(list.get(0).getMeasuretime().toString());
        //System.out.println(list.get(47).getMeasuretime().toString());

        return(list);
    }

    /**
     * 抽出データテーブルにて発電実績値のDB検索を行うメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param execDate 処理日時(これより1時間前から遡って24時間分のデータを検索)
     * @param generationMeasurepointId 発電計測ポイントID
     * @return 抽出データテーブルの検索結果
     * @throws Exception 処理中に問題が発生した場合
     */
    private List<TExtractvalue> getGenerationExtractData(String execDate, Integer generationMeasurepointId) throws Exception {
        List<TExtractvalue> list = null;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
        // startTSへの指定としては24時間+30分前で24時間分
        Timestamp startTS  = new Timestamp(sdf.parse(execDate).getTime() - AppConstants.ROBUST_30MIN_IN_MILLISEC - AppConstants.ROBUST_24HR_IN_MILLISEC);
        Timestamp endTS  = new Timestamp(sdf.parse(execDate).getTime() - AppConstants.ROBUST_1HR_IN_MILLISEC);
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(MessageUtil.getMessage(MessageID.IROB042, sdf.format(startTS)));
            LOGGER.info(MessageUtil.getMessage(MessageID.IROB043, sdf.format(endTS)));
        }

        SqlSession session = null;

        try {
            // DBセッションを取得
            session = this.comInfo.getSqlSessionFactory(
                    CommonConstants.ENVIRONMENT_GMSDB).openSession();

            // SQL実行
            TExtractvalueMapper mapper = session.getMapper(TExtractvalueMapper.class);
            list = mapper.getRangeData(generationMeasurepointId, startTS, endTS, 2);
        } finally {
            // セッションをクローズ
            if (null != session) {
                session.close();
            }
        }

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(MessageUtil.getMessage(MessageID.IROB044, list.size()));
            LOGGER.info(MessageUtil.getMessage(MessageID.IROB045, sdf.format(list.get(0).getMeasuretime())));
            LOGGER.info(MessageUtil.getMessage(MessageID.IROB046, sdf.format(list.get(list.size()-1).getMeasuretime())));
        }

        //System.out.println("Generation Extract");
        //System.out.println(list.get(0).getMeasuretime().toString());
        //System.out.println(list.get(47).getMeasuretime().toString());

        return(list);
    }

    /**
     * 需要予測テーブルのDB検索を行うメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param dgLocationId 需要発電計測ポイントID
     * @param execDate 処理日時
     * @return 需要予測テーブルの検索結果
     * @throws Exception 処理中に問題が発生した場合
     */
    private List<TDemandForecast> getDemandForecast(Integer dgLocationId, String execDate) throws Exception {
        List<TDemandForecast> results = new ArrayList<TDemandForecast>();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
        // startTSへの指定としては24時間+30分前で24時間分
        long startTime  = sdf.parse(execDate).getTime() - AppConstants.ROBUST_30MIN_IN_MILLISEC - AppConstants.ROBUST_24HR_IN_MILLISEC;
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(MessageUtil.getMessage(MessageID.IROB047, sdf.format(new Timestamp(startTime))));
        }

        SqlSession session = null;

        try {
            // DBセッションを取得
            session = this.comInfo.getSqlSessionFactory(
                    CommonConstants.ENVIRONMENT_GMSDB).openSession();

            // SQL実行
            TDemandForecastMapper mapper = session.getMapper(TDemandForecastMapper.class);

            for (int i=0; i<48; i++) {
                Timestamp targetTS = new Timestamp(startTime + i * AppConstants.ROBUST_30MIN_IN_MILLISEC);
                TDemandForecast result = mapper.getDataWithForecastTime(dgLocationId, targetTS);
                results.add(result);

                if (i==47) {
                    if (LOGGER.isInfoEnabled()) {
                        LOGGER.info(MessageUtil.getMessage(MessageID.IROB048, sdf.format(targetTS)));
                    }
                }
            }
        } finally {
            // セッションをクローズ
            if (null != session) {
                session.close();
            }
        }

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(MessageUtil.getMessage(MessageID.IROB049, results.size()));
            LOGGER.info(MessageUtil.getMessage(MessageID.IROB050, sdf.format(results.get(0).getForecastTime())));
            LOGGER.info(MessageUtil.getMessage(MessageID.IROB051, sdf.format(results.get(results.size()-1).getForecastTime())));
        }

        //System.out.println("Demand Forecast");
        //System.out.println(results.get(0).getForecastTime().toString());
        //System.out.println(results.get(47).getForecastTime().toString());

        return(results);
    }

    /**
     * 発電予測テーブルのDB検索を行うメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param dgLocationId 需要発電計測ポイントID
     * @param execDate 処理日時
     * @return 発電予測テーブルの検索結果
     * @throws Exception 処理中に問題が発生した場合
     */
    private List<TGenerationForecast> getGenerationForecast(Integer dgLocationId, String execDate) throws Exception {
        List<TGenerationForecast> results = new ArrayList<TGenerationForecast>();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
        long startTime  = sdf.parse(execDate).getTime() - AppConstants.ROBUST_30MIN_IN_MILLISEC - AppConstants.ROBUST_24HR_IN_MILLISEC;
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(MessageUtil.getMessage(MessageID.IROB052, sdf.format(new Timestamp(startTime))));
        }

        SqlSession session = null;

        try {
            // DBセッションを取得
            session = this.comInfo.getSqlSessionFactory(
                    CommonConstants.ENVIRONMENT_GMSDB).openSession();

            // SQL実行
            TGenerationForecastMapper mapper = session.getMapper(TGenerationForecastMapper.class);

            for (int i=0; i<48; i++) {
                Timestamp targetTS = new Timestamp(startTime + i * AppConstants.ROBUST_30MIN_IN_MILLISEC);
                TGenerationForecast result = mapper.getDataWithForecastTime(dgLocationId, targetTS);
                results.add(result);

                if (i==47) {
                    if (LOGGER.isInfoEnabled()) {
                        LOGGER.info(MessageUtil.getMessage(MessageID.IROB053, sdf.format(targetTS)));
                    }
                }
            }
        } finally {
            // セッションをクローズ
            if (null != session) {
                session.close();
            }
        }

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(MessageUtil.getMessage(MessageID.IROB054, results.size()));
            LOGGER.info(MessageUtil.getMessage(MessageID.IROB055, sdf.format(results.get(0).getForecastTime())));
            LOGGER.info(MessageUtil.getMessage(MessageID.IROB056, sdf.format(results.get(results.size()-1).getForecastTime())));
        }

        //System.out.println("Generation Forecast");
        //System.out.println(results.get(0).getForecastTime().toString());
        //System.out.println(results.get(47).getForecastTime().toString());

        return(results);
    }

    /**
     * 需要予測誤差の算出を行うメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param dgLocationId 需要発電計測ポイントID
     * @param execDate 処理日時
     * @param measureValues 実績値配列
     * @param forecastValues 予測値配列
     * @return 需要予測誤差情報のリスト
     * @throws Exception 処理中に問題が発生した場合
     */
    private List<TDemandForecastError> calcDemandErrInfo(int dgLocationId, String execDate, double[] measureValues, double[] forecastValues) throws Exception {
        List<TDemandForecastError> results = new ArrayList<TDemandForecastError>();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
        long startTime  = sdf.parse(execDate).getTime() - AppConstants.ROBUST_30MIN_IN_MILLISEC - AppConstants.ROBUST_24HR_IN_MILLISEC;

        for (int i=0; i<AppConstants.E_24HR_INPUTDATA_COUNT; i++) {
            Timestamp targetTS = new Timestamp(startTime + i * AppConstants.ROBUST_30MIN_IN_MILLISEC);
            TDemandForecastError result = new TDemandForecastError();
            // 需要発電計測ポイントID
            result.setDGLocationId(dgLocationId);
            //実施時刻
            result.setCalcTime(new Timestamp(sdf.parse(execDate).getTime()));

            // データ時刻
            result.setTime(targetTS);

            if (LOGGER.isInfoEnabled()) {
                LOGGER.info(MessageUtil.getMessage(MessageID.IROB057, sdf.format(targetTS)));
            }

            // 予測積算値
            double sumForecastValues = 0;
            for (int j=0; j<AppConstants.E_24HR_INPUTDATA_COUNT; j++) {
                sumForecastValues += forecastValues[j];
            }
            result.setIntegratedForecastVal(sumForecastValues);

            if (LOGGER.isInfoEnabled()) {
                LOGGER.info(MessageUtil.getMessage(MessageID.IROB058, sumForecastValues));
            }

            // 実績積算値
            double sumMeasureValues = 0;
            for (int j=0; j<AppConstants.E_24HR_INPUTDATA_COUNT; j++) {
                sumMeasureValues += measureValues[j];
            }
            result.setIntegratedActualVal(sumMeasureValues);

            if (LOGGER.isInfoEnabled()) {
                LOGGER.info(MessageUtil.getMessage(MessageID.IROB059, sumMeasureValues));
            }

            // RMSE
            double rmse = 0;
            for (int j=0; j<AppConstants.E_24HR_INPUTDATA_COUNT; j++) {
                rmse += Math.pow(forecastValues[i] - measureValues[i], 2);
            }
            rmse /= AppConstants.E_24HR_INPUTDATA_COUNT;
            rmse = Math.pow(rmse, 0.5);
            result.setRmse(rmse);

            if (LOGGER.isInfoEnabled()) {
                LOGGER.info(MessageUtil.getMessage(MessageID.IROB060, rmse));
            }

            // 誤差率
            double errRate = (forecastValues[i] - measureValues[i]) / forecastValues[i];
            result.setErrorRate(errRate);

            if (LOGGER.isInfoEnabled()) {
                LOGGER.info(MessageUtil.getMessage(MessageID.IROB061, errRate));
            }

            results.add(result);
        }

        return(results);
    }

    /**
     * 発電予測誤差の算出を行うメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param dgLocationId 需要発電計測ポイントID
     * @param execDate 処理日時
     * @param measureValues 実績値配列
     * @param forecastValues 予測値配列
     * @return 発電予測誤差情報のリスト
     * @throws Exception 処理中に問題が発生した場合
     */
    private List<TGenerationForecastError> calcGenerationErrInfo(int dgLocationId, String execDate, double[] measureValues, double[] forecastValues) throws Exception {
        List<TGenerationForecastError> results = new ArrayList<TGenerationForecastError>();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
        long startTime  = sdf.parse(execDate).getTime() - AppConstants.ROBUST_30MIN_IN_MILLISEC - AppConstants.ROBUST_24HR_IN_MILLISEC;

        for (int i=0; i<AppConstants.E_24HR_INPUTDATA_COUNT; i++) {
            Timestamp targetTS = new Timestamp(startTime + i * AppConstants.ROBUST_30MIN_IN_MILLISEC);
            TGenerationForecastError result = new TGenerationForecastError();
            // 需要発電計測ポイントID
            result.setDGLocationId(dgLocationId);
            //実施時刻
            result.setCalcTime(new Timestamp(sdf.parse(execDate).getTime()));

            // データ時刻
            result.setTime(targetTS);

            if (LOGGER.isInfoEnabled()) {
                LOGGER.info(MessageUtil.getMessage(MessageID.IROB062, sdf.format(targetTS)));
            }

            // 予測積算値
            double sumForecastValues = 0;
            for (int j=0; j<AppConstants.E_24HR_INPUTDATA_COUNT; j++) {
                sumForecastValues += forecastValues[j];
            }
            result.setIntegratedForecastVal(sumForecastValues);

            if (LOGGER.isInfoEnabled()) {
                LOGGER.info(MessageUtil.getMessage(MessageID.IROB063, sumForecastValues));
            }

            // 実績積算値
            double sumMeasureValues = 0;
            for (int j=0; j<AppConstants.E_24HR_INPUTDATA_COUNT; j++) {
                sumMeasureValues += measureValues[j];
            }
            result.setIntegratedActualVal(sumMeasureValues);

            if (LOGGER.isInfoEnabled()) {
                LOGGER.info(MessageUtil.getMessage(MessageID.IROB064, sumMeasureValues));
            }

            // RMSE
            double rmse = 0;
            for (int j=0; j<AppConstants.E_24HR_INPUTDATA_COUNT; j++) {
                rmse += Math.pow(forecastValues[i] - measureValues[i], 2);
            }
            rmse /= AppConstants.E_24HR_INPUTDATA_COUNT;
            rmse = Math.pow(rmse, 0.5);
            result.setRmse(rmse);

            if (LOGGER.isInfoEnabled()) {
                LOGGER.info(MessageUtil.getMessage(MessageID.IROB065, rmse));
            }

            // 誤差率
            double errRate = (forecastValues[i] - measureValues[i]) / forecastValues[i];
            result.setErrorRate(errRate);

            if (LOGGER.isInfoEnabled()) {
                LOGGER.info(MessageUtil.getMessage(MessageID.IROB066, errRate));
            }

            results.add(result);
        }

        return(results);
    }

    /**
     * 需要予測誤差のDB登録を行うメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param listForecastErr DB登録対象の需要予測誤差のリスト
     * @throws Exception 処理中に問題が発生した場合
     */
    private void storeDemandErrInfo(List<TDemandForecastError> listForecastErr) throws Exception {
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(MessageUtil.getMessage(MessageID.IROB075));
        }

        SqlSession session = null;

        try {
            // DBセッションを取得
            session = this.comInfo.getSqlSessionFactory(
                    CommonConstants.ENVIRONMENT_GMSDB).openSession();

            // SQL実行
            TDemandForecastErrorMapper mapper = session.getMapper(TDemandForecastErrorMapper.class);

            for (TDemandForecastError param : listForecastErr) {
                mapper.insert(param);
            }

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

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(MessageUtil.getMessage(MessageID.IROB076));
        }
    }

    /**
     * 発電予測誤差のDB登録を行うメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param listGenerationErr DB登録対象の発電予測誤差のリスト
     * @throws Exception 処理中に問題が発生した場合
     */
    private void storeGenerationErrInfo(List<TGenerationForecastError> listForecastErr) throws Exception {
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(MessageUtil.getMessage(MessageID.IROB077));
        }

        SqlSession session = null;

        try {
            // DBセッションを取得
            session = this.comInfo.getSqlSessionFactory(
                    CommonConstants.ENVIRONMENT_GMSDB).openSession();

            // SQL実行
            TGenerationForecastErrorMapper mapper = session.getMapper(TGenerationForecastErrorMapper.class);

            for (TGenerationForecastError param : listForecastErr) {
                mapper.insert(param);
            }

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

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(MessageUtil.getMessage(MessageID.IROB078));
        }
    }
}
