package com.sagag.services.ivds.filter.vehicles.impl;

import com.sagag.services.common.annotation.EshopIntegrationTest;
import com.sagag.services.common.utils.SagJSONUtil;
import com.sagag.services.domain.eshop.dto.TypeItem;
import com.sagag.services.ivds.app.IvdsApplication;
import com.sagag.services.ivds.filter.vehicles.AggregationVehicleMode;
import com.sagag.services.ivds.request.vehicle.VehicleMakeModelTypeSearchRequest;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.hamcrest.core.Is;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { IvdsApplication.class })
@EshopIntegrationTest
@Slf4j
public class TypeAggregationVehicleBuilderIT {

  @Autowired
  private TypeAggregationVehicleBuilder builder;

  private VehicleMakeModelTypeSearchRequest request = new VehicleMakeModelTypeSearchRequest();

  @Test
  public void testBuildTypeAggregation() {
    request.setMakeCode("5");
    request.setModelCode("6724");

    final Map<AggregationVehicleMode, List<Object>> aggMap = builder.buildAggregation(request);
    log.debug("{}", SagJSONUtil.convertObjectToPrettyJson(aggMap));
    if (!MapUtils.isEmpty(aggMap)) {
      List<TypeItem> types = aggMap.get(AggregationVehicleMode.TYPE).stream()
        .map(item -> (TypeItem) item).collect(Collectors.toList());
      log.debug("{}", SagJSONUtil.convertObjectToPrettyJson(types));
      Assert.assertThat(types.get(0).getDisplayType(), Is.is("1.2 TFSI 77 kW CBZB"));
      Assert.assertThat(types.get(1).getDisplayType(), Is.is("1.4 TFSI 92 kW CAXC"));
    }
  }
}
