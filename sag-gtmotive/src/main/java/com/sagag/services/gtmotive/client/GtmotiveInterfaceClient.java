package com.sagag.services.gtmotive.client;

import com.sagag.services.gtmotive.app.GtInterfaceAccountsConfiguration;
import com.sagag.services.gtmotive.builder.GraphicalVehicleRequestBuilder;
import com.sagag.services.gtmotive.builder.gtinterface.GtmotiveNewEstimateIdRegistrationRequestBuilder;
import com.sagag.services.gtmotive.builder.gtinterface.GtmotivePartInfoRequestBuilder;
import com.sagag.services.gtmotive.builder.gtinterface.GtmotivePartInfoResponseBuilder;
import com.sagag.services.gtmotive.builder.gtinterface.GtmotivePartUpdateRequestBuilder;
import com.sagag.services.gtmotive.builder.gtinterface.GtmotiveVehicleInfoRequestBuilder;
import com.sagag.services.gtmotive.builder.gtinterface.GtmotiveVinSecurityCheckRequestBuilder;
import com.sagag.services.gtmotive.config.GtmotiveProfile;
import com.sagag.services.gtmotive.domain.request.AuthenticationData;
import com.sagag.services.gtmotive.domain.request.EstimateIdOperationMode;
import com.sagag.services.gtmotive.domain.request.GtmotiveCriteria;
import com.sagag.services.gtmotive.domain.request.GtmotiveRequest;
import com.sagag.services.gtmotive.dto.request.gtinterface.GtmotivePartInfoCriteria;
import com.sagag.services.gtmotive.dto.request.gtinterface.GtmotivePartInfoRequest;
import com.sagag.services.gtmotive.dto.request.gtinterface.GtmotivePartUpdateCriteria;
import com.sagag.services.gtmotive.dto.request.gtinterface.GtmotivePartUpdateRequest;
import com.sagag.services.gtmotive.dto.request.gtinterface.GtmotiveVehicleInfoCriteria;
import com.sagag.services.gtmotive.dto.request.gtinterface.GtmotiveVinSecurityCheckCriteria;
import com.sagag.services.gtmotive.dto.response.gtinterface.GtmotivePartInfoResponse;
import com.sagag.services.gtmotive.exception.GtmotiveXmlResponseProcessingException;
import com.sagag.services.gtmotive.lang.GtmotiveLanguageProvider;
import com.sagag.services.gtmotive.utils.GtmotiveUtils;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import javax.xml.xpath.XPathExpressionException;

@Component
@GtmotiveProfile
@Slf4j
public class GtmotiveInterfaceClient extends AbstractGtmotiveClient {

  private static final String DF_REGISTRATION_NUMBER = "123CH";

  private static final List<Charset> DF_ACCEPT_CHARSETS =
      Collections.unmodifiableList(Arrays.asList(StandardCharsets.UTF_8));

  private static final String GT_INTERFACE_WS = "[GtInterfaceWS]";
  private static final String GT_ISERVICE = GT_INTERFACE_WS + "[GTIService]";
  private static final String SERVICE_VIN_SECURITY_CHECK = GT_ISERVICE + "[VinSecurityCheck]";
  private static final String SERVICE_REGISTER_ESTIMATE_ID = GT_ISERVICE + "[RegisterEstimateId]";
  private static final String SERVICE_PART_INFO = GT_ISERVICE + "[PartInfo]";
  private static final String SERVICE_PART_UPDATE = GT_ISERVICE + "[PartUpdate]";
  private static final String SERVICE_GET_VEHICLE_INFO = GT_ISERVICE + "[GetVehicleInfo]";

  private static final String SERVICE_GET_GRAPHICAL_IFRAME_INFO =
      GT_ISERVICE + "[GetGraphicalIFrameInfo]";

  @Autowired
  private GtInterfaceAccountsConfiguration gtmotiveAccountsConfiguration;

  @Autowired
  private GtmotiveLanguageProvider gtmotiveLanguageProvider;

  /**
   * Returns the response of graphical iframe from GTMotive.
   *
   * @param criteria the gtmotive criteria
   * @return the xml string
   */
  public String getGraphicalIFrameInfoXml(final GtmotiveCriteria criteria) {
    // #576: can send the same value
    criteria.setRegistrationNumber(DF_REGISTRATION_NUMBER);
    adjustCriteriaForMultiLingualAccount(criteria);
    return exchangeXml(new GtmotiveRequest.Builder(criteria).buildRequest().contents(),
        HttpMethod.POST, SERVICE_GET_GRAPHICAL_IFRAME_INFO);
  }

  public String getVehicleInfo(GtmotiveCriteria criteria) {
    adjustCriteriaForMultiLingualAccount(criteria);
    criteria.setRegistrationNumber(DF_REGISTRATION_NUMBER);
    final GraphicalVehicleRequestBuilder requestBuilder = new GraphicalVehicleRequestBuilder();
    requestBuilder.setCriteria(criteria);
    return exchangeXml(requestBuilder.buildVehicleInfoRequest(), HttpMethod.POST,
        SERVICE_GET_VEHICLE_INFO);
  }

  private void adjustCriteriaForMultiLingualAccount(final GtmotiveCriteria criteria) {
    Locale locale = gtmotiveLanguageProvider.getUserLang();
    criteria.setLocale(locale);
    criteria.bindGtmotiveProfile(getProfile(locale, gtmotiveAccountsConfiguration.getAccounts()));
  }

  public String partUpdate(final GtmotivePartUpdateRequest searchRequest) {
    Locale lang = gtmotiveLanguageProvider.getUserLang();
    AuthenticationData authenData =
        getProfile(lang, gtmotiveAccountsConfiguration.getAccounts()).toAuthenticationData();
    GtmotivePartUpdateCriteria criteria = new GtmotivePartUpdateCriteria(authenData, searchRequest);
    String xmlRequest = new GtmotivePartUpdateRequestBuilder().criteria(criteria).build();

    return exchangeXml(xmlRequest, HttpMethod.POST, SERVICE_PART_UPDATE);
  }

  public GtmotivePartInfoResponse getPartInfo(final GtmotivePartInfoRequest searchRequest)
      throws XPathExpressionException, GtmotiveXmlResponseProcessingException {
    Locale lang = gtmotiveLanguageProvider.getUserLang();
    AuthenticationData authenData =
        getProfile(lang, gtmotiveAccountsConfiguration.getAccounts()).toAuthenticationData();

    GtmotivePartInfoCriteria criteria = new GtmotivePartInfoCriteria(authenData, searchRequest);
    criteria.setAuthenData(
        getProfile(lang, gtmotiveAccountsConfiguration.getAccounts()).toAuthenticationData());
    String xmlRequest = new GtmotivePartInfoRequestBuilder().criteria(criteria).build();
    String xmlResponse = exchangeXml(xmlRequest, HttpMethod.POST, SERVICE_PART_INFO);
    return new GtmotivePartInfoResponseBuilder().unescapseXmlData(xmlResponse).build();
  }

  public GtmotiveVehicleInfoCriteria registerNewEstimateId(
      final GtmotiveVehicleInfoCriteria criteria) {
    criteria.setRegistrationNumber(DF_REGISTRATION_NUMBER);
    adjustCriteriaForMultiLingualAccount(criteria);
    exchangeXml(new GtmotiveNewEstimateIdRegistrationRequestBuilder.Builder(criteria).buildRequest()
        .contents(), HttpMethod.POST, SERVICE_REGISTER_ESTIMATE_ID);
    criteria.setOperation(EstimateIdOperationMode.READ);
    return criteria;
  }

  public String getVinSecurityCheck(final GtmotiveVinSecurityCheckCriteria criteria) {
    criteria.setRegistrationNumber(DF_REGISTRATION_NUMBER);
    adjustCriteriaForMultiLingualAccount(criteria);
    return exchangeXml(
        new GtmotiveVinSecurityCheckRequestBuilder.Builder(criteria).buildRequest().contents(),
        HttpMethod.POST, SERVICE_VIN_SECURITY_CHECK);
  }

  public String getVehicleInfo(GtmotiveVehicleInfoCriteria criteria) {
    adjustCriteriaForMultiLingualAccount(criteria);
    criteria.setRegistrationNumber(DF_REGISTRATION_NUMBER);
    final GtmotiveVehicleInfoRequestBuilder requestBuilder =
        new GtmotiveVehicleInfoRequestBuilder();
    requestBuilder.setCriteria(criteria);
    return exchangeXml(requestBuilder.buildVehicleInfoRequest(), HttpMethod.POST,
        SERVICE_GET_VEHICLE_INFO);
  }

  private String exchangeXml(final String xmlRequest, HttpMethod method, String service) {
    log.info("GTMotive " + service + " XML Request = {}",
        StringEscapeUtils.unescapeXml(xmlRequest));

    final HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
    httpHeaders.setContentType(MediaType.TEXT_XML);
    httpHeaders.setAcceptCharset(DF_ACCEPT_CHARSETS);
    final HttpEntity<String> entity = new HttpEntity<>(xmlRequest, httpHeaders);

    final long start = System.currentTimeMillis();
    ResponseEntity<String> response =
        restTemplate.exchange(gtmotiveAccountsConfiguration.getUrl(), method, entity, String.class);
    final String unescapseXmlData =
        GtmotiveUtils.extractServiceResult(StringEscapeUtils.unescapeXml(response.getBody()));

    log.info("GTMotive " + service + " XML Response = \n{} \nin {} ms", unescapseXmlData,
        System.currentTimeMillis() - start);
    return unescapseXmlData;
  }

  private void adjustCriteriaForMultiLingualAccount(final GtmotiveVehicleInfoCriteria criteria) {
    Locale locale = gtmotiveLanguageProvider.getUserLang();
    criteria.setLocale(locale);
    criteria.bindGtmotiveProfile(getProfile(locale, gtmotiveAccountsConfiguration.getAccounts()));
  }
}
