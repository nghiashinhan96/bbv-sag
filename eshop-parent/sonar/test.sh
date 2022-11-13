#!/usr/bin/env bash

echo $bamboo_sag_jdbc_url
echo $bamboo_sag_db_name
echo $bamboo_sag_db_username
echo $bamboo_sag_db_password
echo $bamboo_sag_cache_members

cd eshop-parent

mvn clean install -Pdev sonar:sonar \
  -Dskip.integration.tests=false \
  -Dsonar.host.url=${bamboo_sonarUrl} \
  -Dsonar.projectKey=${bamboo_sonarProjectKey} \
  -Dsonar.login=${bamboo_sonarToken} \
  -Dapp.cache.tcp-ip-config.members=$bamboo_sag_cache_members \
  -Dapp.db.jdbc-url=$bamboo_sag_jdbc_url \
  -Dapp.db.db-name=$bamboo_sag_db_name \
  -Dapp.db.username=$bamboo_sag_db_username \
  -Dapp.db.password=$bamboo_sag_db_password


export NVM_DIR=~/.nvm
source ~/.nvm/nvm.sh

nvm use 12
node --version

cd ../eshop-web/src/main/resources/client
yarn
yarn run sonar

nvm use 8
node --version