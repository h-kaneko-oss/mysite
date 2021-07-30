#!/bin/sh

# 第1パラメータが12桁の数字で、末尾の分の2桁が00、または30であることを確認
if [[ $1 =~ ^([0-9]{8})([0-9]{2})([0|3][0])$ ]]; then
    # 日付の形式に正しく変換できることを確認
    date -d "${BASH_REMATCH[1]} ${BASH_REMATCH[2]}:${BASH_REMATCH[3]}" >/dev/null 2>&1
    if [ $? -gt 0 ];then
        echo "[ERROR] Invalid date given in args."
        exit 1
    fi
    # パラメータのチェックが通った場合、誤差算出機能を実行
    /opt/gms/batch_robust/CalcErrInfo.sh $1
else
    echo "[ERROR] Minutes value in args must be 00 or 30."
    exit 1
fi

