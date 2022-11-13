package com.sagag.services.service.mail;

import com.sagag.eshop.repo.api.EshopUserRepository;
import com.sagag.eshop.repo.entity.EshopUser;
import com.sagag.eshop.service.app.ServiceApplication;
import com.sagag.services.common.annotation.EshopIntegrationTest;
import com.sagag.services.common.enums.DeliveryMethodType;
import com.sagag.services.common.enums.PaymentMethodType;
import com.sagag.services.common.enums.SendMethodType;
import com.sagag.services.common.enums.SupportedAffiliate;
import com.sagag.services.service.mail.orderconfirmation.OrderConfirmationCriteria;
import com.sagag.services.service.mail.orderconfirmation.OrderConfirmationMailSender;
import com.sagag.services.service.utils.ShoppingBasketTestUtils;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Collections;
import java.util.TimeZone;

/**
 * Class integration test for {@link OrderConfirmationMailSender}.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { ServiceApplication.class })
@EshopIntegrationTest
@Ignore
public class OrderConfirmationMailSenderIT {

  @Autowired
  private EshopUserRepository eshopUserRepo;

  @Autowired
  private OrderConfirmationMailSender sender;

  private EshopUser user;

  @Before
  public void init() {
    // Update user info to send to
    user = eshopUserRepo.findUserLoginByUserId(27L).orElse(null);
  }

  @Test
  public void testMailSenderWithPfand() {

    final OrderConfirmationCriteria criteria = new OrderConfirmationCriteria();
    criteria.setUserId(user.getId());
    criteria.setLangiso(user.getLangiso());
    criteria.setFirstName(user.getFirstName());
    criteria.setLastName(user.getLastName());
    criteria.setEmail(user.getEmail());
    criteria.setCustomerNr("1100005");
    criteria.setAffiliateInUrl(SupportedAffiliate.DERENDINGER_AT.getAffiliate());
    criteria.setAffiliateEmail("thinguyen@bbv.ch");
    criteria.setOrderRequest(ShoppingBasketTestUtils.createSampleOrderRequest());
    criteria.setShoppingCart(ShoppingBasketTestUtils.createSampleShoppingCartHasDepot(true));
    criteria.setAdditionalTextDocMap(Collections.emptyMap());
    criteria.setOrderNr("OrderNr-test");
    criteria.setSendMethodType(SendMethodType.PICKUP);
    criteria.setPaymentMethodType(PaymentMethodType.CARD);
    criteria.setDeliveryMethodType(DeliveryMethodType.NORMAL);
    criteria.setBillingAddress(ShoppingBasketTestUtils.createSampleAddress());
    criteria.setDeliveryAddress(ShoppingBasketTestUtils.createSampleAddress());
    criteria.setTimezone(TimeZone.getDefault());

    sender.send(criteria);
  }

}
