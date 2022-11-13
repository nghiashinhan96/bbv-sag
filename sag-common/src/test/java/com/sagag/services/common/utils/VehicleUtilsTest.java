package com.sagag.services.common.utils;

import org.apache.commons.lang3.StringUtils;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class VehicleUtilsTest {

  @Test
  public void shouldBuildVehicleInfo() {
    final String brand = "VOLVO";
    final String model = "V40";
    final String vehName = "Estate (645) 1.9 DI";
    final String powerKw = "85";
    final String engine = "D 4192 T3";
    final String vehInfo = VehicleUtils.buildVehicleInfo(brand, model, vehName, powerKw, engine);
    Assert.assertThat(vehInfo, Matchers.equalTo("VOLVO V40 Estate (645) 1.9 DI 85 kW D 4192 T3"));
  }

  @Test
  public void shouldBuildVehicleTypeDesc() {
    final String vehName = "Estate (645) 1.9 DI";
    final String powerKw = "85";
    final String engine = "D 4192 T3";
    final String vehInfo = VehicleUtils.buildVehicleTypeDesc(vehName, powerKw, engine);
    Assert.assertThat(vehInfo, Matchers.equalTo("Estate (645) 1.9 DI 85 kW D 4192 T3"));
  }

  @Test
  public void shouldBuildVehId() {
    final String kType = "100";
    String vehId = VehicleUtils.buildVehicleId(kType, "001");
    Assert.assertThat(vehId, Matchers.is("V100M001"));

    Arrays.asList("0", StringUtils.EMPTY, null).stream()
      .map(mtnr -> VehicleUtils.buildVehicleId(kType, mtnr))
      .forEach(vehicleId -> Assert.assertThat(vehicleId, Matchers.is("V100M0")));
  }
  
  @Test
  public void extractKTypeFromVehIdTest() {
    final String vehicleId = "V124808M32594";
    String kType = VehicleUtils.extractKTypeFromVehId(vehicleId);
    Assert.assertThat(kType, Matchers.equalTo("124808"));
  }
}
