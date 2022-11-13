package com.sagag.services.oates.converter;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.sagag.services.common.annotation.ChEshopIntegrationTest;
import com.sagag.services.oates.DataProvider;
import com.sagag.services.oates.OatesApplication;
import com.sagag.services.oates.client.OatesClient;
import com.sagag.services.oates.domain.OatesEquipmentProducts;
import com.sagag.services.oates.dto.OatesEquipmentProductsDto;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { OatesApplication.class })
@ChEshopIntegrationTest
public class OatesApplicationConverterIT {

  @Autowired
  private OatesApplicationConverter converter;

  @Autowired
  private OatesClient client;

  @Test
  public void testConvertOatesApplication() {
    final String href = DataProvider.HREF;
    final OatesEquipmentProducts equipmentProduct = client.getOatesRecommendProducts(href);
    final OatesEquipmentProductsDto applications = converter.apply(equipmentProduct);
    Assert.assertThat(applications.getApplications().isEmpty(), Matchers.is(false));
  }

}
