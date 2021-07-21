/*
 * WeatherForecastStore.java
 * Created on 2021/07/13
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
import java.io.File;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.ibatis.session.SqlSession;

import jp.co.nttf.gms.common.internal.AppLogger;
import jp.co.nttf.gms.common.internal.CommonConstants;
import jp.co.nttf.gms.common.internal.CommonInfo;
import jp.co.nttf.gms.common.internal.Configuration;
import jp.co.nttf.gms.common.internal.MessageUtil;
import jp.co.nttf.gms.common.internal.data.gmsdb.TWeatherForecastG;
import jp.co.nttf.gms.common.internal.data.gmsdb.TWeatherForecastGMapper;
import jp.co.nttf.gms.common.internal.data.gmsdb.TWeatherForecastP;
import jp.co.nttf.gms.common.internal.data.gmsdb.TWeatherForecastPMapper;
import jp.co.nttf.gms.common.internal.data.gmsdb.TWeatherLocation;
import jp.co.nttf.gms.common.internal.data.gmsdb.TWeatherLocationMapper;
import jp.co.nttf.gms.robust.batch.internal.AppConstants;
import jp.co.nttf.gms.robust.batch.internal.CommonBatch;
import jp.co.nttf.gms.robust.batch.internal.MessageID;

/**
 * 気象情報の取り込みを行うバッチクラス. <br>
 * <b>【注意】</b> 特になし.
 *
 * @author sekiya
 */
public final class WeatherForecastStore extends CommonBatch {
    /** 共通情報 */
    protected CommonInfo comInfo = CommonInfo.getInstance();

    /** ロガー */
    private static final AppLogger LOGGER = new AppLogger(WeatherForecastStore.class);

    protected String distributionPath = "";
    protected String tempPath = "";

    /**
     * メインメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param args コマンドライン引数(args[0]:処理日時【必須】[YYYYmmddHHMM])
     * @throws Exception 処理中に問題が発生した場合
     */
    public static void main(String[] args) throws Exception {
        // メッセージプロパティをロード
        MessageUtil.setResourceBundle(ResourceBundle
                .getBundle(AppConstants.MESSAGE_RESOURCE_NAME));

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(MessageUtil.getMessage(MessageID.IROB000));
        }

        // 引数の取得
        if(args.length == 0) {
            LOGGER.error(MessageUtil.getMessage(MessageID.EROB002, args.length));
            throw new Exception();
        }
        String argDate = args[0];

        try {
            // 処理を実行
            WeatherForecastStore reporter = new WeatherForecastStore();
            reporter.execute(argDate);

        } catch (Exception e) {
            // ERRORログ出力
            LOGGER.error(MessageUtil.getMessage(MessageID.ACOM000), e);
            throw e;
        }

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(MessageUtil.getMessage(MessageID.IROB001));
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

        // CommonInfoのインスタンスを生成
        CommonInfo comInfo = CommonInfo.getInstance();
        Configuration gmsConfig = comInfo.getGmsConfig();

        // 配信ディレクトリのパス
        distributionPath = gmsConfig.getString(AppConstants.PROPERTY_ROBUST_ROOT_DIRECTORY);
        distributionPath += AppConstants.W_DISTRIBUTION_PATH;
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(MessageUtil.getMessage(MessageID.IROB002, distributionPath));
        }

        // デコード済みデータ格納先ディレクトリのパス
        tempPath = comInfo.getGmsConfig().getString(AppConstants.PROPERTY_ROBUST_ROOT_DIRECTORY);
        tempPath += AppConstants.W_TEMP_PATH;
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(MessageUtil.getMessage(MessageID.IROB003, tempPath));
        }

        // 気象情報位置情報の取得
        List<TWeatherLocation> listWeatherLocation = this.getWeatherLocationList();
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(MessageUtil.getMessage(MessageID.IROB004, listWeatherLocation.size()));
        }

        // 気象位置情報リスト内の気象位置情報毎にループ
        for(TWeatherLocation location : listWeatherLocation) {
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info(MessageUtil.getMessage(MessageID.IROB005, location.getLocationId(), location.getDataType(), location.getLatitudeIndex(), location.getLongitudeIndex()));
            }

            // 処理中の気象位置情報に対応した気象データのデコード要否判定
            String execDate = this.judgeDecodeNecessity(argDate, location);
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info(MessageUtil.getMessage(MessageID.IROB008, execDate));
            }
            // デコード不要
            if(execDate.equals("")) {
                continue;
            }
            // デコード要
            else {
                // weather_decode.pyのパス文字列生成
                String scriptPath = comInfo.getGmsConfig().getString(AppConstants.PROPERTY_ROBUST_ROOT_DIRECTORY);
                scriptPath += AppConstants.W_SCRIPT_PATH;
                if (LOGGER.isInfoEnabled()) {
                    LOGGER.info(MessageUtil.getMessage(MessageID.IROB009, scriptPath));
                }

                // Pythonコマンド[名/パス]取得
                String pythonPath =comInfo.getGmsConfig().getString(AppConstants.PROPERTY_ROBUST_PYTHON_CMD);
                if (LOGGER.isInfoEnabled()) {
                    LOGGER.info(MessageUtil.getMessage(MessageID.IROB010, pythonPath));
                }

                // 前回のデコード結果が残っていれば削除
                File fileTempPath = new File(tempPath);
                String[] tempFilenameList = fileTempPath.list();
                if (tempFilenameList != null) {
                    for(String tempFilename : tempFilenameList) {
                        Path targetPath = Paths.get(tempPath + File.separator + tempFilename);
                        Files.delete(targetPath);
                        if (LOGGER.isInfoEnabled()) {
                            LOGGER.info(MessageUtil.getMessage(MessageID.IROB017, targetPath));
                        }
                    }
                }

                // 気象情報のデコード処理
                ProcessBuilder processBuilder = new ProcessBuilder(pythonPath, scriptPath,
                    execDate, String.valueOf(location.getDataType()), String.valueOf(location.getLatitudeIndex()),
                    String.valueOf(location.getLongitudeIndex())
                );
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

                if (location.getDataType() == AppConstants.W_DATA_TYPE_G) {
                    // デコード処理結果の読み込み
                    String[][] decodedData = this.loadDecodedData(tempPath + File.separator + AppConstants.W_DECODED_FILENAME_G);
                    // 読み込みデータのDB登録
                    this.storeDecodedDataG(location, execDate, decodedData);
                } else if (location.getDataType() == AppConstants.W_DATA_TYPE_P) {
                    // 100～250までは50刻み(4ファイル)、データ項目数は5(相対湿度は出力されない)
                    for(int i=100; i<=250; i+=50) {
                        // デコード処理結果の読み込み
                        String[][] decodedData = this.loadDecodedData(tempPath + File.separator + String.format(AppConstants.W_DECODED_FILENAME_P,i));
                        // 読み込みデータのDB登録
                        this.storeDecodedDataP(location, execDate, decodedData, i);
                    }
                    // 300～800までは100刻み(6ファイル)、データ項目は6
                    for(int i=300; i<=800; i+=100) {
                        // デコード処理結果の読み込み
                        String[][] decodedData = this.loadDecodedData(tempPath + File.separator + String.format(AppConstants.W_DECODED_FILENAME_P,i));
                        // 読み込みデータのDB登録
                        this.storeDecodedDataP(location, execDate, decodedData, i);
                    }
                    // 850～900までは50刻み(2ファイル)、データ項目は6
                    for(int i=850; i<=900; i+=50) {
                        // デコード処理結果の読み込み
                        String[][] decodedData = this.loadDecodedData(tempPath + File.separator + String.format(AppConstants.W_DECODED_FILENAME_P,i));
                        // 読み込みデータのDB登録
                        this.storeDecodedDataP(location, execDate, decodedData, i);
                    }
                    // 925～1000までは25刻み(4ファイル)、データ項目は6
                    for(int i=925; i<=1000; i+=25) {
                        // デコード処理結果の読み込み
                        String[][] decodedData = this.loadDecodedData(tempPath + File.separator + String.format(AppConstants.W_DECODED_FILENAME_P,i));
                        // 読み込みデータのDB登録
                        this.storeDecodedDataP(location, execDate, decodedData, i);
                    }
                }
            }
        }
    }

    /**
     * 気象位置情報の全件取得を行うメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @return 気象位置情報のリスト
     * @throws Exception 処理中に問題が発生した場合
     */
    private List<TWeatherLocation> getWeatherLocationList() throws Exception {
        List<TWeatherLocation> list = new ArrayList<TWeatherLocation>();

        SqlSession session = null;

        try {
            // DBセッションを取得
            session = this.comInfo.getSqlSessionFactory(
                    CommonConstants.ENVIRONMENT_GMSDB).openSession();

            // SQL実行
            TWeatherLocationMapper mapper = session.getMapper(TWeatherLocationMapper.class);
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
     * デコードが必要か判別を行うメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param argDate メインバッチから渡された基準となる処理日時
     * @param location 処理対象の気象位置情報
     * @return デコード対象の配信ファイルの(ファイル名の)日時(※ デコード不要の場合は空文字列を返却)
     * @throws Exception 処理中に問題が発生した場合
     */
    private String judgeDecodeNecessity(String argDate, TWeatherLocation location) throws Exception {
        // 配信ディレクトリの中から(名前が)最新の配信ファイルを見つけ、その日時を取得
        String latestDistDate = this.getLatestDistributionDate(argDate, location);
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(MessageUtil.getMessage(MessageID.IROB006, latestDistDate));
        }
        if (latestDistDate.equals("")) {
            return("");
        }

        // 検索パラメータとして使用するためにTimestampを生成
        Timestamp latestDistTS = Timestamp.valueOf(
                latestDistDate.substring(0,4)     + "-"
                + latestDistDate.substring(4,6)   + "-"
                + latestDistDate.substring(6,8)   + " "
                + latestDistDate.substring(8,10)  + ":"
                + latestDistDate.substring(10,12) + ":00");

        SqlSession session = null;
        int count = 0;

        // 最新の配信データが既に格納されているか確認
        try {
            // DBセッションを取得
            session = this.comInfo.getSqlSessionFactory(
                    CommonConstants.ENVIRONMENT_GMSDB).openSession();

            // SQL実行
            if (location.getDataType() == AppConstants.W_DATA_TYPE_G) {
                TWeatherForecastGMapper mapper = session.getMapper(TWeatherForecastGMapper.class);
                count = mapper.countDataInDistribution(latestDistTS);
            } else if (location.getDataType() == AppConstants.W_DATA_TYPE_P) {
                TWeatherForecastPMapper mapper = session.getMapper(TWeatherForecastPMapper.class);
                count = mapper.countDataInDistribution(latestDistTS);
            }
        } finally {
            // セッションをクローズ
            if (null != session) {
                session.close();
            }
        }

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(MessageUtil.getMessage(MessageID.IROB007, count));
        }
        // 地上面かつ51時間分、全て登録済みならデコード不要
        if (location.getDataType() == AppConstants.W_DATA_TYPE_G && count >= AppConstants.W_DATA_ROWS) {
            return("");
        }
        // 気圧面かつ51時間分、全ファイル(16ファイル)分が全て登録済みならデコード不要
        else if (location.getDataType() == AppConstants.W_DATA_TYPE_P && count >= AppConstants.W_DATA_ROWS * 16) {
            return("");
        }
        else {
            return(latestDistDate);
        }
    }

    /**
     * 最新の配信ファイルの日時取得を行うメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param argDate メインバッチから渡された基準となる処理日時 (※ 基準日時よりも前、かつ最新の日時の取得を行う)
     * @param location 処理対象の気象位置情報
     * @return デコード対象の配信ファイルの(ファイル名の)日時(※ デコード不要の場合は空文字列を返却)
     * @throws Exception 処理中に問題が発生した場合
     */
    private String getLatestDistributionDate(String argDate, TWeatherLocation location) throws Exception {
        // 処理中の気象情報位置データのデータ種別を取得
        int data_type = location.getDataType();
        // 気象情報配信ディレクトリのファイルリストを取得
        File fileDistributionPath = new File(distributionPath);
        String[] distributionFilenameList = fileDistributionPath.list();
        // 配信日時リスト
        List<String> distributionTimestamp = new ArrayList<String>();

        // 地上面/気圧面の判別を行い、配信ファイル名の正規表現パターンを生成
        Pattern p = null;
        if (data_type == 0) {
            p = Pattern.compile(AppConstants.W_PATTERN_DISTRIBUTION_FILENAME_G);
        } else if (data_type == 1) {
            p = Pattern.compile(AppConstants.W_PATTERN_DISTRIBUTION_FILENAME_P);
        } else {
            return("");
        }

        // 配信ディレクトリ内のファイル毎に実施
        for (String distributionFilename : distributionFilenameList) {
            // 対象のデータ種別の配信ファイルなら、ファイル名内のタイムスタンプをリストに追加
            Matcher m = p.matcher(distributionFilename);
            if(m.find()) {
                String fileDate = m.group(1);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
                // 実行パラメータで渡された日時よりも新しいファイルは候補から外す
                // (古いファイルのみを追加)
                if (sdf.parse(fileDate).getTime() <= sdf.parse(argDate).getTime()) {
                    distributionTimestamp.add(m.group(1));
                }
            }
        }
        Collections.sort(distributionTimestamp, Collections.reverseOrder());
        return(distributionTimestamp.get(0));
    }

    /**
     * デコード済みデータの読み込みを行うメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param targetFilename 読み込み対象のデコード済みデータファイル
     * @return 読み込んだデコード済みデータ
     * @throws Exception 処理中に問題が発生した場合
     */
    private String[][] loadDecodedData(String targetFilename) throws Exception {
        BufferedReader br = null;
        String[][] data = new String[AppConstants.W_DATA_ROWS][AppConstants.W_DATA_COLS];

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(MessageUtil.getMessage(MessageID.IROB011, targetFilename));
        }

        try {
            // デコード済みデータファイルのbr生成
            File file = new File(targetFilename);
            br = new BufferedReader(new FileReader(file));

            int index = 0;
            String line = "";

            // デコード済みデータファイルを1行ずつ読み込み
            while ((line = br.readLine()) != null) {
                // csvファイルなので、行の内容をカンマで分割して気象情報データを配列に格納
                data[index] = line.split(",");
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
        return(data);
    }

    /**
     * 気象情報(地上面)のDB登録を行うメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param location 処理対象の気象位置情報
     * @param execDate 処理日時(処理対象の配信日時)
     * @param data 読み込んだデコード済みデータ
     * @throws Exception 処理中に問題が発生した場合
     */
    private void storeDecodedDataG(TWeatherLocation location, String execDate, String data[][]) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
        Timestamp distTS  = new Timestamp(sdf.parse(execDate).getTime());

        for (int i=0; i<AppConstants.W_DATA_ROWS; i++) {
            // (i+1)時間をミリ秒に変換したものを加算し、予報日時を算出
            Timestamp foreTS  = new Timestamp(sdf.parse(execDate).getTime() + ((i+1) * 60 * 60 * 1000));

            // 登録対象のパラメータをセット
            TWeatherForecastG param = new TWeatherForecastG();
            // 配信日時を設定
            param.setDistributionTime(distTS);
            // 位置情報IDを設定
            param.setLocationId(location.getLocationId());
            // 予報日時を設定
            param.setForecastTime(foreTS);
            // 地上気圧を設定
            param.setSurfacePressure(Double.valueOf(data[i][AppConstants.W_DECODED_DATA_IDX_G_SP]));
            // 風ベクトルUを設定
            param.setUWind(Double.valueOf(data[i][AppConstants.W_DECODED_DATA_IDX_G_UW]));
            // 風ベクトルVを設定
            param.setVWind(Double.valueOf(data[i][AppConstants.W_DECODED_DATA_IDX_G_VW]));
            // 気温を設定
            param.setTemperature(Float.valueOf(data[i][AppConstants.W_DECODED_DATA_IDX_G_TEMP]));
            // 相対湿度を設定
            param.setRelativeHumidity(Float.valueOf(data[i][AppConstants.W_DECODED_DATA_IDX_G_RH]));
            // 下層雲量を設定
            param.setLowCloudCover(Float.valueOf(data[i][AppConstants.W_DECODED_DATA_IDX_G_LCC]));
            // 中層雲量を設定
            param.setMediumCloudCover(Float.valueOf(data[i][AppConstants.W_DECODED_DATA_IDX_G_MCC]));
            // 上層雲量を設定
            param.setHighCloudCover(Float.valueOf(data[i][AppConstants.W_DECODED_DATA_IDX_G_HCC]));
            // 全雲量を設定
            param.setTotalCloudCover(Float.valueOf(data[i][AppConstants.W_DECODED_DATA_IDX_G_TCC]));
            // 降水量を設定
            param.setPrecipitation(Double.valueOf(data[i][AppConstants.W_DECODED_DATA_IDX_G_TP]));
            // 日射量を設定
            param.setSunshine(Double.valueOf(data[i][AppConstants.W_DECODED_DATA_IDX_G_DSWRF]));
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info(MessageUtil.getMessage(MessageID.IROB013));
            }

            SqlSession session = null;

            try {
                // DBセッションを取得
                session = this.comInfo.getSqlSessionFactory(
                        CommonConstants.ENVIRONMENT_GMSDB).openSession();

                // SQL実行
                TWeatherForecastGMapper mapper = session.getMapper(TWeatherForecastGMapper.class);
                mapper.insert(param);

                // コミット
                session.commit();
                if (LOGGER.isInfoEnabled()) {
                    LOGGER.info(MessageUtil.getMessage(MessageID.IROB014));
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
     * 気象情報(気圧面)のDB登録を行うメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param location 処理対象の気象位置情報
     * @param execDate 処理日時(処理対象の配信日時)
     * @param data 読み込んだデコード済みデータ
     * @param pressureLevel 処理対象の気圧面
     * @throws Exception 処理中に問題が発生した場合
     */
    private void storeDecodedDataP(TWeatherLocation location, String execDate, String data[][], int pressureLevel) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
        Timestamp distTS  = new Timestamp(sdf.parse(execDate).getTime());

        for (int i=0; i<AppConstants.W_DATA_ROWS; i++) {
            // (i+1)時間をミリ秒に変換したものを加算し、予報日時を算出
            Timestamp foreTS  = new Timestamp(sdf.parse(execDate).getTime() + ((i+1) * 60 * 60 * 1000));

            // 登録対象のパラメータをセット
            TWeatherForecastP param = new TWeatherForecastP();
            // 配信日時を設定
            param.setDistributionTime(distTS);
            // 位置情報IDを設定
            param.setLocationId(location.getLocationId());
            // 気圧面を設定
            param.setPressureLevel(pressureLevel);
            // 予報日時を設定
            param.setForecastTime(foreTS);
            // ジオポテンシャル高度を設定
            param.setGeopotentialHeight(Double.valueOf(data[i][AppConstants.W_DECODED_DATA_IDX_P_GH]));
            // 風ベクトルUを設定
            param.setUWind(Double.valueOf(data[i][AppConstants.W_DECODED_DATA_IDX_P_UW]));
            // 風ベクトルVを設定
            param.setVWind(Double.valueOf(data[i][AppConstants.W_DECODED_DATA_IDX_P_VW]));
            // 気温を設定
            param.setTemperature(Float.valueOf(data[i][AppConstants.W_DECODED_DATA_IDX_P_TEMP]));
            // 相対湿度は存在している気圧面でのみ登録
            if (!data[i][AppConstants.W_DECODED_DATA_IDX_P_RH].equals("")) {
                // 相対湿度を設定
                param.setRelativeHumidity(Float.valueOf(data[i][AppConstants.W_DECODED_DATA_IDX_P_RH]));
            }
            // 上昇流を設定
            param.setUpdraft(Double.valueOf(data[i][AppConstants.W_DECODED_DATA_IDX_P_UD]));
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info(MessageUtil.getMessage(MessageID.IROB015));
            }

            SqlSession session = null;

            try {
                // DBセッションを取得
                session = this.comInfo.getSqlSessionFactory(
                        CommonConstants.ENVIRONMENT_GMSDB).openSession();

                // SQL実行
                TWeatherForecastPMapper mapper = session.getMapper(TWeatherForecastPMapper.class);
                mapper.insert(param);

                // コミット
                session.commit();
                if (LOGGER.isInfoEnabled()) {
                    LOGGER.info(MessageUtil.getMessage(MessageID.IROB016));
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
