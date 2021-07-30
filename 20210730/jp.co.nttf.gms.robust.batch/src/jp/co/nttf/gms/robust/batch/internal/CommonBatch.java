/*
 * CommonBatch.java
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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.List;
import java.util.Properties;

import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.io.IOUtils;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.dom4j.Document;

import jp.co.nttf.gms.common.internal.AppLogger;
import jp.co.nttf.gms.common.internal.AppUtil;
import jp.co.nttf.gms.common.internal.CommonConstants;
import jp.co.nttf.gms.common.internal.CommonInfo;
import jp.co.nttf.gms.common.internal.Configuration;
import jp.co.nttf.gms.common.internal.GmsConfig;
import jp.co.nttf.gms.common.internal.MessageUtil;
import jp.co.nttf.gms.common.internal.communication.ConnectionInfo;
import jp.co.nttf.gms.common.internal.communication.ConnectionManager;
import jp.co.nttf.gms.common.internal.data.KeyValueInfo;
import jp.co.nttf.gms.common.internal.data.gmsdb.MGmu2resourceidMapper;
import jp.co.nttf.gms.common.internal.data.gmsdb.TPropertyMapper;


/**
 * バッチ処理共通クラス. <br>
 * <b>【注意】</b> 特になし.
 *
 * @author naya
 */
public class CommonBatch {

    /** ロガー */
    private static final AppLogger LOGGER = new AppLogger(CommonBatch.class);

    /**
     * 各種初期化を行うメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @throws IOException 処理中に問題が発生した場合
     */
    protected void init() throws IOException {

        CommonInfo comInfo = CommonInfo.getInstance();

        // MyBatis設定ファイルをロード
        Reader reader = Resources.getResourceAsReader(CommonConstants.MYBATIS_RESOURCE_NAME);
        // gmsdbのSqlSessionFactoryを生成
        SqlSessionFactory gmsfactory = new SqlSessionFactoryBuilder().build(
                reader, CommonConstants.ENVIRONMENT_GMSDB);
        comInfo.setSqlSessionFactory(CommonConstants.ENVIRONMENT_GMSDB,
                gmsfactory);

        // 起動時に必要な情報をロード
        SqlSession gmsSession = null;
        try {
            gmsSession = comInfo.getSqlSessionFactory(
                    CommonConstants.ENVIRONMENT_GMSDB).openSession();

            // GMS設定情報を取得
            TPropertyMapper propertyMapper = gmsSession.getMapper(TPropertyMapper.class);
            List<KeyValueInfo> propertyList = propertyMapper.selectAll();
            // 共通情報に格納
            comInfo.setGmsConfig(new GmsConfig(propertyList));

        } finally {
            // セッションをクローズ
            if (null != gmsSession) {
                gmsSession.close();
            }
        }

    }

    /**
     * 計測ポイントに対応するリソースIDを取得するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param measurepointId 計測ポイントID
     * @return リソースID(存在しなければ空文字を返却)
     */
    protected final String getResourceId(int measurepointId) {

        CommonInfo comInfo = CommonInfo.getInstance();

        String resourceId = null;

        SqlSession gmsSession = null;
        try {
            gmsSession = comInfo.getSqlSessionFactory(
                    CommonConstants.ENVIRONMENT_GMSDB).openSession();

            // リソースIDを取得
            MGmu2resourceidMapper mapper = gmsSession.getMapper(MGmu2resourceidMapper.class);
            resourceId = mapper.selectByMeasurepoint(measurepointId);

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("resourceId=" + resourceId);
            }

        } finally {
            // セッションをクローズ
            if (null != gmsSession) {
                gmsSession.close();
            }
        }

        if (null == resourceId) {
            // nullの場合は空文字に差し替える
            resourceId = "";
        }
        return resourceId;

    }

    /**
     * レポート通知を行うメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param connectionInfo コネクション情報
     * @param documentList Documentのリスト
     */
    protected final void sendReport(ConnectionInfo connectionInfo,
            List<Document> documentList) {

        Configuration gmsConfig = CommonInfo.getInstance().getGmsConfig();
        String charset = gmsConfig.getString(AppConstants.PROPERTY_FCEMS_REPORT_CHARSET);

        try {

            // 生成されたDocumentの数だけループ
            for (Document document : documentList) {

                // 送信実行クラスを生成
                ConnectionManager manager = new ConnectionManager(connectionInfo);
                PostMethod method = new PostMethod();
                // 毎回クローズする命令をヘッダに追加
                method.addRequestHeader("Connection", "close");

                try {
                    String body = AppUtil.toXML(document, charset);
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("body=" + body);
                    }

                    RequestEntity entiry = new StringRequestEntity(body,
                            AppConstants.REPORT_CONTENTTYPE, charset);
                    method.setRequestEntity(entiry);
                    // 送信実行
                    manager.sendExecute(method);
                } finally {
                    // 接続を解放
                    if (null != method) {
                        method.releaseConnection();
                    }
                }
            }

        } catch (Exception e) {
            // ERRORログ出力
            LOGGER.error(MessageUtil.getMessage(MessageID.EFCB000), e);
        }

    }

    /**
     * プロパティファイルをロードするメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param fileName ファイル名
     * @return プロパティ
     * @throws IOException 処理中に問題が発生した場合
     */
    protected final Properties loadProps(String fileName) throws IOException {

        Properties prop = new Properties();

        FileInputStream fis = null;
        try {
            File file = new File(fileName);
            if (file.exists()) {
                // ファイルが存在する場合はロード
                fis = new FileInputStream(file);
                prop.load(fis);
            }
        } finally {
            // クローズ
            IOUtils.closeQuietly(fis);
        }
        return prop;
    }

    /**
     * プロパティファイルを保存するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param fileName ファイル名
     * @param props プロパティ
     * @throws IOException 処理中に問題が発生した場合
     */
    protected final void storeProps(String fileName, Properties props) throws IOException {

        FileOutputStream fos = null;
        try {
            File file = new File(fileName);

            // 保存
            fos = new FileOutputStream(file);
            props.store(fos, null);

        } finally {
            // クローズ
            IOUtils.closeQuietly(fos);
        }
    }

    /**
     * ファイル出力するメソッド. <br>
     * <b>【注意】</b> 特になし.
     *
     * @param filePath ファイルパス
     * @param contents 出力内容
     * @throws IOException 処理中に問題が発生した場合
     */
    protected final void storeFile(String filePath, String contents) throws IOException {

        FileWriter out = null;

        try {
            out = new FileWriter(filePath, true);
            out.write(contents);

        } finally {
            // 静かにクローズ
            IOUtils.closeQuietly(out);
        }

    }

}
