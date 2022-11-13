package com.sagag.services.incentive.linkgenerator.impl;

import com.sagag.services.common.annotation.EshopIntegrationTest;
import com.sagag.services.common.enums.HashType;
import com.sagag.services.incentive.DataProvider;
import com.sagag.services.incentive.IncentiveApplication;
import com.sagag.services.incentive.authcookie.CookiePrivacyException;
import com.sagag.services.incentive.domain.IncentiveLoginDto;

import lombok.extern.slf4j.Slf4j;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { IncentiveApplication.class })
@EshopIntegrationTest
@Slf4j
public class BigPointLinkGeneratorImplIT {

  @Autowired
  private ChBigPointLinkGeneratorImpl generator;

  @Test
  public void testBigPointLinkGenerator() throws CookiePrivacyException {
    final String custNr = "1100005";
    testBigPointLinkGenerator(custNr, "bharani", "FdcFONWLNYYKY", HashType.BLCK_VAR);
  }

  private void testBigPointLinkGenerator(String custNr, String username, String password,
      HashType hashType)
      throws CookiePrivacyException {

    final IncentiveLoginDto login = generator.buildLogin(
        DataProvider.customerNr(custNr),
        DataProvider.username(username),
        DataProvider.passwordHash(password, hashType));

    Assert.assertThat(login.getCustNr(), Matchers.is(custNr));
    Assert.assertThat(login.getUserName(), Matchers.nullValue());
    Assert.assertThat(login.getPassword(), Matchers.nullValue());
    Assert.assertThat(login.getType(), Matchers.nullValue());

    final String link = generator.generate(login);
    log.debug("Big Points Link = {}", link);

    Assert.assertThat(link, Matchers.startsWith("https://shop.connexservice.com/login.php?id="));
  }
}
