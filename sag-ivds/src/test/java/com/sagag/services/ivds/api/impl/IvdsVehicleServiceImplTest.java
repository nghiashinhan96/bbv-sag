package com.sagag.services.ivds.api.impl;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sagag.eshop.service.api.EshopFavoriteService;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.common.utils.PageUtils;
import com.sagag.services.domain.eshop.dto.AdvanceVehicleModel;
import com.sagag.services.domain.eshop.dto.VehicleDto;
import com.sagag.services.elasticsearch.api.LicensePlateSearchService;
import com.sagag.services.elasticsearch.api.VehicleSearchService;
import com.sagag.services.elasticsearch.criteria.vehicle.VehicleSearchCriteria;
import com.sagag.services.elasticsearch.criteria.vehicle.VehicleSearchTermCriteria;
import com.sagag.services.elasticsearch.domain.vehicle.VehicleCode;
import com.sagag.services.elasticsearch.domain.vehicle.VehicleDoc;
import com.sagag.services.elasticsearch.domain.vehicle.licenseplate.LicensePlateDoc;
import com.sagag.services.elasticsearch.dto.VehicleSearchResponse;
import com.sagag.services.ivds.filter.vehicles.AggregationVehicleBuilder;
import com.sagag.services.ivds.request.vehicle.VehicleMakeModelTypeSearchRequest;
import com.sagag.services.ivds.response.VehicleSearchResponseDto;
import com.sagag.services.ivds.validator.PlateNumberValidator;

import java.util.Collections;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * UT to verify {@link IvdsVehicleServiceImpl}.
 */
@RunWith(SpringRunner.class)
public class IvdsVehicleServiceImplTest {

  @InjectMocks
  private IvdsVehicleServiceImpl ivdsVehicleService;

  @Mock
  private VehicleSearchService vehicleSearchService;

  @Mock
  private LicensePlateSearchService plateService;

  @Mock
  private PlateNumberValidator plateNumberValidator;

  @Mock
  private EshopFavoriteService eshopFavoriteService;

  @Mock
  private List<AggregationVehicleBuilder> aggregationVehicleBuilders;

  @Before
  public void setUp() {
  }

  @Test
  public void testSearchVehicleByVehIdWithUpdateFlagFavorite() {
    final String vehid = "V5281M4978";
    final UserInfo user = new UserInfo();
    when(vehicleSearchService.searchVehicleByVehId(vehid)).thenReturn(Optional.of(new VehicleDoc()));
    ivdsVehicleService.searchVehicleByVehId(user, vehid);
    verify(eshopFavoriteService, times(1)).updateFavoriteFlagVehicle(Mockito.any(), Mockito.any());
  }
  
  @Test
  public void testSearchVehicleByVehDataWithTypenschein() {
    final String vehid = "V5281M4978";
    final String vehicleData = "1V6690";
    testSearchVehicleByVehicleData(vehid, vehicleData);
  }

  @Test
  public void testSearchVehicleByVehDataWithNationalCode() {
    final String vehid = "V5281M4978";
    final String vehicleData = "39111";
    testSearchVehicleByVehicleData(vehid, vehicleData);
  }

  @Test
  public void testSearchVehicleByVehDataWithPlateNum() {
    final String vehid = "V18004M18081";
    final String vehicleData = "ZH56541";
    final VehicleSearchTermCriteria searchTerm =
      VehicleSearchTermCriteria.builder().vehicleData(vehicleData).build();
    final VehicleSearchCriteria request = VehicleSearchCriteria.builder().searchTerm(searchTerm).build();
    LicensePlateDoc licensePlate = new LicensePlateDoc();
    licensePlate.setTsn("6PA454");
    final List<LicensePlateDoc> typeRefs = Arrays.asList(licensePlate);
    when(plateNumberValidator.validate(Mockito.any())).thenReturn(true);
    when(plateService.searchLicensePlateByText(vehicleData))
        .thenReturn(typeRefs);

    when(vehicleSearchService.searchVehiclesByCriteria(Mockito.any(), Mockito.eq(PageUtils.DEF_PAGE)))
        .thenReturn(buildMockedVehicleDocsRes(vehid, vehicleData));

    final PageRequest pageRequest = PageRequest.of(0, 10);

    final Page<VehicleDto> dtos =
        ivdsVehicleService.searchVehiclesByCriteria(request, pageRequest).getVehicles();

    final Optional<VehicleDto> firstVehDto = dtos.getContent().stream().findFirst();

    verify(plateService, times(1)).searchLicensePlateByText(vehicleData);
    Assert.assertThat(dtos.getTotalElements(), Matchers.equalTo(Long.valueOf(1)));
    Assert.assertThat(firstVehDto.isPresent(), Matchers.is(true));
    firstVehDto.ifPresent(
        vehicle -> Assert.assertThat(vehid, Matchers.equalTo(vehicle.getVehId())));
  }

  @Test
  public void shouldSearchAdvanceVehicleByCriteria() {
    // Given
    VehicleDoc vehicleDoc = new VehicleDoc();
    vehicleDoc.setId("id");
    vehicleDoc.setVehId("vehId");
    Page<VehicleDoc> vehiclePage = new PageImpl<>(Collections.singletonList(vehicleDoc));
    final VehicleSearchTermCriteria searchTerm = VehicleSearchTermCriteria.builder().build();
    final VehicleSearchCriteria criteria = VehicleSearchCriteria.builder().searchTerm(searchTerm).build();
    final Pageable pageable = mock(Pageable.class);
    VehicleSearchResponse response = VehicleSearchResponse.builder()
        .vehicles(vehiclePage)
        .aggregations(Collections.emptyMap()).build();
    when(vehicleSearchService.searchAdvanceTypesByMakeModel(criteria, pageable)).thenReturn(response);
    // When
    VehicleSearchResponseDto result = ivdsVehicleService.searchAdvanceVehiclesByCriteria(criteria, pageable);
    // Then
    Assert.assertNotNull(result);
    Assert.assertEquals(1, result.getVehicles().stream().count());
  }

  @Test
  public void searchAdvanceAggregationVehicles() {
    // Given
    VehicleMakeModelTypeSearchRequest request = new VehicleMakeModelTypeSearchRequest();
    request.setMakeCode("make");
    request.setVehicleType("type");
    request.setYearFrom("2022");
    // When
    AdvanceVehicleModel result = ivdsVehicleService.searchAdvanceAggregationVehicles(request);
    // Then
    Assert.assertNotNull(result);
  }

  private void testSearchVehicleByVehicleData(final String vehid, final String vehicleData) {
    when(vehicleSearchService.searchVehiclesByCriteria(Mockito.any(),
        Mockito.eq(PageUtils.DEF_PAGE))).thenReturn(buildMockedVehicleDocsRes(vehid, vehicleData));
    when(plateNumberValidator.validate(Mockito.any())).thenReturn(false);
    final PageRequest pageRequest = PageRequest.of(0, 10);
    final VehicleSearchTermCriteria searchTerm =
        VehicleSearchTermCriteria.builder().vehicleData(vehicleData).build();
    final VehicleSearchCriteria request =
        VehicleSearchCriteria.builder().searchTerm(searchTerm).build();
    final Page<VehicleDto> dtos =
        ivdsVehicleService.searchVehiclesByCriteria(request, pageRequest).getVehicles();
    final Optional<VehicleDto> firstVehDto = dtos.getContent().stream().findFirst();

    Assert.assertThat(dtos.getTotalElements(), Matchers.equalTo(Long.valueOf(1)));
    Assert.assertThat(firstVehDto.isPresent(), Matchers.is(true));
    firstVehDto.ifPresent(
        vehicle -> Assert.assertThat(vehid, Matchers.equalTo(vehicle.getVehId())));
    verify(vehicleSearchService, times(1)).searchVehiclesByCriteria(Mockito.any(),
        Mockito.eq(PageUtils.DEF_PAGE));
  }

  private static VehicleSearchResponse buildMockedVehicleDocsRes(String vehId, String vehicleCode) {
    List<VehicleDoc> vehicles = new ArrayList<>();
    final VehicleDoc vehDoc = new VehicleDoc();
    vehDoc.setVehId(vehId);
    vehDoc.setCodes(Arrays.asList(createVehicleCode("typs_ch", "typenschein", vehicleCode)));
    vehicles.add(vehDoc);
    return VehicleSearchResponse.builder().vehicles(new PageImpl<>(vehicles)).build();
  }

  private static VehicleCode createVehicleCode(final String vehCodeType, final String vehCodeAttr,
      final String vehCodeVal) {
    final VehicleCode vehicleCode = new VehicleCode();
    vehicleCode.setVehCodeType(vehCodeType);
    vehicleCode.setVehCodeAttr(vehCodeAttr);
    vehicleCode.setVehCodeValue(vehCodeVal);
    return vehicleCode;
  }

}
