package com.sagag.services.oates.converter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.IOUtils;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.test.context.junit4.SpringRunner;

import com.sagag.services.common.utils.SagJSONUtil;
import com.sagag.services.oates.domain.OatesEquipmentProducts;
import com.sagag.services.oates.dto.OatesEquipmentProductsDto;

@RunWith(SpringRunner.class)
public class OatesApplicationConverterTest {

  @InjectMocks
  private OatesApplicationConverter converter;

  @Test
  public void givenValidDataShouldConvertSuccessfully() throws IOException {
    final String json = IOUtils.toString(getClass()
        .getResourceAsStream("/json/equipment-products.json"), StandardCharsets.UTF_8);
    final OatesEquipmentProducts oatesEquipmentProducts = SagJSONUtil.convertJsonToObject(json,
        OatesEquipmentProducts.class);

    OatesEquipmentProductsDto equipment = converter.apply(oatesEquipmentProducts);
    Assert.assertThat(equipment.getApplications().isEmpty(), Matchers.is(false));
  }

}
