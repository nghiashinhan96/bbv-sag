package com.sagag.services.stakis.erp.client;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

import javax.xml.bind.JAXBElement;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.soap.client.core.SoapActionCallback;

import com.sagag.services.article.api.request.ExternalOrderHistoryRequest;
import com.sagag.services.common.contants.SagConstants;
import com.sagag.services.common.profiles.CzProfile;
import com.sagag.services.common.soap.SoapWsExchangeClient;
import com.sagag.services.common.utils.DateUtils;
import com.sagag.services.stakis.erp.exception.StakisCisExceptionHandler;
import com.sagag.services.stakis.erp.wsdl.cis.BaseResponse;
import com.sagag.services.stakis.erp.wsdl.cis.Filter;
import com.sagag.services.stakis.erp.wsdl.cis.GetCustomerRequestBody;
import com.sagag.services.stakis.erp.wsdl.cis.GetCustomerResponseBody;
import com.sagag.services.stakis.erp.wsdl.cis.GetVoucherDetailsRequestBody;
import com.sagag.services.stakis.erp.wsdl.cis.GetVoucherDetailsResponseBody;
import com.sagag.services.stakis.erp.wsdl.cis.GetVouchersRequestBody;
import com.sagag.services.stakis.erp.wsdl.cis.GetVouchersResponseBody;
import com.sagag.services.stakis.erp.wsdl.cis.ObjectFactory;
import com.sagag.services.stakis.erp.wsdl.cis.OutCustomer;
import com.sagag.services.stakis.erp.wsdl.cis.OutVoucherDetails;
import com.sagag.services.stakis.erp.wsdl.cis.OutVouchers;
import com.sagag.services.stakis.erp.wsdl.cis.QueryType;

@Component
@CzProfile
public class StakisErpCisClient {

  private static final int ORDER_HISTORY_VOUCHER_TYPE_ID = 2;

  private static final int ARTICLE_QUERY_TYPE_ID = 1;

  private static final int ORDER_NUMBER_QUERY_TYPE_ID = 3;

  private static final String CUSTOMER_ID_NOT_NULL_MSG = "The given customer id must not be empty";

  @Autowired
  @Qualifier("stakisErpCisWebServiceTemplate")
  private WebServiceTemplate wsTemplate;

  @Autowired
  private Jaxb2Marshaller stakisErpCisMarshaller;

  @Autowired
  @Qualifier("cisObjectFactory")
  private ObjectFactory cisObjectFactory;

  @Autowired
  private StakisCisExceptionHandler defExceptionHandler;

  @Autowired
  private SoapWsExchangeClient exchangeClient;

  /**
   * Returns the ST-CZ customer information by customer id.
   *
   * @param sessionId
   * @param customerId
   * @param lang
   * @return the result customer information
   */
  public OutCustomer getCustomer(String sessionId, String customerId, String lang) {
    Assert.hasText(customerId, CUSTOMER_ID_NOT_NULL_MSG);
    Assert.hasText(lang, "The given language must not be empty");

    final GetCustomerRequestBody body = cisObjectFactory.createGetCustomerRequestBody();
    body.setSessionId(cisObjectFactory.createGetCustomersRequestBodySessionId(sessionId));
    body.setCustomerId(cisObjectFactory.createGetCustomerRequestBodyCustomerId(customerId));
    body.setLanguage(cisObjectFactory.createGetCustomerRequestBodyLanguage(lang));

    final GetCustomerResponseBody response = exchange(cisObjectFactory.createGetCustomer(body),
        CisSoapAction.GET_CUSTOMER_SOAP_ACTION);
    return checkExceptionResponse(response.getGetCustomerResult().getValue());
  }

  public OutVouchers getOrderHistoryFromVoucher(String customerNr, String sessionId,
      ExternalOrderHistoryRequest request, Integer page, String lang) {
    Assert.hasText(customerNr, CUSTOMER_ID_NOT_NULL_MSG);

    GetVouchersRequestBody body = cisObjectFactory.createGetVouchersRequestBody();
    body.setSessionId(cisObjectFactory.createGetVouchersRequestBodySessionId(sessionId));
    body.setCustomerId(cisObjectFactory.createGetVouchersRequestBodyCustomerId(customerNr));
    body.setLanguage(cisObjectFactory.createGetVouchersRequestBodyLanguage(lang));

    body.setVoucherTypeId(ORDER_HISTORY_VOUCHER_TYPE_ID);
    body.setPageSize(SagConstants.DEFAULT_PAGE_NUMBER);
    body.setPageId(page);

    Map<Integer, String> queryValueByType = new HashMap<>();
    queryValueByType.put(ARTICLE_QUERY_TYPE_ID, request.getOrderNumber());
    queryValueByType.put(ORDER_NUMBER_QUERY_TYPE_ID, request.getArticleNumber());
    Optional<Entry<Integer, String>> filterQuery = MapUtils.emptyIfNull(queryValueByType).entrySet().stream()
        .filter(q -> StringUtils.isNoneBlank(q.getValue())).findFirst();
    Filter filter = cisObjectFactory.createFilter();
    if (filterQuery.isPresent()) {
      QueryType voucherType = cisObjectFactory.createQueryType();
      voucherType.setId(filterQuery.get().getKey());
      filter.setQuery(cisObjectFactory.createFilterQuery(voucherType));
      filter.setQueryValue(cisObjectFactory.createFilterQueryValue(filterQuery.get().getValue()));
    }
    boolean isFilterByOrderNumber = filterQuery.isPresent()
        && (filterQuery.get().getKey() == ORDER_NUMBER_QUERY_TYPE_ID);
    if (!isFilterByOrderNumber) {
      if (StringUtils.isNoneBlank(request.getFrom())) {
        filter.setDateFrom(
            cisObjectFactory.createFilterDateFrom(request.getFrom() + DateUtils.DATE_RESET_TIME));
      }

      if (StringUtils.isNoneBlank(request.getTo())) {
        filter.setDateTo(cisObjectFactory.createFilterDateTo(request.getTo() + DateUtils.DATE_RESET_TIME));
      }
    }

    body.setFilter(cisObjectFactory.createFilter(filter));

    GetVouchersResponseBody response = exchange(cisObjectFactory.createGetVouchers(body),
        CisSoapAction.GET_VOUCHER_SOAP_ACTION);

    return checkExceptionResponse(response.getGetVouchersResult().getValue());
  }

  public OutVoucherDetails getOrderHistoryDetailFromVoucher(String custNr, String orderNr,
      String lang) {
    Assert.hasText(custNr, CUSTOMER_ID_NOT_NULL_MSG);

    GetVoucherDetailsRequestBody body = cisObjectFactory.createGetVoucherDetailsRequestBody();
    body.setCustomerId(cisObjectFactory.createGetVoucherDetailsRequestBodyCustomerId(custNr));
    body.setVoucherId(cisObjectFactory.createGetVoucherDetailsRequestBodyVoucherId(orderNr));
    body.setVoucherTypeId(ORDER_HISTORY_VOUCHER_TYPE_ID);
    body.setLanguage(cisObjectFactory.createGetVoucherDetailsRequestBodyLanguage(lang));

    GetVoucherDetailsResponseBody response = exchange(
        cisObjectFactory.createGetVoucherDetails(body),
        CisSoapAction.GET_VOUCHER_DETAIL_SOAP_ACTION);
    return checkExceptionResponse(response.getGetVoucherDetailsResult().getValue());
  }

  private <R extends BaseResponse> R checkExceptionResponse(R response) {
    defExceptionHandler.resolve(response);
    return response;
  }

  private <T, R> R exchange(JAXBElement<T> request, SoapActionCallback soapAction) {
    return exchangeClient.exchange(wsTemplate, stakisErpCisMarshaller.getJaxbContext(), request,
        soapAction);
  }

}
