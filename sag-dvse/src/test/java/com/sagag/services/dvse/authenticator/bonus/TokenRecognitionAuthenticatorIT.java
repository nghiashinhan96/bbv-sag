package com.sagag.services.dvse.authenticator.bonus;

import javax.transaction.Transactional;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.sagag.services.common.annotation.EshopIntegrationTest;
import com.sagag.services.common.enums.HashType;
import com.sagag.services.dvse.DvseApplication;
import com.sagag.services.dvse.dto.bonus.RecognitionstateType;
import com.sagag.services.dvse.dto.bonus.ValidatedRecognition;
import com.sagag.services.incentive.authcookie.AuthCookieFactory;
import com.sagag.services.incentive.authcookie.CookiePrivacyException;
import com.sagag.services.incentive.config.IncentivePointProperties;
import com.sagag.services.incentive.config.IncentiveProperties;
import com.sagag.services.incentive.domain.IncentiveLoginDto;

/**
 * IT for {@link TokenRecognitionAuthenticator}.
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { DvseApplication.class })
@EshopIntegrationTest
@Transactional
@Ignore("Data for IT is not ready")
public class TokenRecognitionAuthenticatorIT {

  @Autowired
  private IncentiveProperties incentiveProps;
  @Autowired
  private TokenRecognitionAuthenticator authenticator;

  private IncentivePointProperties happyBonusInfo;

  @Before
  public void setUp() {
    happyBonusInfo = incentiveProps.getHappyBonus();
  }

  @Test
  public void shouldVerifyValidToken() throws CookiePrivacyException {
    final IncentiveLoginDto login = new IncentiveLoginDto();
    login.setUserName("nu1.ax");
    login.setType(HashType.BLCK_VAR);
    login.setPassword("123");

    final String token = AuthCookieFactory.createLoginToken(login, happyBonusInfo.getUrl(),
        happyBonusInfo.getSessionTimeoutMs());
    ValidatedRecognition recognition = authenticator.authenticate(token);
    Assert.assertThat(recognition.getRecognition().getState(),
        Matchers.is(RecognitionstateType.VALID));
  }

}
