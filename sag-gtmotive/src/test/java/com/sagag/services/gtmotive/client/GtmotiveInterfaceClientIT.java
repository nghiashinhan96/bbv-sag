package com.sagag.services.gtmotive.client;

import com.sagag.services.common.annotation.EshopIntegrationTest;
import com.sagag.services.gtmotive.DataProvider;
import com.sagag.services.gtmotive.app.GtInterfaceAccountsConfiguration;
import com.sagag.services.gtmotive.app.GtmotiveApplication;
import com.sagag.services.gtmotive.domain.GtmotiveProfileDto;
import com.sagag.services.gtmotive.domain.request.GtmotiveCriteria;
import com.sagag.services.gtmotive.domain.request.GtmotiveRequestMode;
import com.sagag.services.gtmotive.dto.request.gtinterface.GtmotivePartInfoRequest;
import com.sagag.services.gtmotive.dto.request.gtinterface.GtmotivePartUpdateRequest;
import com.sagag.services.gtmotive.dto.response.gtinterface.GtmotivePartInfoResponse;
import com.sagag.services.gtmotive.utils.GtmotiveUtils;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { GtmotiveApplication.class })
@EshopIntegrationTest
public class GtmotiveInterfaceClientIT {

  @Autowired
  private GtInterfaceAccountsConfiguration gtmotiveAccountsConfiguration;

  @Autowired
  private GtmotiveInterfaceClient client;


  @Test
  public void shouldGetIframeInfoXmlRes_WithSelectedVehicle_DE() {
    final GtmotiveCriteria criteria = DataProvider.buildSelectedVehicleCriteria();
    GtmotiveProfileDto profile = gtmotiveAccountsConfiguration.getAccounts().getDe();
    criteria.bindGtmotiveProfile(profile);
    final String xml = client.getGraphicalIFrameInfoXml(criteria);
    Assert.assertThat(xml, Matchers.notNullValue());
  }

  @Test
  public void shouldGetIframeInfoXmlRes_WithSelectedVehicle_FR() {
    final GtmotiveCriteria criteria = DataProvider.buildSelectedVehicleCriteria();
    GtmotiveProfileDto profile = gtmotiveAccountsConfiguration.getAccounts().getFr();
    criteria.bindGtmotiveProfile(profile);
    final String xml = client.getGraphicalIFrameInfoXml(criteria);
    Assert.assertThat(xml, Matchers.notNullValue());
  }

  @Test
  public void shouldGetIframeInfoXmlRes_WithValidVin_DE() {
    final GtmotiveCriteria criteria = DataProvider.buildVinCriteria();
    GtmotiveProfileDto profile = gtmotiveAccountsConfiguration.getAccounts().getDe();
    criteria.bindGtmotiveProfile(profile);
    final String xml = client.getGraphicalIFrameInfoXml(criteria);
    Assert.assertThat(xml, Matchers.notNullValue());
  }

  @Test
  public void shouldGetIframeInfoXmlRes_WithValidVin_FR() {
    final GtmotiveCriteria criteria = DataProvider.buildVinCriteria();
    GtmotiveProfileDto profile = gtmotiveAccountsConfiguration.getAccounts().getFr();
    criteria.bindGtmotiveProfile(profile);
    final String xml = client.getGraphicalIFrameInfoXml(criteria);
    Assert.assertThat(xml, Matchers.notNullValue());
  }

  @Test
  public void shouldGetIframeInfoXmlRes_WithInvalidVin() {
    final GtmotiveCriteria criteria = DataProvider.buildInvalidVinCriteria();
    GtmotiveProfileDto profile = gtmotiveAccountsConfiguration.getAccounts().getDe();
    criteria.bindGtmotiveProfile(profile);
    final String xml = client.getGraphicalIFrameInfoXml(criteria);
    Assert.assertThat(xml, Matchers.notNullValue());
  }

  @Test
  public void shouldGetIframeInfoXmlRes_WithServiceSchedule_DE() {
    final GtmotiveCriteria criteria = DataProvider.buildServiceScheduleCriteria();
    GtmotiveProfileDto profile = gtmotiveAccountsConfiguration.getAccounts().getDe();
    criteria.bindGtmotiveProfile(profile);
    final String xml = client.getGraphicalIFrameInfoXml(criteria);
    Assert.assertThat(xml, Matchers.notNullValue());
  }

  @Test
  public void shouldGetIframeInfoXmlRes_WithServiceSchedule_FR() {
    final GtmotiveCriteria criteria = DataProvider.buildServiceScheduleCriteria();
    GtmotiveProfileDto profile = gtmotiveAccountsConfiguration.getAccounts().getFr();
    criteria.bindGtmotiveProfile(profile);
    final String xml = client.getGraphicalIFrameInfoXml(criteria);
    Assert.assertThat(xml, Matchers.notNullValue());
  }

  @Test
  public void shouldGetVehicleInfo() {
    final GtmotiveCriteria criteria = new GtmotiveCriteria();
    criteria.setVin("WVWZZZAUZEP575926");
    criteria.setRequestMode(GtmotiveRequestMode.VIN);
    criteria.setEstimateId(GtmotiveUtils.generateEstimateId(null));
    client.getVehicleInfo(criteria);
  }


  @Test
  public void partUpdateXml_shouldUpdatePart_givenRequestData() throws Exception {
    GtmotivePartUpdateRequest request = GtmotivePartUpdateRequest.builder()
        .estimateId("11117921556532138015").shortNumber("M0300").build();
    String result = client.partUpdate(request);
    Assert.assertNotNull(result);
  }

  @Test
  public void getPartInfo_shouldGetPartInfo_givenRequestData() throws Exception {
    GtmotivePartInfoRequest request =
        GtmotivePartInfoRequest.builder().estimateId("11117921556532138015").umc("AU02801")
            .equipments(Arrays.asList("CB05", "MU12")).build();
    GtmotivePartInfoResponse result = client.getPartInfo(request);
    Assert.assertNotNull(result);
  }
}
