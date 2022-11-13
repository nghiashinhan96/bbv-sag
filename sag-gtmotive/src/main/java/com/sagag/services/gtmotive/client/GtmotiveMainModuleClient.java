package com.sagag.services.gtmotive.client;

import com.sagag.services.gtmotive.app.GtMainModuleServiceAccountsConfiguration;
import com.sagag.services.gtmotive.builder.mainmoduleservice.GtmotiveEquipmentOptionsRequestBuilder;
import com.sagag.services.gtmotive.builder.mainmoduleservice.GtmotiveEquipmentOptionsResponseBuilder;
import com.sagag.services.gtmotive.builder.mainmoduleservice.GtmotivePartsListRequestBuilder;
import com.sagag.services.gtmotive.builder.mainmoduleservice.GtmotivePartsListResponseBuilder;
import com.sagag.services.gtmotive.builder.mainmoduleservice.GtmotivePartsThreeRequestBuilder;
import com.sagag.services.gtmotive.builder.mainmoduleservice.GtmotivePartsThreeResponseBuilder;
import com.sagag.services.gtmotive.config.GtmotiveProfile;
import com.sagag.services.gtmotive.dto.request.mainmoduleservice.GtmotiveEquipmentOptionsSearchCriteria;
import com.sagag.services.gtmotive.dto.request.mainmoduleservice.GtmotiveEquipmentOptionsSearchRequest;
import com.sagag.services.gtmotive.dto.request.mainmoduleservice.GtmotivePartsListSearchCriteria;
import com.sagag.services.gtmotive.dto.request.mainmoduleservice.GtmotivePartsListSearchRequest;
import com.sagag.services.gtmotive.dto.request.mainmoduleservice.GtmotivePartsThreeSearchCriteria;
import com.sagag.services.gtmotive.dto.request.mainmoduleservice.GtmotivePartsThreeSearchRequest;
import com.sagag.services.gtmotive.dto.response.mainmoduleservice.GtmotiveEquipmentOptionsResponse;
import com.sagag.services.gtmotive.dto.response.mainmoduleservice.GtmotivePartsListResponse;
import com.sagag.services.gtmotive.dto.response.mainmoduleservice.GtmotivePartsThreeResponse;
import com.sagag.services.gtmotive.exception.GtmotiveXmlResponseProcessingException;
import com.sagag.services.gtmotive.lang.GtmotiveLanguageProvider;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Locale;

import javax.xml.xpath.XPathExpressionException;

@Component
@GtmotiveProfile
@Slf4j
public class GtmotiveMainModuleClient extends AbstractGtmotiveClient {

  private static final String SOAP_ACTION = "SOAPAction";

  private static final String SOAP_ACTION_PARTS_THREE =
      "GtMotive.WSGTI.DistributedServices.WSGtMainModuleService/IWSGtMainModuleService/PartsThree";
  private static final String SOAP_ACTION_PARTS_LIST =
      "GtMotive.WSGTI.DistributedServices.WSGtMainModuleService/IWSGtMainModuleService/PartsList";
  private static final String SOAP_ACTION_EQUIPMENT_OPTIONS =
      "GtMotive.WSGTI.DistributedServices.WSGtMainModuleService/IWSGtMainModuleService/EquipmentOptions";

  private static final String PARTS_THREE_RESULT_TERM = "<PartsThreeResult>";
  private static final String PARTS_THREE_RESULT_CLOSED_TERM = "</PartsThreeResult>";
  private static final String PARTS_LIST_RESULT_TERM = "<PartsListResult>";
  private static final String PARTS_LIST_RESULT_CLOSED_TERM = "</PartsListResult>";
  private static final String EQUIPMENT_OPTIONS_RESULT_TERM = "<EquipmentOptionsResult>";
  private static final String EQUIPMENT_OPTIONS_CLOSED_TERM = "</EquipmentOptionsResult>";

  private static final String WS_GT_SERVICE = "[WSGtService]";
  private static final String PART_THREE = WS_GT_SERVICE + "[PartsThree]";
  private static final String PART_LIST = WS_GT_SERVICE + "[PartsList]";
  private static final String EQUIPMENT_OPTIONS = WS_GT_SERVICE + "[EquipmentOptions]";
  private static final String SERVICE_GET_PART_REFERENCE =
      PART_THREE + "[SearchReferencesByPartCode]";
  private static final String SERVICE_GET_PART_LIST = PART_LIST + "[SearchVehiclePartList]";
  private static final String SERVICE_GET_EQUIPMENT_OPTIONS =
      EQUIPMENT_OPTIONS + "[GetEquipmentOptions]";

  @Autowired
  private GtMainModuleServiceAccountsConfiguration gtMainModuleServiceAccountsConfiguration;

  @Autowired
  private GtmotiveLanguageProvider gtmotiveLanguageProvider;

  @Autowired
  private GtmotivePartsThreeResponseBuilder gtmotivePartsThreeResponseBuilder;

  public GtmotivePartsThreeResponse searchReferencesByPartCode(
      GtmotivePartsThreeSearchRequest searchRequest)
      throws XPathExpressionException, GtmotiveXmlResponseProcessingException {
    Locale lang = gtmotiveLanguageProvider.getUserLang();
    GtmotivePartsThreeSearchCriteria criteria = new GtmotivePartsThreeSearchCriteria(
        getProfile(lang, gtMainModuleServiceAccountsConfiguration.getAccounts())
            .toAuthenticationData(),
        searchRequest);
    String xmlRequest = new GtmotivePartsThreeRequestBuilder().criteria(criteria).build();
    String xmlResponse = exchangeXml(xmlRequest, HttpMethod.POST, SOAP_ACTION_PARTS_THREE,
        PARTS_THREE_RESULT_TERM, PARTS_THREE_RESULT_CLOSED_TERM, SERVICE_GET_PART_REFERENCE);
    return gtmotivePartsThreeResponseBuilder.build(xmlResponse);
  }

  public GtmotivePartsListResponse searchVehiclePartList(
      GtmotivePartsListSearchRequest searchRequest)
      throws XPathExpressionException, GtmotiveXmlResponseProcessingException {
    Locale lang = gtmotiveLanguageProvider.getUserLang();
    GtmotivePartsListSearchCriteria criteria = new GtmotivePartsListSearchCriteria(
        getProfile(lang, gtMainModuleServiceAccountsConfiguration.getAccounts())
            .toAuthenticationData(),
        searchRequest);
    String xmlRequest = new GtmotivePartsListRequestBuilder().criteria(criteria).build();
    String xmlResponse = exchangeXml(xmlRequest, HttpMethod.POST, SOAP_ACTION_PARTS_LIST,
        PARTS_LIST_RESULT_TERM, PARTS_LIST_RESULT_CLOSED_TERM, SERVICE_GET_PART_LIST);
    return new GtmotivePartsListResponseBuilder().unescapseXmlData(xmlResponse).build();
  }

  public GtmotiveEquipmentOptionsResponse searchEquipmentOptions(
      GtmotiveEquipmentOptionsSearchRequest searchRequest)
      throws GtmotiveXmlResponseProcessingException, XPathExpressionException {
    Locale lang = gtmotiveLanguageProvider.getUserLang();
    GtmotiveEquipmentOptionsSearchCriteria criteria = new GtmotiveEquipmentOptionsSearchCriteria(
        getProfile(lang, gtMainModuleServiceAccountsConfiguration.getAccounts())
            .toAuthenticationData(),
        searchRequest);
    String xmlRequest = new GtmotiveEquipmentOptionsRequestBuilder().criteria(criteria).build();
    String xmlResponse = exchangeXml(xmlRequest, HttpMethod.POST, SOAP_ACTION_EQUIPMENT_OPTIONS,
        EQUIPMENT_OPTIONS_RESULT_TERM, EQUIPMENT_OPTIONS_CLOSED_TERM,
        SERVICE_GET_EQUIPMENT_OPTIONS);
    return new GtmotiveEquipmentOptionsResponseBuilder().unescapseXmlData(xmlResponse).build();
  }

  private String exchangeXml(final String xmlRequest, HttpMethod method, String soapAction,
      String resultTerm, String resultCloseTerm, String service) {
    log.info("GTMotive " + service + " XML Request = {}",
        StringEscapeUtils.unescapeXml(xmlRequest));
    final HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
    httpHeaders.setContentType(MediaType.TEXT_XML);
    httpHeaders.setAcceptCharset(DF_ACCEPT_CHARSETS);
    httpHeaders.set(SOAP_ACTION, soapAction);
    final HttpEntity<String> entity = new HttpEntity<>(xmlRequest, httpHeaders);

    final long start = System.currentTimeMillis();
    ResponseEntity<String> response = exchangeClient
        .exchangeMainModuleXml(gtMainModuleServiceAccountsConfiguration.getUrl(), method, entity);

    final String unescapseXmlData = extractServiceResult(
        StringEscapeUtils.unescapeXml(response.getBody()), resultTerm, resultCloseTerm);
    log.info("GTMotive " + service + " XML Response = \n{} \nin {} ms", unescapseXmlData,
        System.currentTimeMillis() - start);
    return unescapseXmlData;
  }

  private String extractServiceResult(final String unescapseXmlData, String resultTerm,
      String resultCloseTerm) {
    return StringUtils.substring(unescapseXmlData,
        StringUtils.indexOf(unescapseXmlData, resultTerm) + resultTerm.length(),
        StringUtils.indexOf(unescapseXmlData, resultCloseTerm));
  }
}
