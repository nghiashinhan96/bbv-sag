#!/usr/bin/env bash
##
## The build scripts for UMB PRODUCTION env
##

############################ Verify variables ####################################
echo $bamboo_sag_tomcat_host_1
echo $bamboo_sag_tomcat_user
echo $bamboo_sag_tomcat_password

echo $bamboo_deploy_version
echo $bamboo_buildNumber

echo $bamboo_sag_jdbc_url
echo $bamboo_sag_db_name
echo $bamboo_sag_db_username
echo $bamboo_sag_db_password

echo $bamboo_sag_cache_members

echo $bamboo_sag_es_host
echo $bamboo_sag_es_port
echo $bamboo_sag_es_cls_name

echo $bamboo_sag_hpback_uri
echo $bamboo_sag_hpback_comp_id
echo $bamboo_sag_hpback_dist_password
echo $bamboo_sag_hpback_callback_url
echo $bamboo_sag_hpback_ddch
echo $bamboo_sag_hpback_tm
echo $bamboo_sag_hpback_def_email
echo $bamboo_sag_hpback_at_email
echo $bamboo_erp_max_request_size


############################ Build Eshop Web Front-End ####################################
echo "Running deployment process by scripts"

export NVM_DIR=~/.nvm
source ~/.nvm/nvm.sh

nvm use 12

node --version

echo "Installing node_modules For Eshop Autonet"
cd eshop-autonet/src/main/resources/autonet
rm -rf ./node_modules
yarn check
yarn install

echo "Building Autonet"
yarn run build--prod

echo "Packaging Autonet web"
cd ../../../../
mvn clean install -Dmaven.test.skip=true -Pprod -Dbuild.customer=autonet
cd ../

echo "Copying and Rename Autonet war file"
cp eshop-autonet/target/*.war eshop-autonet/autonet.war

nvm use 8
node --version

############################ Build Back Office Front-End ####################################
## echo "Installing node_modules For Eshop Back Office"
## cd eshop-backoffice/src/main/resources/backoffice
## rm -rf ./node_modules
## npm cache verify
## npm install

## echo "Building Back Office"
## npm run build--autonet-prod
## cd ../../../../../

############################ Build Back-End ####################################
echo "Buiding Back-end services"
ls -a
cd eshop-parent
ls -a
mvn clean install -Pprod \
  -Dmaven.test.skip=true \
  -Dbuild.customer=autonet \
  -Dapp.version=$bamboo_deploy_version \
  -Dapp.releaseBranch=$bamboo_planRepository_branch \
  -Dapp.buildNumber=$bamboo_buildNumber \
  -Dapp.cache.tcp-ip-config.members=$bamboo_sag_cache_members \
  -Dapp.db.jdbc-url=$bamboo_sag_jdbc_url \
  -Dapp.db.db-name=$bamboo_sag_db_name \
  -Dapp.db.username=$bamboo_sag_db_username \
  -Dapp.db.password=$bamboo_sag_db_password \
  -Dapp.elasticsearch.host=$bamboo_sag_es_host \
  -Dapp.elasticsearch.port=$bamboo_sag_es_port \
  -Dapp.elasticsearch.cluster-name=$bamboo_sag_es_cls_name \
  -Dapp.erpConfig.maxRequestSize=$bamboo_erp_max_request_size
cd ../

############################ Undeploy Web Applications ####################################

echo "Undeploying wars $bamboo_sag_tomcat_host_1"
curl -u $bamboo_sag_tomcat_user:$bamboo_sag_tomcat_password "$bamboo_sag_tomcat_host_1/manager/text/undeploy?path=/rest-autonet"
curl -u $bamboo_sag_tomcat_user:$bamboo_sag_tomcat_password "$bamboo_sag_tomcat_host_1/manager/text/undeploy?path=/auth-server-autonet"

curl -u $bamboo_sag_tomcat_user:$bamboo_sag_tomcat_password "$bamboo_sag_tomcat_host_1/manager/text/undeploy?path=/autonet"


