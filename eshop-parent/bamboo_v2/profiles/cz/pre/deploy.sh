#!/usr/bin/env bash
##
## The deployment scripts for PRE-PRODUCTION env
##

############################ Verify variables ####################################
echo $bamboo_sag_tomcat_host_1
echo $bamboo_sag_tomcat_user
echo $bamboo_sag_tomcat_password
echo $bamboo_deploy_version
echo $bamboo_buildNumber
echo $bamboo_rest
echo $bamboo_soap
echo $bamboo_auth
echo $bamboo_st_cz
echo $bamboo_eh_st_cz

############################ Deploy Web Applications ####################################
echo "Deploy Back-end wars $bamboo_sag_tomcat_host_1"

curl -u $bamboo_sag_tomcat_user:$bamboo_sag_tomcat_password --upload-file "sag-rest/target/sag-rest-1.0-SNAPSHOT.war" "$bamboo_sag_tomcat_host_1/manager/text/deploy?path=/$bamboo_rest&update=true"
curl -u $bamboo_sag_tomcat_user:$bamboo_sag_tomcat_password --upload-file "sag-oauth2/target/sag-oauth2-1.0-SNAPSHOT.war" "$bamboo_sag_tomcat_host_1/manager/text/deploy?path=/$bamboo_auth&update=true"
curl -u $bamboo_sag_tomcat_user:$bamboo_sag_tomcat_password --upload-file "sag-dvse/target/sag-dvse-1.0-SNAPSHOT.war" "$bamboo_sag_tomcat_host_1/manager/text/deploy?path=/$bamboo_soap&update=true"
curl -u $bamboo_sag_tomcat_user:$bamboo_sag_tomcat_password --upload-file "sag-admin/target/sag-admin-1.0-SNAPSHOT.war" "$bamboo_sag_tomcat_host_1/manager/text/deploy?path=/$bamboo_admin&update=true"


echo "Deploy Front-end wars $bamboo_sag_tomcat_host_1"
curl -u $bamboo_sag_tomcat_user:$bamboo_sag_tomcat_password --upload-file "eshop-web/st-cz.war" "$bamboo_sag_tomcat_host_1/manager/text/deploy?path=/$bamboo_st_cz&update=true"
curl -u $bamboo_sag_tomcat_user:$bamboo_sag_tomcat_password --upload-file "eshop-web/eh.war" "$bamboo_sag_tomcat_host_1/manager/text/deploy?path=/$bamboo_eh_st_cz&update=true"
curl -u $bamboo_sag_tomcat_user:$bamboo_sag_tomcat_password --upload-file "eshop-backoffice/bo-st.war" "$bamboo_sag_tomcat_host_1/manager/text/deploy?path=/$bamboo_bo&update=true"
