package com.sagag.services.gtmotive.domain.request;

import com.sagag.services.gtmotive.builder.AbstractGraphicalRequestBuilder;

import lombok.Data;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

@Data
public class GtmotiveRequest implements Serializable {

  private static final String ELE_ITEM_CLOSE = AbstractGraphicalRequestBuilder.ELE_ITEM_CLOSE;

  private static final String ELE_ITEM_OPEN = AbstractGraphicalRequestBuilder.ELE_ITEM_OPEN;

  private static final String ATTR_LOCKED_FALSE = AbstractGraphicalRequestBuilder.ATTR_LOCKED_FALSE;

  private static final String ELE_T3_VALUE = "<t3 value=\"";

  private static final String ELE_T2_VALUE = "<t2 value=\"";

  private static final String ELE_T1_VALUE = "<t1 value=\"";

  private static final long serialVersionUID = 8704652697074272738L;

  private static final int NO_PAINT = 5;

  private String estimateId;

  private boolean updateEstimateIdMode;

  private RequestUser requestUser;

  private RequestVehicleInfo vehicleInfo;

  private GtmotiveRequestMode mode;

  /**
   * Default constructor.
   */
  private GtmotiveRequest() {
    this.requestUser = new RequestUser();
    this.vehicleInfo = new RequestVehicleInfo();
  }

  @lombok.Builder
  public static final class Builder {

    private GtmotiveCriteria requestCriteria;

    public Builder(final GtmotiveCriteria criteria) {
      this.requestCriteria = criteria;
    }

    public GtmotiveRequest buildRequest() {
      return new GtmotiveRequest(this);
    }
  }

  @Data
  private static final class RequestUser implements Serializable {

    private static final long serialVersionUID = 7112309547412496506L;

    private String userId;

    private String gsId;

    private String gsPwd;

    private String customerId;
  }

  @Data
  private static final class RequestVehicleInfo implements Serializable {

    private static final long serialVersionUID = -6727610525545712859L;

    private String vehicle;

    private String umc;

    private String gtMod;

    private String gtEng;

    private String gtDrv;

    private String registrationNumber;

    private String vin;

    private String kilometers;
  }

  private GtmotiveRequest(final Builder builder) {
    this();
    this.estimateId = builder.requestCriteria.getEstimateId();
    this.updateEstimateIdMode = builder.requestCriteria.isUpdateEstimateIdMode();
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
      contents.append("<operation value=\"")
          .append(this.updateEstimateIdMode ? EstimateIdOperationMode.UPDATE.name().toLowerCase()
              : EstimateIdOperationMode.CREATE.name().toLowerCase())
          .append("\"></operation>");
      contents.append("<showGui value=\"true\"/>");
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
      contents.append("<useIdCar value=\"true\"/>");
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
    contents.append(ELE_T1_VALUE).append(labourTimeVal).append(ATTR_LOCKED_FALSE);
    contents.append(ELE_T2_VALUE).append(labourTimeVal).append(ATTR_LOCKED_FALSE);
    contents.append(ELE_T3_VALUE).append(labourTimeVal).append(ATTR_LOCKED_FALSE);
    contents.append("</mechanics>");
    contents.append("<body>");
    contents.append(ELE_T1_VALUE).append(labourTimeVal).append(ATTR_LOCKED_FALSE);
    contents.append(ELE_T2_VALUE).append(labourTimeVal).append(ATTR_LOCKED_FALSE);
    contents.append(ELE_T3_VALUE).append(labourTimeVal).append(ATTR_LOCKED_FALSE);
    contents.append("</body>");
    contents.append("<paint>");
    contents.append(ELE_T1_VALUE).append(labourTimeVal).append(ATTR_LOCKED_FALSE);
    contents.append(ELE_T2_VALUE).append(labourTimeVal).append(ATTR_LOCKED_FALSE);
    contents.append(ELE_T3_VALUE).append(labourTimeVal).append(ATTR_LOCKED_FALSE);
    contents.append("</paint>");
    contents.append("<electricity>");
    contents.append(ELE_T1_VALUE).append(labourTimeVal).append(ATTR_LOCKED_FALSE);
    contents.append(ELE_T2_VALUE).append(labourTimeVal).append(ATTR_LOCKED_FALSE);
    contents.append(ELE_T3_VALUE).append(labourTimeVal).append(ATTR_LOCKED_FALSE);
    contents.append("</electricity>");
    contents.append("<trim>");
    contents.append(ELE_T1_VALUE).append(labourTimeVal).append(ATTR_LOCKED_FALSE);
    contents.append(ELE_T2_VALUE).append(labourTimeVal).append(ATTR_LOCKED_FALSE);
    contents.append(ELE_T3_VALUE).append(labourTimeVal).append(ATTR_LOCKED_FALSE);
    contents.append("</trim>");
    contents.append("</priceByHourList>");
    contents.append("<paint>");
    // #868: Change XML request: Paint settings removed as set to “No Paint” (val = 5)
    contents.append("<paintSystem value=\"").append(NO_PAINT).append("\" locked=\"true\"/>");
    contents.append("<includeCalculation value=\"false\" locked=\"true\"/>");
    contents.append("<manualSystem>");
    contents.append("<fixAmount value=\"").append(labourTimeVal).append(ATTR_LOCKED_FALSE);
    contents.append("<labourAmount value=\"").append(labourTimeVal).append(ATTR_LOCKED_FALSE);
    contents.append("<labourTime value=\"").append(labourTimeVal).append(ATTR_LOCKED_FALSE);
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
    if (!StringUtils.isBlank(vehicleInfo.getGtMod())) {
      contents.append(ELE_ITEM_OPEN).append(vehicleInfo.getGtMod()).append(ELE_ITEM_CLOSE);
    }
    if (!StringUtils.isBlank(vehicleInfo.getGtEng())) {
      contents.append(ELE_ITEM_OPEN).append(vehicleInfo.getGtEng()).append(ELE_ITEM_CLOSE);
    }
    if (!StringUtils.isBlank(vehicleInfo.getGtDrv())) {
      contents.append(ELE_ITEM_OPEN).append(vehicleInfo.getGtDrv()).append(ELE_ITEM_CLOSE);
    }
    contents.append("</items>");
    contents.append("</equipmentList>");
  }

  private void appendVinInfo(final StringBuilder contents) {
    appendRegistrationNumber(vehicleInfo.getRegistrationNumber(), contents);
    appendKilometers(vehicleInfo.getKilometers(), contents);
    appendVinCode(vehicleInfo.getVin(), contents);
  }

  private static void appendVinCode(final String vinCode, final StringBuilder contents) {
    contents.append("<vin value=\"").append(vinCode).append(ATTR_LOCKED_FALSE);
  }

  private static void appendUmc(final String umc, final StringBuilder contents) {
    contents.append("<umc value=\"").append(umc).append(ATTR_LOCKED_FALSE);
  }

  private static void appendKilometers(final String kilometers, final StringBuilder contents) {
    if (StringUtils.isBlank(kilometers)) {
      return;
    }
    contents.append("<kilometers value=\"").append(kilometers).append(ATTR_LOCKED_FALSE);
  }

  private static void appendRegistrationNumber(final String registrationNumber,
      final StringBuilder contents) {
    if (StringUtils.isBlank(registrationNumber)) {
      return;
    }
    contents.append("<registrationNumber value=\"");
    contents.append(registrationNumber).append(ATTR_LOCKED_FALSE);
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
