#!/usr/bin/env bash
##
## The build scripts for CH PRODUCTION env
##

bamboo_sag_tomcat_host=($bamboo_sag_tomcat_host_1 $bamboo_sag_tomcat_host_2 $bamboo_sag_tomcat_host_3 $bamboo_sag_tomcat_host_4)
############################ Verify variables ####################################
for tomcat in "${bamboo_sag_tomcat_host[@]}"
do
  echo "$tomcat"
done
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
echo "####################################"
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

echo "Building Derendinger CH"
yarn run build--ddch-prod

echo "Packaging Derendinger CH web"
cd ../../../../
mvn clean install -Dmaven.test.skip=true -Pprod -Dbuild.customer=ch
cd ../

echo "Copying and Rename Derendinger CH war file"
cp eshop-web/target/*.war eshop-web/derendinger-ch.war

echo "Building Matik CH"
cd eshop-web/src/main/resources/client
yarn run build--mtch-prod

echo "Packaging Matik CH web"
cd ../../../../
mvn clean install -Dmaven.test.skip=true -Pprod -Dbuild.customer=ch
cd ../

echo "Copying and Rename Matik CH war file"
cp eshop-web/target/*.war eshop-web/matik-ch-ax.war

echo "Building Technomag CH"
cd eshop-web/src/main/resources/client
yarn run build--tnmch-prod

echo "Packaging Technomag CH web"
cd ../../../../
mvn clean install -Dmaven.test.skip=true -Pprod -Dbuild.customer=ch
cd ../

echo "Copying and Rename Technomag CH war file"
cp eshop-web/target/*.war eshop-web/technomag.war

echo "Building Walchli-Bollier-Bulach CH"
cd eshop-web/src/main/resources/client
yarn run build--wbb-prod

echo "Packaging Walchli-Bollier-Bulach CH web"
cd ../../../../
mvn clean install -Dmaven.test.skip=true -Pprod -Dbuild.customer=ch
cd ../

echo "Copying and Rename Walchli-Bollier-Bulach CH war file"
cp eshop-web/target/*.war eshop-web/wbb.war

echo "Building Wholesaler"
cd eshop-web/src/main/resources/client
yarn run build--ehch-prod

echo "Packaging Wholesaler web"
cd ../../../../
mvn clean install -Dmaven.test.skip=true -Pprod -Dbuild.customer=ch
cd ../

echo "Copying and Rename Wholesaler war file"
cp eshop-web/target/*.war eshop-web/eh.war

############################ Build Back Office Front-End ####################################
echo "####################################"
echo "Installing node_modules For Eshop Back Office"
pwd
ls

cd eshop-backoffice/src/main/resources/backoffice
rm -rf ./node_modules
yarn check
yarn install

echo "Building Back Office CH"
yarn run build--ch-prod
cd ../../../../
mvn clean install -Dmaven.test.skip=true -Pprod -Dbuild.customer=ch
cd ../

echo "Copying and Rename Back Office war file"
cp eshop-backoffice/target/*.war eshop-backoffice/bo-ch-ax.war

nvm use 8
node --version

############################ Build Back-End ####################################
echo "####################################"
echo "Buiding Back-end services"
pwd
ls

cd eshop-parent
mvn clean install -Pprod \
  -Dmaven.test.skip=true \
  -Dbuild.customer=ch \
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
echo "####################################"
for tomcat in "${bamboo_sag_tomcat_host[@]}"
do
	if [[ ! -z "$tomcat" ]]
	then
    echo "####################################"
		echo "Undeploying wars in tomcat server $tomcat"
		echo "Undeploying wars $tomcat"
    curl -u $bamboo_sag_tomcat_user:$bamboo_sag_tomcat_password "$tomcat/manager/text/undeploy?path=/rest-ch-ax"
    curl -u $bamboo_sag_tomcat_user:$bamboo_sag_tomcat_password "$tomcat/manager/text/undeploy?path=/dvse-ch-ax"
    curl -u $bamboo_sag_tomcat_user:$bamboo_sag_tomcat_password "$tomcat/manager/text/undeploy?path=/admin-ch-ax"
    curl -u $bamboo_sag_tomcat_user:$bamboo_sag_tomcat_password "$tomcat/manager/text/undeploy?path=/auth-server-ch-ax"

    curl -u $bamboo_sag_tomcat_user:$bamboo_sag_tomcat_password "$tomcat/manager/text/undeploy?path=/dch-ax"
    curl -u $bamboo_sag_tomcat_user:$bamboo_sag_tomcat_password "$tomcat/manager/text/undeploy?path=/tm-ax"
    curl -u $bamboo_sag_tomcat_user:$bamboo_sag_tomcat_password "$tomcat/manager/text/undeploy?path=/mch-ax"
    curl -u $bamboo_sag_tomcat_user:$bamboo_sag_tomcat_password "$tomcat/manager/text/undeploy?path=/wbb-ax"
    curl -u $bamboo_sag_tomcat_user:$bamboo_sag_tomcat_password "$tomcat/manager/text/undeploy?path=/eh-ch-ax"
    curl -u $bamboo_sag_tomcat_user:$bamboo_sag_tomcat_password "$tomcat/manager/text/undeploy?path=/bo-ch-ax"
	fi
done
