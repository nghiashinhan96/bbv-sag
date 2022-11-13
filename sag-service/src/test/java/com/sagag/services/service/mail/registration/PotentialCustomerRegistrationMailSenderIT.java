package com.sagag.services.service.mail.registration;

import com.sagag.eshop.service.app.ServiceApplication;
import com.sagag.services.common.annotation.EshopIntegrationTest;
import com.sagag.services.service.dto.registration.PotentialCustomerRegistrationDto;
import com.sagag.services.service.dto.registration.PotentialCustomerRegistrationField;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.Locale;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { ServiceApplication.class })
@EshopIntegrationTest
@Ignore
public class PotentialCustomerRegistrationMailSenderIT {

  @Autowired
  private PotentialCustomerRegistrationMailSender mailSender;

  @Test
  public void send_shouldSendEmailToMarketingDept_givenCriteria() throws Exception {
    PotentialCustomerRegistrationField company = PotentialCustomerRegistrationField.builder()
        .key("COMPANY").value("Company A").title("Company").build();
    PotentialCustomerRegistrationField email = PotentialCustomerRegistrationField.builder()
        .key("EMAIL").value("kaka@bbv.ch").title("Email").build();
    PotentialCustomerRegistrationDto model = PotentialCustomerRegistrationDto.builder()
        .collectionShortName("derendinger-at")
        .fields(Arrays.asList(company, email)).langCode("de").build();

    PotentialCustomerRegistrationCriteria criteria = PotentialCustomerRegistrationCriteria
        .of("toEmail@bbv.ch", "fromEmail@bbv.ch", model, Locale.GERMAN);
    mailSender.send(criteria);
  }
}
