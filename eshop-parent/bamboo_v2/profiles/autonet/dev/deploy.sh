#!/usr/bin/env bash
##
## The deployment scripts for DEVELOPMENT env
##

############################ Verify variables ####################################
echo $bamboo_sag_tomcat_host
echo $bamboo_sag_tomcat_user
echo $bamboo_sag_tomcat_password
echo $bamboo_deploy_version
echo $bamboo_buildNumber

############################ Deploy Web Applications ####################################
echo "Deploy Back-end wars"

curl -u $bamboo_sag_tomcat_user:$bamboo_sag_tomcat_password --upload-file "sag-rest/target/sag-rest-1.0-SNAPSHOT.war" "$bamboo_sag_tomcat_host/manager/text/deploy?path=/rest-autonet&update=true"
curl -u $bamboo_sag_tomcat_user:$bamboo_sag_tomcat_password --upload-file "sag-oauth2/target/sag-oauth2-1.0-SNAPSHOT.war" "$bamboo_sag_tomcat_host/manager/text/deploy?path=/auth-server&update=true"

echo "Deploy Front-end wars"
curl -u $bamboo_sag_tomcat_user:$bamboo_sag_tomcat_password --upload-file "eshop-autonet/autonet.war" "$bamboo_sag_tomcat_host/manager/text/deploy?path=/autonet&update=true"
