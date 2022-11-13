package com.sagag.services.ivds.filter.vehicles.impl;

import com.sagag.services.common.utils.SagJSONUtil;
import com.sagag.services.domain.eshop.dto.TypeItem;
import com.sagag.services.elasticsearch.api.VehicleSearchService;
import com.sagag.services.elasticsearch.domain.vehicle.VehicleDoc;
import com.sagag.services.ivds.filter.vehicles.AggregationVehicleMode;
import com.sagag.services.ivds.request.vehicle.VehicleMakeModelTypeSearchRequest;
import com.sagag.services.ivds.utils.IvdsDataTestUtils;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.hamcrest.core.Is;
import org.junit.Assert;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
@Slf4j
public class TypeAggregationVehicleBuilderTest {

  @InjectMocks
  private TypeAggregationVehicleBuilder builder;

  @Mock
  private VehicleSearchService vehicleSearchService;

  private VehicleMakeModelTypeSearchRequest request = new VehicleMakeModelTypeSearchRequest();

  @Test
  public void shouldBeSortedType() {
    request.setMakeCode("213");
    request.setModelCode("214");
    request.setFuelType(StringUtils.EMPTY);
    request.setYearFrom(StringUtils.EMPTY);
    List<VehicleDoc> docs = IvdsDataTestUtils.getMockedVehicleDocList();
    when(vehicleSearchService.searchTypesByMakeModel(213, 214, StringUtils.EMPTY,
      StringUtils.EMPTY)).thenReturn(docs);
    assertTrue("2.0 i".equals(docs.get(0).getVehicleName()));
    assertTrue("1.6 i KAT".equals(docs.get(1).getVehicleName()));

    final Map<AggregationVehicleMode, List<Object>> aggMap = builder.buildAggregation(request);
    log.debug("{}", SagJSONUtil.convertObjectToPrettyJson(aggMap));
    if (!MapUtils.isEmpty(aggMap)) {
      List<TypeItem> types = aggMap.get(AggregationVehicleMode.TYPE).stream()
        .map(item -> (TypeItem) item).collect(Collectors.toList());
      log.debug("{}", SagJSONUtil.convertObjectToPrettyJson(types));
      Assert.assertThat(types.get(0).getVehicleName(), Is.is("1.6 i KAT"));
      Assert.assertThat(types.get(1).getVehicleName(), Is.is("2.0 i"));

    }
  }
}
