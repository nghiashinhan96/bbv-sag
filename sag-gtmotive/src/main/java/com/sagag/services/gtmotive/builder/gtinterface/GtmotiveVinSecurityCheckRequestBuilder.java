package com.sagag.services.gtmotive.builder.gtinterface;

import com.sagag.services.common.contants.SagConstants;
import com.sagag.services.gtmotive.domain.request.EstimateIdOperationMode;
import com.sagag.services.gtmotive.domain.request.GtmotiveRequestMode;
import com.sagag.services.gtmotive.dto.request.gtinterface.GtmotiveVinSecurityCheckCriteria;

import lombok.Data;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class GtmotiveVinSecurityCheckRequestBuilder implements Serializable {

  private static final long serialVersionUID = 8704652697074272738L;

  private static final int NO_PAINT = 5;

  private static final String LOCKED_FALSE_CLOSE = "\" locked=\"false\"/>";

  private static final String OPEN_T_ONE = "<t1 value=\"";

  private static final String OPEN_T_TWO = "<t2 value=\"";

  private static final String OPEN_T_THREE = "<t3 value=\"";

  private String estimateId;

  private EstimateIdOperationMode operation;

  private boolean showGui;

  private RequestUser requestUser;

  private RequestVehicleInfo vehicleInfo;

  private GtmotiveRequestMode mode;

  private boolean useIdCar;

  /**
   * Default constructor.
   */
  private GtmotiveVinSecurityCheckRequestBuilder() {
    this.requestUser = new RequestUser();
    this.vehicleInfo = new RequestVehicleInfo();
  }

  @lombok.Builder
  public static final class Builder {

    private GtmotiveVinSecurityCheckCriteria requestCriteria;

    public Builder(final GtmotiveVinSecurityCheckCriteria criteria) {
      this.requestCriteria = criteria;
    }

    public GtmotiveVinSecurityCheckRequestBuilder buildRequest() {
      return new GtmotiveVinSecurityCheckRequestBuilder(this);
    }
  }

  @Data
  private static final class RequestUser {

    private String userId;

    private String gsId;

    private String gsPwd;

    private String customerId;
  }

  @Data
  private static final class RequestVehicleInfo {

    private String vehicle;
    private String umc;
    private String gtMod;
    private String gtEng;
    private String gtDrv;
    private String registrationNumber;
    private String vin;
    private String kilometers;
    private List<String> equipCodes;
  }

  private GtmotiveVinSecurityCheckRequestBuilder(final Builder builder) {
    this();
    this.estimateId = builder.requestCriteria.getEstimateId();
    this.operation = builder.requestCriteria.getOperation();
    this.showGui = builder.requestCriteria.isShowGui();
    this.requestUser.userId = builder.requestCriteria.getUserId();
    this.requestUser.gsId = builder.requestCriteria.getGsId();
    this.requestUser.gsPwd = builder.requestCriteria.getGsPwd();
    this.requestUser.customerId = builder.requestCriteria.getCustomerId();
    this.vehicleInfo.vehicle = builder.requestCriteria.getVehicle();
    this.vehicleInfo.umc = builder.requestCriteria.getUmc();
    this.vehicleInfo.registrationNumber = builder.requestCriteria.getRegistrationNumber();
    this.vehicleInfo.vin = builder.requestCriteria.getModifiedVin();
    this.vehicleInfo.kilometers = builder.requestCriteria.getKilometers();
    this.vehicleInfo.gtMod = builder.requestCriteria.getGtMod();
    this.vehicleInfo.gtEng = builder.requestCriteria.getGtEng();
    this.vehicleInfo.gtDrv = builder.requestCriteria.getGtDrv();
    this.useIdCar = builder.requestCriteria.isUseIdCar();

    if (StringUtils.isNotBlank(builder.requestCriteria.getGtEquip())) {
      this.vehicleInfo.setEquipCodes(Arrays
          .asList(
              StringUtils.split(builder.requestCriteria.getGtEquip(), SagConstants.COMMA_NO_SPACE))
          .stream().filter(e -> !StringUtils.isBlank(e)).map(String::trim)
          .collect(Collectors.toList()));
    }

    mode = builder.requestCriteria.getRequestMode();
  }

  private StringBuilder beginSoapEnvelope() {
    return new StringBuilder(
        "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:gtm=\"http://gtmotive.com/\">");
  }

  private static StringBuilder soapHeader() {
    // currently, a simple header
    return new StringBuilder("<soapenv:Header/>");
  }

  private static StringBuilder endSoapEnvelope() {
    return new StringBuilder("</soapenv:Envelope>");
  }

  private static StringBuilder beginSoapBody() {
    return new StringBuilder("<soapenv:Body><gtm:GTIService><gtm:GtRequestXml>");
  }

  private static StringBuilder endSoapBody() {
    return new StringBuilder("</gtm:GtRequestXml></gtm:GTIService></soapenv:Body>");
  }

  private StringBuilder soapContents() {
    final StringBuilder contents =
        new StringBuilder("<![CDATA[<?xml version=\"1.0\" encoding=\"utf-16\"?>");
    contents.append("<request>");
    contents.append("<autenticationData>");
    contents.append("<gsId value=\"").append(requestUser.getGsId()).append("\"/>");
    contents.append("<gsPwd value=\"").append(requestUser.getGsPwd()).append("\"/>");
    contents.append("<customerId value=\"").append(requestUser.getCustomerId()).append("\"/>");
    contents.append("<userId value=\"").append(requestUser.getUserId()).append("\"/>");
    contents.append("</autenticationData>");
    contents.append("<requestInfo>");
    contents.append("<estimate>");

    if (mode.isOpenGraphics()) {
      String operationMode = this.operation.name().toLowerCase();
      contents.append("<operation value=\"").append(operationMode).append("\"></operation>");
      if (this.showGui) {
        contents.append("<showGui value=\"true\"/>");
      } else {
        contents.append("<showGui value=\"false\" multipleUmcsDecoded=\"2\"/>");
      }
    } else if (mode.isCloseGraphics()) {
      contents.append("<operation value=\"read\"/>");
      contents.append("<showGui value=\"false\"/>");
      contents.append("<reportType value=\"partList\"/>");
      contents.append("<BMSOptions>");
      contents.append("<includeEstimate value=\"true\"/>");
      contents.append("<includeReport value=\"true\"/>");
      contents.append("<includeImages value=\"false\"/>");
      contents.append("</BMSOptions>");
    }
    contents.append("<estimateInfo>");
    contents.append("<estimateId value=\"").append(this.estimateId).append("\"></estimateId>");
    if (mode.isOpenGraphics()) {
      contents.append(buildVehicleInfo());
      contents.append(buildUserData());
      contents.append("<lockedAll value=\"true\"/>");
      if (mode.isServiceSchedule()) {
        contents.append("<functionalGroup value=\"98000\"/>");
      }
    } else {
      contents.append("<lockedAll value=\"false\"/>");
    }
    // #1014: Interface: GTEstimate- delete operations
    contents.append("<operationList deleteOperations=\"true\" reset=\"true\" />");
    contents.append("</estimateInfo>");
    contents.append("<behaviour>");

    // #868: Change XML request: //New setting
    contents.append("<omitResult value=\"2\"/>");
    if (mode.isVin()) {
      if (useIdCar) {
        contents.append("<useIdCar value=\"true\"/>");
      } else {
        contents.append("<useIdCar value=\"false\"/>");
      }
    }

    contents.append(buildDisableButtons());
    contents.append("</behaviour>");
    contents.append("</estimate>");
    contents.append("</requestInfo>");
    contents.append("</request>]]>");
    return contents;
  }

  private StringBuilder buildUserData() {
    final StringBuilder contents = new StringBuilder("<userData>");
    contents.append("<priceByHourList>");
    contents.append("<mechanics>");
    final String labourTimeVal = mode == GtmotiveRequestMode.VIN ? "1" : "0";
    contents.append(OPEN_T_ONE).append(labourTimeVal).append(LOCKED_FALSE_CLOSE);
    contents.append(OPEN_T_TWO).append(labourTimeVal).append(LOCKED_FALSE_CLOSE);
    contents.append(OPEN_T_THREE).append(labourTimeVal).append(LOCKED_FALSE_CLOSE);
    contents.append("</mechanics>");
    contents.append("<body>");
    contents.append(OPEN_T_ONE).append(labourTimeVal).append(LOCKED_FALSE_CLOSE);
    contents.append(OPEN_T_TWO).append(labourTimeVal).append(LOCKED_FALSE_CLOSE);
    contents.append(OPEN_T_THREE).append(labourTimeVal).append(LOCKED_FALSE_CLOSE);
    contents.append("</body>");
    contents.append("<paint>");
    contents.append(OPEN_T_ONE).append(labourTimeVal).append(LOCKED_FALSE_CLOSE);
    contents.append(OPEN_T_TWO).append(labourTimeVal).append(LOCKED_FALSE_CLOSE);
    contents.append(OPEN_T_THREE).append(labourTimeVal).append(LOCKED_FALSE_CLOSE);
    contents.append("</paint>");
    contents.append("<electricity>");
    contents.append(OPEN_T_ONE).append(labourTimeVal).append(LOCKED_FALSE_CLOSE);
    contents.append(OPEN_T_TWO).append(labourTimeVal).append(LOCKED_FALSE_CLOSE);
    contents.append(OPEN_T_THREE).append(labourTimeVal).append(LOCKED_FALSE_CLOSE);
    contents.append("</electricity>");
    contents.append("<trim>");
    contents.append(OPEN_T_ONE).append(labourTimeVal).append(LOCKED_FALSE_CLOSE);
    contents.append(OPEN_T_TWO).append(labourTimeVal).append(LOCKED_FALSE_CLOSE);
    contents.append(OPEN_T_THREE).append(labourTimeVal).append(LOCKED_FALSE_CLOSE);
    contents.append("</trim>");
    contents.append("</priceByHourList>");
    contents.append("<paint>");
    // #868: Change XML request: Paint settings removed as set to “No Paint” (val = 5)
    contents.append("<paintSystem value=\"").append(NO_PAINT).append("\" locked=\"true\"/>");
    contents.append("<includeCalculation value=\"false\" locked=\"true\"/>");
    contents.append("<manualSystem>");
    contents.append("<fixAmount value=\"").append(labourTimeVal).append(LOCKED_FALSE_CLOSE);
    contents.append("<labourAmount value=\"").append(labourTimeVal).append(LOCKED_FALSE_CLOSE);
    contents.append("<labourTime value=\"").append(labourTimeVal).append(LOCKED_FALSE_CLOSE);
    contents.append("<materialAmount value=\"0\" locked=\"false\"/>");
    contents.append("</manualSystem>");
    contents.append("<manufacturerPaint>");
    contents.append("<materialFinishType value=\"1\"/>");
    contents.append("<materialHourAmount value=\"0\" locked=\"false\"/>");
    contents.append("</manufacturerPaint>");
    contents.append("</paint>");
    contents.append("<functionality>");
    contents.append("<hideEconomicParts value=\"true\"/>");
    contents.append("<hideStandardParts value=\"true\"/>");
    contents.append("</functionality>");
    contents.append("</userData>");
    return contents;
  }

  private StringBuilder buildVehicleInfo() {
    final StringBuilder contents = new StringBuilder("<vehicleInfo>");
    if (mode.isVehicle() || mode.isServiceSchedule()) {
      appendUmc(vehicleInfo.getUmc(), contents);
      appendRegistrationNumber(vehicleInfo.getRegistrationNumber(), contents);
      appendEquipmentList(contents);
    } else if (mode.isVin()) {
      appendVinInfo(contents);
    }
    contents.append("</vehicleInfo>");
    return contents;
  }

  private void appendEquipmentList(final StringBuilder contents) {
    contents.append("<equipmentList>");
    contents.append("<items>");
    final String itemTag = "<item>";
    final String closeItemTag = "</item>";
    final List<String> distinctItemsInOrder = new ArrayList<>();
    final String model = vehicleInfo.getGtMod();
    final String engine = vehicleInfo.getGtEng();
    final String transmission = vehicleInfo.getGtDrv();
    if (!StringUtils.isBlank(model)) {
      distinctItemsInOrder.add(model);
    }
    if (!StringUtils.isBlank(engine)) {
      distinctItemsInOrder.add(engine);
    }
    if (!StringUtils.isBlank(transmission)) {
      distinctItemsInOrder.add(transmission);
    }
    final List<String> equipCodes = vehicleInfo.getEquipCodes();
    if (!CollectionUtils.isEmpty(equipCodes)) {
      equipCodes.stream().forEach(ec -> {
        if (!distinctItemsInOrder.contains(ec)) {
          distinctItemsInOrder.add(ec);
        }
      });
    }
    distinctItemsInOrder.stream()
        .forEach(item -> contents.append(itemTag).append(item).append(closeItemTag));
    contents.append("</items>");
    contents.append("</equipmentList>");
  }

  private void appendVinInfo(final StringBuilder contents) {
    appendRegistrationNumber(vehicleInfo.getRegistrationNumber(), contents);
    appendKilometers(vehicleInfo.getKilometers(), contents);
    appendVinCode(vehicleInfo.getVin(), contents);
  }

  private static void appendVinCode(final String vinCode, final StringBuilder contents) {
    contents.append("<vin value=\"").append(vinCode).append(LOCKED_FALSE_CLOSE);
  }

  private static void appendUmc(final String umc, final StringBuilder contents) {
    contents.append("<umc value=\"").append(umc).append(LOCKED_FALSE_CLOSE);
  }

  private static void appendKilometers(final String kilometers, final StringBuilder contents) {
    if (StringUtils.isBlank(kilometers)) {
      return;
    }
    contents.append("<kilometers value=\"").append(kilometers).append(LOCKED_FALSE_CLOSE);
  }

  private static void appendRegistrationNumber(final String registrationNumber,
      final StringBuilder contents) {
    if (StringUtils.isBlank(registrationNumber)) {
      return;
    }
    contents.append("<registrationNumber value=\"");
    contents.append(registrationNumber).append(LOCKED_FALSE_CLOSE);
  }

  private StringBuilder buildDisableButtons() {
    final StringBuilder contents = new StringBuilder("<disableButtons>");
    contents.append("<main>");
    contents.append("<help value=\"false\"/>");
    contents.append("<exit value=\"true\"/>");
    contents.append("</main>");
    contents.append("<tools>");
    contents.append("<save value=\"true\"/>");
    contents.append("<myData value=\"true\"/>");
    contents.append("<photoGallery value=\"true\"/>");
    contents.append("<report value=\"true\"/>");
    contents.append("<downloadXml value=\"true\"/>");
    contents.append("<changeMode value=\"false\"/>");
    contents.append("<modelIdentification value=\"true\"/>");
    contents.append("</tools>");
    contents.append("<navigation>");
    contents.append("<vehicleIdentification value=\"true\"/>");
    contents.append("</navigation>");
    contents.append("</disableButtons>");
    return contents;
  }

  /**
   * Builds the SOAP full request contents to GTEstimate.
   *
   * @return the SOAP request contents.
   */
  public String contents() {
    // @formatter:off
    return beginSoapEnvelope().append(soapHeader())
              .append(beginSoapBody())
                  .append(soapContents())
              .append(endSoapBody())
           .append(endSoapEnvelope()).toString();
  }
}
