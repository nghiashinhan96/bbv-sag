#!/usr/bin/env bash
##
## The deployment scripts for PRE env
##

############################ Verify variables ####################################
echo $bamboo_sag_tomcat_host_1
echo $bamboo_sag_tomcat_user
echo $bamboo_sag_tomcat_password
echo $bamboo_deploy_version
echo $bamboo_buildNumber

############################ Deploy Web Applications ####################################
echo "####################################"
echo "Deploy Back-end wars $bamboo_sag_tomcat_host_1"

curl -u $bamboo_sag_tomcat_user:$bamboo_sag_tomcat_password --upload-file "sag-rest/target/sag-rest-1.0-SNAPSHOT.war" "$bamboo_sag_tomcat_host_1/manager/text/deploy?path=/rest-at-ax&update=true"
curl -u $bamboo_sag_tomcat_user:$bamboo_sag_tomcat_password --upload-file "sag-oauth2/target/sag-oauth2-1.0-SNAPSHOT.war" "$bamboo_sag_tomcat_host_1/manager/text/deploy?path=/auth-server-ax&update=true"
curl -u $bamboo_sag_tomcat_user:$bamboo_sag_tomcat_password --upload-file "sag-dvse/target/sag-dvse-1.0-SNAPSHOT.war" "$bamboo_sag_tomcat_host_1/manager/text/deploy?path=/dvse-ax&update=true"
curl -u $bamboo_sag_tomcat_user:$bamboo_sag_tomcat_password --upload-file "sag-admin/target/sag-admin-1.0-SNAPSHOT.war" "$bamboo_sag_tomcat_host_1/manager/text/deploy?path=/admin-ax&update=true"

echo "Deploy Front-end wars $bamboo_sag_tomcat_host_1"
curl -u $bamboo_sag_tomcat_user:$bamboo_sag_tomcat_password --upload-file "eshop-web/matik-at-ax.war" "$bamboo_sag_tomcat_host_1/manager/text/deploy?path=/mat-ax&update=true"
curl -u $bamboo_sag_tomcat_user:$bamboo_sag_tomcat_password --upload-file "eshop-web/derendinger-at.war" "$bamboo_sag_tomcat_host_1/manager/text/deploy?path=/dat-ax&update=true"
curl -u $bamboo_sag_tomcat_user:$bamboo_sag_tomcat_password --upload-file "eshop-backoffice/target/eshop-backoffice-1.0-SNAPSHOT.war" "$bamboo_sag_tomcat_host_1/manager/text/deploy?path=/bo-ax&update=true"
