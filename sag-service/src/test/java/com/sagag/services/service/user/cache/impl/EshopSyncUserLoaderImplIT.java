package com.sagag.services.service.user.cache.impl;

import com.sagag.eshop.service.app.ServiceApplication;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.common.annotation.EshopIntegrationTest;
import com.sagag.services.domain.eshop.common.EshopAuthority;

import org.apache.commons.lang.StringUtils;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Description;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { ServiceApplication.class })
@EshopIntegrationTest
@Transactional
public class EshopSyncUserLoaderImplIT {

  private static final long SYS_ADMIN_USER_ID = 1l;

  private static final long SALES_USER_ID = 5l;

  private static final long USER_ADMIN_USER_ID = 26l;

  private static final long FINAL_USER_ADMIN_USER_ID = 30857l;

  @Autowired
  private EshopSyncUserLoaderImpl eshopSyncUserLoader;

  @Test
  public void testLoadSysAdminUserById() {
    UserInfo user = eshopSyncUserLoader.load(SYS_ADMIN_USER_ID, StringUtils.EMPTY,StringUtils.EMPTY,
        Optional.empty());
    assertUserInfo(user, EshopAuthority.SYSTEM_ADMIN);
  }

  @Test
  public void testLoadSalesUserById() {
    UserInfo user = eshopSyncUserLoader.load(SALES_USER_ID, StringUtils.EMPTY, StringUtils.EMPTY,
        Optional.empty());
    assertUserInfo(user, EshopAuthority.SALES_ASSISTANT);
  }

  @Test
  @Ignore("Not found customer number")
  public void testLoadUserAdminUserById() {
    UserInfo user = eshopSyncUserLoader.load(USER_ADMIN_USER_ID, StringUtils.EMPTY,
        StringUtils.EMPTY,Optional.empty());
    assertUserInfo(user, EshopAuthority.USER_ADMIN);
  }

  @Test(expected = IllegalArgumentException.class)
  @Description("Data is out to date")
  public void testLoadWholesalerUserAdminUserById() {
    UserInfo user = eshopSyncUserLoader.load(FINAL_USER_ADMIN_USER_ID, StringUtils.EMPTY,
        StringUtils.EMPTY, Optional.empty());
    assertUserInfo(user, EshopAuthority.FINAL_USER_ADMIN);
  }

  private void assertUserInfo(UserInfo actual, EshopAuthority expAuth) {
    Assert.assertThat(actual, Matchers.notNullValue());
    Assert.assertThat(actual.getRoleName(), Matchers.is(expAuth.name()));
  }
}
