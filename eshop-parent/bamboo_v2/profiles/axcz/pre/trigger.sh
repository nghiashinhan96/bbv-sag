#!/usr/bin/env bash
##
## The build scripts for PRE-PRODUCTION env
##

############################ Verify variables ####################################
echo $bamboo_sag_tomcat_host
echo $bamboo_sag_rest_source
echo $bamboo_sag_rest_key
echo $bamboo_rest

############################ Action Triggers ####################################
curl -X PUT \
  "$bamboo_sag_tomcat_host/$bamboo_rest/cache/refresh?source=$bamboo_sag_rest_source&key=$bamboo_sag_rest_key" \
  -H 'cache-control: no-cache'
