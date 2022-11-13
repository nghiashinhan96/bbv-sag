package com.sagag.services.service.payment;

import com.sagag.services.common.annotation.EshopIntegrationTest;
import com.sagag.services.domain.eshop.dto.PaymentSettingDto;
import com.sagag.services.service.SagServiceApplication;
import com.sagag.services.service.mail.ChangePasswordMailSender;

import org.junit.Assert;
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
@SpringBootTest(classes = { SagServiceApplication.class })
@EshopIntegrationTest
@Transactional
public class UserPaymentSettingsFormBuilderIT {

  @Autowired
  private UserPaymentSettingsFormBuilder builder;

  @Test
  public void shouldBuildUserPaymentSettingWith_ValidUserId() {
    final long userId = 27L;
    final PaymentSettingDto paymentSettings = builder.buildUserPaymentSetting(userId, false);
    Assert.assertNotNull(paymentSettings);
  }

  @Test(expected = IllegalArgumentException.class)
  public void shouldCannotBuildUserPaymentSettingWith_InvalidUserId() {
    final long userId = 0L;
    builder.buildUserPaymentSetting(userId, false);
  }

}
