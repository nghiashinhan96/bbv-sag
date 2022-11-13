#!/usr/bin/env bash
##
## The build scripts for PRODUCTION
##

bamboo_sag_tomcat_host=($bamboo_sag_tomcat_host_1 $bamboo_sag_tomcat_host_2)
############################ Verify variables ####################################
for tomcat in "${bamboo_sag_tomcat_host[@]}"
do
  echo "$tomcat"
done
echo $bamboo_sag_rest_source
echo $bamboo_sag_rest_key
echo $bamboo_rest

############################ Action Triggers ####################################
for tomcat in "${bamboo_sag_tomcat_host[@]}"
do
	if [[ ! -z "$tomcat" ]]
	then
		curl -X PUT \
		  "$tomcat/$bamboo_rest/cache/refresh?source=$bamboo_sag_rest_source&key=$bamboo_sag_rest_key" \
		  -H 'cache-control: no-cache'
	fi	  
done