package com.sagag.services.gtmotive.config;

import com.sagag.services.gtmotive.api.GtmotiveService;
import com.sagag.services.gtmotive.domain.request.GtmotiveVehicleCriteria;
import com.sagag.services.gtmotive.domain.response.GtmotiveVehicleDto;
import com.sagag.services.gtmotive.dto.request.gtinterface.GtmotiveMultiPartSearchRequest;
import com.sagag.services.gtmotive.dto.request.gtinterface.GtmotiveVehicleInfoCriteria;
import com.sagag.services.gtmotive.dto.request.gtinterface.GtmotiveVehicleSearchByGtInfoRequest;
import com.sagag.services.gtmotive.dto.request.gtinterface.GtmotiveVehicleSearchByVinRequest;
import com.sagag.services.gtmotive.dto.request.gtinterface.GtmotiveVinSecurityCheckCriteria;
import com.sagag.services.gtmotive.dto.request.mainmoduleservice.GtmotiveEquipmentOptionsSearchRequest;
import com.sagag.services.gtmotive.dto.request.mainmoduleservice.GtmotivePartsListSearchRequest;
import com.sagag.services.gtmotive.dto.request.mainmoduleservice.GtmotivePartsThreeSearchRequest;
import com.sagag.services.gtmotive.dto.response.gtinterface.GtmotiveMultiPartSearchResponse;
import com.sagag.services.gtmotive.dto.response.gtinterface.GtmotiveVehicleInfoResponse;
import com.sagag.services.gtmotive.dto.response.gtinterface.GtmotiveVinSecurityCheckResponse;
import com.sagag.services.gtmotive.dto.response.mainmoduleservice.GtmotiveEquipmentOptionsResponse;
import com.sagag.services.gtmotive.dto.response.mainmoduleservice.GtmotivePartsListResponse;
import com.sagag.services.gtmotive.dto.response.mainmoduleservice.GtmotivePartsThreeResponse;
import com.sagag.services.gtmotive.exception.GtmotiveXmlResponseProcessingException;
import com.sagag.services.gtmotive.lang.GtmotiveLanguageProvider;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Locale;
import java.util.Optional;

import javax.xml.xpath.XPathExpressionException;

@Configuration
public class GtmotiveCoreConfiguration {

  @Bean
  @ConditionalOnMissingBean(GtmotiveService.class)
  public GtmotiveService defaultGtmotiveService() {
    return new GtmotiveService() {

      @Override
      public String getMakeCodeFromVinDecoder(String vin) {
        throw new UnsupportedOperationException();
      }

      @Override
      public Optional<GtmotiveVehicleDto> getVehicleInfo(String custNr,
          GtmotiveVehicleCriteria criteria) {
        throw new UnsupportedOperationException();
      }

      @Override
      public GtmotivePartsThreeResponse searchReferencesByPartCode(
          GtmotivePartsThreeSearchRequest searchRequest)
          throws XPathExpressionException, GtmotiveXmlResponseProcessingException {
        throw new UnsupportedOperationException();
      }

      @Override
      public GtmotivePartsListResponse searchVehiclePartsList(
          GtmotivePartsListSearchRequest searchRequest)
          throws XPathExpressionException, GtmotiveXmlResponseProcessingException {
        throw new UnsupportedOperationException();
      }

      @Override
      public Optional<GtmotiveVehicleDto> searchVehicleInfoByGtInfo(String custNr,
          GtmotiveVehicleSearchByGtInfoRequest request) {
        throw new UnsupportedOperationException();
      }

      @Override
      public Optional<GtmotiveVehicleDto> searchVehicleByVin(
          GtmotiveVehicleSearchByVinRequest request) {
        throw new UnsupportedOperationException();
      }

      @Override
      public GtmotiveVehicleInfoResponse getVehicleInfo(GtmotiveVehicleInfoCriteria criteria) {
        throw new UnsupportedOperationException();
      }

      @Override
      public GtmotiveVinSecurityCheckResponse checkVinSecurity(
          GtmotiveVinSecurityCheckCriteria criteria) throws GtmotiveXmlResponseProcessingException {
        throw new UnsupportedOperationException();
      }

      @Override
      public GtmotiveMultiPartSearchResponse searchMultiPart(GtmotiveMultiPartSearchRequest request)
          throws XPathExpressionException, GtmotiveXmlResponseProcessingException {
        throw new UnsupportedOperationException();
      }

      @Override
      public void registerNewEstimateId(GtmotiveVehicleInfoCriteria criteria) {
        throw new UnsupportedOperationException();
      }

      @Override
      public GtmotiveEquipmentOptionsResponse searchEquipmentOptions(
          GtmotiveEquipmentOptionsSearchRequest searchRequest)
          throws XPathExpressionException, GtmotiveXmlResponseProcessingException {
        throw new UnsupportedOperationException();
      }
    };
  }

  @Bean
  @ConditionalOnMissingBean(GtmotiveLanguageProvider.class)
  public GtmotiveLanguageProvider defaultGtmotiveLanguageProvider() {
    return new GtmotiveLanguageProvider() {

      @Override
      public Locale getUserLang() {
        return Optional.ofNullable(LocaleContextHolder.getLocale()).orElseGet(() -> Locale.GERMAN);
      }

    };
  }
}
