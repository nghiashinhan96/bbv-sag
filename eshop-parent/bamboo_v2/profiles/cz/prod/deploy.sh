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
echo $bamboo_rest
echo $bamboo_soap
echo $bamboo_auth
echo $bamboo_admin
echo $bamboo_bo
echo $bamboo_st_cz
echo $bamboo_eh_st_cz

############################ Deploy Web Applications ####################################
for tomcat in "${bamboo_sag_tomcat_host[@]}"
do
	if [[ ! -z "$tomcat" ]]
	then
	  	echo "Deploy Back-end wars in tomcat server $tomcat"
		curl -u $bamboo_sag_tomcat_user:$bamboo_sag_tomcat_password --upload-file "sag-rest/target/sag-rest-1.0-SNAPSHOT.war" "$tomcat/manager/text/deploy?path=/$bamboo_rest&update=true"
		curl -u $bamboo_sag_tomcat_user:$bamboo_sag_tomcat_password --upload-file "sag-oauth2/target/sag-oauth2-1.0-SNAPSHOT.war" "$tomcat/manager/text/deploy?path=/$bamboo_auth&update=true"
		curl -u $bamboo_sag_tomcat_user:$bamboo_sag_tomcat_password --upload-file "sag-dvse/target/sag-dvse-1.0-SNAPSHOT.war" "$tomcat/manager/text/deploy?path=/$bamboo_soap&update=true"
		curl -u $bamboo_sag_tomcat_user:$bamboo_sag_tomcat_password --upload-file "sag-admin/target/sag-admin-1.0-SNAPSHOT.war" "$tomcat/manager/text/deploy?path=/$bamboo_admin&update=true"
		
		echo "Deploy Front-end wars $tomcat"
		curl -u $bamboo_sag_tomcat_user:$bamboo_sag_tomcat_password --upload-file "eshop-web/st-cz.war" "$tomcat/manager/text/deploy?path=/$bamboo_st_cz&update=true"
		curl -u $bamboo_sag_tomcat_user:$bamboo_sag_tomcat_password --upload-file "eshop-web/eh.war" "$tomcat/manager/text/deploy?path=/$bamboo_eh_st_cz&update=true"
		curl -u $bamboo_sag_tomcat_user:$bamboo_sag_tomcat_password --upload-file "eshop-backoffice/bo-st.war" "$tomcat/manager/text/deploy?path=/$bamboo_bo&update=true"
	fi
done
