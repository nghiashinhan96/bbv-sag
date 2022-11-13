package com.sagag.eshop.service.api.impl;

import com.sagag.eshop.repo.entity.EshopUser;
import com.sagag.eshop.service.api.LoginService;
import com.sagag.eshop.service.api.UserService;
import com.sagag.eshop.service.app.ServiceApplication;
import com.sagag.services.common.annotation.EshopIntegrationTest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { ServiceApplication.class })
@EshopIntegrationTest
@Transactional
public class LoginServiceImplIT {

  @Autowired
  private UserService userService;

  @Autowired
  private LoginService loginService;

  private EshopUser eshopUser;

  @Before
  public void initEshopUser() {
    eshopUser = userService.getUsersByUsername("user_ddat_ax").stream().findFirst().get();
  }

  @Test
  public void testUpdateLastOnBehalfOfDate() {
    final Date currentDate = Calendar.getInstance().getTime();
    loginService.updateLastOnBehalfOfDate(eshopUser.getLogin().getId(), currentDate);
  }
}
