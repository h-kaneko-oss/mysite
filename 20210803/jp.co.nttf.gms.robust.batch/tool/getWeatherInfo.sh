#!/bin/sh

# コマンド例
# ./getWeatherInfo.sh Z__C_RJTD_20210714000000_MSM_GPV_Rjp_Lsurf_FH00-15_grib2.bin 1
# ./getWeatherInfo.sh Z__C_RJTD_20210710000000_MSM_GPV_Rjp_L-pall_FH00-15_grib2.bin 2

# 定数設定
# DB名
DB_NAME=gmsdb2
# DBユーザ名
DB_USER=postgres

# 変数初期化
TARGET_TABLE=""
TARGET_DATE=""
TARGET_ID=""

# 第1パラメータの配信ファイル名から日時文字列を取得 (地上面の場合)
if [[ $1 =~ ^(Z__C_RJTD_([0-9]{8})([0-9]{2})([0-9]{2})00_MSM_GPV_Rjp_Lsurf_FH[0-9]{2}-[0-9]{2}_grib2).bin$ ]]; then
    # TARGET_DATEを、日付の形式に正しく変換できることを確認
    date -d "${BASH_REMATCH[2]} ${BASH_REMATCH[3]}:${BASH_REMATCH[4]}" >/dev/null 2>&1
    if [ $? -gt 0 ];then
        echo "[ERROR] Invalid date given in args."
        exit 1
    fi

    BASE_FILE_NAME=${BASH_REMATCH[1]}
    TARGET_TABLE=t_weather_forecast_g
    TARGET_DATE=${BASH_REMATCH[2]}${BASH_REMATCH[3]}${BASH_REMATCH[4]}
# 第1パラメータの配信ファイル名から日時文字列を取得 (気圧面の場合)
elif [[ $1 =~ ^(Z__C_RJTD_([0-9]{12})00_MSM_GPV_Rjp_L-pall_FH[0-9]{2}-[0-9]{2}_grib2).bin$ ]]; then
    # TARGET_DATEを、日付の形式に正しく変換できることを確認
    date -d "${BASH_REMATCH[2]} ${BASH_REMATCH[3]}:${BASH_REMATCH[4]}" >/dev/null 2>&1
    if [ $? -gt 0 ];then
        echo "[ERROR] Invalid date given in args."
        exit 1
    fi

    BASE_FILE_NAME=${BASH_REMATCH[1]}
    TARGET_TABLE=t_weather_forecast_p
    TARGET_DATE=${BASH_REMATCH[2]}${BASH_REMATCH[3]}${BASH_REMATCH[4]}
else
    echo "[ERROR] Invalid distribution file name specified."
    exit 1
fi


# 第2パラメータから気象位置情報IDを取得
if [[ $2 =~ ^([0-9]*)$ ]]; then
    TARGET_ID=${BASH_REMATCH[1]}
else
    echo "[ERROR] Invalid location id specified."
    exit 1
fi

#echo ${TARGET_TABLE}
#echo ${TARGET_DATE}
#echo ${TARGET_ID}

# クエリ文字列生成
QUERY="SELECT * FROM ${TARGET_TABLE} WHERE location_id=${TARGET_ID} AND distribution_time=to_timestamp('${TARGET_DATE}', 'YYYYMMDDHH24MI')"

#echo "${QUERY}"

# コマンド文字列生成
PSQL1="psql ${DB_NAME} -U ${DB_USER} -c "
PSQL2=" -A -F, > "
D_QUOTE='"'
CSVFILE="${BASE_FILE_NAME}_${TARGET_ID}.csv"
COMMAND=$PSQL1$D_QUOTE$QUERY$D_QUOTE$PSQL2$CSVFILE

#echo "${COMMAND}"

#コマンド実行
eval "${COMMAND}"

