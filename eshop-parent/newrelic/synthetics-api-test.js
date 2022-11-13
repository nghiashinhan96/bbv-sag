/**
 * Feel free to explore, or check out the full documentation
 * https://docs.newrelic.com/docs/synthetics/new-relic-synthetics/scripting-monitors/writing-api-tests
 * for details.
 */

var assert = require('assert');

var restUrl = 'http://at.bbv-demo.ch/rest-ax/actuator/health';
var authUrl = 'http://at.bbv-demo.ch/auth-server/actuator/health';
var adminUrl = 'http://at.bbv-demo.ch/admin-ax/actuator/health';
var dvseUrl = 'http://at.bbv-demo.ch/dvse/actuator/health';

var apiUrls = [{url: restUrl, message: 'The RESTful API is down'},
                {url: authUrl, message: 'The Authorization App is down'},
                {url: adminUrl, message: 'The Admin App is down'},
                {url: dvseUrl, message: 'The DVSE App is down'}];

apiUrls.forEach(function(ele, index) {
  $http.get(ele.url,
    // Callback
    function (err, response, body) {
      verifyOK(err, response, body, ele.message);
    }
  );
});

function verifyOK(err, response, body, errMsg) {
  if (err) {
      throw new Error(errMsg)
    }
    assert.equal(response.statusCode, 200, errMsg);
    console.log('Response:', body);
}