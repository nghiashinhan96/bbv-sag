#!/usr/bin/env bash
##
## The build scripts for PRE PRODUCTION env
##

############################ Verify variables ####################################
echo $bamboo_sag_tomcat_host_1
echo $bamboo_sag_tomcat_user
echo $bamboo_sag_tomcat_password

echo $bamboo_rest
echo $bamboo_soap
echo $bamboo_auth
echo $bamboo_admin
echo $bamboo_bo
echo $bamboo_st_cz
echo $bamboo_eh_st_cz

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


############################ Build Eshop Web Front-End ####################################
echo "Running deployment process by scripts"

export NVM_DIR=~/.nvm
source ~/.nvm/nvm.sh

nvm use 12
node --version

echo "####################################"
echo "Installing node_modules For Eshop Web"
pwd
ls

cd eshop-web/src/main/resources/client
rm -rf ./node_modules
yarn check
yarn install

yarn run theme
yarn run build:lib

echo "Building Stakis Czech"
yarn run build--st-cz-pre

echo "Packaging Stakis Czech web"
cd ../../../../
mvn -U clean install -Dmaven.test.skip=true -Ppre -Dbuild.customer=cz
cd ../

echo "Copying and Rename Stakis Czech war file"
cp eshop-web/target/*.war eshop-web/st-cz.war

echo "Building Wholesaler"
cd eshop-web/src/main/resources/client
yarn run build--ehcz-pre

echo "Packaging Wholesaler web"
cd ../../../../
mvn -U clean install -Dmaven.test.skip=true -Ppre -Dbuild.customer=cz
cd ../

echo "Copying and Rename Wholesaler war file"
cp eshop-web/target/*.war eshop-web/eh.war

############################ Build Back Office Front-End ####################################
echo "Installing node_modules For Eshop Back Office"
cd eshop-backoffice/src/main/resources/backoffice
rm -rf ./node_modules
yarn check
yarn install

echo "Building Back Office"
yarn run build--cz-pre
cd ../../../../
pwd
ls
mvn clean install -Dmaven.test.skip=true -Ppre -Dbuild.customer=cz
cd ../

echo "Copying and Rename Back Office war file"
cp eshop-backoffice/target/*.war eshop-backoffice/bo-st.war

############################ Build Back-End ####################################
echo "Buiding Back-end services"
ls -a
cd eshop-parent
ls -a
mvn -U clean install -Ppre \
  -Dbuild.customer=cz \
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
  -Dapp.hpback.at-email=$bamboo_sag_hpback_at_email
cd ../

nvm use 8
node --version

############################ Undeploy Web Applications ####################################
echo "Undeploying wars $bamboo_sag_tomcat_host_1"
curl -u $bamboo_sag_tomcat_user:$bamboo_sag_tomcat_password "$bamboo_sag_tomcat_host_1/manager/text/undeploy?path=/$bamboo_auth"
curl -u $bamboo_sag_tomcat_user:$bamboo_sag_tomcat_password "$bamboo_sag_tomcat_host_1/manager/text/undeploy?path=/$bamboo_rest"
curl -u $bamboo_sag_tomcat_user:$bamboo_sag_tomcat_password "$bamboo_sag_tomcat_host_1/manager/text/undeploy?path=/$bamboo_soap"
curl -u $bamboo_sag_tomcat_user:$bamboo_sag_tomcat_password "$bamboo_sag_tomcat_host_1/manager/text/undeploy?path=/$bamboo_admin"

curl -u $bamboo_sag_tomcat_user:$bamboo_sag_tomcat_password "$bamboo_sag_tomcat_host_1/manager/text/undeploy?path=/$bamboo_st_cz"
curl -u $bamboo_sag_tomcat_user:$bamboo_sag_tomcat_password "$bamboo_sag_tomcat_host_1/manager/text/undeploy?path=/$bamboo_eh_st_cz"
curl -u $bamboo_sag_tomcat_user:$bamboo_sag_tomcat_password "$bamboo_sag_tomcat_host_1/manager/text/undeploy?path=/$bamboo_bo"
