/*
 * AppConstants.java
 * Created on 2014/12/09
 * RCSID: $Id$
 *
 * (C) Copyright NTT-F 2014 All rights reserved.
 *
 * This software is furnished under a contract and use, duplication, disclosure
 * and all other uses are restricted to the rights specified in the written
 * contract and memorandum between the contractor and NTT-F.
 */
package jp.co.nttf.gms.robust.batch.internal;

import java.io.File;

import jp.co.nttf.gms.common.internal.CommonConstants;

/**
 * 定数クラス. <br>
 * <b>【注意】</b> 特になし.
 *
 * @author naya
 */
public final class AppConstants implements CommonConstants {

    /** プロパティキー：OpenADRレポート対象連携ファイルのパス */
    public static final String PROPERTY_DSP_FCEMS_REPORT_TARGET_FILE = "dsp.fcems.report.target.file";
    /** プロパティキー：リソースID(エリア情報)のリスト */
    public static final String PROPERTY_DSP_FCEMS_RESOURCEID_LIST = "dsp.fcems.resourceid.list";

    /** プロパティキー：GMS-ID */
    public static final String PROPERTY_FCEMS_REPORT_GMSID = "fcems.report.gmsid";
    /** プロパティキー：レポート対象電力の接尾辞 */
    public static final String PROPERTY_FCEMS_REPORT_TARGET_POWER_SUFFIX = "fcems.report.target.power.suffix";
    /** プロパティキー：レポート対象電力量の接尾辞 */
    public static final String PROPERTY_FCEMS_REPORT_TARGET_ENERGY_SUFFIX = "fcems.report.target.energy.suffix";

    /** プロパティキー：レポート間隔[分] */
    public static final String PROPERTY_FCEMS_REPORT_INTERVAL = "fcems.report.interval";
    /** プロパティキー：レポートの文字コード */
    public static final String PROPERTY_FCEMS_REPORT_CHARSET = "fcems.report.charset";
    /** プロパティキー：計測値の最大要素数 */
    public static final String PROPERTY_FCEMS_REPORT_MEASUREMENT_MAXELEMENT = "fcems.report.measurement.maxelement";
    /** プロパティキー：予測値の最大要素数 */
    public static final String PROPERTY_FCEMS_REPORT_PREDICTION_MAXELEMENT = "fcems.report.prediction.maxelement";

    /** プロパティキー：DRベースライン算出オフセット[分] */
    public static final String PROPERTY_FCEMS_BASELINE_OFFSET = "fcems.baseline.offset";
    /** プロパティキー：DRベースライン算出期間[分] */
    public static final String PROPERTY_FCEMS_BASELINE_INTERVAL = "fcems.baseline.interval";
    /** プロパティキー：DR実績値算出期間[分] */
    public static final String PROPERTY_FCEMS_ACTUAL_INTERVAL = "fcems.actual.interval";
    /** プロパティキー：電力量検索時の前後余白[秒] */
    public static final String PROPERTY_FCEMS_ENERGY_FIND_MARGIN = "fcems.energy.find.margin";
    /** プロパティキー：実績データ算出時のGMU-IDペアマップ([配信用GMU-ID]-[算出用GMU-ID]) */
    public static final String PROPERTY_FCEMS_DREFFECT_TARGET_GMUPAIR = "fcems.dreffect.target.gmupair";
    /** プロパティキー：実績データ算出対象の計測ポイントIDリスト */
    public static final String PROPERTY_FCEMS_DREFFECT_TARGET_MEASUREPOINT = "fcems.dreffect.target.measurepoint";
    /** プロパティキー：DR効果量出力先ディレクトリ */
    public static final String PROPERTY_FCEMS_DREFFECT_DIR = "fcems.dreffect.dir";
    /** プロパティキー：DR効果量出力ファイル名 */
    public static final String PROPERTY_FCEMS_DREFFECT_FILENAME = "fcems.dreffect.filename";
    /** プロパティキー：実績データレポート時の間隔[分] */
    public static final String PROPERTY_FCEMS_DREFFECT_REPORT_INTERVAL = "fcems.dreffect.report.interval";

    /** プロパティキー：GMUと蓄電池残容量のマッピング([配信用GMU-ID]:[蓄電池残容量計測ポイントID]) */
    public static final String PROPERTY_FCEMS_REMAINING_CAPACITY_MAPPING = "fcems.remaining.capacity.mapping";

    /** DR達成逸脱チェック下限値[%] */
    public static final String PROPERTY_FCEMS_DEVIATION_LOWERLIMIT = "fcems.deviation.lowerlimit";
    /** DR達成逸脱チェック上限値[%] */
    public static final String PROPERTY_FCEMS_DEVIATION_UPPERLIMIT = "fcems.deviation.upperlimit";

    /** 分散DR発行間隔[分] */
    public static final String PROPERTY_FCEMS_VARIANCEDR_INTERVAL = "fcems.variancedr.interval";

    /** プロパティキー：節電容量配分最大値[kW] */
    public static final String PROPERTY_FCEMS_ALLOCATION_CAPACITY_MAX = "fcems.allocation.capacity.max";

    /** 電力量の前回値ファイル */
    public static final String LAST_ENERGY_FILE = "last_energy.properties";

    /** 1ユニットの分数 */
    public static final int MINUTES_OF_UNIT = 30;

    /** 1ユニットのフレーム数 */
    public static final int FRAMES_OF_UNIT = 6;

    /** プロパティキー：接点連携制御バッチ設定ファイルパス */
    public static final String PROPERTY_STATUSNOTICEPOINTLINK_CONFIG_FILENAME = "fcems.contactcooperation.config.filename";

    /** 制御開始メールDB接続リトライ間隔 */
    public static final String DSP_SCHEDULE_MAIL_DB_RETRY_INTERVAL = "dsp.schedule.mail.db_retry_interval";

    /** 制御開始メールDB接続リトライ回数 */
    public static final String DSP_SCHEDULE_MAIL_DB_RETRY_COUNT = "dsp.schedule.mail.db_retry_count";

    /** 制御開始メールリトライ間隔 */
    public static final String DSP_SCHEDULE_MAIL_RETRY_INTERVAL = "dsp.schedule.mail.retry_interval";

    /** 制御開始メールリトライ回数 */
    public static final String DSP_SCHEDULE_MAIL_RETRY_COUNT = "dsp.schedule.mail.retry_count";

    /** 制御開始メール送信元メールアドレス */
    public static final String DSP_SCHEDULE_MAIL_SOURCE_ADDRESS = "dsp.schedule.mail.source_address";

    /** 制御開始メール送信元名称 */
    public static final String DSP_SCHEDULE_MAIL_SOURCE_NAME = "dsp.schedule.mail.source_name";

    /** メール送信プロトコル */
    public static final String DSP_MAIL_PROTOCOL = "dsp.mail.protocol";

    /** メール送信用SMTPサーバのホスト名 */
    public static final String DSP_MAIL_HOST = "dsp.mail.host";

    /** メール送信用SMTPサーバのSMTPポート番号 */
    public static final String DSP_MAIL_SMTP_PORT = "dsp.mail.smtp.port";

    /** メール送信用SMTPサーバのPOP3ポート番号 */
    public static final String DSP_MAIL_POP3_PORT = "dsp.mail.pop3.port";

    /** 制御開始メール送信タイムアウト */
    public static final String DSP_SCHEDULE_MAIL_TIMEOUT = "dsp.schedule.mail.timeout";

    /** メール送信用SMTPサーバの接続ユーザ名 */
    public static final String DSP_MAIL_AUTH_USER = "dsp.mail.auth.user";

    /** メール送信用SMTPサーバの接続パスワード */
    public static final String DSP_MAIL_AUTH_PASSWORD = "dsp.mail.auth.password";

    /** メール送信キャラクターコード */
    public static final String DSP_MAIL_CHARCODE = "dsp.mail.charcode";

    /** 制御開始メール(CEMSDR)送信タイミング */
    public static final String DSP_SCHEDULE_MAIL_CEMSDR_TIME = "dsp.schedule.mail.cemsdr_time";

    /** 制御開始メール(設定反映)送信タイミング */
    public static final String DSP_SCHEDULE_MAIL_SETTING_TIME = "dsp.schedule.mail.setting_time";

    /** 制御開始メール(制御テスト)送信タイミング */
    public static final String DSP_SCHEDULE_MAIL_TEST_TIME = "dsp.schedule.mail.test_time";

    /** 制御開始メール(一括登録)送信タイミング */
    public static final String DSP_SCHEDULE_MAIL_BULK_TIME = "dsp.schedule.mail.bulk_time";

    /** 制御開始メール(接点連携制御)送信タイミング */
    public static final String DSP_SCHEDULE_MAIL_POINT_TIME = "dsp.schedule.mail.point_time";

    /** 制御開始メール(再エネ制御)送信タイミング */
    public static final String DSP_SCHEDULE_MAIL_REENERGY_TIME = "dsp.schedule.mail.reenergy_time";

    /** 制御開始メール(FW再起動)送信タイミング */
    public static final String DSP_SCHEDULE_MAIL_FW_TIME = "dsp.schedule.mail.fw_time";

    /** 制御開始メール(DR拒否)送信タイミング */
    public static final String DSP_SCHEDULE_MAIL_REFUSAL_TIME = "dsp.schedule.mail.refusal_time";

    /** 制御メール(CEMSDR)件名 */
    public static final String DSP_SCHEDULE_MAIL_CEMSDR_SUBJECT = "dsp.schedule.mail.cemsdr_subject";

    /** 制御メール(設定反映)件名 */
    public static final String DSP_SCHEDULE_MAIL_SETTING_SUBJECT = "dsp.schedule.mail.setting_subject";

    /** 制御メール(制御テスト)件名 */
    public static final String DSP_SCHEDULE_MAIL_TEST_SUBJECT = "dsp.schedule.mail.test_subject";

    /** 制御メール(一括登録)件名 */
    public static final String DSP_SCHEDULE_MAIL_BULK_SUBJECT = "dsp.schedule.mail.bulk_subject";

    /** 制御メール(接点連携制御)件名 */
    public static final String DSP_SCHEDULE_MAIL_POINT_SUBJECT = "dsp.schedule.mail.point_subject";

    /** 制御メール(再エネ制御)件名 */
    public static final String DSP_SCHEDULE_MAIL_REENERGY_SUBJECT = "dsp.schedule.mail.reenergy_subject";

    /** 制御メール(FW再起動)件名 */
    public static final String DSP_SCHEDULE_MAIL_FW_SUBJECT = "dsp.schedule.mail.fw_subject";

    /** 制御メール(DR拒否)件名 */
    public static final String DSP_SCHEDULE_MAIL_REFUSAL_SUBJECT = "dsp.schedule.mail.refusal_subject";

    /** 制御メール(CEMSDR)本文 */
    public static final String DSP_SCHEDULE_MAIL_CEMSDR_TEXT = "dsp.schedule.mail.cemsdr_text";

    /** 制御メール(設定反映)本文 */
    public static final String DSP_SCHEDULE_MAIL_SETTING_TEXT = "dsp.schedule.mail.setting_text";

    /** 制御メール(制御テスト)本文 */
    public static final String DSP_SCHEDULE_MAIL_TEST_TEXT = "dsp.schedule.mail.test_text";

    /** 制御メール(一括登録)本文 */
    public static final String DSP_SCHEDULE_MAIL_BULK_TEXT = "dsp.schedule.mail.bulk_text";

    /** 制御メール(接点連携制御)本文 */
    public static final String DSP_SCHEDULE_MAIL_POINT_TEXT = "dsp.schedule.mail.point_text";

    /** 制御メール(再エネ制御)本文 */
    public static final String DSP_SCHEDULE_MAIL_REENERGY_TEXT = "dsp.schedule.mail.reenergy_text";

    /** 制御メール(FW再起動)本文 */
    public static final String DSP_SCHEDULE_MAIL_FW_TEXT = "dsp.schedule.mail.fw_text";

    /** 制御メール(DR拒否)本文 */
    public static final String DSP_SCHEDULE_MAIL_REFUSAL_TEXT = "dsp.schedule.mail.refusal_text";

    /** 制御開始メール送信スレッドの上限数 */
    public static final String DSP_SCHEDULE_MAIL_THREAD_NUM = "dsp.schedule.mail.thread.num";

    /** 再エネ比率バッチ戻し制御の開始時刻間隔 */
    public static final String DSP_SCHEDULE_REENERGY_CHECK_CONTROL_INTERVAL = "dsp.schedule.reenergy_check.control.interval";


    /** 以降、ロバスト制御用 **/
    /** 処理結果(正常) */
    public static final int ROBUST_RESULT_OK = 0;

    /** 処理結果(異常) */
    public static final int ROBUST_RESULT_NG = 1;

    /** 1週間のミリ秒表記 **/
    public static final int ROBUST_1WEEK_IN_MILLISEC = 7 * 24 * 60 * 60 * 1000;

    /** 30分のミリ秒表記 **/
    public static final int ROBUST_30MIN_IN_MILLISEC = 30 * 60 * 1000;

    /** 1時間のミリ秒表記 **/
    public static final int ROBUST_1HR_IN_MILLISEC = 60 * 60 * 1000;

    /** 24時間のミリ秒表記 **/
    public static final int ROBUST_24HR_IN_MILLISEC = 24 * 60 * 60 * 1000;

    /** 48時間のミリ秒表記 **/
    public static final int ROBUST_48HR_IN_MILLISEC = 48 * 60 * 60 * 1000;

    /** 30分の時間表記 **/
    public static final double ROBUST_30MIN_IN_HOUR = 0.5;

    /** 瞬時値の積算フラグ(totalflag) **/
    public static final int ROBUST_TOTALFLAG_MOMENT = 0;

    /** 積算データの積算フラグ(totalflag) **/
    public static final int ROBUST_TOTALFLAG_INTEGRATED = 1;

    /** デマンド電力の積算フラグ(totalflag) **/
    public static final int ROBUST_TOTALFLAG_DEMAND = 2;

    /** 制御配信モード(接点連携制御) **/
    public static final int ROBUST_M_DELIVERY_MODE = 103;

    /** 制御配信ステータス(即時) **/
    public static final int ROBUST_M_DELIVERY_STATUS = 1;

    /** SoC下限値変更が必要 **/
    public static final int ROBUST_NEED_SOC_MODIFICATION = 1;

    /** プロパティキー：ロバスト制御実証用システムルートディレクトリ */
    public static final String PROPERTY_ROBUST_ROOT_DIRECTORY = "robust.root.directory";

    /** プロパティキー：ロバスト制御実証用システムPythonコマンド */
    public static final String PROPERTY_ROBUST_PYTHON_CMD = "robust.python.cmd";

    /** プロパティキー：ロバスト制御実証用システム需要予測算出バッチファイルのパス */
    public static final String PROPERTY_ROBUST_DEMAND_FORECAST_SCRIPT = "robust.demand.forecast.script";

    /** プロパティキー：ロバスト制御実証用システム 制御配信 デバイス名 */
    public static final String PROPERTY_ROBUST_DELIVERY_DEVICE_NAME = "robust.delivery.device.name";

    /** プロパティキー：ロバスト制御実証用システム 制御配信 プロパティ */
    public static final String PROPERTY_ROBUST_DELIVERY_DEVICE_PROPERTY = "robust.delivery.device.property";

    /** ルートディレクトリから配信ディレクトリへのパス **/
    public static final String W_DISTRIBUTION_PATH    = File.separator + "weather" + File.separator + "data" + File.separator + "distribution";

    /** ルートディレクトリからデコード済みデータ格納先ディレクトリへのパス **/
    public static final String W_TEMP_PATH             = File.separator + "weather" + File.separator + "data" + File.separator + "temp";

    /** ルートディレクトリからweather_decode.pyスクリプトへのパス **/
    public static final String W_SCRIPT_PATH           = File.separator + "weather" + File.separator + "module" + File.separator + "weather_decode.py";

    /** weather_decode.pyスクリプトの実行完了待ちタイムアウト(秒) **/
    public static final int W_SCRIPT_TIMEOUT = 10 * 60;

    /** 正規表現パターン気象情報配信ファイル名(地上面) **/
    public static final String W_PATTERN_DISTRIBUTION_FILENAME_G = "Z__C_RJTD_(\\d{12})00_MSM_GPV_Rjp_Lsurf_FH40-51_grib2.bin";

    /** 正規表現パターン気象情報配信ファイル名(気圧面) **/
    public static final String W_PATTERN_DISTRIBUTION_FILENAME_P = "Z__C_RJTD_(\\d{12})00_MSM_GPV_Rjp_L-pall_FH42-51_grib2.bin";

    /** デコード済みデータファイル名 (地上面) **/
    public static final String W_DECODED_FILENAME_G = "g.csv";

    /** デコード済みデータファイル名 (気圧面) **/
    public static final String W_DECODED_FILENAME_P = "p%04d.csv";

    /** 地上面を表すデータ種別 **/
    public static final int W_DATA_TYPE_G = 0;

    /** 気圧面を表すデータ種別 **/
    public static final int W_DATA_TYPE_P = 1;

    /** 取得対象の気象情報のデータ種の数 **/
    public static final int W_DATA_COLS = 12;

    /** 1回あたりの配信データ数(時間) **/
    public static final int W_DATA_ROWS = 51;

    /** 気象情報(地上面)の配信データ間隔(時間) **/
    public static final int W_DATA_INTERVAL_G = 1;

    /** 気象情報(気圧面)の配信データ間隔(時間) **/
    public static final int W_DATA_INTERVAL_P = 3;

    /** 気象情報(地上面)のデコードデータ順(海面更正気圧) **/
    public static final int W_DECODED_DATA_IDX_G_SLP     = 0;

    /** 気象情報(地上面)のデコードデータ順(地上気圧) **/
    public static final int W_DECODED_DATA_IDX_G_SP     = 1;

    /** 気象情報(地上面)のデコードデータ順(風ベクトルU) **/
    public static final int W_DECODED_DATA_IDX_G_UW     = 2;

    /** 気象情報(地上面)のデコードデータ順(風ベクトルV) **/
    public static final int W_DECODED_DATA_IDX_G_VW     = 3;

    /** 気象情報(地上面)のデコードデータ順(気温) **/
    public static final int W_DECODED_DATA_IDX_G_TEMP   = 4;

    /** 気象情報(地上面)のデコードデータ順(相対湿度) **/
    public static final int W_DECODED_DATA_IDX_G_RH     = 5;

    /** 気象情報(地上面)のデコードデータ順(下層雲量) **/
    public static final int W_DECODED_DATA_IDX_G_LCC    = 6;

    /** 気象情報(地上面)のデコードデータ順(中層雲量) **/
    public static final int W_DECODED_DATA_IDX_G_MCC    = 7;

    /** 気象情報(地上面)のデコードデータ順(上層雲量) **/
    public static final int W_DECODED_DATA_IDX_G_HCC    = 8;

    /** 気象情報(地上面)のデコードデータ順(全雲量) **/
    public static final int W_DECODED_DATA_IDX_G_TCC    = 9;

    /** 気象情報(地上面)のデコードデータ順(降水量) **/
    public static final int W_DECODED_DATA_IDX_G_TP     = 10;

    /** 気象情報(地上面)のデコードデータ順(日射量) **/
    public static final int W_DECODED_DATA_IDX_G_DSWRF  = 11;

    /** 気象情報(気圧面)のデコードデータ順(ジオポテンシャル高度) **/
    public static final int W_DECODED_DATA_IDX_P_GH     = 0;

    /** 気象情報(気圧面)のデコードデータ順(風ベクトルU) **/
    public static final int W_DECODED_DATA_IDX_P_UW     = 1;

    /** 気象情報(気圧面)のデコードデータ順(風ベクトルV) **/
    public static final int W_DECODED_DATA_IDX_P_VW     = 2;

    /** 気象情報(気圧面)のデコードデータ順(気温) **/
    public static final int W_DECODED_DATA_IDX_P_TEMP   = 3;

    /** 気象情報(気圧面)のデコードデータ順(相対湿度) **/
    public static final int W_DECODED_DATA_IDX_P_RH     = 4;

    /** 気象情報(気圧面)のデコードデータ順(上昇流) **/
    public static final int W_DECODED_DATA_IDX_P_UD     = 5;

    /** 予測設定ファイルのリソース名 **/
    public static final String F_RESOURCE_NAME = "forecast";
    /** 予測設定ファイルのキー名 (データチェックのリトライ回数) **/
    public static final String F_RESOURCE_KEY_RETRYCOUNT = "datacheck_retrycnt";
    /** 予測設定ファイルのキー名 (データチェックのリトライ間隔) **/
    public static final String F_RESOURCE_KEY_RETRYINTERVAL = "datacheck_retryinterval";
    /** 予測設定ファイルのキー名 (第1係数) **/
    public static final String F_RESOURCE_KEY_COEFFICIENT1 = "coefficient1";
    /** 予測設定ファイルのキー名 (第2係数) **/
    public static final String F_RESOURCE_KEY_COEFFICIENT2 = "coefficient2";
    /** 予測設定ファイルのキー名 (第3係数) **/
    public static final String F_RESOURCE_KEY_COEFFICIENT3 = "coefficient3";
    /** 予測設定ファイルのキー名 (第4係数) **/
    public static final String F_RESOURCE_KEY_COEFFICIENT4 = "coefficient4";
    /** 予測設定ファイルのキー名 (第5係数) **/
    public static final String F_RESOURCE_KEY_COEFFICIENT5 = "coefficient5";
    /** 予測設定ファイルのキー名 (第6係数) **/
    public static final String F_RESOURCE_KEY_COEFFICIENT6 = "coefficient6";
    /** 予測設定ファイルのキー名 (第7係数) **/
    public static final String F_RESOURCE_KEY_COEFFICIENT7 = "coefficient7";
    /** 予測設定ファイルのキー名 (予測ステップ数) **/
    public static final String F_RESOURCE_KEY_STEPS = "steps";

    /** 1週間分(30分間隔)の需要電力の抽出データ数 **/
    public static final int D_1WEEK_EXTRACTDATA_COUNT = 24 * 2 * 7;

    /** 48時間分(1時間間隔)の需要電力の抽出データ数 **/
    public static final int G_48HR_EXTRACTDATA_COUNT = 24 * 2;

    /** 48時間分(1時間間隔)の気象情報データ数 **/
    public static final int G_48HR_WEATHERDATA_COUNT = 24 * 2;

    /** ルートディレクトリから一時データファイル(発電予測への入力)へのパス **/
    public static final String G_TEMP_DATAFILE_I = File.separator + "generation" + File.separator + "data" + File.separator + "temp" + File.separator + "input.csv";

    /** ルートディレクトリから一時データファイル(発電予測からの出力)へのパス **/
    public static final String G_TEMP_DATAFILE_O = File.separator + "generation" + File.separator + "data" + File.separator + "temp" + File.separator + "output.csv";

    /** 一時データファイル(発電予測への入力)のタイトル行 **/
    public static final String G_TEMP_DATAFILE_HEADER_I    = "date,surface pressure,10 metre U wind component,10 metre V wind component,temperature,relative humidity,low cloud cover,medium cloud cover,high cloud cover,total cloud cover,rain,solar,gain";

    /** ルートディレクトリから発電予測算出モジュールスクリプトへのパス **/
    public static final String G_MODULE_PATH = File.separator + "generation" + File.separator + "module" + File.separator + "generation_forecast.py";

    /** 需要予測算出バッチの実行完了待ちタイムアウト(秒) **/
    public static final int D_BATCH_TIMEOUT = 10 * 60;

    /** シミュレーション設定ファイルのリソース名 **/
    public static final String S_RESOURCE_NAME = "simulation";

    /** シミュレーション設定ファイルのキー名 (ピークカット設定) **/
    public static final String S_RESOURCE_KEY_P_PEAK = "p_peak";

    /** シミュレーション設定ファイルのキー名 (安全率(需要)) **/
    public static final String S_RESOURCE_KEY_GAMMA_D = "saferate_d";

    /** シミュレーション設定ファイルのキー名 (安全率(発電)) **/
    public static final String S_RESOURCE_KEY_GAMMA_G = "saferate_g";

    /** シミュレーション設定ファイルのキー名 (MPPTユニットの定格容量) **/
    public static final String S_RESOURCE_KEY_P_DCDC = "p_dcdc";

    /** シミュレーション設定ファイルのキー名 (AC/DCコンバータの定格容量) **/
    public static final String S_RESOURCE_KEY_P_ACDC = "p_acdc";

    /** シミュレーション設定ファイルのキー名 (蓄電池定格容量) **/
    public static final String S_RESOURCE_KEY_WC = "wc";

    /** シミュレーション設定ファイルのキー名 (待機電力) **/
    public static final String S_RESOURCE_KEY_WT = "wt";

    /** シミュレーション設定ファイルのキー名 (分解能) **/
    public static final String S_RESOURCE_KEY_T = "t";

    /** シミュレーション設定ファイルのキー名 (MPPTユニットの電力変換効率の定数1) **/
    public static final String S_RESOURCE_KEY_K_DCDC_COE1 = "k_dcdc_coe1";

    /** シミュレーション設定ファイルのキー名 (MPPTユニットの電力変換効率の定数2) **/
    public static final String S_RESOURCE_KEY_K_DCDC_COE2 = "k_dcdc_coe2";

    /** シミュレーション設定ファイルのキー名 (MPPTユニットの電力変換効率の定数3) **/
    public static final String S_RESOURCE_KEY_K_DCDC_COE3 = "k_dcdc_coe3";

    /** シミュレーション設定ファイルのキー名 (MPPTユニットの電力変換効率の定数4) **/
    public static final String S_RESOURCE_KEY_K_DCDC_COE4 = "k_dcdc_coe4";

    /** シミュレーション設定ファイルのキー名 (MPPTユニットの電力変換効率の定数5) **/
    public static final String S_RESOURCE_KEY_K_DCDC_COE5 = "k_dcdc_coe5";

    /** シミュレーション設定ファイルのキー名 (MPPTユニットの電力変換効率の定数6) **/
    public static final String S_RESOURCE_KEY_K_DCDC_COE6 = "k_dcdc_coe6";

    /** シミュレーション設定ファイルのキー名 (AC/DCコンバータの充電時電力変換効率の定数1) **/
    public static final String S_RESOURCE_KEY_K_ACDC_C_COE1 = "k_acdc_c_coe1";

    /** シミュレーション設定ファイルのキー名 (AC/DCコンバータの充電時電力変換効率の定数2) **/
    public static final String S_RESOURCE_KEY_K_ACDC_C_COE2 = "k_acdc_c_coe2";

    /** シミュレーション設定ファイルのキー名 (AC/DCコンバータの充電時電力変換効率の定数3) **/
    public static final String S_RESOURCE_KEY_K_ACDC_C_COE3 = "k_acdc_c_coe3";

    /** シミュレーション設定ファイルのキー名 (AC/DCコンバータの充電時電力変換効率の定数4) **/
    public static final String S_RESOURCE_KEY_K_ACDC_C_COE4 = "k_acdc_c_coe4";

    /** シミュレーション設定ファイルのキー名 (AC/DCコンバータの充電時電力変換効率の定数5) **/
    public static final String S_RESOURCE_KEY_K_ACDC_C_COE5 = "k_acdc_c_coe5";

    /** シミュレーション設定ファイルのキー名 (AC/DCコンバータの充電時電力変換効率の定数6) **/
    public static final String S_RESOURCE_KEY_K_ACDC_C_COE6 = "k_acdc_c_coe6";

    /** シミュレーション設定ファイルのキー名 (AC/DCコンバータの放電時電力変換効率の定数1) **/
    public static final String S_RESOURCE_KEY_K_ACDC_D_COE1 = "k_acdc_d_coe1";

    /** シミュレーション設定ファイルのキー名 (AC/DCコンバータの放電時電力変換効率の定数2) **/
    public static final String S_RESOURCE_KEY_K_ACDC_D_COE2 = "k_acdc_d_coe2";

    /** シミュレーション設定ファイルのキー名 (AC/DCコンバータの放電時電力変換効率の定数3) **/
    public static final String S_RESOURCE_KEY_K_ACDC_D_COE3 = "k_acdc_d_coe3";

    /** シミュレーション設定ファイルのキー名 (AC/DCコンバータの放電時電力変換効率の定数4) **/
    public static final String S_RESOURCE_KEY_K_ACDC_D_COE4 = "k_acdc_d_coe4";

    /** シミュレーション設定ファイルのキー名 (AC/DCコンバータの放電時電力変換効率の定数5) **/
    public static final String S_RESOURCE_KEY_K_ACDC_D_COE5 = "k_acdc_d_coe5";

    /** シミュレーション設定ファイルのキー名 (AC/DCコンバータの放電時電力変換効率の定数6) **/
    public static final String S_RESOURCE_KEY_K_ACDC_D_COE6 = "k_acdc_d_coe6";

    /** 受電ステータス(停電) **/
    public static final int S_BATSTATUS_POWER_OUTAGE = 0;

    /** 受電ステータス(受電) **/
    public static final int S_BATSTATUS_RECEIVING_POWER = 1;

    /** 24時間分(30分間隔)の予測データ数 **/
    public static final int S_24HR_SIMDATA_COUNT = 24 * 2;

    /** 受電ステータスフラグ **/
    public static final int S_FLAG_RECEIVE_POWER_STATUS = 1 << 9;

    /** 24時間分(30分間隔)の抽出データ数 **/
    public static final int E_24HR_EXTRACTDATA_COUNT = 24 * 2;

    /** 24時間分(30分間隔)の誤差算出入力データ数 **/
    public static final int E_24HR_INPUTDATA_COUNT = 24 * 2;

    /** 24時間分(30分間隔)のデータ数 **/
    public static final int DS_24HR_DATA_COUNT = 24 * 2;

    /** ルートディレクトリからデータシート出力先へのパス **/
    public static final String DS_OUTPUT_PATH = File.separator + "conf" + File.separator + "tool" + File.separator + "datasheet" + File.separator;

    /** データシートファイル拡張子 **/
    public static final String DS_FILEEXT = ".csv";

    /** データシートファイル名(需要) **/
    public static final String DS_DM_FILENAME = "DataSheet_DM_";

    /** データシートファイル名(発電) **/
    public static final String DS_GN_FILENAME = "DataSheet_GN_";

    /** データシートファイル名(シミュレーション) **/
    public static final String DS_SM_FILENAME = "DataSheet_SM_";

    /** データシートファイル名(蓄電池) **/
    public static final String DS_BT_FILENAME = "DataSheet_BT_";

    /** データシートファイル名(PV) **/
    public static final String DS_PV_FILENAME = "DataSheet_PV";

    /** データシートファイル名(マルチメータ) **/
    public static final String DS_MM_FILENAME = "DataSheet_MM_";

    /** 発電計測ポイントリスト内の買電しきい値設定の項目の順番(※ 1番目なら0を設定) **/
    public static final int BT_BUYLIM_ORDER = 0;

    /** 発電計測ポイントリスト内のSoC放電禁止設定の項目の順番(※ 2番目なら1を設定) **/
    public static final int BT_SOC_MIN_ORDER = 1;

    /** 発電計測ポイントリスト内のSoC下限設定(自立運転用)の項目の順番(※ 3番目なら2を設定) **/
    public static final int BT_SOC_ISLAND_ORDER = 2;

    /** 発電計測ポイントリスト内の双方向電源動作ステータスの項目の順番(※ 4番目なら3を設定) **/
    public static final int BT_STATUS_DATA_ORDER = 3;

    /** 発電計測ポイントリスト内の出力電圧モニタ(代表値)の項目の順番(※ 5番目なら4を設定) **/
    public static final int BT_VOUT_ORDER = 4;

    /** 発電計測ポイントリスト内の出力電流モニタ(代表値)の項目の順番(※ 6番目なら5を設定) **/
    public static final int BT_IOUT_ORDER = 5;

    /** 発電計測ポイントリスト内の出力周波数モニタの項目の順番(※ 7番目なら6を設定) **/
    public static final int BT_FREQ_OUT_ORDER = 6;

    /** 発電計測ポイントリスト内の出力有効電力モニタ(代表値)の項目の順番(※ 8番目なら7を設定) **/
    public static final int BT_POUT_ORDER = 7;

    /** 発電計測ポイントリスト内の出力力率モニタの項目の順番(※ 9番目なら8を設定) **/
    public static final int BT_PFOUT_ORDER = 8;

    /** 発電計測ポイントリスト内のINV電圧モニタ(代表値)の項目の順番(※ 10番目なら9を設定) **/
    public static final int BT_VINV_ORDER = 9;

    /** 発電計測ポイントリスト内のINV電流モニタ(代表値)の項目の順番(※ 11番目なら10を設定) **/
    public static final int BT_IINV_ORDER = 10;

    /** 発電計測ポイントリスト内のINV有効電力モニタ(代表値)の項目の順番(※ 12番目なら11を設定) **/
    public static final int BT_PINV_ORDER = 11;

    /** 発電計測ポイントリスト内のINV力率モニタの項目の順番(※ 13番目なら12を設定) **/
    public static final int BT_PFINV_ORDER = 12;

    /** 発電計測ポイントリスト内の入力電圧モニタ(代表値)の項目の順番(※ 14番目なら13を設定) **/
    public static final int BT_VINP_ORDER = 13;

    /** 発電計測ポイントリスト内の入力電流モニタ(代表値)の項目の順番(※ 15番目なら14を設定) **/
    public static final int BT_IINP_ORDER = 14;

    /** 発電計測ポイントリスト内の入力有効電力モニタ(代表値)の項目の順番(※ 16番目なら15を設定) **/
    public static final int BT_PINP_ORDER = 15;

    /** 発電計測ポイントリスト内の入力力率モニタの項目の順番(※ 17番目なら16を設定) **/
    public static final int BT_PFINP_ORDER = 16;

    /** 発電計測ポイントリスト内の蓄電池電圧モニタの項目の順番(※ 18番目なら17を設定) **/
    public static final int BT_VBAT_ORDER = 17;

    /** 発電計測ポイントリスト内の蓄電池電流モニタの項目の順番(※ 19番目なら18を設定) **/
    public static final int BT_IBAT_ORDER = 18;

    /** 発電計測ポイントリスト内の蓄電池電力モニタの項目の順番(※ 20番目なら19を設定) **/
    public static final int BT_PBAT_ORDER = 19;

    /** 発電計測ポイントリスト内の蓄電池SoCの項目の順番(※ 21番目なら20を設定) **/
    public static final int BT_SOC_DATA_ORDER = 20;

    /** 発電計測ポイントリスト内の最大セル温度の項目の順番(※ 22番目なら21を設定) **/
    public static final int BT_TCEL_MAX_ORDER = 21;

    /** 発電計測ポイントリスト内のPV発電モニタ(全ユニット)の項目の順番(※ 23番目なら22を設定) **/
    public static final int BT_PPV_ALL_ORDER = 22;

    /** 発電計測ポイントリスト内の蓄電池累積電力モニタ(放電方向のみ)の項目の順番(※ 24番目なら23を設定) **/
    public static final int BT_PBAT_D_ORDER = 23;

    /** 発電計測ポイントリスト内の蓄電池累積電力モニタ(充電方向のみ)の項目の順番(※ 25番目なら24を設定) **/
    public static final int BT_PBAT_C_ORDER = 24;

    /** 発電計測ポイントリスト内の買電累積電力モニタの項目の順番(※ 26番目なら25を設定) **/
    public static final int BT_PBUY_ORDER = 25;

    /** 発電計測ポイントリスト内の売電累積電力モニタの項目の順番(※ 27番目なら26を設定) **/
    public static final int BT_PSELL_ORDER = 26;

    /** 発電計測ポイントリスト内の発電量(PV発電累積電力モニタ)の項目の順番(※ 28番目なら27を設定) **/
    public static final int BT_GENERATION_DATA_ORDER = 27;


    /** 発電計測ポイントリスト内の発電量(PVパネル電圧モニタ1)の項目の順番(※ 29番目なら28を設定) **/
    public static final int BT_VPV_1_ORDER = 28;

    /** 発電計測ポイントリスト内の発電量(PVパネル電流モニタ1)の項目の順番(※ 30番目なら29を設定) **/
    public static final int BT_IPV_1_ORDER = 29;

    /** 発電計測ポイントリスト内の発電量(PVパネル発電電力モニタ1)の項目の順番(※ 31番目なら30を設定) **/
    public static final int BT_PPV_1_ORDER = 30;

    /** 発電計測ポイントリスト内の発電量(PVパネル直流電圧モニタ1)の項目の順番(※ 32番目なら31を設定) **/
    public static final int BT_VDC_1_ORDER = 31;

    /** 発電計測ポイントリスト内の発電量(PVパネル直流電流モニタ1)の項目の順番(※ 33番目なら32を設定) **/
    public static final int BT_IDC_1_ORDER = 32;

    /** 発電計測ポイントリスト内の発電量(PVパネル直流電力モニタ1)の項目の順番(※ 34番目なら33を設定) **/
    public static final int BT_PDC_1_ORDER = 33;


    /**
     * コンストラクタ. <br>
     * このクラスのインスタンスが生成されることを防ぐ役割を持つ. <br>
     * <b>【注意】</b> 特になし.
     */
    private AppConstants() {
    }

}
