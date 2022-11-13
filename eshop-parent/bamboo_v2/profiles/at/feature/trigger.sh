#!/usr/bin/env bash
##
## The build scripts for FEATURE env
##

############################ Verify variables ####################################
echo $bamboo_sag_tomcat_host
echo $bamboo_sag_rest_source
echo $bamboo_sag_rest_key

############################ Action Triggers ####################################
curl -X PUT \
  "$bamboo_sag_tomcat_host/rest-ax/cache/refresh?source=$bamboo_sag_rest_source&key=$bamboo_sag_rest_key" \
  -H 'cache-control: no-cache'
