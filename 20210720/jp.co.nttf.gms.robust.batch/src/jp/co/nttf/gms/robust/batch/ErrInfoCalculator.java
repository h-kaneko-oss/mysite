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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

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
import jp.co.nttf.gms.common.internal.data.gmsdb.TWeatherForecastG;
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
    private static final AppLogger LOGGER = new AppLogger(GenerationForecaster.class);

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

        // 需要発電計測ポイント情報のリストを取得
        List<TDemandGenerationLocation> listDGLocation = this.getDGLocationList();

        // 需要発電計測ポイント情報毎に実施
        for (TDemandGenerationLocation dgLocation : listDGLocation) {

            // 需要発電計測ポイント情報の需要計測ポイントリストIDに対応する需要計測ポイントリストを取得
            TDemandMeasurepointList listDemandMeasurepointId = this.getDemandMeasurepointId(dgLocation.getGenerationMeasurepointListId());

            double[] arrayDMeasurevalues = new double[AppConstants.E_24HR_INPUTDATA_COUNT];
            double[] arrayDForecastvalues = new double[AppConstants.E_24HR_INPUTDATA_COUNT];
            double[] arrayGMeasurevalues = new double[AppConstants.E_24HR_INPUTDATA_COUNT];
            double[] arrayGForecastvalues = new double[AppConstants.E_24HR_INPUTDATA_COUNT];

            // 需要計測ポイントID毎に実施
            for (int demandMeasurepointId : listDemandMeasurepointId.getDemandMeasurepointId()) {
                // 需要実績をt_extractvalueテーブルから取得
                List<TExtractvalue> listDExtractvalues = this.getDemandExtractData(argDate, demandMeasurepointId);

                // 取得したデータ数が想定した24時間分のデータ数と一致した場合
                if (listDExtractvalues.size() == AppConstants.E_24HR_EXTRACTDATA_COUNT) {
                    // 需要発電計測ポイント単位で加算
                    for (int i=0; i<AppConstants.E_24HR_EXTRACTDATA_COUNT; i++) {

                        // 需要実績を1時間おき24時間分のデータ
                        if (i%2 == 0) {
                            arrayDMeasurevalues[i/2] += listDExtractvalues.get(i).getMeasurevalue();
                        }
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
                    // 需要予測を1時間おき24時間分のデータ
                    if (i%2 == 0) {
                        arrayDForecastvalues[i/2] = listDForecastvalues.get(i).getDemandForecastValue();
                    }
                }
            } else {
                LOGGER.error(MessageUtil.getMessage(MessageID.EROB004, listDForecastvalues.size(), AppConstants.E_24HR_EXTRACTDATA_COUNT));
                throw(new Exception());
            }

            // 需要予測の誤差の算出

            // 需要発電計測ポイント情報の発電計測ポイントリストIDに対応する発電計測ポイントリストを取得
            TGenerationMeasurepointList listGenerationMeasurepointId = this.getGenerationMeasurepointId(dgLocation.getGenerationMeasurepointListId());

            // 発電計測ポイントリスト内の発電量の計測ポイントIDを取得
            int generationMeasurepointId = listGenerationMeasurepointId.getGenerationMeasurepointId()[AppConstants.G_GENERATION_DATA_ORDER];

            // 発電実績をt_extractvalueテーブルから取得
            List<TExtractvalue> listGExtractvalues = this.getGenerationExtractData(argDate, generationMeasurepointId);
            // 発電予測をt_generation_forecastテーブルから取得
            List<TGenerationForecast> listGForecastvalues = this.getGenerationForecast(dgLocation.getDGLocationId(), argDate);

            // 取得したデータ数が想定した24時間分のデータ数と一致した場合
            if (listGExtractvalues.size() == AppConstants.E_24HR_EXTRACTDATA_COUNT
                    && listGForecastvalues.size() == AppConstants.E_24HR_EXTRACTDATA_COUNT) {

                for (int i=0; i<AppConstants.E_24HR_EXTRACTDATA_COUNT; i++) {
                    // 需要予測を1時間おき24時間分のデータ
                    if (i%2 == 0) {
                        arrayGMeasurevalues[i/2] = listGExtractvalues.get(i).getMeasurevalue();
                        arrayGForecastvalues[i/2] = listGForecastvalues.get(i).getGenerationForecastValue();
                    }
                }
            } else {
                LOGGER.error(MessageUtil.getMessage(MessageID.EROB004, listGExtractvalues.size(), AppConstants.E_24HR_EXTRACTDATA_COUNT));
                throw(new Exception());
            }

            // 発電予測の誤差の算出

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
     * 抽出データテーブルのDB検索を行うメソッド. <br>
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
        Timestamp startTS  = new Timestamp(sdf.parse(execDate).getTime() - AppConstants.ROBUST_1HR_IN_MILLISEC - AppConstants.ROBUST_24HR_IN_MILLISEC);
        Timestamp endTS  = new Timestamp(sdf.parse(execDate).getTime() - AppConstants.ROBUST_1HR_IN_MILLISEC);
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(MessageUtil.getMessage(MessageID.IROB020, sdf.format(startTS)));
            LOGGER.info(MessageUtil.getMessage(MessageID.IROB021, sdf.format(endTS)));
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
            LOGGER.info(MessageUtil.getMessage(MessageID.IROB022, list.size()));
        }

        return(list);
    }

    /**
     * 抽出データテーブルのDB検索を行うメソッド. <br>
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
        Timestamp startTS  = new Timestamp(sdf.parse(execDate).getTime() - AppConstants.ROBUST_1HR_IN_MILLISEC - AppConstants.ROBUST_24HR_IN_MILLISEC);
        Timestamp endTS  = new Timestamp(sdf.parse(execDate).getTime() - AppConstants.ROBUST_1HR_IN_MILLISEC);
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(MessageUtil.getMessage(MessageID.IROB020, sdf.format(startTS)));
            LOGGER.info(MessageUtil.getMessage(MessageID.IROB021, sdf.format(endTS)));
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
            LOGGER.info(MessageUtil.getMessage(MessageID.IROB022, list.size()));
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
    private List<TDemandForecast> getDemandForecast(Integer dgLocationId, String execDate) throws Exception {
        List<TDemandForecast> results = new ArrayList<TDemandForecast>();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
        long startTime  = sdf.parse(execDate).getTime() - AppConstants.ROBUST_1HR_IN_MILLISEC - AppConstants.ROBUST_24HR_IN_MILLISEC;

        SqlSession session = null;

        try {
            // DBセッションを取得
            session = this.comInfo.getSqlSessionFactory(
                    CommonConstants.ENVIRONMENT_GMSDB).openSession();

            // SQL実行
            TDemandForecastMapper mapper = session.getMapper(TDemandForecastMapper.class);

            for (int i=0; i<24; i++) {
                Timestamp targetTS = new Timestamp(startTime + (i+1) * AppConstants.ROBUST_1HR_IN_MILLISEC);
                TDemandForecast result = mapper.getDataWithForecastTime(dgLocationId, targetTS);
                results.add(result);
            }
        } finally {
            // セッションをクローズ
            if (null != session) {
                session.close();
            }
        }

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
        long startTime  = sdf.parse(execDate).getTime() - AppConstants.ROBUST_1HR_IN_MILLISEC - AppConstants.ROBUST_24HR_IN_MILLISEC;

        SqlSession session = null;

        try {
            // DBセッションを取得
            session = this.comInfo.getSqlSessionFactory(
                    CommonConstants.ENVIRONMENT_GMSDB).openSession();

            // SQL実行
            TGenerationForecastMapper mapper = session.getMapper(TGenerationForecastMapper.class);

            for (int i=0; i<48; i++) {
                Timestamp targetTS = new Timestamp(startTime + (i+1) * AppConstants.ROBUST_1HR_IN_MILLISEC);
                TGenerationForecast result = mapper.getDataWithForecastTime(dgLocationId, targetTS);
                results.add(result);
            }
        } finally {
            // セッションをクローズ
            if (null != session) {
                session.close();
            }
        }

        return(results);
    }



    /**
     * 需要予測誤差の算出を行うメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param dgLocationId 需要発電計測ポイントID
     * @param execDate 処理日時
     * @return 発電予測テーブルの検索結果
     * @throws Exception 処理中に問題が発生した場合
     */
    private List<TGenerationForecast> calcDemandErrInfo(Integer dgLocationId, String execDate) throws Exception {
        List<TGenerationForecast> results = new ArrayList<TGenerationForecast>();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
        long startTime  = sdf.parse(execDate).getTime() - AppConstants.ROBUST_1HR_IN_MILLISEC - AppConstants.ROBUST_24HR_IN_MILLISEC;

        SqlSession session = null;

        try {
            // DBセッションを取得
            session = this.comInfo.getSqlSessionFactory(
                    CommonConstants.ENVIRONMENT_GMSDB).openSession();

            // SQL実行
            TGenerationForecastMapper mapper = session.getMapper(TGenerationForecastMapper.class);

            for (int i=0; i<48; i++) {
                Timestamp targetTS = new Timestamp(startTime + (i+1) * AppConstants.ROBUST_1HR_IN_MILLISEC);
                TGenerationForecast result = mapper.getDataWithForecastTime(dgLocationId, targetTS);
                results.add(result);
            }
        } finally {
            // セッションをクローズ
            if (null != session) {
                session.close();
            }
        }

        return(results);
    }









    /**
     * 一時データファイルの生成を行うメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param listExtractValues データ元発電電力リスト
     * @param listWeatherInfo データ元気象情報リスト
     * @throws Exception 処理中に問題が発生した場合
     */
    private void storeTempDataFile(List<TExtractvalue> listExtractValues, List<TWeatherForecastG> listWeatherInfo) throws Exception {
        // CommonInfoのインスタンスを生成
        CommonInfo comInfo = CommonInfo.getInstance();
        Configuration gmsConfig = comInfo.getGmsConfig();

        // 一時データファイルのパス
        String tempFilePath = gmsConfig.getString(AppConstants.PROPERTY_ROBUST_ROOT_DIRECTORY);
        tempFilePath += AppConstants.G_TEMP_DATAFILE_I;
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(MessageUtil.getMessage(MessageID.IROB002, tempFilePath));
        }

        // 一時ファイルのfw(上書き)、pw生成
        FileWriter fw = new FileWriter(tempFilePath, false);
        PrintWriter pw = new PrintWriter(new BufferedWriter(fw));

        try {
            // 見出し行の出力
            pw.println(AppConstants.G_TEMP_DATAFILE_HEADER_I);

            for (int i=0; i<48; i++) {
                String line = "";
                @SuppressWarnings("deprecation")
                int dataHr = listWeatherInfo.get(i).getForecastTime().getHours();
                line += String.valueOf(dataHr) + ".0,";
                // 地上気圧
                line += String.valueOf(listWeatherInfo.get(i).getSurfacePressure()) + ",";
                // 風ベクトルU (東西)
                line += String.valueOf(listWeatherInfo.get(i).getUWind()) + ",";
                // 風ベクトルV (南北)
                line += String.valueOf(listWeatherInfo.get(i).getVWind()) + ",";
                // 気温
                line += String.valueOf(listWeatherInfo.get(i).getTemperature()) + ",";
                // 相対湿度
                line += String.valueOf(listWeatherInfo.get(i).getRelativeHumidity()) + ",";
                // 下層雲量
                line += String.valueOf(listWeatherInfo.get(i).getLowCloudCover()) + ",";
                // 中層雲量
                line += String.valueOf(listWeatherInfo.get(i).getMediumCloudCover()) + ",";
                // 上層雲量
                line += String.valueOf(listWeatherInfo.get(i).getHighCloudCover()) + ",";
                // 全雲量
                line += String.valueOf(listWeatherInfo.get(i).getTotalCloudCover()) + ",";
                // 降水量
                line += String.valueOf(listWeatherInfo.get(i).getPrecipitation()) + ",";
                // 日射量
                line += String.valueOf(listWeatherInfo.get(i).getSunshine()) + ",";
                // 発電電力量 (電力に0.5(時間)をかけて、30分辺りの電力量に変換)
                line += String.valueOf(listExtractValues.get(i).getMeasurevalue() * AppConstants.ROBUST_30MIN_IN_HOUR);
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
     * 発電予測の算出を行うメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @throws Exception 処理中に問題が発生した場合
     */
    private void calcForecastValue() throws Exception {
        // CommonInfoのインスタンスを生成
        CommonInfo comInfo = CommonInfo.getInstance();
        Configuration gmsConfig = comInfo.getGmsConfig();

        // 発電予測算出モジュールスクリプトのパス
        // 一時データファイルのパス
        String scriptPath = gmsConfig.getString(AppConstants.PROPERTY_ROBUST_ROOT_DIRECTORY);
        scriptPath += AppConstants.G_MODULE_PATH;
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(MessageUtil.getMessage(MessageID.IROB002, scriptPath));
        }

        // Pythonコマンド[名/パス]取得
        String pythonPath =comInfo.getGmsConfig().getString(AppConstants.PROPERTY_ROBUST_PYTHON_CMD);
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(MessageUtil.getMessage(MessageID.IROB010, pythonPath));
        }

        // 発電予測算出モジュールの実行
        ProcessBuilder processBuilder = new ProcessBuilder(pythonPath, scriptPath);
        Process execProcess = processBuilder.start();

        // プロセスが終了するまでWait
        if(!execProcess.waitFor(AppConstants.W_SCRIPT_TIMEOUT, TimeUnit.SECONDS)) {
            LOGGER.error(MessageUtil.getMessage(MessageID.EROB000));
            throw new Exception();
        }

        // weather_decode.pyの戻り値確認
        if (execProcess.exitValue() >= AppConstants.ROBUST_RESULT_NG) {
            LOGGER.error(MessageUtil.getMessage(MessageID.EROB001, execProcess.exitValue()));
            throw new Exception();
        }
    }

    /**
     * 一時データファイルの読み込みを行うメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @throws Exception 処理中に問題が発生した場合
     */
    private double[] loadTempDataFile() throws Exception {
        // CommonInfoのインスタンスを生成
        CommonInfo comInfo = CommonInfo.getInstance();
        Configuration gmsConfig = comInfo.getGmsConfig();

        // 一時データファイルのパス
        String tempFilePath = gmsConfig.getString(AppConstants.PROPERTY_ROBUST_ROOT_DIRECTORY);
        tempFilePath += AppConstants.G_TEMP_DATAFILE_O;
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(MessageUtil.getMessage(MessageID.IROB002, tempFilePath));
        }

        BufferedReader br = null;
        double[] results = new double[24];

        try {
            // デコード済みデータファイルのbr生成
            File file = new File(tempFilePath);
            br = new BufferedReader(new FileReader(file));

            int index = 0;
            String line = "";

            // デコード済みデータファイルを1行ずつ読み込み
            while ((line = br.readLine()) != null) {
                results[index] = Double.valueOf(line);
                index++;
            }
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info(MessageUtil.getMessage(MessageID.IROB012, index));
            }
        } finally {
            //brクローズ
            if (br != null) {
                br.close();
                br = null;
            }
        }
        return(results);
    }

    /**
     * 発電予測のDB登録を行うメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param dgLocationId DB登録対象の需要発電計測ポイントID
     * @param execDate DB登録対象の処理日時(予測実施時刻)
     * @param extractValues t_extractvalueテーブルから取得したデータ
     * @param forecastValues DB登録対象の発電量予測
     * @throws Exception 処理中に問題が発生した場合
     */
    private void storeForecastData(int dgLocationId, String execDate, List<TExtractvalue> extractValues, double[] forecastValues) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
        Timestamp calcTS  = new Timestamp(sdf.parse(execDate).getTime());

        // 処理日時の1時間後から開始
        //Timestamp foreTS  = new Timestamp(calcTS.getTime() + AppConstants.ROBUST_1HR_IN_MILLISEC);

        for (int i=0; i<24; i++) {
            // 24個のデータを中間値を線形補完しながら登録するため、2個1で処理を実施
            TGenerationForecast[] params = new TGenerationForecast[2];
            if (i==0) {
                params[0] = this.generateFirstForecastDataset(dgLocationId, calcTS, extractValues, forecastValues);
                params[1] = this.generateOddForecastDataset(i, dgLocationId, calcTS, forecastValues);
            } else {
                params[0] = this.generateEvenForecastDataset(i, dgLocationId, calcTS, forecastValues);
                params[1] = this.generateOddForecastDataset(i, dgLocationId, calcTS, forecastValues);
            }

            if (LOGGER.isInfoEnabled()) {
                LOGGER.info(MessageUtil.getMessage(MessageID.IROB023));
            }

            SqlSession session = null;

            try {
                // DBセッションを取得
                session = this.comInfo.getSqlSessionFactory(
                        CommonConstants.ENVIRONMENT_GMSDB).openSession();

                // SQL実行
                TGenerationForecastMapper mapper = session.getMapper(TGenerationForecastMapper.class);

                for (TGenerationForecast param : params) {
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
        }
    }

    /**
     * 発電予測の登録データセット(最初)の生成を行うメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param dgLocationId DB登録対象の需要発電計測ポイントID
     * @param calcTS 予測実施時刻のTimestamp
     * @param extractValues t_extractvalueテーブルから取得したデータ
     * @param forecastValues DB登録対象の発電量予測
     * @throws Exception 処理中に問題が発生した場合
     */
    private TGenerationForecast generateFirstForecastDataset(int dgLocationId, Timestamp calcTS, List<TExtractvalue> extractValues, double[] forecastValues) throws Exception {
        // 登録対象のパラメータをセット
        TGenerationForecast param = new TGenerationForecast();
        // 需要発電計測ポイントID
        param.setDGLocationId(dgLocationId);
        // 予測実施時刻を設定
        param.setCalcTime(calcTS);
        // 予測データ時刻を設定
        param.setForecastTime(new Timestamp(calcTS.getTime() + AppConstants.ROBUST_30MIN_IN_MILLISEC));
        // 発電量予測を設定
        param.setGenerationForecastValue((extractValues.get(0).getMeasurevalue() + (forecastValues[0])/2));
        return(param);
    }

    /**
     * 発電予測の登録データセット(奇数番目)の生成を行うメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param idx データの順番(0スタート、23まで)
     * @param dgLocationId DB登録対象の需要発電計測ポイントID
     * @param calcTS 予測実施時刻のTimestamp
     * @param forecastValues DB登録対象の発電量予測
     * @throws Exception 処理中に問題が発生した場合
     */
    private TGenerationForecast generateOddForecastDataset(int idx, int dgLocationId, Timestamp calcTS, double[] forecastValues) throws Exception {
        // 登録対象のパラメータをセット
        TGenerationForecast param = new TGenerationForecast();
        // 需要発電計測ポイントID
        param.setDGLocationId(dgLocationId);
        // 予測実施時刻を設定
        param.setCalcTime(calcTS);
        // 予測データ時刻を設定
        param.setForecastTime(new Timestamp(calcTS.getTime() + 2*(idx+1) * AppConstants.ROBUST_30MIN_IN_MILLISEC));
        // 発電量予測を設定
        param.setGenerationForecastValue(forecastValues[idx]);
        return(param);
    }

    /**
     * 発電予測の登録データセット(偶数番目)の生成を行うメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param idx データの順番(0スタート、23まで)
     * @param dgLocationId DB登録対象の需要発電計測ポイントID
     * @param calcTS 予測実施時刻のTimestamp
     * @param forecastValues DB登録対象の発電量予測
     * @throws Exception 処理中に問題が発生した場合
     */
    private TGenerationForecast generateEvenForecastDataset(int idx, int dgLocationId, Timestamp calcTS, double[] forecastValues) throws Exception {
        // 登録対象のパラメータをセット
        TGenerationForecast param = new TGenerationForecast();
        // 需要発電計測ポイントID
        param.setDGLocationId(dgLocationId);
        // 予測実施時刻を設定
        param.setCalcTime(calcTS);
        // 予測データ時刻を設定
        param.setForecastTime(new Timestamp(calcTS.getTime() + (2*(idx+1)+1) * AppConstants.ROBUST_30MIN_IN_MILLISEC));
        // 発電量予測を設定
        param.setGenerationForecastValue((forecastValues[idx-1] + forecastValues[idx])/2);
        return(param);
    }
}
