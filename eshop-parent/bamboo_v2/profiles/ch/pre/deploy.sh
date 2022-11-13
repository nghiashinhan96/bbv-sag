#!/usr/bin/env bash
##
## The deployment scripts for CH PRE env
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
for tomcat in "${bamboo_sag_tomcat_host[@]}"
do
	if [[ ! -z "$tomcat" ]]
	then
	
		echo "############################"
		echo "Deploy Back-end wars $tomcat"
		curl -u $bamboo_sag_tomcat_user:$bamboo_sag_tomcat_password --upload-file "sag-rest/target/sag-rest-1.0-SNAPSHOT.war" "$tomcat/manager/text/deploy?path=/rest-ch-ax&update=true"
		curl -u $bamboo_sag_tomcat_user:$bamboo_sag_tomcat_password --upload-file "sag-oauth2/target/sag-oauth2-1.0-SNAPSHOT.war" "$tomcat/manager/text/deploy?path=/auth-server-ch-ax&update=true"
		curl -u $bamboo_sag_tomcat_user:$bamboo_sag_tomcat_password --upload-file "sag-dvse/target/sag-dvse-1.0-SNAPSHOT.war" "$tomcat/manager/text/deploy?path=/dvse-ch-ax&update=true"
		curl -u $bamboo_sag_tomcat_user:$bamboo_sag_tomcat_password --upload-file "sag-admin/target/sag-admin-1.0-SNAPSHOT.war" "$tomcat/manager/text/deploy?path=/admin-ch-ax&update=true"
		
		echo "Deploy Front-end wars $tomcat"
		curl -u $bamboo_sag_tomcat_user:$bamboo_sag_tomcat_password --upload-file "eshop-web/derendinger-ch.war" "$tomcat/manager/text/deploy?path=/dch-ax&update=true"
		curl -u $bamboo_sag_tomcat_user:$bamboo_sag_tomcat_password --upload-file "eshop-web/technomag.war" "$tomcat/manager/text/deploy?path=/tm-ax&update=true"
		curl -u $bamboo_sag_tomcat_user:$bamboo_sag_tomcat_password --upload-file "eshop-web/matik-ch-ax.war" "$tomcat/manager/text/deploy?path=/mch-ax&update=true"
		curl -u $bamboo_sag_tomcat_user:$bamboo_sag_tomcat_password --upload-file "eshop-web/wbb.war" "$tomcat/manager/text/deploy?path=/wbb-ax&update=true"
		curl -u $bamboo_sag_tomcat_user:$bamboo_sag_tomcat_password --upload-file "eshop-web/eh.war" "$tomcat/manager/text/deploy?path=/eh-ch-ax&update=true"
		curl -u $bamboo_sag_tomcat_user:$bamboo_sag_tomcat_password --upload-file "eshop-backoffice/target/eshop-backoffice-1.0-SNAPSHOT.war" "$tomcat/manager/text/deploy?path=/bo-ch-ax&update=true"
	fi
done
