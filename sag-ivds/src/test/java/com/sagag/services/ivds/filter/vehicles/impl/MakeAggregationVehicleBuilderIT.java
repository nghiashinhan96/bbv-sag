package com.sagag.services.ivds.filter.vehicles.impl;

import com.sagag.services.common.annotation.EshopIntegrationTest;
import com.sagag.services.common.enums.SupportedAffiliate;
import com.sagag.services.common.utils.SagJSONUtil;
import com.sagag.services.hazelcast.api.impl.MakeCacheServiceImpl;
import com.sagag.services.ivds.app.IvdsApplication;
import com.sagag.services.ivds.filter.vehicles.AggregationVehicleMode;
import com.sagag.services.ivds.request.vehicle.VehicleMakeModelTypeSearchRequest;

import lombok.extern.slf4j.Slf4j;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { IvdsApplication.class })
@EshopIntegrationTest
@Slf4j
public class MakeAggregationVehicleBuilderIT {

  @Autowired
  private MakeAggregationVehicleBuilder builder;

  @Autowired
  private MakeCacheServiceImpl makeCacheService;

  private VehicleMakeModelTypeSearchRequest request = new VehicleMakeModelTypeSearchRequest();

  @Test
  public void testBuildAggregation() {
    makeCacheService.refreshCacheAll();

    request.setAffiliate(SupportedAffiliate.DERENDINGER_CH);
    request.setVehicleType("pc");
    final Map<AggregationVehicleMode, List<Object>> aggMap = builder.buildAggregation(request);
    log.debug("{}", SagJSONUtil.convertObjectToPrettyJson(aggMap));
  }
}
