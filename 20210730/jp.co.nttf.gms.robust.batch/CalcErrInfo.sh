#!/bin/sh

cd `dirname $0`

JVM_COMMAND="java"

SECURITY_OPT=""
LOCALE="-Duser.region=JP -Duser.timezone=JST"

CLASSPATH="-classpath ./bin:"
for name in `ls /opt/gms/batch_robust/lib/*.jar`; do
  CLASSPATH="${CLASSPATH}:$name"
done

MAINCLASS="jp.co.nttf.gms.robust.batch.ErrInfoCalculator"

LOCKFILE=/tmp/_baseline_errinfo_calculator.sh_.lock

### 二重実行チェック ###
if [ -f $LOCKFILE ] ; then
    echo "$0 is running"
    exit 1
fi

### 実行中ロックファイル作成 ###
touch $LOCKFILE

COMMAND_LINE="$JVM_COMMAND $SECURITY_OPT $LOCALE $CLASSPATH $MAINCLASS $1"
RET=$?
$COMMAND_LINE

### 実行中ロックファイル削除 ###
rm -f $LOCKFILE

exit $RET
