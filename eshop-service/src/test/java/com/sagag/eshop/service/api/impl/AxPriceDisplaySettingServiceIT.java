package com.sagag.eshop.service.api.impl;

import com.sagag.eshop.repo.entity.CustomerSettings;
import com.sagag.eshop.service.api.impl.price.ATPriceDisplaySettingImpl;
import com.sagag.eshop.service.app.ServiceApplication;
import com.sagag.services.common.annotation.EshopIntegrationTest;
import com.sagag.services.domain.eshop.dto.PriceDisplaySettingDto;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import javax.transaction.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { ServiceApplication.class })
@EshopIntegrationTest
@Transactional
public class AxPriceDisplaySettingServiceIT {

  @Autowired
  private ATPriceDisplaySettingImpl atPriceDisplaySetting;

  @Test
  public void getPriceDisplaySetting_ShouldReturnForAt() {
    CustomerSettings customerSetting = CustomerSettings.builder().priceDisplayId(1).build();
    List<PriceDisplaySettingDto> result = atPriceDisplaySetting.getPriceDisplaySetting(customerSetting);
    Assert.assertThat(result.size(), Matchers.equalTo(1));
  }


}
