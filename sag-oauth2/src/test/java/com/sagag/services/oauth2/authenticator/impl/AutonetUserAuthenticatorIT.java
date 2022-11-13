package com.sagag.services.oauth2.authenticator.impl;

import com.sagag.services.common.annotation.AutonetEshopIntegrationTest;
import com.sagag.services.oauth2.DataProvider;
import com.sagag.services.oauth2.OAuth2Application;

import lombok.extern.slf4j.Slf4j;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.transaction.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { OAuth2Application.class })
@AutonetEshopIntegrationTest
@Transactional
@Slf4j
@Ignore
public class AutonetUserAuthenticatorIT {

  @Autowired
  private AutonetUserAuthenticator authenticator;

  @Test
  public void testAuthenticatedValidRequest1() {
    OAuth2AccessToken accessToken = authenticator.authenticate(
        DataProvider.buildVisitRegistration());
    log.debug("{}", accessToken);
  }

  @Test
  public void testAuthenticatedValidRequest2() {
    OAuth2AccessToken accessToken = authenticator.authenticate(
        DataProvider.buildSampleVisitRegistration());
    log.debug("{}", accessToken);
  }
}
