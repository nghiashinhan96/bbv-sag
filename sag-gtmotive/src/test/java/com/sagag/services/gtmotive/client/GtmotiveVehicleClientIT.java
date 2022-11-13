package com.sagag.services.gtmotive.client;

import com.sagag.services.common.annotation.EshopIntegrationTest;
import com.sagag.services.gtmotive.app.GtmotiveApplication;
import com.sagag.services.gtmotive.domain.response.GtVehicleInfoResponse;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { GtmotiveApplication.class })
@EshopIntegrationTest
public class GtmotiveVehicleClientIT {

  @Autowired
  private GtmotiveVehicleClient client;

  @Test
  public void shouldGetVehicleInfoFromVinCode_WUAZZZ8E27N903459() {
    final String vin = "WUAZZZ8E27N903459";
    final GtVehicleInfoResponse res = client.getVinDecoder(vin);
    Assert.assertThat(res.getResponseInfo().getVehicleInfoDec().getVin(), Matchers.is(vin));
  }

  @Test
  public void shouldGetVehicleInfoFromVinCode_U5YHN813GDL034339() {
    final String vin = "U5YHN813GDL034339";
    final GtVehicleInfoResponse res = client.getVinDecoder(vin);
    Assert.assertThat(res.getResponseInfo().getVehicleInfoDec().getVin(), Matchers.is(vin));
  }

}
