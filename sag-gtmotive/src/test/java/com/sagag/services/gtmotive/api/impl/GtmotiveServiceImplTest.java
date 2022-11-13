package com.sagag.services.gtmotive.api.impl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.sagag.services.gtmotive.DataProvider;
import com.sagag.services.gtmotive.client.GtmotiveInterfaceClient;
import com.sagag.services.gtmotive.client.GtmotiveVehicleClient;
import com.sagag.services.gtmotive.domain.request.GtmotiveCriteria;
import com.sagag.services.gtmotive.domain.request.GtmotiveVehicleCriteria;
import com.sagag.services.gtmotive.domain.response.EquipmentRank;
import com.sagag.services.gtmotive.domain.response.GtVehicleInfoResponse;
import com.sagag.services.gtmotive.domain.response.GtmotiveVehicleDto;

import org.apache.commons.lang3.StringUtils;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class GtmotiveServiceImplTest {

  @InjectMocks
  private GtmotiveServiceImpl gtmotiveService;

  @Mock
  private GtmotiveVehicleClient gtmotiveVehicleClient;

  @Mock
  private GtmotiveInterfaceClient gtmotiveInterfaceClient;

  @Test(expected = IllegalArgumentException.class)
  public void shouldGetVehicleInfo_NullCriteria() throws FileNotFoundException, IOException {
    gtmotiveService.getVehicleInfo(String.valueOf(DataProvider.CUST_NR_1100005), null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void shouldGetVehicleInfo_NullCustomer() throws FileNotFoundException, IOException {
    gtmotiveService.getVehicleInfo(null, DataProvider.criteria_V119702M33633());
  }

  @Test(expected = IllegalArgumentException.class)
  public void shouldGetVehicleInfo_InvalidRequest() throws FileNotFoundException, IOException {
    gtmotiveService.getVehicleInfo(null, new GtmotiveVehicleCriteria());
  }

  @Test
  public void shouldGetVehicleInfo_WrongXmlFormat() throws FileNotFoundException, IOException {
    final GtmotiveVehicleCriteria criteria = DataProvider.criteria_V119702M33633();

    when(gtmotiveInterfaceClient.getGraphicalIFrameInfoXml(any())).thenReturn(StringUtils.EMPTY);
    when(gtmotiveInterfaceClient.getVehicleInfo(any(GtmotiveCriteria.class)))
        .thenReturn(StringUtils.EMPTY);

    Optional<GtmotiveVehicleDto> actual =
        gtmotiveService.getVehicleInfo(String.valueOf(DataProvider.CUST_NR_1100005), criteria);

    Assert.assertThat(actual.isPresent(), Matchers.is(false));
  }

  @Test
  public void shouldGetVehicleInfo_InvalidVin() throws IOException {
    final GtmotiveVehicleCriteria criteria = DataProvider.criteria_VSSZZZ5FZH6561928();
    when(gtmotiveVehicleClient.getVinDecoder(any())).thenReturn(null);

    Optional<GtmotiveVehicleDto> actual =
        gtmotiveService.getVehicleInfo(String.valueOf(DataProvider.CUST_NR_1100005), criteria);

    Assert.assertThat(actual.isPresent(), Matchers.is(false));
  }

  @Test
  public void shouldGetVehicleInfo_V58944M27193() throws IOException {
    final GtmotiveVehicleCriteria criteria = DataProvider.criteria_V119702M33633();
    when(gtmotiveInterfaceClient.getGraphicalIFrameInfoXml(any())).thenReturn(StringUtils.EMPTY);
    when(gtmotiveInterfaceClient.getVehicleInfo(any(GtmotiveCriteria.class)))
        .thenReturn(DataProvider.getVehicleInfo_V58944M27193());

    Optional<GtmotiveVehicleDto> actual =
        gtmotiveService.getVehicleInfo(String.valueOf(DataProvider.CUST_NR_1100005), criteria);

    Assert.assertThat(actual.isPresent(), Matchers.is(true));
    assertVehicleInfo_V58944M27193(actual.get());
  }

  private void assertVehicleInfo_V58944M27193(GtmotiveVehicleDto actual) {
    Assert.assertThat(actual.getVin(), Matchers.is(StringUtils.EMPTY));
    Assert.assertThat(actual.getUmc(), Matchers.is(DataProvider.UMC_SE02301));
    Assert.assertThat(actual.getEquipmentItems().size(), Matchers.is(4));
    Assert.assertThat(actual.getEquipmentRanks(), Matchers.empty());
  }

  @Test
  public void shouldGetVehicleInfo_VSSZZZ5FZH6561928() throws IOException {
    final GtmotiveVehicleCriteria criteria = DataProvider.criteria_VSSZZZ5FZH6561928();

    when(gtmotiveVehicleClient.getVinDecoder(any()))
        .thenReturn(DataProvider.vehicleResponse_VSSZZZ5FZH6561928());
    when(gtmotiveInterfaceClient.getGraphicalIFrameInfoXml(any())).thenReturn(StringUtils.EMPTY);
    when(gtmotiveInterfaceClient.getVehicleInfo(any(GtmotiveCriteria.class)))
        .thenReturn(DataProvider.getVehicleInfo_VSSZZZ5FZH6561928());

    Optional<GtmotiveVehicleDto> actual =
        gtmotiveService.getVehicleInfo(String.valueOf(DataProvider.CUST_NR_1100005), criteria);

    Assert.assertThat(actual.isPresent(), Matchers.is(true));
    assertVehicleInfo_VSSZZZ5FZH6561928(actual.get());
  }

  private void assertVehicleInfo_VSSZZZ5FZH6561928(GtmotiveVehicleDto actual) {
    Assert.assertThat(actual.getVin(), Matchers.is(DataProvider.VIN_VSSZZZ5FZH6561928));
    Assert.assertThat(actual.getUmc(), Matchers.is(DataProvider.UMC_SE02301));
    Assert.assertThat(actual.getEquipmentItems().size(), Matchers.greaterThan(4));
    Assert.assertThat(actual.getEquipmentRanks(), Matchers.not(Matchers.empty()));

    for (EquipmentRank rank : actual.getEquipmentRanks()) {
      assertEquipmentRank(rank);
    }
  }

  private void assertEquipmentRank(EquipmentRank actual) {
    Assert.assertThat(actual.getValue(), Matchers.notNullValue());
    Assert.assertThat(actual.getFamily(), Matchers.notNullValue());
    Assert.assertThat(actual.getSubFamily(), Matchers.notNullValue());
  }

  @Test
  public void shouldGetMakeCodeFromVinDecoder_NoResponse() throws IOException {
    final String vin = DataProvider.VIN_VSSZZZ5FZH6561928;

    when(gtmotiveVehicleClient.getVinDecoder(vin)).thenReturn(null);

    final String makeCode = gtmotiveService.getMakeCodeFromVinDecoder(vin);
    Assert.assertThat(makeCode, Matchers.is(StringUtils.EMPTY));
  }

  @Test
  public void shouldGetMakeCodeFromVinDecoder_InvalidVin() throws IOException {
    final String vin = DataProvider.VIN_VSSZZZ5FZH6561928;

    when(gtmotiveVehicleClient.getVinDecoder(vin)).thenReturn(new GtVehicleInfoResponse());

    final String makeCode = gtmotiveService.getMakeCodeFromVinDecoder(vin);
    Assert.assertThat(makeCode, Matchers.is(StringUtils.EMPTY));
  }

  @Test
  public void shouldGetMakeCodeFromVinDecoder_VSSZZZ5FZH6561928() throws IOException {
    final String vin = DataProvider.VIN_VSSZZZ5FZH6561928;

    when(gtmotiveVehicleClient.getVinDecoder(vin))
        .thenReturn(DataProvider.vehicleResponse_VSSZZZ5FZH6561928());

    final String makeCode = gtmotiveService.getMakeCodeFromVinDecoder(vin);
    Assert.assertThat(makeCode, Matchers.is("SE1"));
  }

}
