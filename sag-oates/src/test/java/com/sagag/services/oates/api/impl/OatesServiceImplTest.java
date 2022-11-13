package com.sagag.services.oates.api.impl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.sagag.services.common.utils.SagJSONUtil;
import com.sagag.services.oates.DataProvider;
import com.sagag.services.oates.client.OatesClient;
import com.sagag.services.oates.converter.OatesApplicationConverter;
import com.sagag.services.oates.domain.OatesEquipmentProducts;
import com.sagag.services.oates.domain.OatesRecommendVehicles;
import com.sagag.services.oates.dto.OatesApplicationDto;
import com.sagag.services.oates.dto.OatesEquipmentProductsDto;
import com.sagag.services.oates.dto.OatesVehicleDto;

@RunWith(MockitoJUnitRunner.class)
public class OatesServiceImplTest {

  @InjectMocks
  private OatesServiceImpl service;

  @Mock
  private OatesClient oatesClient;

  @Mock
  private OatesApplicationConverter oatesApplicationConverter;

  @Test
  public void givenEmptyVehIdShouldBeReturnOptionalEmpty() {
    final String vehId = StringUtils.EMPTY;
    final Optional<OatesVehicleDto> result = service.searchOatesVehicle(vehId);
    Assert.assertThat(result.isPresent(), Matchers.is(false));
  }

  @Test
  public void givenVehIdShouldBeReturnNullData() {
    final String vehId = "V124833308M32594";
    Mockito.when(oatesClient.getOatesRecommendVehicles(any(), eq(StringUtils.EMPTY),
        eq(StringUtils.EMPTY))).thenReturn(null);
    final Optional<OatesVehicleDto> result = service.searchOatesVehicle(vehId);

    Assert.assertThat(result.isPresent(), Matchers.is(false));
    Mockito.verify(oatesClient, Mockito.times(1)).getOatesRecommendVehicles(
        any(), eq(StringUtils.EMPTY), eq(StringUtils.EMPTY));
  }

  @Test
  public void givenVehIdShouldBeReturnInvalidData1() {
    final String vehId = "V12483333308M32594";
    Mockito.when(oatesClient.getOatesRecommendVehicles(any(), eq(StringUtils.EMPTY),
        eq(StringUtils.EMPTY))).thenReturn(DataProvider.buildNullOatesVehicles());
    final Optional<OatesVehicleDto> result = service.searchOatesVehicle(vehId);

    Assert.assertThat(result.isPresent(), Matchers.is(false));
    Mockito.verify(oatesClient, Mockito.times(1)).getOatesRecommendVehicles(
        any(), eq(StringUtils.EMPTY), eq(StringUtils.EMPTY));
  }

  @Test
  public void givenVehIdShouldBeReturnInvalidData2() {
    final String vehId = "V12483333308M32594";
    Mockito.when(oatesClient.getOatesRecommendVehicles(any(), eq(StringUtils.EMPTY),
        eq(StringUtils.EMPTY))).thenReturn(DataProvider.buildEmptyOatesVehicles());
    final Optional<OatesVehicleDto> result = service.searchOatesVehicle(vehId);

    Assert.assertThat(result.isPresent(), Matchers.is(false));
    Mockito.verify(oatesClient, Mockito.times(1)).getOatesRecommendVehicles(
        any(), eq(StringUtils.EMPTY), eq(StringUtils.EMPTY));
  }

  @Test
  public void givenValidResponseShouldBeReturnValidVehicles() throws IOException {
    final String vehId = DataProvider.VEHICLE_ID;
    final String json = IOUtils.toString(getClass().getResourceAsStream("/json/vehicle.json"),
        StandardCharsets.UTF_8);
    final OatesRecommendVehicles oatesRecommendVehicles = SagJSONUtil.convertJsonToObject(json,
        OatesRecommendVehicles.class);
    Mockito.when(oatesClient.getOatesRecommendVehicles(DataProvider.KTYPE, StringUtils.EMPTY,
        StringUtils.EMPTY))
    .thenReturn(oatesRecommendVehicles);

    final Optional<OatesVehicleDto> result = service.searchOatesVehicle(vehId);

    Assert.assertThat(result.isPresent(), Matchers.is(true));
    result.ifPresent(item -> Assert.assertThat(item.getHref(),
        Matchers.equalTo(DataProvider.HREF)));

    Mockito.verify(oatesClient, Mockito.times(1)).getOatesRecommendVehicles(
        DataProvider.KTYPE, StringUtils.EMPTY, StringUtils.EMPTY);
  }

  @Test
  public void givenValidResponseShouldBeReturnValidEquipmentProducts() throws IOException {
    final String hrefStr = DataProvider.HREF;
    final String json = IOUtils.toString(getClass()
        .getResourceAsStream("/json/equipment-products.json"), StandardCharsets.UTF_8);
    final OatesEquipmentProducts oatesEquipmentProducts = SagJSONUtil.convertJsonToObject(json,
        OatesEquipmentProducts.class);
    Mockito.when(oatesClient.getOatesRecommendProducts(hrefStr)).thenReturn(oatesEquipmentProducts);
    Mockito.doCallRealMethod().when(oatesApplicationConverter).apply(oatesEquipmentProducts);

    final OatesEquipmentProductsDto results = service.searchOatesEquipment(hrefStr);

    Assert.assertThat(results.getApplications().isEmpty(), Matchers.is(false));
    for (OatesApplicationDto application : results.getApplications()) {
      Assert.assertThat(application.getAppTypeOriginal(),
          Matchers.isOneOf(DataProvider.OATES_APP_TYPE_ORIGINALS));
    }

    Mockito.verify(oatesClient, Mockito.times(1)).getOatesRecommendProducts(hrefStr);
    Mockito.verify(oatesApplicationConverter, Mockito.times(1)).apply(oatesEquipmentProducts);
  }
}
