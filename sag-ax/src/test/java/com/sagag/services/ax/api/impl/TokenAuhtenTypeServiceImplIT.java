package com.sagag.services.ax.api.impl;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.sagag.services.ax.AxApplication;
import com.sagag.services.ax.config.AxInitialResource;
import com.sagag.services.ax.token.TokenAuthenTypeService;
import com.sagag.services.common.annotation.EshopIntegrationTest;

import lombok.extern.slf4j.Slf4j;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { AxApplication.class })
@EshopIntegrationTest
@Slf4j
public class TokenAuhtenTypeServiceImplIT {

  @Autowired
  private TokenAuthenTypeService axProcessor;

  @Autowired
  private AxInitialResource axInitialResource;

  @Test
  public void testGetAxToken() {
    String expectedToken = axInitialResource.getAccessToken();
    String token = axProcessor.getAxToken();

    log.debug("ExpectedToken = \n{} \n ActualToken = \n{}", expectedToken, token);

    Assert.assertThat(token, Matchers.is(expectedToken));
  }

}
