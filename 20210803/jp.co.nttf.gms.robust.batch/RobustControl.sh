#!/bin/sh

# コマンド例
# ./RobustControl.sh 202107150000

cd `dirname $0`

JVM_COMMAND="java"

SECURITY_OPT=""
LOCALE="-Duser.region=JP -Duser.timezone=JST"

CLASSPATH="-classpath ./bin:"
for name in `ls /opt/gms/batch_robust/lib/*.jar`; do
    CLASSPATH="${CLASSPATH}:$name"
done

LOCKFILE=/tmp/_baseline_robust_control.sh_.lock

### 二重実行チェック ###
if [ -f $LOCKFILE ] ; then
    echo "$0 is running"
    exit 1
fi

### 実行中ロックファイル作成 ###
touch $LOCKFILE


# 気象情報取り込み機能
MAINCLASS="jp.co.nttf.gms.robust.batch.WeatherForecastStore"

COMMAND_LINE="$JVM_COMMAND $SECURITY_OPT $LOCALE $CLASSPATH $MAINCLASS $1"
$COMMAND_LINE
RET=$?

if [ $RET -gt 0 ]; then
    echo "[ERROR] Exception occurs in WeatherForecastStore class."

    ### 実行中ロックファイル削除 ###
    rm -f $LOCKFILE

    exit $RET
fi


# 発電予測算出機能
MAINCLASS="jp.co.nttf.gms.robust.batch.GenerationForecaster"

COMMAND_LINE="$JVM_COMMAND $SECURITY_OPT $LOCALE $CLASSPATH $MAINCLASS $1"
$COMMAND_LINE
RET=$?

if [ $RET -gt 0 ]; then
    echo "[ERROR] Exception occurs in GenerationForecaster class."

    ### 実行中ロックファイル削除 ###
    rm -f $LOCKFILE

    exit $RET
fi


# 充放電シミュレーション機能
MAINCLASS="jp.co.nttf.gms.robust.batch.SimulationCalculator"

COMMAND_LINE="$JVM_COMMAND $SECURITY_OPT $LOCALE $CLASSPATH $MAINCLASS $1"
$COMMAND_LINE
RET=$?

if [ $RET -gt 0 ]; then
    echo "[ERROR] Exception occurs in SimulationCalculator class."

    ### 実行中ロックファイル削除 ###
    rm -f $LOCKFILE

    exit $RET
fi


# SoC下限値設定機能
MAINCLASS="jp.co.nttf.gms.robust.batch.SocConfigurator"

COMMAND_LINE="$JVM_COMMAND $SECURITY_OPT $LOCALE $CLASSPATH $MAINCLASS $1"
$COMMAND_LINE
RET=$?

if [ $RET -gt 0 ]; then
    echo "[ERROR] Exception occurs in SocConfigurator class."

    ### 実行中ロックファイル削除 ###
    rm -f $LOCKFILE

    exit $RET
fi

### 実行中ロックファイル削除 ###
rm -f $LOCKFILE

exit $RET
