package com.sagag.services.service.mail;

import com.sagag.eshop.service.app.ServiceApplication;
import com.sagag.services.common.annotation.EshopIntegrationTest;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * IT for {@link ChangePasswordMailSender}.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { ServiceApplication.class })
@EshopIntegrationTest
@Transactional
@Ignore("Enable will spam")
public class RegisterUserMailSenderIT {

  @Autowired
  private RegisterUserMailSender registerUserMailSender;

  @Test
  public void createSendConfirmEmailJob_shouldSendToCorrectEmail_givenFullyInfo() throws Exception {

    RegisterAccountCriteria criteria =
        RegisterAccountCriteria
            .builder()
            .email("danhnguyen@bbv.ch1")
            .username("a.danh.24")
            .affiliateEmail("shop@derendinger.at")
            .accessUrl(
                "http://ax.sib-services.ch/derendinger-at/forgotpassword/verifycode"
                + "?code=711303&reg=$2a$10$fOcFNqAK4yGz/rZGhBD5v.ih8VXvi74492INvNOJqLSoI7Z4INiaO")
            .companyName("Derendinger-Austria").build();
    registerUserMailSender.createSendConfirmEmailJob(criteria, false);

  }
}
