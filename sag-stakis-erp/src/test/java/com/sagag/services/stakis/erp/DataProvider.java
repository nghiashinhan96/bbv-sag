package com.sagag.services.stakis.erp;

import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import com.sagag.services.article.api.executor.AttachedArticleSearchCriteria;
import com.sagag.services.article.api.request.AttachedArticleRequest;
import com.sagag.services.article.api.request.BasketPosition;
import com.sagag.services.common.enums.PaymentMethodType;
import com.sagag.services.common.utils.SagJSONUtil;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.stakis.erp.domain.TmAttachedArticleRequest;
import com.sagag.services.stakis.erp.domain.TmBasketPosition;
import com.sagag.services.stakis.erp.domain.TmSendOrderExternalRequest;
import com.sagag.services.stakis.erp.domain.TmUserCredentials;
import com.sagag.services.stakis.erp.wsdl.cis.GetCustomerResponseBody;
import com.sagag.services.stakis.erp.wsdl.tmconnect.GetErpInformationResponseBody;

import lombok.experimental.UtilityClass;

@UtilityClass
@SuppressWarnings("unchecked")
public class DataProvider {

  public static final String TEST_ARTICLE_NUMBER = "10602512";

  public static final String TEST_CUSTOMER = "201906";

  public static final double DF_VAT_FOR_CZ = 21.0;

  public JAXBElement<GetCustomerResponseBody> getFullGetCustomerResponseBody()
      throws IOException, JAXBException {
    final String xmlFile = "/data/CustomerInfo_Full.xml";
    final String xmlStr = IOUtils.toString(DataProvider.class.getResourceAsStream(xmlFile),
        StandardCharsets.UTF_8);
    return (JAXBElement<GetCustomerResponseBody>) createCisUnmarshaller()
        .unmarshal(new StringReader(xmlStr));
  }

  private Unmarshaller createCisUnmarshaller() throws JAXBException {
    return stakisErpCisMarshaller().getJaxbContext().createUnmarshaller();
  }

  private Jaxb2Marshaller stakisErpCisMarshaller() {
    final Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
    marshaller.setContextPath("com.sagag.services.stakis.erp.wsdl.cis");
    return marshaller;
  }

  public TmUserCredentials tmUserCredentials() {
    TmUserCredentials credentials = new TmUserCredentials();
    credentials.setLang(Locale.GERMAN.getLanguage());
    credentials.setCustomerId("201906");
    credentials.setPassword("2wad6WRyh");
    credentials.setUsername("SAGtest");
    return credentials;
  }

  public Map<PaymentMethodType, String> payments() {
    final Map<PaymentMethodType, String> payments = new EnumMap<>(PaymentMethodType.class);
    payments.put(PaymentMethodType.CASH, "Platba v hotovosti");

    return payments;
  }

  public List<ArticleDocDto> buildArticles() {
    return buildArticles(TEST_ARTICLE_NUMBER);
  }

  public List<ArticleDocDto> buildArticles(String... ids) {
    return Stream.of(ids)
        .map(id -> {
          final ArticleDocDto article1 = new ArticleDocDto();
          article1.setIdSagsys(id);
          article1.setSupplier(StringUtils.EMPTY);
          article1.setArtnr(StringUtils.EMPTY);
          article1.setIdDlnr(StringUtils.EMPTY);
          article1.setAmountNumber(200);
          article1.setSalesQuantity(1);
          article1.setQtyMultiple(1);

          return article1;
        }).collect(Collectors.toList());
  }

  public TmSendOrderExternalRequest buildExternalOrderRequest() {
    return buildExternalOrderRequest(buildArticles());
  }

  public TmSendOrderExternalRequest buildExternalOrderRequest(List<ArticleDocDto> articles) {
    final String sendMethodCode = "TOUR";
    final List<BasketPosition> bPositions = articles.stream()
        .map(a -> {
          final TmBasketPosition bPosition = new TmBasketPosition();
          bPosition.setArticleId(a.getIdSagsys());
          bPosition.setQuantity(a.getAmountNumber());
          bPosition.setBrand(a.getSupplier());
          bPosition.setBrandId(Long.valueOf(a.getSupplierId()));
          bPosition.setKtType(117751);
          return bPosition;
        }).collect(Collectors.toList());
    final TmSendOrderExternalRequest request = new TmSendOrderExternalRequest();
    request.setBasketPositions(bPositions);
    request.setSendMethodCode(sendMethodCode);
    return request;
  }

  public static AttachedArticleSearchCriteria buildCriteria() {
    final TmAttachedArticleRequest artRequest1 = new TmAttachedArticleRequest();
    artRequest1.setAttachedArticleId("");
    artRequest1.setCartKey(UUID.randomUUID().toString());
    artRequest1.setQuantity(1);
    artRequest1.setSalesQuantity(1);
    artRequest1.setDepositArticle(new ArticleDocDto());

    final TmAttachedArticleRequest artRequest2 = new TmAttachedArticleRequest();
    artRequest2.setAttachedArticleId("");
    artRequest2.setCartKey(UUID.randomUUID().toString());
    artRequest2.setQuantity(1);
    artRequest2.setSalesQuantity(1);
    artRequest2.setDepositArticle(new ArticleDocDto());

    final List<AttachedArticleRequest> requestList = new ArrayList<>();
    requestList.add(artRequest1);
    requestList.add(artRequest2);

    final AttachedArticleSearchCriteria criteria = new AttachedArticleSearchCriteria();
    criteria.setAttachedArticleRequestList(requestList);
    return criteria;
  }

  private Unmarshaller createTmUnmarshaller() throws JAXBException {
    return stakisErpTmMarshaller().getJaxbContext().createUnmarshaller();
  }

  private Jaxb2Marshaller stakisErpTmMarshaller() {
    final Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
    marshaller.setContextPath("com.sagag.services.stakis.erp.wsdl.tmconnect");
    return marshaller;
  }

  public JAXBElement<GetErpInformationResponseBody> getGetErpArticlesResponseBody(String xmlFile)
      throws IOException, JAXBException {
    final String xmlStr = IOUtils.toString(DataProvider.class.getResourceAsStream(xmlFile),
        StandardCharsets.UTF_8);
    return (JAXBElement<GetErpInformationResponseBody>) createTmUnmarshaller()
        .unmarshal(new StringReader(xmlStr));
  }

  public ArticleDocDto convertJsonToArticle(String jsonFile) throws IOException {
    final String jsonStr = IOUtils.toString(DataProvider.class.getResourceAsStream(jsonFile),
        StandardCharsets.UTF_8);
    return SagJSONUtil.convertJsonToObject(jsonStr, ArticleDocDto.class);
  }

  public Map<String, String> buildFGasTextMap() {
    Map<String, String> fgasTxtMap = new HashMap<>();
    fgasTxtMap.put("en", "F-Gas item only sellable to customers with valid identification number");
    fgasTxtMap.put("de",
        "„F-Gas“ Artikel sind nur an Kunden mit gültiger Identifikationsnummer verkäuflich.");
    fgasTxtMap.put("cs", "F-Gas položky lze prodat pouze zákazníkům s platnou F-Gas certifikací.");
    return fgasTxtMap;
  }
}
