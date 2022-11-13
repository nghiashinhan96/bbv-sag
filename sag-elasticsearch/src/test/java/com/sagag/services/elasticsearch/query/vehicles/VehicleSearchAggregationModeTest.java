package com.sagag.services.elasticsearch.query.vehicles;

import com.sagag.services.elasticsearch.query.vehicles.builder.AggregationKwHpQueryBuilder;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;

public class VehicleSearchAggregationModeTest {

  @Test
  public void shouldBuildScript_WithVehicleDesc() {
    final VehicleSearchAggregationMode mode = VehicleSearchAggregationMode.VEHICLE_POWER_KW_HP;
    Assert.assertThat(mode.getQueryBuilder(),
        Matchers.instanceOf(AggregationKwHpQueryBuilder.class));
  }

}
