#!/bin/bash
# Modified : 2021-Jun-06

# java -Xmx32m -Xss256k ...

# JARFILE="-jar $HOME/bin/BarcodeSvc-0.0.1.jar"
# CACERTS_PASS=changeit
export BARCODE_KEYSTORE_PASS=cTxE790gIa

# TRUSTSTORE_NAME="-Djavax.net.ssl.trustStore=classpath:cacerts"
# TRUSTSTORE_PASS="-Djavax.net.ssl.trustStorePassword=$CACERTS_PASS"

mvn package

# -END-
