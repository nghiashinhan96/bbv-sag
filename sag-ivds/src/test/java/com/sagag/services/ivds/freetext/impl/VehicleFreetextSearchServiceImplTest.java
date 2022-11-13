package com.sagag.services.ivds.freetext.impl;

import com.sagag.services.elasticsearch.dto.FreetextVehicleDto;
import com.sagag.services.elasticsearch.dto.FreetextVehicleResponse;
import com.sagag.services.elasticsearch.enums.FreetextSearchOption;
import com.sagag.services.ivds.DataProvider;
import com.sagag.services.ivds.api.IvdsVehicleService;
import com.sagag.services.ivds.request.FreetextSearchRequest;
import com.sagag.services.ivds.response.FreetextResponseDto;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.PageImpl;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class VehicleFreetextSearchServiceImplTest {

  @InjectMocks
  private VehicleFreetextSearchServiceImpl service;

  @Mock
  private IvdsVehicleService vehicleService;

  @Test
  public void testSearchVehiclesFreetext() {
    final FreetextSearchRequest request = DataProvider.buildVehicleSearchFreetext("audi");
    final FreetextResponseDto response = new FreetextResponseDto();

    boolean isSupported = service.support(request.getSearchOptions());
    Assert.assertThat(isSupported, Matchers.is(true));

    FreetextVehicleResponse vehResponse = new FreetextVehicleResponse();
    vehResponse.setVehicles(new PageImpl<>(Arrays.asList(new FreetextVehicleDto())));
    Mockito.when(vehicleService.searchFreetext(Mockito.any(), Mockito.eq(request)))
    .thenReturn(Optional.of(vehResponse));
    service.search(request, response);

    Assert.assertThat(response.getVehData(), Matchers.notNullValue());
    Assert.assertThat(response.getVehData().getVehicles().hasContent(), Matchers.is(true));

    Mockito.verify(vehicleService, Mockito.times(1)).searchFreetext(Mockito.any(),
        Mockito.eq(request));
  }

  @Test
  public void testSearchVehiclesFreetextWithVehMotors() {
    final FreetextSearchRequest request = DataProvider.buildVehicleSearchFreetext("audi");
    request.setSearchOptions(Arrays.asList(FreetextSearchOption.VEHICLES_MOTOR.lowerCase()));
    final FreetextResponseDto response = new FreetextResponseDto();

    boolean isSupported = service.support(request.getSearchOptions());
    Assert.assertThat(isSupported, Matchers.is(true));

    FreetextVehicleResponse vehResponse = new FreetextVehicleResponse();
    vehResponse.setVehicles(new PageImpl<>(Arrays.asList(new FreetextVehicleDto())));
    Mockito.when(vehicleService.searchFreetext(Mockito.any(), Mockito.eq(request)))
    .thenReturn(Optional.of(vehResponse));
    service.search(request, response);

    Assert.assertThat(response.getVehData(), Matchers.notNullValue());
    Assert.assertThat(response.getVehData().getVehicles().hasContent(), Matchers.is(true));

    Mockito.verify(vehicleService, Mockito.times(1)).searchFreetext(Mockito.any(),
        Mockito.eq(request));
  }

  @Test
  public void testSearchVehiclesFreetextWithEmptyResult() {
    final FreetextSearchRequest request = DataProvider.buildVehicleSearchFreetext("empty");
    final FreetextResponseDto response = new FreetextResponseDto();

    boolean isSupported = service.support(request.getSearchOptions());
    Assert.assertThat(isSupported, Matchers.is(true));

    Mockito.when(vehicleService.searchFreetext(Mockito.any(), Mockito.eq(request)))
    .thenReturn(Optional.empty());
    service.search(request, response);

    Assert.assertThat(response.getVehData(), Matchers.nullValue());

    Mockito.verify(vehicleService, Mockito.times(1)).searchFreetext(Mockito.any(),
        Mockito.eq(request));
  }

  @Test
  public void testSearchVehiclesFreetextWithNotSupportSearch() {
    final FreetextSearchRequest request = DataProvider.buildArticleSearchFreetext("empty");
    request.setSearchOptions(Collections.emptyList());
    boolean isSupported = service.support(request.getSearchOptions());
    Assert.assertThat(isSupported, Matchers.is(false));

  }
}
