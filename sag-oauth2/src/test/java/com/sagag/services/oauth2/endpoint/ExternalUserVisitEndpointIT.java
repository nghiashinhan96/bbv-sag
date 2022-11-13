package com.sagag.services.oauth2.endpoint;

import com.sagag.services.common.annotation.AutonetEshopIntegrationTest;
import com.sagag.services.common.enums.SupportedAffiliate;
import com.sagag.services.oauth2.DataProvider;
import com.sagag.services.oauth2.OAuth2Application;

import lombok.extern.slf4j.Slf4j;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.transaction.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { OAuth2Application.class })
@AutonetEshopIntegrationTest
@Transactional
@Slf4j
@Ignore
public class ExternalUserVisitEndpointIT {

  @Autowired
  private ExternalUserVisitEndpoint endpoint;

  @Test
  public void test() {
    ResponseEntity<OAuth2AccessToken> accessToken = endpoint.visit(null,
        SupportedAffiliate.AUTONET_HUNGARY.getAffiliate(), DataProvider.buildVisitRegistration());
    log.debug("{}", accessToken);
  }

}
