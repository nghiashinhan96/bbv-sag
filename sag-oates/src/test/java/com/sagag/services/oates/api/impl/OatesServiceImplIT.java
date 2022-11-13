package com.sagag.services.oates.api.impl;

import java.util.Optional;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.sagag.services.common.annotation.ChEshopIntegrationTest;
import com.sagag.services.common.utils.SagJSONUtil;
import com.sagag.services.oates.OatesApplication;
import com.sagag.services.oates.api.OatesService;
import com.sagag.services.oates.dto.OatesEquipmentProductsDto;
import com.sagag.services.oates.dto.OatesVehicleDto;

import lombok.extern.slf4j.Slf4j;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { OatesApplication.class })
@ChEshopIntegrationTest
@Slf4j
public class OatesServiceImplIT {

  private static final String HREF = "/equipment/a7_sportback_4ga_4gf_2_8_fsi_quattro_162kw_cvpa_FawEcBR7Y";

  private static final String VEHICLE_ID = "V124808M32594";
  @Autowired
  private OatesService oatesService;

  @Test
  public void testSearchOatesVehicle_V124808M32594() throws Exception {

    Optional<OatesVehicleDto> result = oatesService.searchOatesVehicle(VEHICLE_ID);
    log.debug("result of searchOatesVehicle_V124808M32594: {}",
        SagJSONUtil.convertObjectToPrettyJson(result));
    Assert.assertThat(result.isPresent(), Matchers.is(true));
    if (result.isPresent()) {
      Assert.assertThat(result.get().getHref(), Matchers.equalTo(HREF));
    }
  }

  @Test
  public void testGetProductRecommendation_FawEcBR7Y() throws Exception {
    OatesEquipmentProductsDto result = oatesService.searchOatesEquipment(HREF);
    log.debug("result of getProductRecommendation_FawEcBR7Y: {}",
        SagJSONUtil.convertObjectToPrettyJson(result));
  }

  @Test
  public void testGetProductRecommendation_FawEcB0gM() throws Exception {
    final String href = "/equipment/tiguan_5n__2_0_tdi_4motion_103kw_FawEcB0gM";
    OatesEquipmentProductsDto result = oatesService.searchOatesEquipment(href);
    log.debug("result of getProductRecommendation_FawEcB0gM: {}",
        SagJSONUtil.convertObjectToPrettyJson(result));
  }

  @Test
  @Ignore("Data is out of date")
  public void testGetProductRecommendation_FawEcAzCW() throws Exception {
    final String href = "/equipment/golf_v_1k1_1_9_tdi_77kw_FawEcAzCW";
    OatesEquipmentProductsDto result = oatesService.searchOatesEquipment(href);
    log.debug("result of getProductRecommendation_FawEcAzCW: {}",
        SagJSONUtil.convertObjectToPrettyJson(result));
  }

}
