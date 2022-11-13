#!/usr/bin/env bash
##
## The build scripts for DEVELOPMENT env
##

############################ Verify variables ####################################
echo $bamboo_sag_tomcat_host
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
echo "########################################################"
echo "Running deployment process by scripts"

export NVM_DIR=~/.nvm
source ~/.nvm/nvm.sh

nvm use 12
node --version

echo "########################################################"
echo "Installing node_modules For Eshop Web"
cd eshop-web/src/main/resources/client
rm -rf ./node_modules
yarn check
yarn install

yarn run theme
yarn run build:lib

echo "Building Derendinger AT"
yarn run build--ddat-dev

echo "Packaging Derendinger AT web"
cd ../../../../
mvn clean install -Dmaven.test.skip=true -Pdev
cd ../

echo "Copying and Rename Derendinger AT war file"
cp eshop-web/target/*.war eshop-web/derendinger-at.war

echo "Building Matik AT"
cd eshop-web/src/main/resources/client
yarn run build--mtat-dev

echo "Packaging Matik AT web"
cd ../../../../
mvn clean install -Dmaven.test.skip=true -Pdev
cd ../

echo "Copying and Rename Matik AT war file"
cp eshop-web/target/*.war eshop-web/matik-at-ax.war

############################ Build Back Office Front-End ####################################
echo "########################################################"
echo "Installing node_modules For Eshop Back Office"

cd eshop-backoffice/src/main/resources/backoffice
rm -rf ./node_modules
yarn check
yarn install

echo "Building Back Office AT"
yarn run build--at-dev
cd ../../../../
pwd
ls
mvn clean install -Dmaven.test.skip=true -Pdev -Dbuild.customer=at
cd ../

echo "Copying and Rename Back Office war file"
cp eshop-backoffice/target/*.war eshop-backoffice/bo-ax.war

nvm use 8
node --version

############################ Build Back-End ####################################
echo "########################################################"
echo "Buiding Back-end services"
pwd
ls
cd eshop-parent
mvn clean install -Pdev \
  -Dmaven.test.skip=true \
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
  -Dapp.hpback.uri=$bamboo_sag_hpback_uri \
  -Dapp.hpback.companyIdentificaton=$bamboo_sag_hpback_comp_id \
  -Dapp.hpback.distributorPassword=$bamboo_sag_hpback_dist_password \
  -Dapp.hpback.callbackUrl=$bamboo_sag_hpback_callback_url \
  -Dapp.hpback.derendinger-ch-email=$bamboo_sag_hpback_ddch \
  -Dapp.hpback.technomag-email=$bamboo_sag_hpback_tm \
  -Dapp.hpback.default-email=$bamboo_sag_hpback_def_email \
  -Dapp.hpback.at-email=$bamboo_sag_hpback_at_email \
  -Dapp.erpConfig.maxRequestSize=$bamboo_erp_max_request_size
cd ../

############################ Undeploy Web Applications ####################################
echo "########################################################"
echo "Undeploying wars"
curl -u $bamboo_sag_tomcat_user:$bamboo_sag_tomcat_password "$bamboo_sag_tomcat_host/manager/text/undeploy?path=/rest-ax"
curl -u $bamboo_sag_tomcat_user:$bamboo_sag_tomcat_password "$bamboo_sag_tomcat_host/manager/text/undeploy?path=/dvse"
curl -u $bamboo_sag_tomcat_user:$bamboo_sag_tomcat_password "$bamboo_sag_tomcat_host/manager/text/undeploy?path=/admin-ax"
curl -u $bamboo_sag_tomcat_user:$bamboo_sag_tomcat_password "$bamboo_sag_tomcat_host/manager/text/undeploy?path=/auth-server"

curl -u $bamboo_sag_tomcat_user:$bamboo_sag_tomcat_password "$bamboo_sag_tomcat_host/manager/text/undeploy?path=/derendinger-at"
curl -u $bamboo_sag_tomcat_user:$bamboo_sag_tomcat_password "$bamboo_sag_tomcat_host/manager/text/undeploy?path=/matik-at-ax"
curl -u $bamboo_sag_tomcat_user:$bamboo_sag_tomcat_password "$bamboo_sag_tomcat_host/manager/text/undeploy?path=/bo-ax"
