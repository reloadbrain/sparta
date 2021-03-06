#!/bin/bash

#Setup vault hosts (better in universe??)
export VAULT_HOSTS=($VAULT_HOST)

source /kms_utils.sh

#Ensure security folder is created
mkdir -p /etc/sds/sparta/security

# Main execution

## Init
export TENANT_NAME='sparta'   # MARATHON_APP_ID without slash
#Setup tenant_normalized for access kms_utils
export TENANT_UNDERSCORE=${TENANT_NAME//-/_}
export TENANT_NORM="${TENANT_UNDERSCORE^^}"

function _log_sparta_sec() {
    local message=$1
    echo -e "$(date +'%b %d %R:%S.%N') [SPARTA-SEC] $message" | tee -a "$PWD/sparta-sec.log"
}

####################################################
## Get TLS Server Info and set SPARTA_KEYSTORE_PASS
####################################################
_log_sparta_sec "Configuring tls ..."
source /tls-config.sh
_log_sparta_sec "Configuring tls Ok"

#######################################################
## Create XD Truststore and set DEFAULT_KEYSTORE_PASS
#######################################################
_log_sparta_sec "Configuring truststore ..."
source /truststore-config.sh
_log_sparta_sec "Configuring truststore OK"

####################################################
## Kerberos config set SPARTA_PRINCIPAL_NAME and SPARTA_KEYTAB_PATH
####################################################
_log_sparta_sec "Configuring kerberos ..."
source /kerberos-server-config.sh
_log_sparta_sec "Configuring kerberos Ok"

#######################################################
## Oauth config set OAUTH2_ENABLE OAUTH2_CLIENT_ID OAUTH2_CLIENT_SECRET
#######################################################
_log_sparta_sec "Configuring Oauth ..."
source /oauth2.sh
_log_sparta_sec "Configuring Oauth Ok"

#######################################################
## MesosSecurity config set MESOS_USER and MESOS_PASS
#######################################################
_log_sparta_sec "Configuring Mesos Security ..."
source mesos-security.sh
_log_sparta_sec "Configuring Mesos Security Ok"
