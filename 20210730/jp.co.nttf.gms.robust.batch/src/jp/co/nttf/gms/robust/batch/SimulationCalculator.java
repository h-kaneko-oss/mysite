/*
 * SimulationCalculator.java
 * Created on 2021/07/16
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
 * 充放電シミュレーションを行うバッチクラス. <br>
 * <b>【注意】</b> 特になし.
 *
 * @author sekiya
 */
public final class SimulationCalculator extends CommonBatch {
    /** 共通情報 */
    protected CommonInfo comInfo = CommonInfo.getInstance();

    /** ロガー */
    private static final AppLogger LOGGER = new AppLogger(SimulationCalculator.class);

    // 受電ステータス
    private static int sb;
    // 連系放電禁止SoC
    private static double socmin;
    // 自立運転禁止SoC
    private static double socisland;

    // ■ 入力情報(測定)
    // 発電電力量
    private static double[] wg = new double[AppConstants.S_24HR_SIMDATA_COUNT+1];
    // 需要電力量
    private static double[] wd = new double[AppConstants.S_24HR_SIMDATA_COUNT+1];

    // ■ 計算結果
    // 蓄電池のSoC
    private static double[] soc = new double[AppConstants.S_24HR_SIMDATA_COUNT+1];
    // MPPTユニットの運転負荷率
    private static double[] lfdcdc = new double[AppConstants.S_24HR_SIMDATA_COUNT+1];
    // 双方向AC/DCの運転負荷率(AC/DCコンバータの運転負荷率)
    private static double[] lfacdc = new double[AppConstants.S_24HR_SIMDATA_COUNT+1];
    // MPPTユニットの電力変換効率
    private static double[] kdcdc = new double[AppConstants.S_24HR_SIMDATA_COUNT+1];
    // 双方向AC/DCの充電時電力変換効率
    private static double[] kacdc_c = new double[AppConstants.S_24HR_SIMDATA_COUNT+1];
    // 双方向AC/DCの放電時電力変換効率
    private static double[] kacdc_d = new double[AppConstants.S_24HR_SIMDATA_COUNT+1];
    // 需要機器へ供給可能な平均発電電力
    private static double[] w1 = new double[AppConstants.S_24HR_SIMDATA_COUNT+1];
    // 平均需要電力の合計
    private static double[] w2 = new double[AppConstants.S_24HR_SIMDATA_COUNT+1];
    // 系統電力
    private static double[] wu = new double[AppConstants.S_24HR_SIMDATA_COUNT+1];
    // 充放電電力
    private static double[] wb = new double[AppConstants.S_24HR_SIMDATA_COUNT+1];
    // 過放電状態
    private static int[] sover = new int[AppConstants.S_24HR_SIMDATA_COUNT+1];

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
            LOGGER.info(MessageUtil.getMessage(MessageID.IROB069));
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
            SimulationCalculator reporter = new SimulationCalculator();
            reporter.execute(argDate);

        } catch (Exception e) {
            // ERRORログ出力
            LOGGER.error(MessageUtil.getMessage(MessageID.ACOM000), e);
            throw e;
        }

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(MessageUtil.getMessage(MessageID.IROB070));
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

            // 需要発電計測ポイント情報の発電計測ポイントリストIDに対応する発電計測ポイントリストを取得
            TGenerationMeasurepointList listGenerationMeasurepointId = this.getGenerationMeasurepointId(dgLocation.getGenerationMeasurepointListId());

            // t_extractvalueテーブルから最新のSoC放電禁止設定を取得
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info(MessageUtil.getMessage(MessageID.IROB098, listGenerationMeasurepointId.getGenerationMeasurepointId()[AppConstants.BT_SOC_MIN_ORDER]));
            }
            TExtractvalue datasetSocMin = this.getGenerationExtractData(listGenerationMeasurepointId.getGenerationMeasurepointId()[AppConstants.BT_SOC_MIN_ORDER]);
            socmin = datasetSocMin.getMeasurevalue();

            // t_extractvalueテーブルから最新のSoC下限設定(自立運転用)を取得
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info(MessageUtil.getMessage(MessageID.IROB099, listGenerationMeasurepointId.getGenerationMeasurepointId()[AppConstants.BT_SOC_ISLAND_ORDER]));
            }
            TExtractvalue datasetSocIsland = this.getGenerationExtractData(listGenerationMeasurepointId.getGenerationMeasurepointId()[AppConstants.BT_SOC_ISLAND_ORDER]);
            socisland = datasetSocIsland.getMeasurevalue();

            // t_extractvalueテーブルから最新の双方向電源動作ステータスを取得
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info(MessageUtil.getMessage(MessageID.IROB100, listGenerationMeasurepointId.getGenerationMeasurepointId()[AppConstants.BT_STATUS_DATA_ORDER]));
            }
            TExtractvalue datasetBatStatus = this.getGenerationExtractData(listGenerationMeasurepointId.getGenerationMeasurepointId()[AppConstants.BT_STATUS_DATA_ORDER]);
            if (((int)(double)datasetBatStatus.getMeasurevalue() & AppConstants.S_FLAG_RECEIVE_POWER_STATUS) != 0) {
                sb = 1;
            } else {
                sb = 0;
            }

            // t_extractvalueテーブルから最新の蓄電池SoCを取得
            TExtractvalue datasetSoc = this.getGenerationExtractData(listGenerationMeasurepointId.getGenerationMeasurepointId()[AppConstants.BT_SOC_DATA_ORDER]);
            soc[0] = datasetSoc.getMeasurevalue();

            // 処理日時の30分後から24時間分の需要予測を取得
            List<TDemandForecast> demandForecasts = this.getDemandForecast(dgLocation.getDGLocationId(), argDate);
            // 処理日時の30分後から24時間分の発電予測を取得
            List<TGenerationForecast> generationForecasts = this.getGenerationForecast(dgLocation.getDGLocationId(), argDate);

            // 取得したデータ数が想定した24時間分のデータ数と一致した場合
            if (demandForecasts.size() == AppConstants.S_24HR_SIMDATA_COUNT
                    && generationForecasts.size() == AppConstants.S_24HR_SIMDATA_COUNT) {

                // i=0はSoCの初期値のみを設定するので、i=1からスタート
                // 各予測値をシミュレーション入力用配列に詰め直す
                for (int i=1; i<=AppConstants.S_24HR_SIMDATA_COUNT; i++) {
                    wd[i] = demandForecasts.get(i-1).getDemandForecastValue();
                    wg[i] = generationForecasts.get(i-1).getGenerationForecastValue();
                }

                // 充放電シミュレーションの実施
                this.calcSimulation(dgLocation.getDGLocationId(), argDate);

            } else {
                LOGGER.error(MessageUtil.getMessage(MessageID.EROB005));
                LOGGER.error(MessageUtil.getMessage(MessageID.EROB006, demandForecasts.size(), AppConstants.S_24HR_SIMDATA_COUNT));
                LOGGER.error(MessageUtil.getMessage(MessageID.EROB006, generationForecasts.size(), AppConstants.S_24HR_SIMDATA_COUNT));
                throw(new Exception());
            }
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
     * @param generationMeasurepointId 発電計測ポイントID
     * @return 抽出データテーブルの検索結果
     * @throws Exception 処理中に問題が発生した場合
     */
    private TExtractvalue getGenerationExtractData(Integer generationMeasurepointId) throws Exception {
        TExtractvalue result = null;
        TExtractvalue param = new TExtractvalue();
        param.setMeasurepointid(generationMeasurepointId);
        param.setTotalflag(0); // 瞬時データ

        SqlSession session = null;

        try {
            // DBセッションを取得
            session = this.comInfo.getSqlSessionFactory(
                    CommonConstants.ENVIRONMENT_GMSDB).openSession();

            // SQL実行
            TExtractvalueMapper mapper = session.getMapper(TExtractvalueMapper.class);
            result = mapper.selectLatest(param);
        } finally {
            // セッションをクローズ
            if (null != session) {
                session.close();
            }
        }

        return(result);
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
        long startTime  = sdf.parse(execDate).getTime() + AppConstants.ROBUST_30MIN_IN_MILLISEC;
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
        long startTime  = sdf.parse(execDate).getTime() + AppConstants.ROBUST_30MIN_IN_MILLISEC;
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

        return(results);
    }

    /**
     * シミュレーション結果のDB登録を行うメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param rcvStatus 受電ステータス
     * @param latestSoc 最新の蓄電池のSoC
     * @param demandForecasts 需要予測 (30分間隔の24時間分)
     * @param generationForecasts 発電予測 (30分間隔の24時間分)
     * @throws Exception 処理中に問題が発生した場合
     */
    private void calcSimulation(int dgLocationId, String execDate) throws Exception {
        // シミュレーション設定ファイルの読み込み
        ResourceBundle simBundle = ResourceBundle.getBundle(AppConstants.S_RESOURCE_NAME);

        // ピークカット設定
        double ppeak = Double.valueOf(simBundle.getString(AppConstants.S_RESOURCE_KEY_P_PEAK));

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");

        // ■ i=nまで計算
        for (int i=1; i<=AppConstants.S_24HR_SIMDATA_COUNT; i++) {
            //System.out.println("=========================");
            //System.out.println(i + "番目のデータ処理");

            // SB：受電ステータス
            // 0: 停電(power outage)
            // 1: 受電(receiving power)
            //System.out.println("SB = " + sb[i]);
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info(MessageUtil.getMessage(MessageID.IROB101, "SB", i, sb));
            }

            // 連系運転モード
            if (sb == AppConstants.S_BATSTATUS_RECEIVING_POWER) {
                //System.out.println("連系運転モード");
                // ■ A

                // ■ 演算式1
                this.doCalculations1(simBundle, i);

                //System.out.println("W1[i] = " + w1[i]);
                //System.out.println("W2[i] = " + w2[i]);

                // ■ 余剰あり
                if (w1[i] > w2[i]) {
                    //System.out.println("余剰あり");
                    //System.out.println("SoC[i-1] =" + soc[i-1]);

                    // ■ 充電可
                    if (soc[i-1] < 1) {
                        //System.out.println("充電可");

                        // ■ 演算式2
                        this.doCalculations2(simBundle, i);

                        //System.out.println("SoC[i] = " + soc[i]);

                        // 全量充電可
                        if (soc[i] <= 1) {
                            //System.out.println("全量充電可");

                            // 何もしない
                        }
                        // 全量充電不可 (一部発電抑制)
                        else {
                            //System.out.println("全量充電不可 (一部発電抑制)");

                            // ■ 演算式3
                            this.doCalculations3(simBundle, i);
                        }

                    } // end - if (soc[i-1] < 1)
                    // ■ 充電不可 (発電抑制)
                    else {
                        //System.out.println("充電不可 (発電抑制)");

                        // ■ 演算式4
                        this.doCalculations4(simBundle, i);

                    } // end - else
                } // end - if (w1[i] > w2[i])
                // ■ 余剰なし
                else {
                    //System.out.println("余剰なし");

                    // 不足電力がピークカット設定値超
                    if (Math.abs(w1[i] - w2[i]) > ppeak) {
                        //System.out.println("不足電力がピークカット設定値超");

                        // 放電可
                        if (soc[i-1] > socmin) {
                            //System.out.println("放電可");

                            // ■ 演算式5
                            this.doCalculations5(simBundle, i);

                            // 全量放電可
                            if (soc[i] > socmin) {
                                //System.out.println("全量放電可");

                                // 何もしない
                            } // end - if (soc[i] > socmin)
                            // 全量放電不可 (一部系統供給)
                            else {
                                //System.out.println("全量放電不可 (一部系統供給)");

                                // ■ 演算式6
                                this.doCalculations6(simBundle, i);

                            } // end - else -> if (soc[i] > socmin)

                        } // end - if (soc[i-1] > socmin)
                        // 放電不可 (系統供給)
                        else {
                            //System.out.println("放電不可 (系統供給)");

                            // ■ 演算式7
                            this.doCalculations7(simBundle, i);

                        } // end - else -> if (soc[i-1] > socmin)

                    } // end - if (Math.abs(w1[i] - w2[i]) > ppeak)
                    // 不足電力がピークカット目標値内
                    else {
                        //System.out.println("不足電力がピークカット設定値内");

                        // 全量充電可
                        if (soc[i-1] < 1) {
                            //System.out.println("全量充電可");

                            // ■ 演算式8
                            this.doCalculations8(simBundle, i);

                            // 全量充電可
                            if (soc[i] <= 1) {
                                //System.out.println("全量充電可");

                                // 何もしない
                            } // end - if (soc[i] <= 1)
                            // 全量充電不可 (浮動充電)
                            else {
                                //System.out.println("全量充電不可 (浮動充電)");

                                // ■ 演算式9
                                this.doCalculations9(simBundle, i);

                            } // end - else -> if (soc[i] <= 1)

                        } // end - if (soc[i-1] < 1)
                        // 充電不可 (充電停止)
                        else {
                            //System.out.println("充電不可 (充電停止)");

                            // ■ 演算式10
                            this.doCalculations10(simBundle, i);

                        } // end - else -> if (soc[i-1] < 1)

                    } // end - else -> if (Math.abs(w1[i] - w2[i]) > ppeak)

                } // end - else -> if (w1[i] > w2[i])

                //System.out.println("Receiving Power");
            } // end - if (sb[i] == RECEIVING_POWER)
          // 自立運転モード
          else {
            //System.out.println("自立運転モード");
            // ■ B

            // ■ 演算式1
            this.doCalculations1(simBundle, i);

            //System.out.println("W1[i] = " + w1[i]);
            //System.out.println("W2[i] = " + w2[i]);

            // ■ 余剰あり
            if (w1[i] > w2[i]) {
              //System.out.println("余剰あり");
              //System.out.println("SoC[i-1] =" + soc[i-1]);

              // ■ 充電可
              if (soc[i-1] < 1) {
                //System.out.println("充電可");

                // ■ 演算式2
                this.doCalculations2(simBundle, i);

                //System.out.println("SoC[i] = " + soc[i]);

                // 全量充電可
                if (soc[i] <= 1) {
                  //System.out.println("全量充電可");

                  // 何もしない
                }
                // 全量充電不可 (一部発電抑制)
                else {
                  //System.out.println("全量充電不可 (一部発電抑制)");

                  // ■ 演算式3
                  this.doCalculations3(simBundle, i);
                }

              } // end - if (soc[i-1] < 1)
              // ■ 充電不可 (発電抑制)
              else {
                //System.out.println("充電不可 (発電抑制)");

                // ■ 演算式4
                this.doCalculations4(simBundle, i);

              } // end - else
            } // end - if (w1[i] > w2[i])
            // ■ 余剰なし
            else {
              //System.out.println("余剰なし");

              // 放電可
              if (soc[i-1] > socisland) {
                //System.out.println("放電可");

                // ■ 演算式13
                this.doCalculations13(simBundle, i);

                // 全量放電可
                if (soc[i] > socisland) {
                  //System.out.println("全量放電可");

                  // 何もしない
                } // end - if (soc[i] > socisland)
                // 全量放電不可 (一部系統供給)
                else {
                  //System.out.println("全量放電不可 (一部系統供給)");

                  // ■ 演算式11
                  this.doCalculations11(simBundle, i);

                } // end - else -> if (soc[i] > socisland)

              } // end - if (soc[i-1] > socisland)
              // 放電不可 (停電)
              else {
                //System.out.println("放電不可 (停電)");

                // ■ 演算式12
                this.doCalculations12(simBundle, i);

              } // end - else -> if (soc[i-1] > socisland)

            } // end - else -> if (w1[i] > w2[i])

            //System.out.println("Power Outage");
          } // end - else -> if (sb[i] == RECEIVING_POWER)

          //System.out.println("");

          TSimulationIntermediate simIntermediate = new TSimulationIntermediate();
          simIntermediate.setDGLocationId(dgLocationId);
          simIntermediate.setSimulationTime(new Timestamp(sdf.parse(execDate).getTime()));
          simIntermediate.setTimeSeriesIndex(i); // 1-48
          simIntermediate.setSoc((float)soc[i]);
          simIntermediate.setWU(wu[i]);
          simIntermediate.setWB(wb[i]);
          simIntermediate.setSOver(sover[i]);
          simIntermediate.setLfAcdc((float)lfacdc[i]);
          simIntermediate.setLfDcdc((float)lfdcdc[i]);
          simIntermediate.setKAcdcC(kacdc_c[i]);
          simIntermediate.setKAcdcD(kacdc_d[i]);
          simIntermediate.setKDcdc(kdcdc[i]);
          this.storeSimulationIntermediate(simIntermediate);
        } // end - for (int i=0; i<=n; i++)

        // ■ C

        // ■ 演算式14
        TSimulationResult simResult = this.doCalculations14(simBundle, dgLocationId, new Timestamp(sdf.parse(execDate).getTime()));
        this.storeSimulationResult(simResult);

        // SOCMIN：連系放電禁止SoC
        if (socmin >= 1) {
          socmin = 1;
        }
    }

    /**
     * シミュレーション結果のDB登録を行うメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param param DB登録対象のシミュレーション結果
     * @throws Exception 処理中に問題が発生した場合
     */
    private void storeSimulationResult(TSimulationResult param) throws Exception {
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(MessageUtil.getMessage(MessageID.IROB033));
        }

        SqlSession session = null;

        try {
            // DBセッションを取得
            session = this.comInfo.getSqlSessionFactory(
                    CommonConstants.ENVIRONMENT_GMSDB).openSession();

            // SQL実行
            TSimulationResultMapper mapper = session.getMapper(TSimulationResultMapper.class);
            mapper.insert(param);

            // コミット
            session.commit();

            if (LOGGER.isInfoEnabled()) {
                LOGGER.info(MessageUtil.getMessage(MessageID.IROB034));
            }
        } finally {
            // セッションをクローズ
            if (null != session) {
                session.close();
            }
        }
    }

    /**
     * シミュレーション中間結果のDB登録を行うメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param param DB登録対象のシミュレーション中間結果
     * @throws Exception 処理中に問題が発生した場合
     */
    private void storeSimulationIntermediate(TSimulationIntermediate param) throws Exception {
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(MessageUtil.getMessage(MessageID.IROB102));
        }

        SqlSession session = null;

        try {
            // DBセッションを取得
            session = this.comInfo.getSqlSessionFactory(
                    CommonConstants.ENVIRONMENT_GMSDB).openSession();

            // SQL実行
            TSimulationIntermediateMapper mapper = session.getMapper(TSimulationIntermediateMapper.class);
            mapper.insert(param);

            // コミット
            session.commit();

            if (LOGGER.isInfoEnabled()) {
                LOGGER.info(MessageUtil.getMessage(MessageID.IROB103));
            }
        } finally {
            // セッションをクローズ
            if (null != session) {
                session.close();
            }
        }
    }

    /**
     * 充放電シミュレーションの演算式1を行うメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param ResourceBundle シミュレーション設定
     * @param idx 各種データのインデックス
     * @throws Exception 処理中に問題が発生した場合
     */
    private void doCalculations1(ResourceBundle simBundle, int idx) throws Exception {
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(MessageUtil.getMessage(MessageID.IROB084));
        }

        //System.out.println("演算式1を実施");

        // 安全率 (需要)
        double gammad = Double.valueOf(simBundle.getString(AppConstants.S_RESOURCE_KEY_GAMMA_D));
        // 安全率 (発電)
        double gammag = Double.valueOf(simBundle.getString(AppConstants.S_RESOURCE_KEY_GAMMA_G));
        // MPPTユニットの定格容量
        double pdcdc = Double.valueOf(simBundle.getString(AppConstants.S_RESOURCE_KEY_P_DCDC));
        // AC/DCコンバータの定格容量
        double pacdc = Double.valueOf(simBundle.getString(AppConstants.S_RESOURCE_KEY_P_ACDC));
        // 待機電力
        double wt = Double.valueOf(simBundle.getString(AppConstants.S_RESOURCE_KEY_WT));

        // MPPTユニットの電力変換効率の定数1
        double kdcdc_coe1 = Double.valueOf(simBundle.getString(AppConstants.S_RESOURCE_KEY_K_DCDC_COE1));
        // MPPTユニットの電力変換効率の定数2
        double kdcdc_coe2 = Double.valueOf(simBundle.getString(AppConstants.S_RESOURCE_KEY_K_DCDC_COE2));
        // MPPTユニットの電力変換効率の定数3
        double kdcdc_coe3 = Double.valueOf(simBundle.getString(AppConstants.S_RESOURCE_KEY_K_DCDC_COE3));
        // MPPTユニットの電力変換効率の定数4
        double kdcdc_coe4 = Double.valueOf(simBundle.getString(AppConstants.S_RESOURCE_KEY_K_DCDC_COE4));
        // MPPTユニットの電力変換効率の定数5
        double kdcdc_coe5 = Double.valueOf(simBundle.getString(AppConstants.S_RESOURCE_KEY_K_DCDC_COE5));
        // MPPTユニットの電力変換効率の定数6
        double kdcdc_coe6 = Double.valueOf(simBundle.getString(AppConstants.S_RESOURCE_KEY_K_DCDC_COE6));

        // AC/DCコンバータの充電時電力変換効率の定数1
        double kacdc_c_coe1 = Double.valueOf(simBundle.getString(AppConstants.S_RESOURCE_KEY_K_ACDC_C_COE1));
        // AC/DCコンバータの充電時電力変換効率の定数2
        double kacdc_c_coe2 = Double.valueOf(simBundle.getString(AppConstants.S_RESOURCE_KEY_K_ACDC_C_COE2));
        // AC/DCコンバータの充電時電力変換効率の定数3
        double kacdc_c_coe3 = Double.valueOf(simBundle.getString(AppConstants.S_RESOURCE_KEY_K_ACDC_C_COE3));
        // AC/DCコンバータの充電時電力変換効率の定数4
        double kacdc_c_coe4 = Double.valueOf(simBundle.getString(AppConstants.S_RESOURCE_KEY_K_ACDC_C_COE4));
        // AC/DCコンバータの充電時電力変換効率の定数5
        double kacdc_c_coe5 = Double.valueOf(simBundle.getString(AppConstants.S_RESOURCE_KEY_K_ACDC_C_COE5));
        // AC/DCコンバータの充電時電力変換効率の定数6
        double kacdc_c_coe6 = Double.valueOf(simBundle.getString(AppConstants.S_RESOURCE_KEY_K_ACDC_C_COE6));

        // AC/DCコンバータの放電時電力変換効率の定数1
        double kacdc_d_coe1 = Double.valueOf(simBundle.getString(AppConstants.S_RESOURCE_KEY_K_ACDC_D_COE1));
        // AC/DCコンバータの放電時電力変換効率の定数2
        double kacdc_d_coe2 = Double.valueOf(simBundle.getString(AppConstants.S_RESOURCE_KEY_K_ACDC_D_COE2));
        // AC/DCコンバータの放電時電力変換効率の定数3
        double kacdc_d_coe3 = Double.valueOf(simBundle.getString(AppConstants.S_RESOURCE_KEY_K_ACDC_D_COE3));
        // AC/DCコンバータの放電時電力変換効率の定数4
        double kacdc_d_coe4 = Double.valueOf(simBundle.getString(AppConstants.S_RESOURCE_KEY_K_ACDC_D_COE4));
        // AC/DCコンバータの放電時電力変換効率の定数5
        double kacdc_d_coe5 = Double.valueOf(simBundle.getString(AppConstants.S_RESOURCE_KEY_K_ACDC_D_COE5));
        // AC/DCコンバータの放電時電力変換効率の定数6
        double kacdc_d_coe6 = Double.valueOf(simBundle.getString(AppConstants.S_RESOURCE_KEY_K_ACDC_D_COE6));

        // [1-1] MPPTユニットの運転負荷率
        lfdcdc[idx] = gammag * wg[idx] / pdcdc;
        //System.out.println("[1-1] MPPTユニットの運転負荷率: " + lfdcdc[idx]);
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(MessageUtil.getMessage(MessageID.IROB101, "[1-1] lfdcdc", idx, lfdcdc[idx]));
        }

        // [1-2] MPPTユニットの電力変換効率
        kdcdc[idx] = kdcdc_coe1 * Math.pow(lfdcdc[idx], 5);
        kdcdc[idx] -= kdcdc_coe2 * Math.pow(lfdcdc[idx], 4);
        kdcdc[idx] += kdcdc_coe3 * Math.pow(lfdcdc[idx], 3);
        kdcdc[idx] -= kdcdc_coe4 * Math.pow(lfdcdc[idx], 2);
        kdcdc[idx] += kdcdc_coe5 * lfdcdc[idx];
        kdcdc[idx] += kdcdc_coe6;
        //System.out.println("[1-2] MPPTユニットの電力変換効率: " + kdcdc[idx]);
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(MessageUtil.getMessage(MessageID.IROB101, "[1-2] kdcdc", idx, kdcdc[idx]));
        }

        // [1-3] 平均需要電力の合計
        w2[idx] = wt + gammad * wd[idx];
        //System.out.println("[1-3] 平均需要電力の合計: " + w2[idx]);
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(MessageUtil.getMessage(MessageID.IROB101, "[1-3] w2", idx, w2[idx]));
        }

        // [1-4] AC/DCコンバータの運転負荷率
        lfacdc[idx] = w2[idx] / pacdc;
        //System.out.println("[1-4] AC/DCコンバータの運転負荷率: " + lfacdc[idx]);
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(MessageUtil.getMessage(MessageID.IROB101, "[1-4] lfacdc", idx, lfacdc[idx]));
        }

        // [1-5] AC/DCコンバータの電力変換効率
        kacdc_c[idx] = kacdc_c_coe1 * Math.pow(lfacdc[idx], 5);
        kacdc_c[idx] -= kacdc_c_coe2 * Math.pow(lfacdc[idx], 4);
        kacdc_c[idx] += kacdc_c_coe3 * Math.pow(lfacdc[idx], 3);
        kacdc_c[idx] -= kacdc_c_coe4 * Math.pow(lfacdc[idx], 2);
        kacdc_c[idx] += kacdc_c_coe5 * lfacdc[idx];
        kacdc_c[idx] += kacdc_c_coe6;
        //System.out.println("[1-5] AC/DCコンバータの電力変換効率: " + kacdc_c[idx]);
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(MessageUtil.getMessage(MessageID.IROB101, "[1-5] kacdc_c", idx, kacdc_c[idx]));
        }

        kacdc_d[idx] = kacdc_d_coe1 * Math.pow(lfacdc[idx], 5);
        kacdc_d[idx] -= kacdc_d_coe2 * Math.pow(lfacdc[idx], 4);
        kacdc_d[idx] += kacdc_d_coe3 * Math.pow(lfacdc[idx], 3);
        kacdc_d[idx] -= kacdc_d_coe4 * Math.pow(lfacdc[idx], 2);
        kacdc_d[idx] += kacdc_d_coe5 * lfacdc[idx];
        kacdc_d[idx] += kacdc_d_coe6;
        //System.out.println("[1-5] AC/DCコンバータの電力変換効率: " + kacdc_d[idx]);
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(MessageUtil.getMessage(MessageID.IROB101, "[1-5] kacdc_d", idx, kacdc_d[idx]));
        }

        // [1-6] 需要機器へ供給可能な平均発電電力
        // 【要確認】電力変換効率はidxのデータを使用するで良い？(行列の計算のようなことはせず)
        w1[idx] = gammag * kdcdc[idx] * kacdc_d[idx] * wg[idx];
        //System.out.println("[1-6] 需要機器へ供給可能な平均発電電力: " + w1[idx]);
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(MessageUtil.getMessage(MessageID.IROB101, "[1-6] w1", idx, w1[idx]));
        }

        // [1-7] SoCの最新値をSoC(i)の初期値として代入
        // execute()メソッドにて実施
    }

    /**
     * 充放電シミュレーションの演算式2を行うメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param ResourceBundle シミュレーション設定
     * @param idx 各種データのインデックス
     * @throws Exception 処理中に問題が発生した場合
     */
    private void doCalculations2(ResourceBundle simBundle, int idx) throws Exception {
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(MessageUtil.getMessage(MessageID.IROB085));
        }

        //System.out.println("演算式2を実施");

        // 蓄電池定格容量
        double wc = Double.valueOf(simBundle.getString(AppConstants.S_RESOURCE_KEY_WC));
        // 分解能
        double t = Double.valueOf(simBundle.getString(AppConstants.S_RESOURCE_KEY_T));
        // 演算時間単位
        double T = t/60;

        // [2-1] 平均系統電力
        wu[idx] = 0;
        //System.out.println("[2-1] 平均系統電力: " + wu[idx]);
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(MessageUtil.getMessage(MessageID.IROB101, "[2-1] wu", idx, wu[idx]));
        }

        // [2-2] 平均充電電力(余剰電力を全量充電)
        wb[idx] = -1 * Math.abs(kdcdc[idx] * wg[idx] - w2[idx] / kacdc_d[idx]);
        //System.out.println("[2-2] 平均充電電力(余剰電力を全量充電): " + wb[idx]);
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(MessageUtil.getMessage(MessageID.IROB101, "[2-2] wb", idx, wb[idx]));
        }

        // [2-3] SoCに充電分を加算
        soc[idx] = soc[idx-1] + (-1 * wb[idx] / T) / wc;
        //System.out.println("[2-3] SoCに充電分を加算: " + soc[idx]);
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(MessageUtil.getMessage(MessageID.IROB101, "[2-3] soc", idx, soc[idx]));
        }

        // [2-4] 過放電なし
        sover[idx] = 0;
        //System.out.println("[2-4] 過放電なし: " + sover[idx]);
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(MessageUtil.getMessage(MessageID.IROB101, "[2-4] sover", idx, sover[idx]));
        }
    }

    /**
     * 充放電シミュレーションの演算式3を行うメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param ResourceBundle シミュレーション設定
     * @param idx 各種データのインデックス
     * @throws Exception 処理中に問題が発生した場合
     */
    private void doCalculations3(ResourceBundle simBundle, int idx) throws Exception {
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(MessageUtil.getMessage(MessageID.IROB086));
        }

        //System.out.println("演算式3を実施");

        // 蓄電池定格容量
        double wc = Double.valueOf(simBundle.getString(AppConstants.S_RESOURCE_KEY_WC));

        // [3-1] 平均系統電力
        wu[idx] = 0;
        //System.out.println("[3-1] 平均系統電力: " + wu[idx]);
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(MessageUtil.getMessage(MessageID.IROB101, "[3-1] wu", idx, wu[idx]));
        }

        // [3-2] 平均充電電力(充電可能量を全量充電)
        wb[idx] = -1 * Math.abs(wc * (1 - soc[idx-1]));
        //System.out.println("[3-2] 平均充電電力(充電可能量を全量充電): " + wb[idx]);
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(MessageUtil.getMessage(MessageID.IROB101, "[3-2] wb", idx, wb[idx]));
        }

        // [3-3] SoCに充電分を加算 (1になる)
        soc[idx] = soc[idx-1] + (-1 * wb[idx]) / wc;
        //System.out.println("[3-3] SoCに充電分を加算(1になる): " + soc[idx]);
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(MessageUtil.getMessage(MessageID.IROB101, "[3-3] soc", idx, soc[idx]));
        }

        // [3-4] 過放電なし
        sover[idx] = 0;
        //System.out.println("[3-4] 過放電なし: " + sover[idx]);
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(MessageUtil.getMessage(MessageID.IROB101, "[3-4] sover", idx, sover[idx]));
        }
    }

    /**
     * 充放電シミュレーションの演算式4を行うメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param ResourceBundle シミュレーション設定
     * @param idx 各種データのインデックス
     * @throws Exception 処理中に問題が発生した場合
     */
    private void doCalculations4(ResourceBundle simBundle, int idx) throws Exception {
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(MessageUtil.getMessage(MessageID.IROB087));
        }

        //System.out.println("演算式4を実施");

        // 蓄電池定格容量
        double wc = Double.valueOf(simBundle.getString(AppConstants.S_RESOURCE_KEY_WC));
        // 分解能
        double t = Double.valueOf(simBundle.getString(AppConstants.S_RESOURCE_KEY_T));
        // 演算時間単位
        double T = t/60;

        // [4-1] 平均系統電力
        wu[idx] = 0;
        //System.out.println("[4-1] 平均系統電力: " + wu[idx]);
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(MessageUtil.getMessage(MessageID.IROB101, "[4-1] wu", idx, wu[idx]));
        }

        // [4-2] 充電電力量 (余剰電力の充電なし)
        wb[idx] = 0;
        //System.out.println("[4-2] 充電電力(余剰電力の充電なし): " + wb[idx]);
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(MessageUtil.getMessage(MessageID.IROB101, "[4-2] wb", idx, wb[idx]));
        }

        // [4-3] SoCを前値保持
        soc[idx] = soc[idx-1] + (wb[idx] / T) / wc;
        //System.out.println("[4-3] SoCを前値保持: " + soc[idx]);
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(MessageUtil.getMessage(MessageID.IROB101, "[4-3] soc", idx, soc[idx]));
        }

        // [4-4] 過放電なし
        sover[idx] = 0;
        //System.out.println("[4-4] 過放電なし: " + sover[idx]);
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(MessageUtil.getMessage(MessageID.IROB101, "[4-4] sover", idx, sover[idx]));
        }
    }

    /**
     * 充放電シミュレーションの演算式5を行うメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param ResourceBundle シミュレーション設定
     * @param idx 各種データのインデックス
     * @throws Exception 処理中に問題が発生した場合
     */
    private void doCalculations5(ResourceBundle simBundle, int idx) throws Exception {
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(MessageUtil.getMessage(MessageID.IROB088));
        }

        //System.out.println("演算式5を実施");

        // ピークカット設定
        double ppeak = Double.valueOf(simBundle.getString(AppConstants.S_RESOURCE_KEY_P_PEAK));
        // 蓄電池定格容量
        double wc = Double.valueOf(simBundle.getString(AppConstants.S_RESOURCE_KEY_WC));
        // 分解能
        double t = Double.valueOf(simBundle.getString(AppConstants.S_RESOURCE_KEY_T));
        // 演算時間単位
        double T = t/60;

        // [5-1] ピークカット設定値にセット
        // 【要確認】ppeakはidxに依存しないはず。
        wu[idx] = ppeak;
        //System.out.println("[5-1] ピークカット設定値にセット: " + wu[idx]);
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(MessageUtil.getMessage(MessageID.IROB101, "[5-1] wu", idx, wu[idx]));
        }

        // [5-2] 放電電力量 (不足分を全量放電)
        wb[idx] = Math.abs((w2[idx] - ppeak) / kacdc_d[idx] - kdcdc[idx] * wg[idx]);
        //System.out.println("[5-2] 放電電力量(不足分を全量放電): " + wb[idx]);
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(MessageUtil.getMessage(MessageID.IROB101, "[5-2] wb", idx, wb[idx]));
        }

        // [5-3] SoCは放電分を減算
        soc[idx] = soc[idx-1] + (-1 * wb[idx] / T) / wc;
        //System.out.println("[5-3] SoCは放電分を減算: " + soc[idx]);
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(MessageUtil.getMessage(MessageID.IROB101, "[5-3] soc", idx, soc[idx]));
        }

        // [5-4] 過放電なし
        sover[idx] = 0;
        //System.out.println("[5-4] 過放電なし: " + sover[idx]);
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(MessageUtil.getMessage(MessageID.IROB101, "[5-4] sover", idx, sover[idx]));
        }
    }

    /**
     * 充放電シミュレーションの演算式6を行うメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param ResourceBundle シミュレーション設定
     * @param idx 各種データのインデックス
     * @throws Exception 処理中に問題が発生した場合
     */
    private void doCalculations6(ResourceBundle simBundle, int idx) throws Exception {
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(MessageUtil.getMessage(MessageID.IROB089));
        }

        //System.out.println("演算式6を実施");

        // 安全率 (発電)
        double gammag = Double.valueOf(simBundle.getString(AppConstants.S_RESOURCE_KEY_GAMMA_G));
        // 蓄電池定格容量
        double wc = Double.valueOf(simBundle.getString(AppConstants.S_RESOURCE_KEY_WC));

        // [6-1] 放電電力量 (放電可能量を全量放電)
        wb[idx] = Math.abs(wc * (soc[idx-1] - socmin));
        //System.out.println("[6-1] 放電電量量(放電可能量を全量放電): " + wb[idx]);
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(MessageUtil.getMessage(MessageID.IROB101, "[6-1] wb", idx, wb[idx]));
        }

        // [6-2] 不足分を全量系統から供給
        wu[idx] = w2[idx] - kacdc_d[idx] * (wb[idx] + gammag * kdcdc[idx] * wg[idx]);
        //System.out.println("[6-2] 不足分を全量系統から供給: " + wu[idx]);
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(MessageUtil.getMessage(MessageID.IROB101, "[6-2] wu", idx, wu[idx]));
        }

        // [6-3] SoCは放電分を減算 (SoCMINになる)
        soc[idx] = soc[idx-1] + (-1 * wb[idx]) / wc;
        //System.out.println("[6-3] SoCは充電分を減算(SoCMINになる): " + soc[idx]);
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(MessageUtil.getMessage(MessageID.IROB101, "[6-3] soc", idx, soc[idx]));
        }

        // [6-4] 過放電なし
        sover[idx] = 0;
        //System.out.println("[6-4] 過放電なし: " + sover[idx]);
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(MessageUtil.getMessage(MessageID.IROB101, "[6-4] sover", idx, sover[idx]));
        }
    }

    /**
     * 充放電シミュレーションの演算式7を行うメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param ResourceBundle シミュレーション設定
     * @param idx 各種データのインデックス
     * @throws Exception 処理中に問題が発生した場合
     */
    private void doCalculations7(ResourceBundle simBundle, int idx) throws Exception {
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(MessageUtil.getMessage(MessageID.IROB090));
        }

        //System.out.println("演算式7を実施");

        // 蓄電池定格容量
        double wc = Double.valueOf(simBundle.getString(AppConstants.S_RESOURCE_KEY_WC));
        // 分解能
        double t = Double.valueOf(simBundle.getString(AppConstants.S_RESOURCE_KEY_T));
        // 演算時間単位
        double T = t/60;

        // [7-1] 放電電力量を0をセット
        wb[idx] = 0;
        //System.out.println("[7-1] 放電電力量を0をセット: " + wb[idx]);
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(MessageUtil.getMessage(MessageID.IROB101, "[7-1] wb", idx, wb[idx]));
        }

        // [7-2] 不足分を全量系統から供給
        wu[idx] = w2[idx] - w1[idx];
        //System.out.println("[7-2] 不足分を全量系統から供給: " + wu[idx]);
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(MessageUtil.getMessage(MessageID.IROB101, "[7-2] wu", idx, wu[idx]));
        }

        // [7-3] SoCは放電分を減算 (SoCMINになる)
        soc[idx] = soc[idx-1] + (-1 * wb[idx] / T) / wc;
        //System.out.println("[7-3] SoCは放電分を減算(SoCMINになる): " + soc[idx]);
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(MessageUtil.getMessage(MessageID.IROB101, "[7-3] soc", idx, soc[idx]));
        }

        // [7-4] 過放電なし
        sover[idx] = 0;
        //System.out.println("[7-4] 過放電なし: " + sover[idx]);
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(MessageUtil.getMessage(MessageID.IROB101, "[7-4] sover", idx, sover[idx]));
        }
    }

    /**
     * 充放電シミュレーションの演算式8を行うメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param ResourceBundle シミュレーション設定
     * @param idx 各種データのインデックス
     * @throws Exception 処理中に問題が発生した場合
     */
    private void doCalculations8(ResourceBundle simBundle, int idx) throws Exception {
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(MessageUtil.getMessage(MessageID.IROB091));
        }

        //System.out.println("演算式8を実施");

        // ピークカット設定
        double ppeak = Double.valueOf(simBundle.getString(AppConstants.S_RESOURCE_KEY_P_PEAK));
        // 蓄電池定格容量
        double wc = Double.valueOf(simBundle.getString(AppConstants.S_RESOURCE_KEY_WC));
        // 分解能
        double t = Double.valueOf(simBundle.getString(AppConstants.S_RESOURCE_KEY_T));
        // 演算時間単位
        double T = t/60;

        // [8-1] ピークカット目標値分の供給
        // 【要確認】ppeakはidxに依存しないはず。
        wu[idx] = ppeak;
        //System.out.println("[8-1] ピークカット目標値分の供給:" + wu[idx]);
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(MessageUtil.getMessage(MessageID.IROB101, "[8-1] wu", idx, wu[idx]));
        }

        // [8-2] 平均充電電力 (系統充電)
        // 【要確認】ppeakはidxに依存しないはず。
        wb[idx] = -1 * Math.abs(ppeak - (w2[idx] - w1[idx])) * kacdc_c[idx];
        //System.out.println("[8-2] 平均充電電力 (系統充電): " + wb[idx]);
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(MessageUtil.getMessage(MessageID.IROB101, "[8-2] wb", idx, wb[idx]));
        }

        // [8-3] SoCは余剰分を加算
        soc[idx] = soc[idx-1] + (-1 * wb[idx] / T) / wc;
        //System.out.println("[8-3] SoCは余剰分を加算: " + soc[idx]);
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(MessageUtil.getMessage(MessageID.IROB101, "[8-3] soc", idx, soc[idx]));
        }

        // [8-4] 過放電なし
        sover[idx] = 0;
        //System.out.println("[8-4] 過放電なし: " + sover[idx]);
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(MessageUtil.getMessage(MessageID.IROB101, "[8-4] sover", idx, sover[idx]));
        }
    }

    /**
     * 充放電シミュレーションの演算式9を行うメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param ResourceBundle シミュレーション設定
     * @param idx 各種データのインデックス
     * @throws Exception 処理中に問題が発生した場合
     */
    private void doCalculations9(ResourceBundle simBundle, int idx) throws Exception {
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(MessageUtil.getMessage(MessageID.IROB092));
        }

        //System.out.println("演算式9を実施");

        // 蓄電池定格容量
        double wc = Double.valueOf(simBundle.getString(AppConstants.S_RESOURCE_KEY_WC));

        // [9-1] 平均充電電力 (系統充電)
        wb[idx] = -1 * Math.abs(wc * (1 - soc[idx-1]));
        //System.out.println("[9-1] 平均充電電力(系統充電): " + wb[idx]);
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(MessageUtil.getMessage(MessageID.IROB101, "[9-1] wb", idx, wb[idx]));
        }

        // [9-2] ピークカット目標値分の供給
        wu[idx] = -1 * wb[idx] / kacdc_c[idx] + (w2[idx] - w1[idx]);
        //System.out.println("[9-2] ピークカット目標値分の供給: " + wu[idx]);
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(MessageUtil.getMessage(MessageID.IROB101, "[9-2] wu", idx, wu[idx]));
        }

        // [9-3] SoCは余剰分を加算
        soc[idx] = soc[idx-1] + (-1 * wb[idx]) / wc;
        //System.out.println("[9-3] SoCは余剰分を加算: " + soc[idx]);
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(MessageUtil.getMessage(MessageID.IROB101, "[9-3] soc", idx, soc[idx]));
        }

        // [9-4] 過放電なし
        sover[idx] = 0;
        //System.out.println("[9-4] 過放電なし: " + sover[idx]);
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(MessageUtil.getMessage(MessageID.IROB101, "[9-4] sover", idx, sover[idx]));
        }
    }

    /**
     * 充放電シミュレーションの演算式10を行うメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param ResourceBundle シミュレーション設定
     * @param idx 各種データのインデックス
     * @throws Exception 処理中に問題が発生した場合
     */
    private void doCalculations10(ResourceBundle simBundle, int idx) throws Exception {
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(MessageUtil.getMessage(MessageID.IROB093));
        }

        //System.out.println("演算式10を実施");

        // 蓄電池定格容量
        double wc = Double.valueOf(simBundle.getString(AppConstants.S_RESOURCE_KEY_WC));
        // 分解能
        double t = Double.valueOf(simBundle.getString(AppConstants.S_RESOURCE_KEY_T));
        // 演算時間単位
        double T = t/60;

        // [10-1] 平均充電電力 (系統充電)
        wb[idx] = 0;
        //System.out.println("[10-1] 平均充電電力(系統充電): " + wb[idx]);
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(MessageUtil.getMessage(MessageID.IROB101, "[10-1] wb", idx, wb[idx]));
        }

        // [10-2] ピークカット目標値分の供給
        wu[idx] = w2[idx] - w1[idx];
        //System.out.println("[10-2] ピークカット目標値分の供給: " + wu[idx]);
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(MessageUtil.getMessage(MessageID.IROB101, "[10-2] wu", idx, wu[idx]));
        }

        // [10-3] SoCは余剰分を加算
        soc[idx] = soc[idx-1] + (-1 * wb[idx] / T) / wc;
        //System.out.println("[10-3] SoCは余剰分を加算: " + soc[idx]);
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(MessageUtil.getMessage(MessageID.IROB101, "[10-3] soc", idx, soc[idx]));
        }

        // [10-4] 過放電なし
        sover[idx] = 0;
        //System.out.println("[10-4] 過放電なし: " + sover[idx]);
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(MessageUtil.getMessage(MessageID.IROB101, "[10-4] sover", idx, sover[idx]));
        }
    }

    /**
     * 充放電シミュレーションの演算式11を行うメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param ResourceBundle シミュレーション設定
     * @param idx 各種データのインデックス
     * @throws Exception 処理中に問題が発生した場合
     */
    private void doCalculations11(ResourceBundle simBundle, int idx) throws Exception {
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(MessageUtil.getMessage(MessageID.IROB094));
        }

        //System.out.println("演算式11を実施");

        // 蓄電池定格容量
        double wc = Double.valueOf(simBundle.getString(AppConstants.S_RESOURCE_KEY_WC));

        // [11-1] 放電電力量 (放電可能量を全量放電)
        wb[idx] = Math.abs(wc * (soc[idx-1] - socisland));
        //System.out.println("[11-1] 放電電力量(放電可能量を全量放電): " + wb[idx]);
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(MessageUtil.getMessage(MessageID.IROB101, "[11-1] wb", idx, wb[idx]));
        }

        // [11-2] 系統から補給不可
        wu[idx] = 0;
        //System.out.println("[11-2] 系統から補給不可: " + wu[idx]);
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(MessageUtil.getMessage(MessageID.IROB101, "[11-2] wu", idx, wu[idx]));
        }

        // [11-3] SoCは放電分を減算 (SoCMINになる)
        soc[idx] = soc[idx-1] + (-1 * wb[idx]) / wc;
        //System.out.println("[11-3] SoCは放電分を減算 (SoCMINになる): " + soc[idx]);
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(MessageUtil.getMessage(MessageID.IROB101, "[11-3] soc", idx, soc[idx]));
        }

        // [11-4] 過放電あり
        sover[idx] = 1;
        //System.out.println("[11-4] 過放電あり: " + sover[idx]);
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(MessageUtil.getMessage(MessageID.IROB101, "[11-4] sover", idx, sover[idx]));
        }
    }

    /**
     * 充放電シミュレーションの演算式12を行うメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param ResourceBundle シミュレーション設定
     * @param idx 各種データのインデックス
     * @throws Exception 処理中に問題が発生した場合
     */
    private void doCalculations12(ResourceBundle simBundle, int idx) throws Exception {
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(MessageUtil.getMessage(MessageID.IROB095));
        }

        //System.out.println("演算式12を実施");

        // 蓄電池定格容量
        double wc = Double.valueOf(simBundle.getString(AppConstants.S_RESOURCE_KEY_WC));
        // 分解能
        double t = Double.valueOf(simBundle.getString(AppConstants.S_RESOURCE_KEY_T));
        // 演算時間単位
        double T = t/60;

        // [12-1] 放電不可
        wb[idx] = 0;
        //System.out.println("[12-1] 放電不可: " + wb[idx]);
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(MessageUtil.getMessage(MessageID.IROB101, "[12-1] wb", idx, wb[idx]));
        }

        // [12-2] 系統から補給不可
        wu[idx] = 0;
        //System.out.println("[12-2] 系統から補給不可: " + wu[idx]);
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(MessageUtil.getMessage(MessageID.IROB101, "[12-2] wu", idx, wu[idx]));
        }

        // [12-3] SoCは放電分を減算 (SoCMINになる)
        soc[idx] = soc[idx-1] + (-1 * wb[idx] / T) / wc;
        //System.out.println("[12-3] SoCは放電分を減算(SoCMINになる): " + soc[idx]);
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(MessageUtil.getMessage(MessageID.IROB101, "[12-3] soc", idx, soc[idx]));
        }

        // [12-4] 過放電あり
        sover[idx] = 1;
        //System.out.println("[12-4] 過放電あり: " + sover[idx]);
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(MessageUtil.getMessage(MessageID.IROB101, "[12-4] sover", idx, sover[idx]));
        }
    }

    /**
     * 充放電シミュレーションの演算式13を行うメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param ResourceBundle シミュレーション設定
     * @param idx 各種データのインデックス
     * @throws Exception 処理中に問題が発生した場合
     */
    private void doCalculations13(ResourceBundle simBundle, int idx) throws Exception {
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(MessageUtil.getMessage(MessageID.IROB096));
        }

        //System.out.println("演算式13を実施");

        // 安全率 (発電)
        double gammag = Double.valueOf(simBundle.getString(AppConstants.S_RESOURCE_KEY_GAMMA_G));
        // 蓄電池定格容量
        double wc = Double.valueOf(simBundle.getString(AppConstants.S_RESOURCE_KEY_WC));
        // 分解能
        double t = Double.valueOf(simBundle.getString(AppConstants.S_RESOURCE_KEY_T));
        // 演算時間単位
        double T = t/60;

        // [13-1] 系統から補給不可
        wu[idx] = 0;
        //System.out.println("[13-1] 系統から補給不可: " + wu[idx]);
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(MessageUtil.getMessage(MessageID.IROB101, "[13-1] wu", idx, wu[idx]));
        }

        // [13-2] 放電電力量 (不足分を全量放電)
        wb[idx] = Math.abs(w2[idx] / kacdc_d[idx] - gammag * kdcdc[idx] * wg[idx]);
        //System.out.println("[13-2] 放電電力量(不足分を全量放電): " + wb[idx]);
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(MessageUtil.getMessage(MessageID.IROB101, "[13-2] wb", idx, wb[idx]));
        }

        // [13-3] SoCは放電分を減算
        soc[idx] = soc[idx-1] + (-1 * wb[idx] / T) / wc;
        //System.out.println("[13-3] SoCは放電分を減算: " + soc[idx]);
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(MessageUtil.getMessage(MessageID.IROB101, "[13-3] soc", idx, soc[idx]));
        }

        // [13-4] 過放電なし
        sover[idx] = 0;
        //System.out.println("[13-4] 過放電なし: " + sover[idx]);
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(MessageUtil.getMessage(MessageID.IROB101, "[13-4] sover", idx, sover[idx]));
        }
    }


    // ■ 演算式14を実施
    private TSimulationResult doCalculations14(ResourceBundle simBundle, int dgLocationId, Timestamp execTS) throws Exception {
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(MessageUtil.getMessage(MessageID.IROB097));
        }

        //System.out.println("演算式14を実施");

        // 蓄電池定格容量
        double wc = Double.valueOf(simBundle.getString(AppConstants.S_RESOURCE_KEY_WC));

        // [14-1] 演算区間nにおけるSOVERの総和 (1以上の場合、演算区間nで過放電が1回以上あるということ)
        int sover2 = 0;
        for (int i=1; i<=AppConstants.S_24HR_SIMDATA_COUNT; i++) {
            sover2 += sover[i];
        }
        //System.out.println("[14-1] 演算区間nにおけるSOVERの総和(1以上の場合、演算区間nで過放電が1回以上あるということ): " + sover2);
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(MessageUtil.getMessage(MessageID.IROB101, "[14-1] sover2", "-", sover2));
        }

        // [14-2] 自家消費率 (1から全需要電力量の内、供給した全商用電力量の割合を減じたもの)
        double sum_wu = 0;
        double sum_w2 = 0;
        for (int i=0; i<AppConstants.S_24HR_SIMDATA_COUNT; i++) {
            sum_wu += wu[i];
            sum_w2 += w2[i];
        }
        double risrand = 1 - sum_wu / sum_w2;
        //System.out.println("[14-2] 自家消費率(1から全需要電力量の内、供給した全商用電力量の割合を減じたもの): " + risland);
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(MessageUtil.getMessage(MessageID.IROB101, "[14-2] sover2", "-", risrand));
        }

        // [14-3] 最低バックアップ容量 (演算区間nの最初のSoC値と最小SoCの差に相当する容量に安全率を乗じたもの)
        double tmpsocmin = soc[1];
        for (int i=2; i<AppConstants.S_24HR_SIMDATA_COUNT; i++) {
            if (tmpsocmin > soc[i]) {
                tmpsocmin = soc[i];
            }
        }
        double wr = wc * (soc[0] - tmpsocmin);
        //System.out.println("[14-3] 最低バックアップ容量(演算区間nの最初のSoC値と最小SoCの差に相当する容量): " + wr);
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(MessageUtil.getMessage(MessageID.IROB101, "[14-3] wr", "-", wr));
        }

        // [14-4] 運用下限変更値
        double socmin2 = wr / wc;
        //System.out.println("[14-4] 運用下限変更値: " + socmin2);
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(MessageUtil.getMessage(MessageID.IROB101, "[14-4] socmin2", "-", socmin2));
        }

        TSimulationResult param = new TSimulationResult();
        param.setDGLocationId(dgLocationId);
        param.setSimulationTime(execTS);
        if (sb == 1) {
            param.setSocModNeed(1);
        } else {
            param.setSocModNeed(0);
        }
        param.setSocMin(socmin2);
        param.setRIsrand((float)risrand);
        param.setWR(wr);
        return(param);
    }



}
