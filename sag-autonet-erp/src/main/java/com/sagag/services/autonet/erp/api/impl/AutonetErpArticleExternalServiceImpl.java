package com.sagag.services.autonet.erp.api.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ws.soap.client.core.SoapActionCallback;

import com.sagag.services.article.api.domain.article.AdditionalSearchCriteria;
import com.sagag.services.autonet.erp.api.AutonetErpArticleExternalService;
import com.sagag.services.autonet.erp.builder.GetErpInformationRequestBuilder;
import com.sagag.services.autonet.erp.client.AutonetErpClient;
import com.sagag.services.autonet.erp.config.AutonetErpProperties;
import com.sagag.services.autonet.erp.converter.GetErpInformationResponseConverter;
import com.sagag.services.autonet.erp.domain.AutonetErpUserInfo;
import com.sagag.services.autonet.erp.wsdl.tmconnect.GetErpInformationRequestBodyType;
import com.sagag.services.autonet.erp.wsdl.tmconnect.GetErpInformationResponseBodyType;
import com.sagag.services.common.profiles.AutonetProfile;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.domain.eshop.dto.VehicleDto;

@Service
@AutonetProfile
public class AutonetErpArticleExternalServiceImpl implements AutonetErpArticleExternalService {

  @Autowired
  private AutonetErpClient autonetErpClient;

  @Autowired
  private GetErpInformationResponseConverter getErpInformationResponseConverter;

  @Autowired
  private GetErpInformationRequestBuilder getErpInformationRequestBuilder;

  @Autowired
  private AutonetErpProperties props;

  @Override
  public List<ArticleDocDto> searchArticlePricesAndAvailabilities(String username,
      String customerId, String securityToken, String language, List<ArticleDocDto> articles,
      double vatRate, AdditionalSearchCriteria additional) {
     final AutonetErpUserInfo userInfo =
     buildUserInfo(username, customerId, securityToken, language);

    final GetErpInformationRequestBodyType erpInformation =
        getErpInformationRequestBuilder.buildRequest(userInfo, articles, additional);
    final SoapActionCallback soapAction =
        new SoapActionCallback(props.getSoapActionGetErpInformation());
    final GetErpInformationResponseBodyType response =
        autonetErpClient.getErpInformation(erpInformation, soapAction);
    return getErpInformationResponseConverter.apply(articles, response, vatRate, language);
  }

  @Override
  public List<ArticleDocDto> searchArticlePricesAndAvailabilities(String username,
      String customerId, String securityToken, String language, List<ArticleDocDto> articles,
      double vatRate, Optional<VehicleDto> vehicleOpt) {
    throw new UnsupportedOperationException("No support this API");
  }

  @Override
  public List<ArticleDocDto> searchArticleAvailabilitiesDetails(String username, String customerId,
      String securityToken, String language, List<ArticleDocDto> articles, double vatRate,
      Optional<VehicleDto> vehicleOpt) {
    throw new UnsupportedOperationException("No support this API");
  }

}
