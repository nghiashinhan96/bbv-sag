package com.sagag.services.oauth2.api.impl.user;

import com.sagag.services.common.annotation.EshopIntegrationTest;
import com.sagag.services.common.utils.SagJSONUtil;
import com.sagag.services.oauth2.OAuth2Application;

import lombok.extern.slf4j.Slf4j;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.transaction.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { OAuth2Application.class })
@EshopIntegrationTest
@Transactional
@Slf4j
public class FinalCustomerUserDetailsServiceImplIT {

  @Autowired
  private FinalCustomerUserDetailsServiceImpl service;

  @Test
  public void test() {
    UserDetails userDetails = service.loadUserByUsername("user.test.final");
    log.debug("{}", SagJSONUtil.convertObjectToPrettyJson(userDetails));
  }

}
