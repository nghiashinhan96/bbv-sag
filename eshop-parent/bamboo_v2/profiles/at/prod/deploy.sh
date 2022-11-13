#!/usr/bin/env bash
##
## The deployment scripts for PRODUCTION env
##

bamboo_sag_tomcat_host=($bamboo_sag_tomcat_host_1 $bamboo_sag_tomcat_host_2)
############################ Verify variables ####################################
for tomcat in "${bamboo_sag_tomcat_host[@]}"
do
  echo "$tomcat"
done
echo $bamboo_sag_tomcat_user
echo $bamboo_sag_tomcat_password
echo $bamboo_deploy_version
echo $bamboo_buildNumber

############################ Deploy Web Applications ####################################
echo "####################################"
for tomcat in "${bamboo_sag_tomcat_host[@]}"
do
	if [[ ! -z "$tomcat" ]]
	then
	
		echo "Deploy Back-end wars $tomcat"
		
		curl -u $bamboo_sag_tomcat_user:$bamboo_sag_tomcat_password --upload-file "sag-rest/target/sag-rest-1.0-SNAPSHOT.war" "$tomcat/manager/text/deploy?path=/rest-at-ax&update=true"
		curl -u $bamboo_sag_tomcat_user:$bamboo_sag_tomcat_password --upload-file "sag-oauth2/target/sag-oauth2-1.0-SNAPSHOT.war" "$tomcat/manager/text/deploy?path=/auth-server-ax&update=true"
		curl -u $bamboo_sag_tomcat_user:$bamboo_sag_tomcat_password --upload-file "sag-dvse/target/sag-dvse-1.0-SNAPSHOT.war" "$tomcat/manager/text/deploy?path=/dvse-ax&update=true"
		curl -u $bamboo_sag_tomcat_user:$bamboo_sag_tomcat_password --upload-file "sag-admin/target/sag-admin-1.0-SNAPSHOT.war" "$tomcat/manager/text/deploy?path=/admin-ax&update=true"
		
		echo "Deploy Front-end wars $tomcat"
		curl -u $bamboo_sag_tomcat_user:$bamboo_sag_tomcat_password --upload-file "eshop-web/matik-at-ax.war" "$tomcat/manager/text/deploy?path=/mat-ax&update=true"
		curl -u $bamboo_sag_tomcat_user:$bamboo_sag_tomcat_password --upload-file "eshop-web/derendinger-at.war" "$tomcat/manager/text/deploy?path=/dat-ax&update=true"
		curl -u $bamboo_sag_tomcat_user:$bamboo_sag_tomcat_password --upload-file "eshop-backoffice/target/eshop-backoffice-1.0-SNAPSHOT.war" "$tomcat/manager/text/deploy?path=/bo-ax&update=true"


	fi
done
