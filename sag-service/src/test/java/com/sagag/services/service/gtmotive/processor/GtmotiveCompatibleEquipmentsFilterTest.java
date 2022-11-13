package com.sagag.services.service.gtmotive.processor;

import static org.junit.Assert.assertThat;

import com.sagag.services.gtmotive.dto.response.mainmoduleservice.GtmotiveEquipmentData;
import com.sagag.services.gtmotive.dto.response.mainmoduleservice.GtmotiveEquipmentOptionBaseItem;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class GtmotiveCompatibleEquipmentsFilterTest {

  @InjectMocks
  private GtmotiveCompatibleEquipmentsFilter gtmotiveCompatibleEquipmentsFilter;

  @Before
  public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void filter_shouldReturnCompatibleEquipments_givenDataHasOneIncompatibleItem()
      throws Exception {

    GtmotiveEquipmentOptionBaseItem applicability1 =
        GtmotiveEquipmentOptionBaseItem.builder().code("C01").description("C01").build();
    GtmotiveEquipmentData gtmotiveEquipmentData1 = GtmotiveEquipmentData.builder()
        .applicability(applicability1).incompatibilityGroupList(Arrays.asList("1")).build();

    GtmotiveEquipmentOptionBaseItem applicability2 =
        GtmotiveEquipmentOptionBaseItem.builder().code("C02").description("C02").build();
    GtmotiveEquipmentData gtmotiveEquipmentData2 = GtmotiveEquipmentData.builder()
        .applicability(applicability2).incompatibilityGroupList(Arrays.asList("2")).build();
    List<GtmotiveEquipmentData> equipmentDatas =
        Arrays.asList(gtmotiveEquipmentData1, gtmotiveEquipmentData2);


    GtmotiveEquipmentOptionBaseItem applicability3 =
        GtmotiveEquipmentOptionBaseItem.builder().code("C03").description("C03").build();
    GtmotiveEquipmentData gtmotiveEquipmentData3 = GtmotiveEquipmentData.builder()
        .applicability(applicability3).incompatibilityGroupList(Arrays.asList("2")).build();
    List<GtmotiveEquipmentData> functionalEquipmentDatas = Arrays.asList(gtmotiveEquipmentData3);


    List<String> vehEquipments = Arrays.asList("C03", "C04", "C05");
    List<GtmotiveEquipmentData> results = gtmotiveCompatibleEquipmentsFilter.filter(equipmentDatas,
        functionalEquipmentDatas, vehEquipments);

    assertThat(results.size(), Matchers.is(1));
    assertThat(results.get(0).getApplicability().getCode(), Matchers.is("C01"));
  }
}
