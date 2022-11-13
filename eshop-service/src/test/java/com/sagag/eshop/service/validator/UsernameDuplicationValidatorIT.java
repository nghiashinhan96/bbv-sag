package com.sagag.eshop.service.validator;

import com.sagag.eshop.service.app.ServiceApplication;
import com.sagag.eshop.service.exception.UsernameDuplicationException;
import com.sagag.eshop.service.validator.criteria.UsernameDuplicationCriteria;
import com.sagag.services.common.annotation.EshopIntegrationTest;
import com.sagag.services.common.enums.SupportedAffiliate;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.transaction.Transactional;

/**
 * IT to verify for {@link UsernameDuplicationValidator}.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { ServiceApplication.class })
@EshopIntegrationTest
@Transactional
public class UsernameDuplicationValidatorIT {

  @Autowired
  private UsernameDuplicationValidator validator;

  @Test(expected = UsernameDuplicationException.class)
  public void givenDuplicateUserName_shouldThrowDuplicatedUsernameInAffiliateException()
      throws UsernameDuplicationException {
    validator.validate(new UsernameDuplicationCriteria("tuan2.ax",
        SupportedAffiliate.DERENDINGER_AT.getAffiliate()));
  }

}
