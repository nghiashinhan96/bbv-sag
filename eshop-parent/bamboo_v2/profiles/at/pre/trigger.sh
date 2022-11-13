#!/usr/bin/env bash
##
## The build scripts for TESTING
##

############################ Verify variables ####################################
echo $bamboo_sag_tomcat_host_1
##echo $bamboo_sag_tomcat_host_2
echo $bamboo_sag_rest_source
echo $bamboo_sag_rest_key

############################ Action Triggers ####################################
curl -X PUT \
  "$bamboo_sag_tomcat_host_1/rest-at-ax/cache/refresh?source=$bamboo_sag_rest_source&key=$bamboo_sag_rest_key" \
  -H 'cache-control: no-cache'

##curl -X PUT \
##  "$bamboo_sag_tomcat_host_2/rest-at-ax/cache/refresh?source=$bamboo_sag_rest_source&key=$bamboo_sag_rest_key" \
##  -H 'cache-control: no-cache'