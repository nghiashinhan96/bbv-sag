package com.sagag.services.gtmotive;

import com.sagag.services.common.utils.SagJSONUtil;
import com.sagag.services.gtmotive.domain.request.GtmotiveCriteria;
import com.sagag.services.gtmotive.domain.request.GtmotiveRequestMode;
import com.sagag.services.gtmotive.domain.request.GtmotiveVehicleCriteria;
import com.sagag.services.gtmotive.domain.response.GtVehicleInfoResponse;
import com.sagag.services.gtmotive.utils.GtmotiveUtils;

import lombok.experimental.UtilityClass;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@UtilityClass
public class DataProvider {

  public static final long CUST_NR_1100005 = 1100005L;

  public static final String VIN_VSSZZZ5FZH6561928 = "VSSZZZ5FZH6561928";

  public static final String VIN_WVWZZZAUZEP575926 = "WVWZZZAUZEP575926";

  public static final String UMC_VW04802 = "VW04802";

  public static final String GT_DRV_NULL = "null";

  public static final String GT_ENG_MUAG = "MUAG";

  public static final String GT_MOD_CB05 = "CB05";

  public static final String UMC_SE02301 = "SE02301";

  public static final String GT_DRV_RR05 = "RR05";

  public static final String GT_ENG_MTCX = "MTCX";

  public static final String GT_MOD_CT05 = "CT05";

  private static final String GT_ENG_MU12 = "MU12";

  private static final String GT_MOD_CB03 = "CB03";

  private static final String REG_NO_123CH = "123CH";

  private static final String UMC_VW04801 = "VW04801";


  public GtmotiveVehicleCriteria criteria_V119702M33633() {
    final GtmotiveVehicleCriteria criteria = new GtmotiveVehicleCriteria();
    criteria.setUmc("SE02301");
    criteria.setGtDrv("RR05");
    criteria.setGtEng("MTCX");
    criteria.setGtMod("CT05");
    criteria.setEstimateId(GtmotiveUtils.generateEstimateId(null));
    return criteria;
  }

  public GtmotiveVehicleCriteria criteria_V58944M27193() {
    final GtmotiveVehicleCriteria criteria = new GtmotiveVehicleCriteria();
    criteria.setUmc(UMC_VW04802);
    criteria.setGtDrv(GT_DRV_NULL);
    criteria.setGtEng(GT_ENG_MUAG);
    criteria.setGtMod(GT_MOD_CB05);
    criteria.setEstimateId(GtmotiveUtils.generateEstimateId(null));
    return criteria;
  }

  public GtmotiveVehicleCriteria criteria_VSSZZZ5FZH6561928() {
    final GtmotiveVehicleCriteria criteria = new GtmotiveVehicleCriteria();
    criteria.setVin("VSSZZZ5FZH6561928");
    criteria.setEstimateId(GtmotiveUtils.generateEstimateId(null));
    return criteria;
  }

  public GtmotiveVehicleCriteria criteria_WVWZZZAUZEP575926() {
    final GtmotiveVehicleCriteria criteria = new GtmotiveVehicleCriteria();
    criteria.setVin("WVWZZZAUZEP575926");
    criteria.setEstimateId(GtmotiveUtils.generateEstimateId(null));
    return criteria;
  }

  public GtVehicleInfoResponse vehicleResponse_VSSZZZ5FZH6561928() throws IOException {
    final String json = IOUtils.toString(
        DataProvider.class.getResourceAsStream("/data_test/vin_decoder_VSSZZZ5FZH6561928.json"),
        StandardCharsets.UTF_8);
    return SagJSONUtil.convertJsonToObject(StringUtils.trim(json), GtVehicleInfoResponse.class);
  }

  public String getVehicleInfo_V58944M27193() throws IOException {
    return IOUtils.toString(DataProvider.class.getResourceAsStream("/data_test/V58944M27193.xml"),
        StandardCharsets.UTF_8);
  }

  public String getVehicleInfo_VSSZZZ5FZH6561928() throws IOException {
    return IOUtils.toString(
        DataProvider.class.getResourceAsStream("/data_test/VSSZZZ5FZH6561928.xml"),
        StandardCharsets.UTF_8);
  }

  public static String iframeXml_VSSZZZ5FZH6561928() throws IOException {
    return IOUtils.toString(
        DataProvider.class.getResourceAsStream("/data_test/iframe_url_VSSZZZ5FZH6561928.xml"),
        StandardCharsets.UTF_8);
  }

  public GtmotiveCriteria criteriaIFrame_VSSZZZ5FZH6561928() {
    final GtmotiveCriteria criteria = new GtmotiveCriteria();
    criteria.setVin(VIN_VSSZZZ5FZH6561928);
    criteria.setRequestMode(GtmotiveRequestMode.VIN);
    return criteria;
  }

  /**
   * Builds the criteria for selected vehicle request.
   *
   * @return the {@link com.sagag.services.gtmotive.domain.request.GtmotiveCriteria} for selected
   *         vehicle
   */
  public static GtmotiveCriteria buildSelectedVehicleCriteria() {
    // @formatter:off
    GtmotiveCriteria criteria = new GtmotiveCriteria();
    criteria.setUmc(UMC_VW04801);
    criteria.setRegistrationNumber(REG_NO_123CH);
    criteria.setGtMod(GT_MOD_CB03);
    criteria.setGtEng(GT_ENG_MU12);
    criteria.setGtDrv(StringUtils.EMPTY);
    criteria.setEstimateId(GtmotiveUtils.generateEstimateId(null));
    criteria.setRequestMode(GtmotiveRequestMode.VEHICLE);
    return criteria;
  }

  /**
   * Builds the criteria for VIN request.
   *
   * @return the {@link GtmotiveCriteria} for VIN
   */
  public static GtmotiveCriteria buildVinCriteria() {
    GtmotiveCriteria criteria = new GtmotiveCriteria();
    criteria.setVin("WBAPH5G59ANM35638");
    criteria.setRegistrationNumber(REG_NO_123CH);
    criteria.setEstimateId(GtmotiveUtils.generateEstimateId(null));
    criteria.setRequestMode(GtmotiveRequestMode.VIN);
    return criteria;
  }

  /**
   * Builds the criteria for service schedule request.
   *
   * @return the {@link GtmotiveCriteria} for service schedule
   */
  public static GtmotiveCriteria buildServiceScheduleCriteria() {
    GtmotiveCriteria criteria = new GtmotiveCriteria();
    criteria.setUmc(UMC_VW04801);
    criteria.setRegistrationNumber(REG_NO_123CH);
    criteria.setGtMod(GT_MOD_CB03);
    criteria.setGtEng(GT_ENG_MU12);
    criteria.setGtDrv(StringUtils.EMPTY);
    criteria.setEstimateId(GtmotiveUtils.generateEstimateId(null));
    criteria.setRequestMode(GtmotiveRequestMode.SERVICE_SCHEDULE);
    return criteria;
  }

  /**
   * Builds the criteria for invalid VIN request.
   *
   * @return the {@link GtmotiveCriteria} for invalid VIN
   */
  public static GtmotiveCriteria buildInvalidVinCriteria() {
    GtmotiveCriteria criteria = new GtmotiveCriteria();
    criteria.setVin("invalidVIN");
    criteria.setRegistrationNumber(REG_NO_123CH);
    criteria.setEstimateId(GtmotiveUtils.generateEstimateId(null));
    criteria.setRequestMode(GtmotiveRequestMode.VIN);
    return criteria;
  }

  public static String gtmotivePartInfoResponse() throws IOException {
    return IOUtils.toString(
        DataProvider.class.getResourceAsStream("/data_test/part_info_response.xml"),
        StandardCharsets.UTF_8);
  }

  public static String gtmotivePartsListResponse() throws IOException {
    return IOUtils.toString(
        DataProvider.class.getResourceAsStream("/data_test/parts_list_response.xml"),
        StandardCharsets.UTF_16);
  }

  public static String gtmotivePartsThreeCaseOneResponse() throws IOException {
    return IOUtils.toString(
        DataProvider.class.getResourceAsStream("/data_test/parts_three_response_case_one.xml"),
        StandardCharsets.UTF_16);
  }

  public static String gtmotivePartsThreeCaseTwoResponse() throws IOException {
    return IOUtils.toString(
        DataProvider.class.getResourceAsStream("/data_test/parts_three_response_case_two.xml"),
        StandardCharsets.UTF_16);
  }

  public static String gtmotivePartsThreeCaseThreeResponse() throws IOException {
    return IOUtils.toString(
        DataProvider.class.getResourceAsStream("/data_test/parts_three_response_case_three.xml"),
        StandardCharsets.UTF_16);
  }

  public static String gtmotivePartsThreeResponse() throws IOException {
    return IOUtils.toString(
        DataProvider.class.getResourceAsStream("/data_test/parts_three_response.xml"),
        StandardCharsets.UTF_16);
  }

  public static String gtmotivePartsThreeCaseHasError() throws IOException {
    return IOUtils.toString(
        DataProvider.class.getResourceAsStream("/data_test/parts_three_response_case_error.xml"),
        StandardCharsets.UTF_16);
  }

  public static String gtmotiveEquipmentOptionsResponse() throws IOException {
    return IOUtils.toString(
        DataProvider.class.getResourceAsStream("/data_test/equipment_option_response.xml"),
        StandardCharsets.UTF_16);
  }
}
