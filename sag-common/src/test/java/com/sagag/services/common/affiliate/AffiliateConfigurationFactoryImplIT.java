package com.sagag.services.common.affiliate;

import com.sagag.services.common.CommonApplication;
import com.sagag.services.common.affiliate.impl.AffiliateConfigurationFactoryImpl;
import com.sagag.services.common.annotation.EshopIntegrationTest;
import com.sagag.services.common.enums.HashType;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { CommonApplication.class })
@EshopIntegrationTest
public class AffiliateConfigurationFactoryImplIT {

  @Autowired
  private AffiliateConfigurationFactoryImpl factory;

  @Test
  @Ignore("Run with At as default")
  public void verifyAttributesForCh() {
    HashType hashType = factory.getHashType();
    Assert.assertThat(hashType, Matchers.is(HashType.BLCK_VAR));
  }

  @Test
  public void verifyAttributesForAt() {
    HashType hashType = factory.getHashType();
    Assert.assertThat(hashType, Matchers.is(HashType.BCRYPT));
  }

}
