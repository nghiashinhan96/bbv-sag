#!/usr/bin/env bash
##
## The deployment scripts for UMB PRODUCTION env
##

############################ Verify variables ####################################
echo $bamboo_sag_tomcat_host_1
echo $bamboo_sag_tomcat_user
echo $bamboo_sag_tomcat_password
echo $bamboo_deploy_version
echo $bamboo_buildNumber

############################ Deploy Web Applications ####################################
echo "Deploy Back-end wars $bamboo_sag_tomcat_host_1"
curl -u $bamboo_sag_tomcat_user:$bamboo_sag_tomcat_password --upload-file "sag-rest/target/sag-rest-1.0-SNAPSHOT.war" "$bamboo_sag_tomcat_host_1/manager/text/deploy?path=/rest-autonet&update=true"
curl -u $bamboo_sag_tomcat_user:$bamboo_sag_tomcat_password --upload-file "sag-oauth2/target/sag-oauth2-1.0-SNAPSHOT.war" "$bamboo_sag_tomcat_host_1/manager/text/deploy?path=/auth-server-autonet&update=true"

echo "Deploy Front-end wars $bamboo_sag_tomcat_host_1"
curl -u $bamboo_sag_tomcat_user:$bamboo_sag_tomcat_password --upload-file "eshop-autonet/autonet.war" "$bamboo_sag_tomcat_host_1/manager/text/deploy?path=/autonet&update=true"