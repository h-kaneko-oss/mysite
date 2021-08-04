/*
 * SocConfigurator.java
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
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.ibatis.session.SqlSession;

import jp.co.nttf.gms.common.internal.AppLogger;
import jp.co.nttf.gms.common.internal.CommonConstants;
import jp.co.nttf.gms.common.internal.CommonInfo;
import jp.co.nttf.gms.common.internal.Configuration;
import jp.co.nttf.gms.common.internal.MessageUtil;
import jp.co.nttf.gms.common.internal.data.gmsdb.TDemandGenerationLocation;
import jp.co.nttf.gms.common.internal.data.gmsdb.TDemandGenerationLocationMapper;
import jp.co.nttf.gms.common.internal.data.gmsdb.TMaintenanceDeviceConfig;
import jp.co.nttf.gms.common.internal.data.gmsdb.TMaintenanceDeviceConfigMapper;
import jp.co.nttf.gms.common.internal.data.gmsdb.TMaintenanceSchedule;
import jp.co.nttf.gms.common.internal.data.gmsdb.TMaintenanceScheduleMapper;
import jp.co.nttf.gms.common.internal.data.gmsdb.TSimulationResult;
import jp.co.nttf.gms.common.internal.data.gmsdb.TSimulationResultMapper;
import jp.co.nttf.gms.robust.batch.internal.AppConstants;
import jp.co.nttf.gms.robust.batch.internal.CommonBatch;
import jp.co.nttf.gms.robust.batch.internal.MessageID;

/**
 * SoC下限値設定を行うバッチクラス. <br>
 * <b>【注意】</b> 特になし.
 *
 * @author sekiya
 */
public final class SocConfigurator extends CommonBatch {
    /** 共通情報 */
    protected CommonInfo comInfo = CommonInfo.getInstance();

    /** ロガー */
    private static final AppLogger LOGGER = new AppLogger(SocConfigurator.class);

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
            LOGGER.info(MessageUtil.getMessage(MessageID.IROB071));
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
            SocConfigurator reporter = new SocConfigurator();
            reporter.execute(argDate);

        } catch (Exception e) {
            // ERRORログ出力
            LOGGER.error(MessageUtil.getMessage(MessageID.ACOM000), e);
            throw e;
        }

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(MessageUtil.getMessage(MessageID.IROB072));
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

            TSimulationResult simResult = this.getSimulationResult(dgLocation.getDGLocationId());

            // 需要発電計測ポイントの設定、及びシミュレーション結果のSoC下限値変更フラグが、共に要の場合
            if (dgLocation.getSocModSetting() == AppConstants.ROBUST_NEED_SOC_MODIFICATION
                    && simResult.getSocModNeed() == AppConstants.ROBUST_NEED_SOC_MODIFICATION) {

                // 制御配信を実施
                // メンテナンススケジュールテーブルへのDB登録
                int schedule_id = this.storeMaintenanceSchedule(dgLocation.getGmuId());
                // メンテナンスデバイス設定テーブルへのDB登録
                this.storeMaintenanceDeviceConfig(schedule_id, simResult.getSocMin());

                // シミュレーション情報テーブルを更新(スケジュールIDを書き込み)
                simResult.setScheduleId(schedule_id);
                this.updateSimulationResult(simResult);
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
     * シミュレーション情報テーブルのDB検索を行うメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param dgLocationId 需要計測ポイントリストID
     * @return シミュレーション情報テーブルの検索結果
     * @throws Exception 処理中に問題が発生した場合
     */
    private TSimulationResult getSimulationResult(Integer dgLocationId) throws Exception {
        TSimulationResult result = null;

        SqlSession session = null;

        try {
            // DBセッションを取得
            session = this.comInfo.getSqlSessionFactory(
                    CommonConstants.ENVIRONMENT_GMSDB).openSession();

            TSimulationResult param = new TSimulationResult();
            param.setDGLocationId(dgLocationId);

            // SQL実行
            TSimulationResultMapper mapper = session.getMapper(TSimulationResultMapper.class);
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
     * メンテナンススケジュールのDB登録を行うメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param gmu_id DB登録対象のGMUID
     * @throws Exception 処理中に問題が発生した場合
     */
    private int storeMaintenanceSchedule(String gmu_id) throws Exception {
        Timestamp curTS = new Timestamp(System.currentTimeMillis());

        TMaintenanceSchedule param = new TMaintenanceSchedule();
        param.setGmuid(gmu_id);
        // 接点連携制御
        param.setModeno(AppConstants.ROBUST_M_DELIVERY_MODE);
        param.setStarttime(curTS);
        param.setEndtime(curTS);
        param.setUpdatetime(curTS);
        // 即時配信
        param.setStatus((short)AppConstants.ROBUST_M_DELIVERY_STATUS);

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(MessageUtil.getMessage(MessageID.IROB029));
        }

        SqlSession session = null;

        try {
            // DBセッションを取得
            session = this.comInfo.getSqlSessionFactory(
                    CommonConstants.ENVIRONMENT_GMSDB).openSession();

            // SQL実行
            TMaintenanceScheduleMapper mapper = session.getMapper(TMaintenanceScheduleMapper.class);
            mapper.insertMaintenance(param);

            // コミット
            session.commit();
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info(MessageUtil.getMessage(MessageID.IROB030));
            }
        } finally {
            // セッションをクローズ
            if (null != session) {
                session.close();
            }
        }

        return(param.getScheduleid());
    }

    /**
     * メンテナンスデバイス設定のDB登録を行うメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param schedule_id DB登録対象のスケジュールID
     * @param socmin SoC下限設定値
     * @throws Exception 処理中に問題が発生した場合
     */
    private void storeMaintenanceDeviceConfig(int schedule_id, double socmin) throws Exception {
        // CommonInfoのインスタンスを生成
        CommonInfo comInfo = CommonInfo.getInstance();
        Configuration gmsConfig = comInfo.getGmsConfig();

        // デバイス名
        String deviceName = gmsConfig.getString(AppConstants.PROPERTY_ROBUST_DELIVERY_DEVICE_NAME);
        // プロパティ
        String deviceProperty = gmsConfig.getString(AppConstants.PROPERTY_ROBUST_DELIVERY_DEVICE_PROPERTY);

        TMaintenanceDeviceConfig param = new TMaintenanceDeviceConfig();
        param.setScheduleid(schedule_id);
        param.setDevicename(deviceName);
        param.setProperty(deviceProperty);

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(MessageUtil.getMessage(MessageID.IROB030));
        }

        SqlSession session = null;

        try {
            // DBセッションを取得
            session = this.comInfo.getSqlSessionFactory(
                    CommonConstants.ENVIRONMENT_GMSDB).openSession();

            // SQL実行
            TMaintenanceDeviceConfigMapper mapper = session.getMapper(TMaintenanceDeviceConfigMapper.class);
            mapper.insertDeviceConfig(param);

            // コミット
            session.commit();
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info(MessageUtil.getMessage(MessageID.IROB031));
            }
        } finally {
            // セッションをクローズ
            if (null != session) {
                session.close();
            }
        }
    }

    /**
     * シミュレーション情報のDB更新を行うメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param schedule_id DB登録対象のスケジュールID
     * @param socmin SoC下限設定値
     * @throws Exception 処理中に問題が発生した場合
     */
    private void updateSimulationResult(TSimulationResult param) throws Exception {
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
            mapper.updateResult(param);

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
}
