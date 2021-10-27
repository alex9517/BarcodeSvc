#!/bin/bash
# Modified : 2021-Jun-07

# java -Xmx32m -Xss256k ...

cd $HOME/bin

JARFILE="-jar $HOME/bin/BarcodeSvc-0.0.1-SNAPSHOT.jar"
CACERTS_PASS=changeit

export BARCODE_KEYSTORE_PASS=cTxE790gIa

TRUSTSTORE_NAME="-Djavax.net.ssl.trustStore=classpath:cacerts"
TRUSTSTORE_PASS="-Djavax.net.ssl.trustStorePassword=$CACERTS_PASS"
# TRUSTSTORE_TYPE="-Djavax.net.ssl.trustStoreType=PKCS12"

CMDLINE="java $TRUSTSTORE_PASS $TRUSTSTORE_NAME $JARFILE"

if [ -z $BARCODE_KEYSTORE_PASS ]; then
    echo "Cannot get BARCODE_KEYSTORE_PASS .. exiting.."
    exit 1
fi

LOG_LEVEL=''
# LOG_LEVEL='TRACE'
# LOG_LEVEL='DEBUG'
# LOG_FILE=''
LOG_FILE="$HOME/tmp/Barcode.log"
PROFILE='dev'

LEN=${#PROFILE}

if [ $LEN -gt 2 ]; then
  CMDLINE="$CMDLINE --spring.profiles.active=$PROFILE"
fi

LEN=${#LOG_LEVEL}

if [ $LEN -ge 4 ]; then
  CMDLINE="$CMDLINE --logging.level.root=$LOG_LEVEL"
fi

LEN=${#LOG_FILE}

if [ $LEN -ge 5 ]; then
  CMDLINE="$CMDLINE --logging.file=$LOG_FILE"
fi

# CMDLINE="$CMDLINE $JARFILE"

# Run it!

$CMDLINE

cd -

# -END-
