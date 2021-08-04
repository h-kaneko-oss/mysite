/*
 * DataSheetDownloader.java
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

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.ibatis.session.SqlSession;

import jp.co.nttf.gms.common.internal.AppLogger;
import jp.co.nttf.gms.common.internal.CommonConstants;
import jp.co.nttf.gms.common.internal.CommonInfo;
import jp.co.nttf.gms.common.internal.Configuration;
import jp.co.nttf.gms.common.internal.MessageUtil;
import jp.co.nttf.gms.common.internal.data.gmsdb.TDemandForecast;
import jp.co.nttf.gms.common.internal.data.gmsdb.TDemandForecastMapper;
import jp.co.nttf.gms.common.internal.data.gmsdb.TDemandGenerationLocation;
import jp.co.nttf.gms.common.internal.data.gmsdb.TDemandGenerationLocationMapper;
import jp.co.nttf.gms.common.internal.data.gmsdb.TDemandMeasurepointList;
import jp.co.nttf.gms.common.internal.data.gmsdb.TDemandMeasurepointListMapper;
import jp.co.nttf.gms.common.internal.data.gmsdb.TExtractvalue;
import jp.co.nttf.gms.common.internal.data.gmsdb.TExtractvalueMapper;
import jp.co.nttf.gms.common.internal.data.gmsdb.TGenerationForecast;
import jp.co.nttf.gms.common.internal.data.gmsdb.TGenerationForecastMapper;
import jp.co.nttf.gms.common.internal.data.gmsdb.TGenerationMeasurepointList;
import jp.co.nttf.gms.common.internal.data.gmsdb.TGenerationMeasurepointListMapper;
import jp.co.nttf.gms.common.internal.data.gmsdb.TSimulationIntermediate;
import jp.co.nttf.gms.common.internal.data.gmsdb.TSimulationIntermediateMapper;
import jp.co.nttf.gms.common.internal.data.gmsdb.TSimulationResult;
import jp.co.nttf.gms.common.internal.data.gmsdb.TSimulationResultMapper;
import jp.co.nttf.gms.robust.batch.internal.AppConstants;
import jp.co.nttf.gms.robust.batch.internal.CommonBatch;
import jp.co.nttf.gms.robust.batch.internal.MessageID;

/**
 * 需要予測値の算出を行うバッチクラス. <br>
 * <b>【注意】</b> 特になし.
 *
 * @author sekiya
 */
public final class DataSheetDownloader extends CommonBatch {
    /** 共通情報 */
    protected CommonInfo comInfo = CommonInfo.getInstance();

    /** ロガー */
    private static final AppLogger LOGGER = new AppLogger(DataSheetDownloader.class);

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
            LOGGER.info(MessageUtil.getMessage(MessageID.IROB107));
        }

        // 引数の取得
        if (args.length < 3) {
            LOGGER.error(MessageUtil.getMessage(MessageID.EROB002, args.length));
            throw new Exception();
        }
        int dgLocationId = Integer.valueOf(args[0]);
        String startDate = args[1];
        String endDate = args[2];
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(MessageUtil.getMessage(MessageID.IROB104, dgLocationId));
            LOGGER.info(MessageUtil.getMessage(MessageID.IROB105, startDate));
            LOGGER.info(MessageUtil.getMessage(MessageID.IROB106, endDate));
        }

        try {
            // 処理を実行
            DataSheetDownloader reporter = new DataSheetDownloader();
            reporter.execute(dgLocationId, startDate, endDate);

        } catch (Exception e) {
            // ERRORログ出力
            LOGGER.error(MessageUtil.getMessage(MessageID.ACOM000), e);
            throw e;
        }

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(MessageUtil.getMessage(MessageID.IROB108));
        }
    }

    /**
     * 実行メソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param dgLocationId コマンドパラメータとして渡された需要発電計測ポイントID
     * @param startDate コマンドパラメータとして渡された検索の始点日時
     * @param endDate コマンドラインパラメータとして渡された検索の終点日時
     * @throws Exception 処理中に問題が発生した場合
     */
    private void execute(int dgLocationId, String startDate, String endDate) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
        long curTime = sdf.parse(startDate).getTime();
        long endTime = sdf.parse(endDate).getTime();
        if (curTime > endTime) {
            LOGGER.error(MessageUtil.getMessage(MessageID.EROB008));
            throw new Exception();
        }

        // 初期化処理
        this.init();

        // 同名のデータシートファイルが既に存在している場合、一度中身をクリア
        this.initDatasheet(dgLocationId, AppConstants.DS_DM_FILENAME, startDate);
        this.initDatasheet(dgLocationId, AppConstants.DS_GN_FILENAME, startDate);
        this.initDatasheet(dgLocationId, AppConstants.DS_SM_FILENAME, startDate);
        this.initDatasheet(dgLocationId, AppConstants.DS_BT_FILENAME, startDate);
        this.initDatasheet(dgLocationId, AppConstants.DS_PV_FILENAME + "1_", startDate);
        this.initDatasheet(dgLocationId, AppConstants.DS_PV_FILENAME + "2_", startDate);
        this.initDatasheet(dgLocationId, AppConstants.DS_PV_FILENAME + "3_", startDate);
        this.initDatasheet(dgLocationId, AppConstants.DS_PV_FILENAME + "4_", startDate);
        this.initDatasheet(dgLocationId, AppConstants.DS_MM_FILENAME, startDate);

        // 需要発電計測ポイント情報を取得
        TDemandGenerationLocation dgLocation = this.getDGLocationList(dgLocationId);

        // 需要発電計測ポイント情報の需要計測ポイントリストIDに対応する需要計測ポイントリストを取得
        TDemandMeasurepointList listDemandMeasurepointId = this.getDemandMeasurepointId(dgLocation.getDemandMeasurepointListId());

        // 需要発電計測ポイント情報の需要計測ポイントリストIDに対応する需要計測ポイントリストを取得
        TGenerationMeasurepointList listGenerationMeasurepointId = this.getGenerationMeasurepointId(dgLocation.getGenerationMeasurepointListId());

        // パラメータで指定された範囲で、30分ごとに実施
        while (curTime <= endTime) {
            Double[] arraySumDMeasurevalues = new Double[AppConstants.DS_24HR_DATA_COUNT];
            List<Double> listSumGMeasurevalues = new ArrayList<Double>();

            // 需要予測の取得
            List<TDemandForecast> listDForecast = this.getDemandForecast(dgLocationId, new Timestamp(curTime));

            // 発電予測の取得
            List<TGenerationForecast> listGForecast = this.getGenerationForecast(dgLocationId, new Timestamp(curTime));

            // 需要計測ポイントリスト内の計測ポイントID毎に実施
            for (int measurepointId : listDemandMeasurepointId.getDemandMeasurepointId()) {
                if (LOGGER.isInfoEnabled()) {
                    LOGGER.info(MessageUtil.getMessage(MessageID.IROB036, measurepointId));
                }

                // t_extractvalueテーブルから予測日時に対応する需要電力の実績値を取得
                List<TExtractvalue> listExtractvalues = this.getExtractData(
                        measurepointId, listDForecast.get(0).getForecastTime(), listDForecast.get(47).getForecastTime(),
                        AppConstants.ROBUST_TOTALFLAG_DEMAND);

                // 取得したデータ数が想定した1週間分のデータ数と一致した場合
                if (listExtractvalues.size() == AppConstants.DS_24HR_DATA_COUNT) {
                    // 需要発電計測ポイント単位で加算
                    for (int i=0; i<AppConstants.DS_24HR_DATA_COUNT; i++) {
                        if (arraySumDMeasurevalues[i] == null) {
                            arraySumDMeasurevalues[i] = listExtractvalues.get(i).getMeasurevalue();
                        } else {
                            arraySumDMeasurevalues[i] += listExtractvalues.get(i).getMeasurevalue();
                        }
                    }
                } else {
                    LOGGER.error(MessageUtil.getMessage(MessageID.EROB004, listExtractvalues.size(), AppConstants.DS_24HR_DATA_COUNT));
                    throw(new Exception());
                }
            }

            // t_extractvalueテーブルから予測日時に対応する発電電力の実績値を取得
            List<TExtractvalue> listExtractvalues = this.getExtractData(
                    listGenerationMeasurepointId.getGenerationMeasurepointId()[AppConstants.BT_GENERATION_DATA_ORDER],
                    listGForecast.get(0).getForecastTime(), listGForecast.get(47).getForecastTime(),
                    AppConstants.ROBUST_TOTALFLAG_DEMAND);

            for (TExtractvalue data : listExtractvalues) {
                listSumGMeasurevalues.add(data.getMeasurevalue());
            }

            // 予測と実績のデータ数が一致している場合、データシート出力
            if (listDForecast.size() == arraySumDMeasurevalues.length && listGForecast.size() == listSumGMeasurevalues.size()) {
                // データシート(需要)の出力
                this.storeDemandDatasheet(listDForecast, Arrays.asList(arraySumDMeasurevalues));

                // データシート(発電)の出力
                this.storeGenerationDatasheet(listGForecast, listSumGMeasurevalues);

                // シミュレーション情報の取得
                TSimulationResult simResult = this.getSimulationResult(dgLocationId, new Timestamp(curTime));
                // シミュレーション中間結果の取得
                List<TSimulationIntermediate> listSimInter = this.getSimulationIntermediate(dgLocationId, new Timestamp(curTime));

                // データシート(シミュレーション)の出力
                this.storeSimulationDatasheet(simResult, listSimInter, listDForecast, listGForecast);

                // データシート(マルチメータ)の出力
                this.storeMmDatasheet(dgLocationId, listDemandMeasurepointId, startDate, curTime);
            }

            curTime += AppConstants.ROBUST_30MIN_IN_MILLISEC;
        }

        // データシート(蓄電池)の出力
        this.storeBatDatasheet(dgLocationId, listGenerationMeasurepointId, startDate, endDate);

        // データシート(PV1)の出力
        this.storePvDatasheet(dgLocationId, listGenerationMeasurepointId, startDate, endDate, 1);
        // データシート(PV2)の出力
        if (listGenerationMeasurepointId.getGenerationMeasurepointId().length > AppConstants.BT_PDC_1_ORDER + 6) {
            this.storePvDatasheet(dgLocationId, listGenerationMeasurepointId, startDate, endDate, 2);
        }
        // データシート(PV3)の出力
        if (listGenerationMeasurepointId.getGenerationMeasurepointId().length > AppConstants.BT_PDC_1_ORDER + 6 * 2) {
            this.storePvDatasheet(dgLocationId, listGenerationMeasurepointId, startDate, endDate, 3);
        }
        // データシート(PV4)の出力
        if (listGenerationMeasurepointId.getGenerationMeasurepointId().length > AppConstants.BT_PDC_1_ORDER + 6 * 3) {
            this.storePvDatasheet(dgLocationId, listGenerationMeasurepointId, startDate, endDate, 4);
        }

    }

    /**
     * 需要発電計測ポイント情報の全件取得を行うメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @return 需要発電計測ポイント情報のリスト
     * @throws Exception 処理中に問題が発生した場合
     */
    private TDemandGenerationLocation getDGLocationList(int dgLocationId) throws Exception{
        TDemandGenerationLocation result = null;

        SqlSession session = null;

        try {
            // DBセッションを取得
            session = this.comInfo.getSqlSessionFactory(
                    CommonConstants.ENVIRONMENT_GMSDB).openSession();

            // SQL実行
            TDemandGenerationLocationMapper mapper = session.getMapper(TDemandGenerationLocationMapper.class);
            result = mapper.selectWithId(dgLocationId);
        } finally {
            // セッションをクローズ
            if (null != session) {
                session.close();
            }
        }
        return(result);
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
     * 需要予測テーブルのDB検索を行うメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param dgLocationId 需要発電計測ポイントID
     * @param execDate 処理日時
     * @return 需要予測テーブルの検索結果
     * @throws Exception 処理中に問題が発生した場合
     */
    private List<TDemandForecast> getDemandForecast(Integer dgLocationId, Timestamp execTS) throws Exception {
        List<TDemandForecast> results = null;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(MessageUtil.getMessage(MessageID.IROB109, sdf.format(execTS)));
        }

        SqlSession session = null;

        try {
            // DBセッションを取得
            session = this.comInfo.getSqlSessionFactory(
                    CommonConstants.ENVIRONMENT_GMSDB).openSession();

            // SQL実行
            TDemandForecastMapper mapper = session.getMapper(TDemandForecastMapper.class);
            results = mapper.getDataWithCalcTime(dgLocationId, execTS);
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
    private List<TGenerationForecast> getGenerationForecast(Integer dgLocationId, Timestamp execTS) throws Exception {
        List<TGenerationForecast> results = null;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(MessageUtil.getMessage(MessageID.IROB109, sdf.format(execTS)));
        }

        SqlSession session = null;

        try {
            // DBセッションを取得
            session = this.comInfo.getSqlSessionFactory(
                    CommonConstants.ENVIRONMENT_GMSDB).openSession();

            // SQL実行
            TGenerationForecastMapper mapper = session.getMapper(TGenerationForecastMapper.class);
            results = mapper.getDataWithCalcTime(dgLocationId, execTS);
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
     * シミュレーション情報テーブルのDB検索を行うメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param dgLocationId 需要発電計測ポイントID
     * @param execDate 処理日時
     * @return シミュレーション情報テーブルの検索結果
     * @throws Exception 処理中に問題が発生した場合
     */
    private TSimulationResult getSimulationResult(Integer dgLocationId, Timestamp execTS) throws Exception {
        TSimulationResult result = null;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(MessageUtil.getMessage(MessageID.IROB111, sdf.format(execTS)));
        }

        SqlSession session = null;

        try {
            // DBセッションを取得
            session = this.comInfo.getSqlSessionFactory(
                    CommonConstants.ENVIRONMENT_GMSDB).openSession();

            // SQL実行
            TSimulationResultMapper mapper = session.getMapper(TSimulationResultMapper.class);
            result = mapper.getDataWithSimTime(dgLocationId, execTS);
        } finally {
            // セッションをクローズ
            if (null != session) {
                session.close();
            }
        }

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(MessageUtil.getMessage(MessageID.IROB113, sdf.format(result.getSimulationTime())));
        }

        //System.out.println("Simulation Result");
        //System.out.println(result.getSimulationTime());

        return(result);
    }

    /**
     * シミュレーション中間結果テーブルのDB検索を行うメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param dgLocationId 需要発電計測ポイントID
     * @param execDate 処理日時
     * @return シミュレーション情報テーブルの検索結果
     * @throws Exception 処理中に問題が発生した場合
     */
    private List<TSimulationIntermediate> getSimulationIntermediate(Integer dgLocationId, Timestamp execTS) throws Exception {
        List<TSimulationIntermediate> results = null;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(MessageUtil.getMessage(MessageID.IROB112, sdf.format(execTS)));
        }

        SqlSession session = null;

        try {
            // DBセッションを取得
            session = this.comInfo.getSqlSessionFactory(
                    CommonConstants.ENVIRONMENT_GMSDB).openSession();

            // SQL実行
            TSimulationIntermediateMapper mapper = session.getMapper(TSimulationIntermediateMapper.class);
            results = mapper.getDataWithSimTime(dgLocationId, execTS);
        } finally {
            // セッションをクローズ
            if (null != session) {
                session.close();
            }
        }

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(MessageUtil.getMessage(MessageID.IROB114, sdf.format(results.size())));
            LOGGER.info(MessageUtil.getMessage(MessageID.IROB115, sdf.format(results.get(0).getSimulationTime())));
        }

        //System.out.println("Simulation Intermediate");
        //System.out.println(result.size());
        //System.out.println(result.getSimulationTime());

        return(results);
    }

    /**
     * 抽出データテーブルのDB検索を行うメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param measurepointId 計測ポイントID
     * @param startTS 検索対象日時の始点
     * @param endTS 検索対象日時の終点
     * @return 抽出データテーブルの検索結果
     * @throws Exception 処理中に問題が発生した場合
     */
    private List<TExtractvalue> getExtractData(int measurepointId, Timestamp startTS, Timestamp endTS, int totalFlag) throws Exception {
        List<TExtractvalue> list = null;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
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
            list = mapper.getRangeData(measurepointId, startTS, endTS, totalFlag);
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
     * データシートファイルの初期化を行うメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param dgLocationId 対象需要発電計測ポイントID
     * @param execDate 対象日時の始点
     * @throws Exception 処理中に問題が発生した場合
     */
    private void initDatasheet(int dgLocationId, String fileName, String execDate) throws Exception {
        // CommonInfoのインスタンスを生成
        CommonInfo comInfo = CommonInfo.getInstance();
        Configuration gmsConfig = comInfo.getGmsConfig();

        // データシート出力先パス
        String tempFilePath = gmsConfig.getString(AppConstants.PROPERTY_ROBUST_ROOT_DIRECTORY);
        tempFilePath += AppConstants.DS_OUTPUT_PATH;
        tempFilePath += fileName + dgLocationId + "_";
        tempFilePath += execDate + AppConstants.DS_FILEEXT;
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(MessageUtil.getMessage(MessageID.IROB110, tempFilePath));
        }

        // データシートファイルのfw(上書き)、pw生成
        FileWriter fw = new FileWriter(tempFilePath, false);
        PrintWriter pw = new PrintWriter(new BufferedWriter(fw));

        //pwクローズ
        if (pw != null) {
            pw.close();
            pw = null;
        }
    }

    /**
     * データシート(需要)ファイルの生成を行うメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param listDForecast データ元需要予測リスト
     * @param listMeasurevalues データ元需要実績リスト
     * @throws Exception 処理中に問題が発生した場合
     */
    @SuppressWarnings("deprecation")
    private void storeDemandDatasheet(List<TDemandForecast> listDForecast, List<Double> listMeasurevalues) throws Exception {
        // CommonInfoのインスタンスを生成
        CommonInfo comInfo = CommonInfo.getInstance();
        Configuration gmsConfig = comInfo.getGmsConfig();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");

        // データシート出力先パス
        String tempFilePath = gmsConfig.getString(AppConstants.PROPERTY_ROBUST_ROOT_DIRECTORY);
        tempFilePath += AppConstants.DS_OUTPUT_PATH;
        tempFilePath += AppConstants.DS_DM_FILENAME + listDForecast.get(0).getDGLocationId() + "_";
        tempFilePath += sdf.format(listDForecast.get(0).getCalcTime()) + AppConstants.DS_FILEEXT;
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(MessageUtil.getMessage(MessageID.IROB110, tempFilePath));
        }

        // データシートファイルのfw(追記)、pw生成
        FileWriter fw = new FileWriter(tempFilePath, true);
        PrintWriter pw = new PrintWriter(new BufferedWriter(fw));

        SimpleDateFormat sdfOutput = new SimpleDateFormat("yyyy/MM/dd HH:mm");

        try {
            for (int i=0; i<48; i++) {
                String line = "";
                line += sdfOutput.format(listDForecast.get(i).getCalcTime()) + ",";
                line += sdfOutput.format(listDForecast.get(i).getForecastTime()) + ",";

                line += (listDForecast.get(i).getForecastTime().getTime() - listDForecast.get(i).getCalcTime().getTime())/AppConstants.ROBUST_1HR_IN_MILLISEC + ":";
                line += String.format("%02d", (listDForecast.get(i).getForecastTime().getMinutes() - listDForecast.get(i).getCalcTime().getMinutes())) + ",";

                line += listMeasurevalues.get(i) + ",";
                line += listDForecast.get(i).getDemandForecastValue() + ",";
                line += listMeasurevalues.get(i) - listDForecast.get(i).getDemandForecastValue();
                pw.println(line);
            }
        } finally {
            //pwクローズ
            if (pw != null) {
                pw.close();
                pw = null;
            }
        }
    }

    /**
     * データシート(発電)ファイルの生成を行うメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param listGForecast データ元発電予測リスト
     * @param listMeasurevalues データ元発電実績リスト
     * @throws Exception 処理中に問題が発生した場合
     */
    @SuppressWarnings("deprecation")
    private void storeGenerationDatasheet(List<TGenerationForecast> listGForecast, List<Double> listMeasurevalues) throws Exception {
        // CommonInfoのインスタンスを生成
        CommonInfo comInfo = CommonInfo.getInstance();
        Configuration gmsConfig = comInfo.getGmsConfig();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");

        // データシート出力先パス
        String tempFilePath = gmsConfig.getString(AppConstants.PROPERTY_ROBUST_ROOT_DIRECTORY);
        tempFilePath += AppConstants.DS_OUTPUT_PATH;
        tempFilePath += AppConstants.DS_GN_FILENAME + listGForecast.get(0).getDGLocationId() + "_";
        tempFilePath += sdf.format(listGForecast.get(0).getCalcTime()) + AppConstants.DS_FILEEXT;
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(MessageUtil.getMessage(MessageID.IROB110, tempFilePath));
        }

        // データシートファイルのfw(追記)、pw生成
        FileWriter fw = new FileWriter(tempFilePath, true);
        PrintWriter pw = new PrintWriter(new BufferedWriter(fw));

        SimpleDateFormat sdfOutput = new SimpleDateFormat("yyyy/MM/dd HH:mm");

        try {
            for (int i=0; i<48; i++) {
                String line = "";
                line += sdfOutput.format(listGForecast.get(i).getCalcTime()) + ",";
                line += sdfOutput.format(listGForecast.get(i).getForecastTime()) + ",";

                line += (listGForecast.get(i).getForecastTime().getTime() - listGForecast.get(i).getCalcTime().getTime())/AppConstants.ROBUST_1HR_IN_MILLISEC + ":";
                line += String.format("%02d", (listGForecast.get(i).getForecastTime().getMinutes() - listGForecast.get(i).getCalcTime().getMinutes())) + ",";

                line += listMeasurevalues.get(i) + ",";
                line += listGForecast.get(i).getGenerationForecastValue() + ",";
                line += listMeasurevalues.get(i) - listGForecast.get(i).getGenerationForecastValue();
                pw.println(line);
            }
        } finally {
            //pwクローズ
            if (pw != null) {
                pw.close();
                pw = null;
            }
        }
    }

    /**
     * データシート(シミュレーション)ファイルの生成を行うメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param listSimResult データ元シミュレーション情報リスト
     * @param listSimIntermediate データ元シミュレーション中間結果リスト
     * @throws Exception 処理中に問題が発生した場合
     */
    @SuppressWarnings("deprecation")
    private void storeSimulationDatasheet(TSimulationResult simResult, List<TSimulationIntermediate> listSimInter,
            List<TDemandForecast> listDForecast, List<TGenerationForecast> listGForecast) throws Exception {

        // CommonInfoのインスタンスを生成
        CommonInfo comInfo = CommonInfo.getInstance();
        Configuration gmsConfig = comInfo.getGmsConfig();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");

        // データシート出力先パス
        String tempFilePath = gmsConfig.getString(AppConstants.PROPERTY_ROBUST_ROOT_DIRECTORY);
        tempFilePath += AppConstants.DS_OUTPUT_PATH;
        tempFilePath += AppConstants.DS_SM_FILENAME + simResult.getDGLocationId() + "_";
        tempFilePath += sdf.format(simResult.getSimulationTime()) + AppConstants.DS_FILEEXT;
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(MessageUtil.getMessage(MessageID.IROB110, tempFilePath));
        }

        // データシートファイルのfw(追記)、pw生成
        FileWriter fw = new FileWriter(tempFilePath, true);
        PrintWriter pw = new PrintWriter(new BufferedWriter(fw));

        SimpleDateFormat sdfOutput = new SimpleDateFormat("yyyy/MM/dd HH:mm");

        try {
            for (int i=0; i<48; i++) {
                String line = "";
                line += sdfOutput.format(simResult.getSimulationTime()) + ",";
                line += sdfOutput.format(new Timestamp(simResult.getSimulationTime().getTime() + (i+1) * AppConstants.ROBUST_30MIN_IN_MILLISEC)) + ",";

                line += (listGForecast.get(i).getForecastTime().getTime() - simResult.getSimulationTime().getTime())/AppConstants.ROBUST_1HR_IN_MILLISEC + ":";
                line += String.format("%02d", (listGForecast.get(i).getForecastTime().getMinutes() - simResult.getSimulationTime().getMinutes())) + ",";

                line += listDForecast.get(i).getDemandForecastValue() + ",";
                line += listGForecast.get(i).getGenerationForecastValue() + ",";
                line += listSimInter.get(i).getSoc() + ",";
                // 系統電力
                line += listSimInter.get(i).getWU() + ",";
                // 充放電電力
                line += listSimInter.get(i).getWB() + ",";
                // 過放電状態
                line += listSimInter.get(i).getSOver() + ",";
                // 双方向AC/DCの運転負荷率
                line += listSimInter.get(i).getLfAcdc() + ",";
                // MPPTユニットの運転負荷率
                line += listSimInter.get(i).getLfDcdc() + ",";
                // 双方向AC/DCの充電時電力変換効率
                line += listSimInter.get(i).getKAcdcC() + ",";
                // 双方向AC/DCの放電時電力変換効率
                line += listSimInter.get(i).getKAcdcD() + ",";
                // MPPTユニットの電力変換効率
                line += listSimInter.get(i).getKDcdc() + ",";
                // SoC変更要否
                line += simResult.getSocModNeed() + ",";
                // スケジュールID
                line += simResult.getScheduleId() + ",";
                // 運用下限変更値
                line += simResult.getSocMin() + ",";
                // 自家消費率
                line += simResult.getRIsrand() + ",";
                // バックアップ最低所要電力量
                line += simResult.getWR();

                pw.println(line);
            }
        } finally {
            //pwクローズ
            if (pw != null) {
                pw.close();
                pw = null;
            }
        }
    }

    /**
     * データシート(蓄電池)ファイルの生成を行うメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param dgLocationId データシート出力対象の需要発電計測ポイントID
     * @param gMPList データシート出力対象の発電計測ポイントリスト
     * @param startDate データシート出力対象の始点日時
     * @param endDate データシート出力対象の終点日時
     * @throws Exception 処理中に問題が発生した場合
     */
    private void storeBatDatasheet(int dgLocationId, TGenerationMeasurepointList gMPList, String startDate, String endDate) throws Exception {

        // CommonInfoのインスタンスを生成
        CommonInfo comInfo = CommonInfo.getInstance();
        Configuration gmsConfig = comInfo.getGmsConfig();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");

        // データシート出力先パス
        String tempFilePath = gmsConfig.getString(AppConstants.PROPERTY_ROBUST_ROOT_DIRECTORY);
        tempFilePath += AppConstants.DS_OUTPUT_PATH;
        tempFilePath += AppConstants.DS_BT_FILENAME + dgLocationId + "_";
        tempFilePath += startDate + AppConstants.DS_FILEEXT;
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(MessageUtil.getMessage(MessageID.IROB110, tempFilePath));
        }

        // 取得データ数チェック用
        int dataCnt = 0;

        // 1
        // データシート元リスト(買電しきい値設定)
        List<TExtractvalue> listBuylim = this.getExtractData((int)gMPList.getGenerationMeasurepointId()[AppConstants.BT_BUYLIM_ORDER],
                new Timestamp(sdf.parse(startDate).getTime()), new Timestamp(sdf.parse(endDate).getTime()), AppConstants.ROBUST_TOTALFLAG_MOMENT);
        dataCnt = listBuylim.size();

        // データシート元リスト(SoC放電禁止設定)
        List<TExtractvalue> listSocMin = this.getExtractData((int)gMPList.getGenerationMeasurepointId()[AppConstants.BT_SOC_MIN_ORDER],
                new Timestamp(sdf.parse(startDate).getTime()), new Timestamp(sdf.parse(endDate).getTime()), AppConstants.ROBUST_TOTALFLAG_MOMENT);
        if (listSocMin.size() != dataCnt) {
            LOGGER.error(MessageUtil.getMessage(MessageID.EROB009, listSocMin.size(), dataCnt));
            throw new Exception();
        }

        // データシート元リスト(SoC下限設定(自立運転用))
        List<TExtractvalue> listSocIsland = this.getExtractData((int)gMPList.getGenerationMeasurepointId()[AppConstants.BT_SOC_ISLAND_ORDER],
                new Timestamp(sdf.parse(startDate).getTime()), new Timestamp(sdf.parse(endDate).getTime()), AppConstants.ROBUST_TOTALFLAG_MOMENT);
        if (listSocIsland.size() != dataCnt) {
            LOGGER.error(MessageUtil.getMessage(MessageID.EROB009, listSocIsland.size(), dataCnt));
            throw new Exception();
        }

        // データシート元リスト(双方向電源動作ステータス)
        List<TExtractvalue> listStatusData = this.getExtractData((int)gMPList.getGenerationMeasurepointId()[AppConstants.BT_STATUS_DATA_ORDER],
                new Timestamp(sdf.parse(startDate).getTime()), new Timestamp(sdf.parse(endDate).getTime()), AppConstants.ROBUST_TOTALFLAG_MOMENT);
        if (listStatusData.size() != dataCnt) {
            LOGGER.error(MessageUtil.getMessage(MessageID.EROB009, listStatusData.size(), dataCnt));
            throw new Exception();
        }

        // データシート元リスト(出力電圧モニタ(代表値))
        List<TExtractvalue> listVout = this.getExtractData((int)gMPList.getGenerationMeasurepointId()[AppConstants.BT_VOUT_ORDER],
                new Timestamp(sdf.parse(startDate).getTime()), new Timestamp(sdf.parse(endDate).getTime()), AppConstants.ROBUST_TOTALFLAG_MOMENT);
        if (listVout.size() != dataCnt) {
            LOGGER.error(MessageUtil.getMessage(MessageID.EROB009, listVout.size(), dataCnt));
            throw new Exception();
        }

        // データシート元リスト(出力電流モニタ(代表値))
        List<TExtractvalue> listIout = this.getExtractData((int)gMPList.getGenerationMeasurepointId()[AppConstants.BT_IOUT_ORDER],
                new Timestamp(sdf.parse(startDate).getTime()), new Timestamp(sdf.parse(endDate).getTime()), AppConstants.ROBUST_TOTALFLAG_MOMENT);
        if (listIout.size() != dataCnt) {
            LOGGER.error(MessageUtil.getMessage(MessageID.EROB009, listIout.size(), dataCnt));
            throw new Exception();
        }

        // データシート元リスト(出力周波数モニタ)
        List<TExtractvalue> listFreqOut = this.getExtractData((int)gMPList.getGenerationMeasurepointId()[AppConstants.BT_FREQ_OUT_ORDER],
                new Timestamp(sdf.parse(startDate).getTime()), new Timestamp(sdf.parse(endDate).getTime()), AppConstants.ROBUST_TOTALFLAG_MOMENT);
        if (listFreqOut.size() != dataCnt) {
            LOGGER.error(MessageUtil.getMessage(MessageID.EROB009, listFreqOut.size(), dataCnt));
            throw new Exception();
        }

        // データシート元リスト(出力有効電力モニタ(代表値))
        List<TExtractvalue> listPout = this.getExtractData((int)gMPList.getGenerationMeasurepointId()[AppConstants.BT_POUT_ORDER],
                new Timestamp(sdf.parse(startDate).getTime()), new Timestamp(sdf.parse(endDate).getTime()), AppConstants.ROBUST_TOTALFLAG_MOMENT);
        if (listPout.size() != dataCnt) {
            LOGGER.error(MessageUtil.getMessage(MessageID.EROB009, listPout.size(), dataCnt));
            throw new Exception();
        }

        // データシート元リスト(出力力率モニタ)
        List<TExtractvalue> listPfout = this.getExtractData((int)gMPList.getGenerationMeasurepointId()[AppConstants.BT_PFOUT_ORDER],
                new Timestamp(sdf.parse(startDate).getTime()), new Timestamp(sdf.parse(endDate).getTime()), AppConstants.ROBUST_TOTALFLAG_MOMENT);
        if (listPfout.size() != dataCnt) {
            LOGGER.error(MessageUtil.getMessage(MessageID.EROB009, listPfout.size(), dataCnt));
            throw new Exception();
        }

        // データシート元リスト(INV電圧モニタ(代表値))
        List<TExtractvalue> listVinv = this.getExtractData((int)gMPList.getGenerationMeasurepointId()[AppConstants.BT_VINV_ORDER],
                new Timestamp(sdf.parse(startDate).getTime()), new Timestamp(sdf.parse(endDate).getTime()), AppConstants.ROBUST_TOTALFLAG_MOMENT);
        if (listVinv.size() != dataCnt) {
            LOGGER.error(MessageUtil.getMessage(MessageID.EROB009, listVinv.size(), dataCnt));
            throw new Exception();
        }

        // 11
        // データシート元リスト(INV電流モニタ(代表値))
        List<TExtractvalue> listIinv = this.getExtractData((int)gMPList.getGenerationMeasurepointId()[AppConstants.BT_IINV_ORDER],
                new Timestamp(sdf.parse(startDate).getTime()), new Timestamp(sdf.parse(endDate).getTime()), AppConstants.ROBUST_TOTALFLAG_MOMENT);
        if (listIinv.size() != dataCnt) {
            LOGGER.error(MessageUtil.getMessage(MessageID.EROB009, listIinv.size(), dataCnt));
            throw new Exception();
        }

        // データシート元リスト(INV有効電力モニタ(代表値))
        List<TExtractvalue> listPinv = this.getExtractData((int)gMPList.getGenerationMeasurepointId()[AppConstants.BT_PINV_ORDER],
                new Timestamp(sdf.parse(startDate).getTime()), new Timestamp(sdf.parse(endDate).getTime()), AppConstants.ROBUST_TOTALFLAG_MOMENT);
        if (listPinv.size() != dataCnt) {
            LOGGER.error(MessageUtil.getMessage(MessageID.EROB009, listPinv.size(), dataCnt));
            throw new Exception();
        }

        // データシート元リスト(INV力率モニタ)
        List<TExtractvalue> listPfinv = this.getExtractData((int)gMPList.getGenerationMeasurepointId()[AppConstants.BT_PFINV_ORDER],
                new Timestamp(sdf.parse(startDate).getTime()), new Timestamp(sdf.parse(endDate).getTime()), AppConstants.ROBUST_TOTALFLAG_MOMENT);
        if (listPfinv.size() != dataCnt) {
            LOGGER.error(MessageUtil.getMessage(MessageID.EROB009, listPfinv.size(), dataCnt));
            throw new Exception();
        }

        // データシート元リスト(入力電圧モニタ(代表値))
        List<TExtractvalue> listVinp = this.getExtractData((int)gMPList.getGenerationMeasurepointId()[AppConstants.BT_VINP_ORDER],
                new Timestamp(sdf.parse(startDate).getTime()), new Timestamp(sdf.parse(endDate).getTime()), AppConstants.ROBUST_TOTALFLAG_MOMENT);
        if (listVinp.size() != dataCnt) {
            LOGGER.error(MessageUtil.getMessage(MessageID.EROB009, listVinp.size(), dataCnt));
            throw new Exception();
        }

        // データシート元リスト(入力電流モニタ(代表値))
        List<TExtractvalue> listIinp = this.getExtractData((int)gMPList.getGenerationMeasurepointId()[AppConstants.BT_IINP_ORDER],
                new Timestamp(sdf.parse(startDate).getTime()), new Timestamp(sdf.parse(endDate).getTime()), AppConstants.ROBUST_TOTALFLAG_MOMENT);
        if (listIinp.size() != dataCnt) {
            LOGGER.error(MessageUtil.getMessage(MessageID.EROB009, listIinp.size(), dataCnt));
            throw new Exception();
        }

        // データシート元リスト(入力有効電力モニタ(代表値))
        List<TExtractvalue> listPinp = this.getExtractData((int)gMPList.getGenerationMeasurepointId()[AppConstants.BT_PINP_ORDER],
                new Timestamp(sdf.parse(startDate).getTime()), new Timestamp(sdf.parse(endDate).getTime()), AppConstants.ROBUST_TOTALFLAG_MOMENT);
        if (listPinp.size() != dataCnt) {
            LOGGER.error(MessageUtil.getMessage(MessageID.EROB009, listPinp.size(), dataCnt));
            throw new Exception();
        }

        // データシート元リスト(入力力率モニタ)
        List<TExtractvalue> listPfinp = this.getExtractData((int)gMPList.getGenerationMeasurepointId()[AppConstants.BT_PFINP_ORDER],
                new Timestamp(sdf.parse(startDate).getTime()), new Timestamp(sdf.parse(endDate).getTime()), AppConstants.ROBUST_TOTALFLAG_MOMENT);
        if (listPfinp.size() != dataCnt) {
            LOGGER.error(MessageUtil.getMessage(MessageID.EROB009, listPfinp.size(), dataCnt));
            throw new Exception();
        }

        // データシート元リスト(蓄電池電圧モニタ)
        List<TExtractvalue> listVbat = this.getExtractData((int)gMPList.getGenerationMeasurepointId()[AppConstants.BT_VBAT_ORDER],
                new Timestamp(sdf.parse(startDate).getTime()), new Timestamp(sdf.parse(endDate).getTime()), AppConstants.ROBUST_TOTALFLAG_MOMENT);
        if (listVbat.size() != dataCnt) {
            LOGGER.error(MessageUtil.getMessage(MessageID.EROB009, listVbat.size(), dataCnt));
            throw new Exception();
        }

        // データシート元リスト(蓄電池電流モニタ)
        List<TExtractvalue> listIbat = this.getExtractData((int)gMPList.getGenerationMeasurepointId()[AppConstants.BT_IBAT_ORDER],
                new Timestamp(sdf.parse(startDate).getTime()), new Timestamp(sdf.parse(endDate).getTime()), AppConstants.ROBUST_TOTALFLAG_MOMENT);
        if (listIbat.size() != dataCnt) {
            LOGGER.error(MessageUtil.getMessage(MessageID.EROB009, listIbat.size(), dataCnt));
            throw new Exception();
        }

        // データシート元リスト(蓄電池電力モニタ)
        List<TExtractvalue> listPbat = this.getExtractData((int)gMPList.getGenerationMeasurepointId()[AppConstants.BT_PBAT_ORDER],
                new Timestamp(sdf.parse(startDate).getTime()), new Timestamp(sdf.parse(endDate).getTime()), AppConstants.ROBUST_TOTALFLAG_MOMENT);
        if (listPbat.size() != dataCnt) {
            LOGGER.error(MessageUtil.getMessage(MessageID.EROB009, listPbat.size(), dataCnt));
            throw new Exception();
        }

        // 21
        // データシート元リスト(蓄電池SoC)
        List<TExtractvalue> listSocData = this.getExtractData((int)gMPList.getGenerationMeasurepointId()[AppConstants.BT_SOC_DATA_ORDER],
                new Timestamp(sdf.parse(startDate).getTime()), new Timestamp(sdf.parse(endDate).getTime()), AppConstants.ROBUST_TOTALFLAG_MOMENT);
        if (listSocData.size() != dataCnt) {
            LOGGER.error(MessageUtil.getMessage(MessageID.EROB009, listSocData.size(), dataCnt));
            throw new Exception();
        }

        // データシート元リスト(最大セル温度)
        List<TExtractvalue> listTcelMax = this.getExtractData((int)gMPList.getGenerationMeasurepointId()[AppConstants.BT_TCEL_MAX_ORDER],
                new Timestamp(sdf.parse(startDate).getTime()), new Timestamp(sdf.parse(endDate).getTime()), AppConstants.ROBUST_TOTALFLAG_MOMENT);
        if (listTcelMax.size() != dataCnt) {
            LOGGER.error(MessageUtil.getMessage(MessageID.EROB009, listTcelMax.size(), dataCnt));
            throw new Exception();
        }

        // データシート元リスト(PV発電モニタ(全ユニット))
        List<TExtractvalue> listPpvAll = this.getExtractData((int)gMPList.getGenerationMeasurepointId()[AppConstants.BT_PPV_ALL_ORDER],
                new Timestamp(sdf.parse(startDate).getTime()), new Timestamp(sdf.parse(endDate).getTime()), AppConstants.ROBUST_TOTALFLAG_MOMENT);
        if (listPpvAll.size() != dataCnt) {
            LOGGER.error(MessageUtil.getMessage(MessageID.EROB009, listPpvAll.size(), dataCnt));
            throw new Exception();
        }

        // データシート元リスト(蓄電池累積電力モニタ(放電方向のみ))
        List<TExtractvalue> listPbatD = this.getExtractData((int)gMPList.getGenerationMeasurepointId()[AppConstants.BT_PBAT_D_ORDER],
                new Timestamp(sdf.parse(startDate).getTime()), new Timestamp(sdf.parse(endDate).getTime()), AppConstants.ROBUST_TOTALFLAG_MOMENT);
        if (listPbatD.size() != dataCnt) {
            LOGGER.error(MessageUtil.getMessage(MessageID.EROB009, listPbatD.size(), dataCnt));
            throw new Exception();
        }

        // データシート元リスト(蓄電池累積電力モニタ(充電方向のみ))
        List<TExtractvalue> listPbatC = this.getExtractData((int)gMPList.getGenerationMeasurepointId()[AppConstants.BT_PBAT_C_ORDER],
                new Timestamp(sdf.parse(startDate).getTime()), new Timestamp(sdf.parse(endDate).getTime()), AppConstants.ROBUST_TOTALFLAG_MOMENT);
        if (listPbatC.size() != dataCnt) {
            LOGGER.error(MessageUtil.getMessage(MessageID.EROB009, listPbatC.size(), dataCnt));
            throw new Exception();
        }

        // データシート元リスト(買電累積電力モニタ)
        List<TExtractvalue> listPbuy = this.getExtractData((int)gMPList.getGenerationMeasurepointId()[AppConstants.BT_PBUY_ORDER],
                new Timestamp(sdf.parse(startDate).getTime()), new Timestamp(sdf.parse(endDate).getTime()), AppConstants.ROBUST_TOTALFLAG_MOMENT);
        if (listPbuy.size() != dataCnt) {
            LOGGER.error(MessageUtil.getMessage(MessageID.EROB009, listPbuy.size(), dataCnt));
            throw new Exception();
        }

        // データシート元リスト(売電累積電力モニタ)
        List<TExtractvalue> listPsell = this.getExtractData((int)gMPList.getGenerationMeasurepointId()[AppConstants.BT_PSELL_ORDER],
                new Timestamp(sdf.parse(startDate).getTime()), new Timestamp(sdf.parse(endDate).getTime()), AppConstants.ROBUST_TOTALFLAG_MOMENT);
        if (listPsell.size() != dataCnt) {
            LOGGER.error(MessageUtil.getMessage(MessageID.EROB009, listPsell.size(), dataCnt));
            throw new Exception();
        }

        // データシート元リスト(PV発電累積電力モニタ)
        List<TExtractvalue> listGenerationData = this.getExtractData((int)gMPList.getGenerationMeasurepointId()[AppConstants.BT_GENERATION_DATA_ORDER],
                new Timestamp(sdf.parse(startDate).getTime()), new Timestamp(sdf.parse(endDate).getTime()), AppConstants.ROBUST_TOTALFLAG_DEMAND);
        if (listGenerationData.size() != dataCnt) {
            LOGGER.error(MessageUtil.getMessage(MessageID.EROB009, listGenerationData.size(), dataCnt));
            throw new Exception();
        }

        // データシートファイルのfw(追記)、pw生成
        FileWriter fw = new FileWriter(tempFilePath, true);
        PrintWriter pw = new PrintWriter(new BufferedWriter(fw));

        SimpleDateFormat sdfOutput = new SimpleDateFormat("yyyy/MM/dd HH:mm");

        try {
            for (int i=0; i<dataCnt; i++) {
                String line = "";
                line += sdfOutput.format(new Timestamp(sdf.parse(startDate).getTime() + i * AppConstants.ROBUST_30MIN_IN_MILLISEC)) + ",";
                line += listBuylim.get(i).getMeasurevalue() + ",";
                line += listSocMin.get(i).getMeasurevalue() + ",";
                line += listSocIsland.get(i).getMeasurevalue() + ",";
                line += listStatusData.get(i).getMeasurevalue() + ",";
                line += listVout.get(i).getMeasurevalue() + ",";
                line += listIout.get(i).getMeasurevalue() + ",";
                line += listFreqOut.get(i).getMeasurevalue() + ",";
                line += listPout.get(i).getMeasurevalue() + ",";
                line += listPfout.get(i).getMeasurevalue() + ",";
                line += listVinv.get(i).getMeasurevalue() + ",";
                line += listIinv.get(i).getMeasurevalue() + ",";
                line += listPinv.get(i).getMeasurevalue() + ",";
                line += listPfinv.get(i).getMeasurevalue() + ",";
                line += listVinp.get(i).getMeasurevalue() + ",";
                line += listIinp.get(i).getMeasurevalue() + ",";
                line += listPinp.get(i).getMeasurevalue() + ",";
                line += listPfinp.get(i).getMeasurevalue() + ",";
                line += listVbat.get(i).getMeasurevalue() + ",";
                line += listIbat.get(i).getMeasurevalue() + ",";
                line += listPbat.get(i).getMeasurevalue() + ",";
                line += listSocData.get(i).getMeasurevalue() + ",";
                line += listTcelMax.get(i).getMeasurevalue() + ",";
                line += listPpvAll.get(i).getMeasurevalue() + ",";
                line += listPbatD.get(i).getMeasurevalue() + ",";
                line += listPbatC.get(i).getMeasurevalue() + ",";
                line += listPbuy.get(i).getMeasurevalue() + ",";
                line += listPsell.get(i).getMeasurevalue() + ",";
                line += listGenerationData.get(i).getMeasurevalue() * AppConstants.ROBUST_30MIN_IN_HOUR;

                pw.println(line);
            }
        } finally {
            //pwクローズ
            if (pw != null) {
                pw.close();
                pw = null;
            }
        }
    }

    /**
     * データシート(PV)ファイルの生成を行うメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param dgLocationId データシート出力対象の需要発電計測ポイントID
     * @param gMPList データシート出力対象の発電計測ポイントリスト
     * @param startDate データシート出力対象の始点日時
     * @param endDate データシート出力対象の終点日時
     * @param pvId データシート出力対象のPVのインデクス
     * @throws Exception 処理中に問題が発生した場合
     */
    private void storePvDatasheet(int dgLocationId, TGenerationMeasurepointList gMPList, String startDate, String endDate, int pvId) throws Exception {

        // CommonInfoのインスタンスを生成
        CommonInfo comInfo = CommonInfo.getInstance();
        Configuration gmsConfig = comInfo.getGmsConfig();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");

        // データシート出力先パス
        String tempFilePath = gmsConfig.getString(AppConstants.PROPERTY_ROBUST_ROOT_DIRECTORY);
        tempFilePath += AppConstants.DS_OUTPUT_PATH;
        tempFilePath += AppConstants.DS_PV_FILENAME + pvId + "_" + dgLocationId + "_";
        tempFilePath += startDate + AppConstants.DS_FILEEXT;
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(MessageUtil.getMessage(MessageID.IROB110, tempFilePath));
        }

        // 取得データ数チェック用
        int dataCnt = 0;

        // 1
        // データシート元リスト(PVパネル電圧モニタ)
        List<TExtractvalue> listVpv = this.getExtractData((int)gMPList.getGenerationMeasurepointId()[AppConstants.BT_VPV_1_ORDER + 6 * (pvId-1)],
                new Timestamp(sdf.parse(startDate).getTime()), new Timestamp(sdf.parse(endDate).getTime()), AppConstants.ROBUST_TOTALFLAG_MOMENT);
        dataCnt = listVpv.size();

        // データシート元リスト(PVパネル電流モニタ)
        List<TExtractvalue> listIpv = this.getExtractData((int)gMPList.getGenerationMeasurepointId()[AppConstants.BT_IPV_1_ORDER + 6 * (pvId-1)],
                new Timestamp(sdf.parse(startDate).getTime()), new Timestamp(sdf.parse(endDate).getTime()), AppConstants.ROBUST_TOTALFLAG_MOMENT);
        if (listIpv.size() != dataCnt) {
            LOGGER.error(MessageUtil.getMessage(MessageID.EROB009, listIpv.size(), dataCnt));
            throw new Exception();
        }

        // データシート元リスト(PVパネル発電電力モニタ)
        List<TExtractvalue> listPpv = this.getExtractData((int)gMPList.getGenerationMeasurepointId()[AppConstants.BT_PPV_1_ORDER + 6 * (pvId-1)],
                new Timestamp(sdf.parse(startDate).getTime()), new Timestamp(sdf.parse(endDate).getTime()), AppConstants.ROBUST_TOTALFLAG_MOMENT);
        if (listPpv.size() != dataCnt) {
            LOGGER.error(MessageUtil.getMessage(MessageID.EROB009, listPpv.size(), dataCnt));
            throw new Exception();
        }

        // データシート元リスト(PV直流電圧モニタ)
        List<TExtractvalue> listVdc = this.getExtractData((int)gMPList.getGenerationMeasurepointId()[AppConstants.BT_VDC_1_ORDER + 6 * (pvId-1)],
                new Timestamp(sdf.parse(startDate).getTime()), new Timestamp(sdf.parse(endDate).getTime()), AppConstants.ROBUST_TOTALFLAG_MOMENT);
        if (listVdc.size() != dataCnt) {
            LOGGER.error(MessageUtil.getMessage(MessageID.EROB009, listVdc.size(), dataCnt));
            throw new Exception();
        }

        // データシート元リスト(PV直流電流モニタ)
        List<TExtractvalue> listIdc = this.getExtractData((int)gMPList.getGenerationMeasurepointId()[AppConstants.BT_IDC_1_ORDER + 6 * (pvId-1)],
                new Timestamp(sdf.parse(startDate).getTime()), new Timestamp(sdf.parse(endDate).getTime()), AppConstants.ROBUST_TOTALFLAG_MOMENT);
        if (listIdc.size() != dataCnt) {
            LOGGER.error(MessageUtil.getMessage(MessageID.EROB009, listIdc.size(), dataCnt));
            throw new Exception();
        }

        // データシート元リスト(PV直流電力モニタ)
        List<TExtractvalue> listPdc = this.getExtractData((int)gMPList.getGenerationMeasurepointId()[AppConstants.BT_PDC_1_ORDER + 6 * (pvId-1)],
                new Timestamp(sdf.parse(startDate).getTime()), new Timestamp(sdf.parse(endDate).getTime()), AppConstants.ROBUST_TOTALFLAG_MOMENT);
        if (listPdc.size() != dataCnt) {
            LOGGER.error(MessageUtil.getMessage(MessageID.EROB009, listPdc.size(), dataCnt));
            throw new Exception();
        }

        // データシートファイルのfw(追記)、pw生成
        FileWriter fw = new FileWriter(tempFilePath, true);
        PrintWriter pw = new PrintWriter(new BufferedWriter(fw));

        SimpleDateFormat sdfOutput = new SimpleDateFormat("yyyy/MM/dd HH:mm");

        try {
            for (int i=0; i<dataCnt; i++) {
                String line = "";
                line += sdfOutput.format(new Timestamp(sdf.parse(startDate).getTime() + i * AppConstants.ROBUST_30MIN_IN_MILLISEC)) + ",";
                line += listVpv.get(i).getMeasurevalue() + ",";
                line += listIpv.get(i).getMeasurevalue() + ",";
                line += listPpv.get(i).getMeasurevalue() + ",";
                line += listVdc.get(i).getMeasurevalue() + ",";
                line += listIdc.get(i).getMeasurevalue() + ",";
                line += listPdc.get(i).getMeasurevalue();

                pw.println(line);
            }
        } finally {
            //pwクローズ
            if (pw != null) {
                pw.close();
                pw = null;
            }
        }
    }

    /**
     * データシート(マルチメータ)ファイルの生成を行うメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param dgLocationId データシート出力対象の需要発電計測ポイントID
     * @param gMPList データシート出力対象の発電計測ポイントリスト
     * @param startDate データシート出力対象の始点日時 (ファイル名用)
     * @param execDate データシート出力対象の日時 (検索条件用)
     * @throws Exception 処理中に問題が発生した場合
     */
    private void storeMmDatasheet(int dgLocationId, TDemandMeasurepointList dMPList, String startDate, long execTime) throws Exception {

        // CommonInfoのインスタンスを生成
        CommonInfo comInfo = CommonInfo.getInstance();
        Configuration gmsConfig = comInfo.getGmsConfig();

        // データシート出力先パス
        String tempFilePath = gmsConfig.getString(AppConstants.PROPERTY_ROBUST_ROOT_DIRECTORY);
        tempFilePath += AppConstants.DS_OUTPUT_PATH;
        tempFilePath += AppConstants.DS_MM_FILENAME + dgLocationId + "_";
        tempFilePath += startDate + AppConstants.DS_FILEEXT;
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(MessageUtil.getMessage(MessageID.IROB110, tempFilePath));
        }

        // 取得データ数チェック用
        int dataCnt = 0;

        Integer[] arrayDMId = dMPList.getDemandMeasurepointId();

        // データシート出力用電力リスト
        double pW = 0.0;

        // データシート出力用電力量リスト
        double pWh = 0.0;

        for (int id : arrayDMId) {
            // 電力
            List<TExtractvalue> listPW = this.getExtractData(id, new Timestamp(execTime),  new Timestamp(execTime),
                    AppConstants.ROBUST_TOTALFLAG_MOMENT);

            dataCnt = listPW.size();

            pW += listPW.get(0).getMeasurevalue();

            // 電力量
            List<TExtractvalue> listPWh = this.getExtractData(id, new Timestamp(execTime), new Timestamp(execTime),
                    AppConstants.ROBUST_TOTALFLAG_DEMAND);

            if (listPWh.size() != dataCnt) {
                LOGGER.error(MessageUtil.getMessage(MessageID.EROB009, listPWh.size(), dataCnt));
                throw new Exception();
            }

            pWh += listPWh.get(0).getMeasurevalue();
        }

        // データシートファイルのfw(追記)、pw生成
        FileWriter fw = new FileWriter(tempFilePath, true);
        PrintWriter pw = new PrintWriter(new BufferedWriter(fw));

        SimpleDateFormat sdfOutput = new SimpleDateFormat("yyyy/MM/dd HH:mm");

        try {
            String line = "";
            line += sdfOutput.format(new Timestamp(execTime)) + ",";
            line += pW + ",";
            line += pWh;

            pw.println(line);
        } finally {
            //pwクローズ
            if (pw != null) {
                pw.close();
                pw = null;
            }
        }
    }
}
