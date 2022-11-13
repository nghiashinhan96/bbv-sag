package com.sagag.services.oates.client;

import java.util.Locale;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.sagag.services.common.annotation.ChEshopIntegrationTest;
import com.sagag.services.oates.OatesApplication;
import com.sagag.services.oates.domain.OatesEquipmentProducts;
import com.sagag.services.oates.domain.OatesRecommendVehicles;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { OatesApplication.class })
@ChEshopIntegrationTest
public class OatesClientIT {

  private static final String KTYPE = "124808";
  private static final String EQUIPMENT_HREF = "/equipment"
      + "/a7_sportback_4ga_4gf_2_8_fsi_quattro_162kw_cvpa_FawEcBR7Y";
  private static final String GUID = "3405187513347080";

  @Autowired
  private OatesClient client;

  @Before
  public void setup() {
    LocaleContextHolder.setLocale(Locale.GERMAN);
  }

  @Test
  public void getOatesRecommendVehicles() {

    OatesRecommendVehicles oatesRecommendVehicles = client.getOatesRecommendVehicles(KTYPE,
        StringUtils.EMPTY, StringUtils.EMPTY);
    Assert.assertThat(!CollectionUtils.isEmpty(oatesRecommendVehicles.getEquipmentList()
        .getEquipment()), Matchers.is(true));
    Assert.assertThat(oatesRecommendVehicles.getEquipmentList().getEquipment().get(0).getHref(),
        Matchers.equalTo(EQUIPMENT_HREF));
  }

  @Test
  public void getOatesRecommendProducts() {

    OatesEquipmentProducts oatesEquipmentProducts =
        client.getOatesRecommendProducts(EQUIPMENT_HREF);

    Assert.assertThat(oatesEquipmentProducts.getEquipment().getGuid(),
        Matchers.equalTo(GUID));
    Assert.assertThat(oatesEquipmentProducts.getEquipment().getHref(),
        Matchers.equalTo(EQUIPMENT_HREF));
  }
}
