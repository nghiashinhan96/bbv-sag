package com.sagag.services.ivds.filter.vehicles.impl;

import com.sagag.services.common.annotation.EshopIntegrationTest;
import com.sagag.services.common.utils.SagJSONUtil;
import com.sagag.services.domain.eshop.dto.AdvanceVehicleModel;
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
public class ModelAggregationVehicleBuilderIT {

  @Autowired
  private ModelAggregationVehicleBuilder builder;

  private VehicleMakeModelTypeSearchRequest request = new VehicleMakeModelTypeSearchRequest();

  @Test
  public void test() {
    request.setMakeCode("5");
    request.setModelCode("6724");

    final Map<AggregationVehicleMode, List<Object>> aggMap = builder.buildAggregation(request);
    log.debug("{}", SagJSONUtil.convertObjectToPrettyJson(aggMap));
  }

  @Test
  public void shouldBuildAdvanceAggregation() {
    // Given
    request.setMakeCode("5");
    request.setVehicleType("type");
    request.setYearFrom("2022");
    // When
    AdvanceVehicleModel result = builder.buildAdvanceAggregation(request);
    // Then
    log.debug("{}", SagJSONUtil.convertObjectToPrettyJson(result));
  }
}
