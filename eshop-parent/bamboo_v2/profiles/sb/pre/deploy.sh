#!/usr/bin/env bash
##
## The deployment scripts for PRE-PRODUCTION env
##

bamboo_sag_tomcat_host=($bamboo_sag_tomcat_host_1)
############################ Verify variables ####################################
for tomcat in "${bamboo_sag_tomcat_host[@]}"
do
  echo "$tomcat"
done

echo $bamboo_sag_tomcat_user
echo $bamboo_sag_tomcat_password
echo $bamboo_deploy_version
echo $bamboo_buildNumber
echo $bamboo_rest
echo $bamboo_soap
echo $bamboo_auth
echo $bamboo_wt_sb
echo $bamboo_admin
echo $bamboo_bo

############################ Deploy Web Applications ####################################
for tomcat in "${bamboo_sag_tomcat_host[@]}"
do
  echo "Deploy Back-end wars in tomcat server $tomcat"

  curl -u $bamboo_sag_tomcat_user:$bamboo_sag_tomcat_password --upload-file "sag-rest/target/sag-rest-1.0-SNAPSHOT.war" "$tomcat/manager/text/deploy?path=/$bamboo_rest&update=true"
  curl -u $bamboo_sag_tomcat_user:$bamboo_sag_tomcat_password --upload-file "sag-oauth2/target/sag-oauth2-1.0-SNAPSHOT.war" "$tomcat/manager/text/deploy?path=/$bamboo_auth&update=true"
  curl -u $bamboo_sag_tomcat_user:$bamboo_sag_tomcat_password --upload-file "sag-dvse/target/sag-dvse-1.0-SNAPSHOT.war" "$tomcat/manager/text/deploy?path=/$bamboo_soap&update=true"
  curl -u $bamboo_sag_tomcat_user:$bamboo_sag_tomcat_password --upload-file "sag-admin/target/sag-admin-1.0-SNAPSHOT.war" "$tomcat/manager/text/deploy?path=/$bamboo_admin&update=true"

  echo "Deploy Front-end wars in tomcat server $tomcat"
  curl -u $bamboo_sag_tomcat_user:$bamboo_sag_tomcat_password --upload-file "eshop-web/wt-sb.war" "$tomcat/manager/text/deploy?path=/$bamboo_wt_sb&update=true"
  curl -u $bamboo_sag_tomcat_user:$bamboo_sag_tomcat_password --upload-file "eshop-backoffice/target/eshop-backoffice-1.0-SNAPSHOT.war" "$tomcat/manager/text/deploy?path=/$bamboo_bo&update=true"

done
