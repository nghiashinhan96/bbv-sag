package com.sagag.services.service.mail;

import com.sagag.eshop.service.app.ServiceApplication;
import com.sagag.services.common.annotation.EshopIntegrationTest;
import com.sagag.services.service.mail.orderconfirmation.OrderConfirmationMailSender;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Locale;

/**
 * Class integration test for {@link OrderConfirmationMailSender}.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { ServiceApplication.class })
@EshopIntegrationTest
@Ignore
public class DigiInvoiceMailSenderIT {

  @Autowired
  private DigiInvoiceMailSender sender;

  @Test
  public void testMailSenderWithPfand() {
    DigiInvoiceCriteria criteria = DigiInvoiceCriteria
            .buildCriteria("nguyencung3@gmail.com", Locale.GERMAN, "cungnguyen@bbv.ch",
                    "nguyencung4@gmail.com","1234 HCM city");
    sender.send(criteria);
  }
}
