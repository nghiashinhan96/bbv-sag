package com.sagag.services.ivds.api.impl;

import com.sagag.services.common.annotation.EshopIntegrationTest;
import com.sagag.services.domain.eshop.dto.VehicleDto;
import com.sagag.services.elasticsearch.criteria.vehicle.VehicleFilteringCriteria;
import com.sagag.services.elasticsearch.criteria.vehicle.VehicleSearchCriteria;
import com.sagag.services.elasticsearch.criteria.vehicle.VehicleSearchTermCriteria;
import com.sagag.services.ivds.api.IvdsVehicleService;
import com.sagag.services.ivds.app.IvdsApplication;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.lang.StringUtils;
import org.hamcrest.Matchers;
import org.hamcrest.core.Is;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * IT to verify {@link IvdsVehicleService}.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { IvdsApplication.class })
@EshopIntegrationTest
public class IvdsVehicleServiceImplIT {

  private static final String VEHID_V5281M4978 = "V5281M4978";
  private static final String ENGCODE = "MTAD";
  private static final String MAKECODE_VW04801 = "VW04801";
  private static final Object VEHID_V18004M18081 = "V18004M18081";

  @Autowired
  private IvdsVehicleService ivdsVehicleService;

  @Test
  public void testSearchGtmotiveVehicle() throws Exception {
    String gtModelCode = "CB05";

    final VehicleDto vehicle =
        ivdsVehicleService.searchGtmotiveVehicle(MAKECODE_VW04801, Arrays.asList(gtModelCode),
            Arrays.asList(ENGCODE), Collections.emptyList(), StringUtils.EMPTY).orElse(null);

    Assert.assertThat(vehicle, Matchers.notNullValue());
  }

  @Test
  public void testSearchVehicleByVehDataWithTypenschein() {
    final String vehData = "1V6701";
    final PageRequest pageRequest = PageRequest.of(0, 10);
    final VehicleSearchTermCriteria term = VehicleSearchTermCriteria.builder().vehicleData(vehData).build();
    final VehicleSearchCriteria request = VehicleSearchCriteria.builder().searchTerm(term).build();
    final Page<VehicleDto> vehicle =
      ivdsVehicleService.searchVehiclesByCriteria(request, pageRequest).getVehicles();
    Assert.assertThat(vehicle.getTotalElements(), Is.is(1L));
    Assert.assertThat(vehicle.getContent().get(0).getVehId(), Matchers.is(VEHID_V5281M4978));
  }

  @Test
  public void testSearchVehicleByVehDataWithNationalCode() {
    final String vehData = "24924";
    final PageRequest pageRequest = PageRequest.of(0, 10);
    final VehicleSearchTermCriteria term = VehicleSearchTermCriteria.builder().vehicleData(vehData).build();
    final VehicleSearchCriteria request = VehicleSearchCriteria.builder().searchTerm(term).build();
    final Page<VehicleDto> vehicle =
      ivdsVehicleService.searchVehiclesByCriteria(request, pageRequest).getVehicles();
    Assert.assertThat(vehicle.getTotalElements(), Is.is(1L));
    Assert.assertThat(vehicle.getContent().get(0).getVehId(), Matchers.is(VEHID_V5281M4978));
  }

  @Test
  public void testSearchVehicleByVehDataWithIdSagType() {
    final String vehData = "1000000000193540352";
    final PageRequest pageRequest = PageRequest.of(0, 10);
    final VehicleSearchTermCriteria term = VehicleSearchTermCriteria.builder().vehicleData(vehData).build();
    final VehicleSearchCriteria request = VehicleSearchCriteria.builder().searchTerm(term).build();
    final Page<VehicleDto> vehicle =
      ivdsVehicleService.searchVehiclesByCriteria(request, pageRequest).getVehicles();
    Assert.assertThat(vehicle.getTotalElements(), Matchers.greaterThanOrEqualTo(0L));
  }

  @Test
  public void testSearchVehicleByVehDataWithPlateNum() {
    final String vehData = "9116AAQ";
    final PageRequest pageRequest = PageRequest.of(0, 10);
    final VehicleSearchTermCriteria term = VehicleSearchTermCriteria.builder().vehicleData(vehData).build();
    final VehicleSearchCriteria request = VehicleSearchCriteria.builder().searchTerm(term).build();
    final Page<VehicleDto> vehicle =
      ivdsVehicleService.searchVehiclesByCriteria(request, pageRequest).getVehicles();
    Assert.assertThat(vehicle.getTotalElements(), Is.is(1L));
    Assert.assertThat(vehicle.getContent().get(0).getVehId(), Matchers.is(VEHID_V18004M18081));
  }

  @Test
  public void testSearchVehicleByVehDataWithPlateNumIncludeSpace() {
    final String vehData = " 9116 AAQ";
    final PageRequest pageRequest = PageRequest.of(0, 10);
    final VehicleSearchTermCriteria term = VehicleSearchTermCriteria.builder().vehicleData(vehData).build();
    final VehicleSearchCriteria request = VehicleSearchCriteria.builder().searchTerm(term).build();
    final Page<VehicleDto> vehicle =
        ivdsVehicleService.searchVehiclesByCriteria(request, pageRequest).getVehicles();
    Assert.assertThat(vehicle.getTotalElements(), Is.is(1L));
    Assert.assertThat(vehicle.getContent().get(0).getVehId(),
      Matchers.is(VEHID_V18004M18081));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSearchVehicleByVehDataWithEmpty() {
    final String vehData = StringUtils.EMPTY;
    final PageRequest pageRequest = PageRequest.of(0, 10);
    final VehicleSearchTermCriteria term = VehicleSearchTermCriteria.builder().vehicleData(vehData).build();
    final VehicleSearchCriteria request = VehicleSearchCriteria.builder().searchTerm(term).build();
    ivdsVehicleService.searchVehiclesByCriteria(request, pageRequest);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSearchVehicleByVehDataWithNull() {
    final String vehData = null;
    final PageRequest pageRequest = PageRequest.of(0, 10);
    final VehicleSearchTermCriteria term = VehicleSearchTermCriteria.builder().vehicleData(vehData).build();
    final VehicleSearchCriteria request = VehicleSearchCriteria.builder().searchTerm(term).build();
    ivdsVehicleService.searchVehiclesByCriteria(request, pageRequest);
  }

  @Test
  public void testSearchVehicleByVehDescWithNullFiltering() {
    final String vehDesc = "MERCEDES BENZ";
    final PageRequest pageRequest = PageRequest.of(0, 10);
    final VehicleSearchTermCriteria term = VehicleSearchTermCriteria.builder().vehicleDesc(vehDesc).build();
    final VehicleSearchCriteria request = VehicleSearchCriteria.builder().searchTerm(term).build();
    final Page<VehicleDto> vehicle =
        ivdsVehicleService.searchVehiclesByCriteria(request, pageRequest).getVehicles();
    Assert.assertThat(vehicle.getTotalElements(), Matchers.greaterThan(0L));
  }

  @Test
  public void testSearchVehicleByVehDescWithBodyTypeFiltering() {
    final String vehDesc = "MERCEDES BENZ";
    final String bodyType = "Cabriolet";
    final PageRequest pageRequest = PageRequest.of(0, 10);
    final VehicleSearchTermCriteria term =
        VehicleSearchTermCriteria.builder().vehicleDesc(vehDesc).build();
    final VehicleFilteringCriteria filtering =
        VehicleFilteringCriteria.builder().vehBodyType(bodyType).build();
    final VehicleSearchCriteria request =
        VehicleSearchCriteria.builder().searchTerm(term).filtering(filtering).build();
    final Page<VehicleDto> vehicle =
        ivdsVehicleService.searchVehiclesByCriteria(request, pageRequest).getVehicles();
    Assert.assertThat(vehicle.getTotalElements(), Matchers.greaterThan(0L));
    final List<VehicleDto> results = vehicle.getContent();
    final List<VehicleDto> filterResult = results.stream()
        .filter(veh -> bodyType.equals(veh.getVehicleBodyType())).collect(Collectors.toList());
    Assert.assertThat(filterResult.size(), Matchers.equalTo(results.size()));
  }

  @Test
  public void testSearchVehicleByVehDescWithPowerFiltering() {
    final String vehDesc = "MERCEDES BENZ";
    final String vehPower = "170";
    final PageRequest pageRequest = PageRequest.of(0, 10);
    final VehicleSearchTermCriteria term =
        VehicleSearchTermCriteria.builder().vehicleDesc(vehDesc).build();
    final VehicleFilteringCriteria filtering =
        VehicleFilteringCriteria.builder().vehPower(vehPower).build();
    final VehicleSearchCriteria request =
        VehicleSearchCriteria.builder().searchTerm(term).filtering(filtering).build();
    final Page<VehicleDto> vehicle =
        ivdsVehicleService.searchVehiclesByCriteria(request, pageRequest).getVehicles();
    Assert.assertThat(vehicle.getTotalElements(), Matchers.greaterThan(0L));
    final List<VehicleDto> results = vehicle.getContent();
    final List<VehicleDto> filterResult = results.stream()
        .filter(veh -> vehPower.equals(veh.getVehiclePowerKw())).collect(Collectors.toList());
    Assert.assertThat(filterResult.size(), Matchers.equalTo(results.size()));
  }

  @Ignore("Waiting for Simon change ES metadata in 3327")
  @Test
  public void testSearchVehicleByVehDescWithFuelTypeFiltering() {
    final String vehDesc = "MERCEDES-BENZ";
    final String vehFuelType = "Benzin";
    final PageRequest pageRequest = PageRequest.of(0, 10);
    final VehicleSearchTermCriteria term =
        VehicleSearchTermCriteria.builder().vehicleDesc(vehDesc).build();
    final VehicleFilteringCriteria filtering =
        VehicleFilteringCriteria.builder().vehFuelType(vehFuelType).build();
    final VehicleSearchCriteria request =
        VehicleSearchCriteria.builder().searchTerm(term).filtering(filtering).build();
    final Page<VehicleDto> vehicle =
        ivdsVehicleService.searchVehiclesByCriteria(request, pageRequest).getVehicles();
    Assert.assertThat(vehicle.getTotalElements(), Matchers.greaterThan(0L));
    final List<VehicleDto> results = vehicle.getContent();
    final List<VehicleDto> filterResult = results.stream()
        .filter(veh -> vehFuelType.equals(veh.getVehicleFuelType())).collect(Collectors.toList());
    Assert.assertThat(filterResult.size(), Matchers.equalTo(results.size()));
  }

  @Test
  public void testSearchVehicleByVehDescWithBodyTypeAndMotorCodeFiltering() {
    final String vehDesc = "MERCEDES-BENZ";
    final String vehBodyType = "Cabriolet";
    final String vehMotorCode = "2996";
    final PageRequest pageRequest = PageRequest.of(0, 10);
    final VehicleSearchTermCriteria term =
        VehicleSearchTermCriteria.builder().vehicleDesc(vehDesc).build();
    final VehicleFilteringCriteria filtering = VehicleFilteringCriteria.builder()
        .vehBodyType(vehBodyType).vehMotorCode(vehMotorCode).build();
    final VehicleSearchCriteria request =
        VehicleSearchCriteria.builder().searchTerm(term).filtering(filtering).build();
    final Page<VehicleDto> vehicle =
        ivdsVehicleService.searchVehiclesByCriteria(request, pageRequest).getVehicles();
    Assert.assertThat(vehicle.getTotalElements(), Matchers.greaterThanOrEqualTo(0L));
    final List<VehicleDto> results = vehicle.getContent();
    final List<VehicleDto> filterResult =
        results.stream().filter(veh -> vehBodyType.equals(veh.getVehicleBodyType())
            && vehMotorCode.equals(veh.getVehicleCapacityCcTech().toString())).collect(Collectors.toList());

    Assert.assertThat(filterResult.size(), Matchers.equalTo(results.size()));
  }

  @Test
  public void testSearchVehicleByVehDescWithMultiFiltering() {
    final String vehDesc = "MERCEDES-BENZ";
    final String vehBodyType = "Cabriolet";
    final String vehMotorCode = "2996";
    final int vehZylinder = 6;
    final String vehPower = "170";
    final String vehEngine = "3.0";
    final String vehBuiltYearMonthFrom = "01/2008";
    final PageRequest pageRequest = PageRequest.of(0, 10);
    final VehicleSearchTermCriteria term =
        VehicleSearchTermCriteria.builder().vehicleDesc(vehDesc).build();
    final VehicleFilteringCriteria filtering =
        VehicleFilteringCriteria.builder().vehBodyType(vehBodyType).vehMotorCode(vehMotorCode)
            .vehZylinder(vehZylinder).vehPower(vehPower).vehEngine(vehEngine)
            .vehBuiltYearMonthFrom(vehBuiltYearMonthFrom).build();
    final VehicleSearchCriteria request =
        VehicleSearchCriteria.builder().searchTerm(term).filtering(filtering).build();
    final Page<VehicleDto> vehicle =
        ivdsVehicleService.searchVehiclesByCriteria(request, pageRequest).getVehicles();
    Assert.assertThat(vehicle.getTotalElements(), Matchers.greaterThanOrEqualTo(0L));
    final List<VehicleDto> results = vehicle.getContent();
    final List<VehicleDto> filterResult = results.stream()
        .filter(veh -> vehBodyType.equals(veh.getVehicleBodyType())
            && vehMotorCode.equals(veh.getVehicleCapacityCcTech().toString())
            && vehPower.equals(veh.getVehiclePowerKw()) && vehEngine.equals(veh.getVehicleEngine())
            && vehZylinder == veh.getVehicleZylinder())
        .collect(Collectors.toList());
    Assert.assertThat(filterResult.size(), Matchers.equalTo(results.size()));
  }
}
