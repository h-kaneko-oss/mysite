#!/bin/sh

# コマンド例
# ./getSocHistory.sh 1 202107150000 202107160000

# 定数設定
# DB名
DB_NAME=gmsdb2
# DBユーザ名
DB_USER=postgres

# 変数初期化
START_DATE=""
END_DATE=""
TARGET_ID=""

# 第1パラメータから需要発電計測ポイントIDを取得
if [[ $1 =~ ^([0-9]*)$ ]]; then
    TARGET_ID=${BASH_REMATCH[1]}
else
    echo "[ERROR] Invalid d_g_location_id specified."
    exit 1
fi

# 第2パラメータが12桁の数字で、末尾の分の2桁が00、または30であることを確認
if [[ $2 =~ ^([0-9]{8})([0-9]{2})([0|3][0])$ ]]; then
    # 日付の形式に正しく変換できることを確認
    date -d "${BASH_REMATCH[1]} ${BASH_REMATCH[2]}:${BASH_REMATCH[3]}" >/dev/null 2>&1
    if [ $? -gt 0 ];then
        echo "[ERROR] Invalid start date given in args."
        exit 1
    fi
    # パラメータのチェックが通った場合、始点の日時を取得
    START_DATE=${BASH_REMATCH[1]}${BASH_REMATCH[2]}${BASH_REMATCH[3]}
else
    echo "[ERROR] Minutes value in start date must be 00 or 30."
    exit 1
fi

# 第3パラメータが12桁の数字で、末尾の分の2桁が00、または30であることを確認
if [[ $3 =~ ^([0-9]{8})([0-9]{2})([0|3][0])$ ]]; then
    # 日付の形式に正しく変換できることを確認
    date -d "${BASH_REMATCH[1]} ${BASH_REMATCH[2]}:${BASH_REMATCH[3]}" >/dev/null 2>&1
    if [ $? -gt 0 ];then
        echo "[ERROR] Invalid end date given in args."
        exit 1
    fi
    # パラメータのチェックが通った場合、始点の日時を取得
    END_DATE=${BASH_REMATCH[1]}${BASH_REMATCH[2]}${BASH_REMATCH[3]}
else
    echo "[ERROR] Minutes value in end date must be 00 or 30."
    exit 1
fi

#echo ${TARGET_DATE}
#echo ${TARGET_ID}

# クエリ文字列生成
QUERY="SELECT simulation_time, soc_mod_need, schedule_id, soc_min FROM t_simulation_result WHERE d_g_location_id=${TARGET_ID} AND simulation_time>=to_timestamp('${START_DATE}', 'YYYYMMDDHH24MI') AND simulation_time<=to_timestamp('${END_DATE}', 'YYYYMMDDHH24MI')"

#echo "${QUERY}"

# コマンド文字列生成
PSQL1="psql ${DB_NAME} -U ${DB_USER} -c "
PSQL2=" -A -F, > "
D_QUOTE='"'
CSVFILE="SocHistory_${TARGET_ID}_${START_DATE}_${END_DATE}.csv"
COMMAND=$PSQL1$D_QUOTE$QUERY$D_QUOTE$PSQL2$CSVFILE

#echo "${COMMAND}"

#コマンド実行
eval "${COMMAND}"

