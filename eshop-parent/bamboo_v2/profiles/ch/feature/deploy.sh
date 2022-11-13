#!/usr/bin/env bash
##
## The deployment scripts for CH DEVELOPMENT env
##

############################ Verify variables ####################################
echo $bamboo_sag_tomcat_host
echo $bamboo_sag_tomcat_user
echo $bamboo_sag_tomcat_password
echo $bamboo_deploy_version
echo $bamboo_buildNumber

############################ Deploy Web Applications ####################################
echo "########################################################"
echo "Deploy Back-end wars"

curl -u $bamboo_sag_tomcat_user:$bamboo_sag_tomcat_password --upload-file "sag-rest/target/sag-rest-1.0-SNAPSHOT.war" "$bamboo_sag_tomcat_host/manager/text/deploy?path=/rest-ax&update=true"
curl -u $bamboo_sag_tomcat_user:$bamboo_sag_tomcat_password --upload-file "sag-oauth2/target/sag-oauth2-1.0-SNAPSHOT.war" "$bamboo_sag_tomcat_host/manager/text/deploy?path=/auth-server&update=true"
curl -u $bamboo_sag_tomcat_user:$bamboo_sag_tomcat_password --upload-file "sag-dvse/target/sag-dvse-1.0-SNAPSHOT.war" "$bamboo_sag_tomcat_host/manager/text/deploy?path=/dvse&update=true"
curl -u $bamboo_sag_tomcat_user:$bamboo_sag_tomcat_password --upload-file "sag-admin/target/sag-admin-1.0-SNAPSHOT.war" "$bamboo_sag_tomcat_host/manager/text/deploy?path=/admin-ax&update=true"

echo "Deploy Front-end wars"
curl -u $bamboo_sag_tomcat_user:$bamboo_sag_tomcat_password --upload-file "eshop-web/derendinger-ch.war" "$bamboo_sag_tomcat_host/manager/text/deploy?path=/derendinger-ch&update=true"
curl -u $bamboo_sag_tomcat_user:$bamboo_sag_tomcat_password --upload-file "eshop-web/technomag.war" "$bamboo_sag_tomcat_host/manager/text/deploy?path=/technomag&update=true"
curl -u $bamboo_sag_tomcat_user:$bamboo_sag_tomcat_password --upload-file "eshop-web/matik-ch-ax.war" "$bamboo_sag_tomcat_host/manager/text/deploy?path=/matik-ch-ax&update=true"
curl -u $bamboo_sag_tomcat_user:$bamboo_sag_tomcat_password --upload-file "eshop-web/wbb.war" "$bamboo_sag_tomcat_host/manager/text/deploy?path=/wbb&update=true"
curl -u $bamboo_sag_tomcat_user:$bamboo_sag_tomcat_password --upload-file "eshop-web/eh.war" "$bamboo_sag_tomcat_host/manager/text/deploy?path=/eh-ch&update=true"
curl -u $bamboo_sag_tomcat_user:$bamboo_sag_tomcat_password --upload-file "eshop-backoffice/bo-ch-ax.war" "$bamboo_sag_tomcat_host/manager/text/deploy?path=/bo-ch-ax&update=true"