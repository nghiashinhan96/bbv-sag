package com.sagag.services.mdm.client;

import com.sagag.services.common.utils.XmlUtils;
import com.sagag.services.mdm.dto.DvseCustomer;
import com.sagag.services.mdm.dto.DvseCustomerSearchResult;
import com.sagag.services.mdm.dto.DvseCustomerUser;
import com.sagag.services.mdm.dto.DvseCustomerUserInfo;
import com.sagag.services.mdm.request.DvseGetCustomerRequest;
import com.sagag.services.mdm.request.DvseGetCustomerSearchResultRequest;
import com.sagag.services.mdm.request.DvseGetCustomerUsersRequest;
import com.sagag.services.mdm.request.DvseLoginRequest;
import com.sagag.services.mdm.request.DvseLogoutRequest;
import com.sagag.services.mdm.request.DvseRequest;
import com.sagag.services.mdm.request.DvseSetCustomerRequest;
import com.sagag.services.mdm.request.DvseSetCustomerUsersRequest;
import com.sagag.services.mdm.response.DvseGetCustomerResponse;
import com.sagag.services.mdm.response.DvseGetCustomerSearchResultResponse;
import com.sagag.services.mdm.response.DvseGetCustomerUsersResponse;
import com.sagag.services.mdm.response.DvseLoginResponse;
import com.sagag.services.mdm.response.DvseLogoutResponse;
import com.sagag.services.mdm.response.DvseSetCustomerResponse;
import com.sagag.services.mdm.response.DvseSetCustomerUsersResponse;
import com.sagag.services.mdm.utils.MdmUtils;
import com.sagag.services.mdm.wsdl.ExecuteXml;
import com.sagag.services.mdm.wsdl.ExecuteXmlResponse;
import com.sagag.services.mdm.wsdl.GetBackXml;
import com.sagag.services.mdm.wsdl.ObjectFactory;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.soap.client.core.SoapActionCallback;

import java.text.MessageFormat;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * The consumer to create, remove or get customer and user from MDM services.
 * </p>
 *
 * @since 05.2017
 *
 */
@Component
@Slf4j
public class MdmClient {

  private static final int SUCCESS_RESPONSE_CODE = 0;

  private static final String DVSE_ERROR_MESSAGE_PATTERN =
      "DVSE response has error code = {0} and message = {1}";

  private static final SoapActionCallback EXECUTE_XML_SOAP_ACTION_CALLBACK =
      new SoapActionCallback("http://tempuri.org/IService/ExecuteXml");

  @Value("${external.webservice.dvse.uri}")
  private String uri;

  @Autowired
  @Qualifier("mdmWebServiceTemplate")
  private WebServiceTemplate webServiceTemplate;

  public String getSessionId(String username, String password) {
    log.info("Start get session id with username = {}, password = {}", username, password);

    final DvseLoginRequest request = new DvseLoginRequest();
    request.setTimestamp(MdmUtils.now());
    request.setUsername(username);
    request.setPassword(password);

    return doExecuteXml(request, DvseLoginResponse.class, null).getSessionId();
  }

  public boolean invalidateSessionId(String sessionId) {
    log.info("Start invalidate session id = {}", sessionId);

    final DvseLogoutRequest request = new DvseLogoutRequest();
    request.setTimestamp(MdmUtils.now());

    return doExecuteXml(request, DvseLogoutResponse.class, sessionId).isSuccess();
  }

  public DvseCustomer getCustomer(String sessionId, String customerId) {
    log.debug("Start get customer with session id = {} and customer id = {}", sessionId, customerId);

    final DvseGetCustomerRequest request = new DvseGetCustomerRequest();
    request.setCustomerId(customerId);
    request.setTimestamp(MdmUtils.now());

    return doExecuteXml(request, DvseGetCustomerResponse.class, sessionId).getCustomer();
  }

  public List<DvseCustomerUser> getCustomerUsers(String sessionId, String customerId) {
    log.info("Start get customer user with session id = {} and customer id = {}", sessionId, customerId);

    final DvseGetCustomerUsersRequest request = new DvseGetCustomerUsersRequest();
    request.setCustomerId(customerId);
    request.setTimestamp(MdmUtils.now());

    return doExecuteXml(request, DvseGetCustomerUsersResponse.class, sessionId).getCustomerUsers();
  }

  public String createOrDeleteCustomer(String sessionId, DvseCustomer customer, MdmUpdateMode mode) {
    log.info("Start create or delete customer with session id = {}, customer = {} and mode = {}",
        sessionId, customer, mode);

    final DvseCustomer cutomerRequest = new DvseCustomer();
    cutomerRequest.setTraderId(customer.getTraderId());
    cutomerRequest.setCustomerId(customer.getCustomerId());
    cutomerRequest.setCustomerName(customer.getCustomerName());
    if (customer.getCustomerId() == null) {
      cutomerRequest.setCdate(MdmUtils.now());
    }
    cutomerRequest.setUdate(MdmUtils.now());
    cutomerRequest.setMemo(MdmUtils.SYSTEM);
    cutomerRequest.setDelete(String.valueOf(mode.getValue()));
    cutomerRequest.setModules(customer.getModules());

    final DvseSetCustomerRequest request = new DvseSetCustomerRequest();
    request.setTimestamp(MdmUtils.now());
    request.setCustomer(cutomerRequest);

    return doExecuteXml(request, DvseSetCustomerResponse.class, sessionId).getCustomerId();
  }

  public List<DvseCustomerUserInfo> createOrDeleteCustomerUsers(String sessionId, String customerId,
      List<DvseCustomerUser> users, MdmUpdateMode mode) {
    log.info("Start create or delete customer user with session id = {}, "
      + "customer id = {}, users = {} and mode = {}", sessionId, customerId, users, mode);

    final List<DvseCustomerUser> customerUsersRequest = users.stream().map(user -> {
      final DvseCustomerUser dvseCusUser = new DvseCustomerUser();
      dvseCusUser.setSeqNumber(user.getSeqNumber());
      dvseCusUser.setCdate(MdmUtils.now());
      dvseCusUser.setUdate(MdmUtils.now());
      dvseCusUser.setDelete(String.valueOf(mode.getValue()));
      dvseCusUser.setModules(user.getModules());
      dvseCusUser.setSubModules(user.getSubModules());
      return dvseCusUser;
    }).collect(Collectors.toList());

    final DvseSetCustomerUsersRequest request = new DvseSetCustomerUsersRequest();
    request.setCustomerId(customerId);
    request.setTimestamp(MdmUtils.now());
    request.setCustomerUsers(customerUsersRequest);

    return doExecuteXml(request, DvseSetCustomerUsersResponse.class, sessionId).getUsersInfos();
  }

  public List<DvseCustomerSearchResult> getCustomerSearchResult(String sessionId, String traderId,
      String searchId, String searchString) {
    final DvseGetCustomerSearchResultRequest request = new DvseGetCustomerSearchResultRequest();
    request.setTimestamp(MdmUtils.now());
    request.setTraderId(traderId);
    request.setSearchId(searchId);
    request.setSearchString(searchString);
    return doExecuteXml(request, DvseGetCustomerSearchResultResponse.class, sessionId)
        .getCustomerSearchResults();
  }

  private <T> T doExecuteXml(DvseRequest request, Class<T> responseClazz, String sessionId) {
    log.info("\t -> | Start do execute DVSE web service");

    final String header = MdmUtils.toHeader(request.getClass(), request.getTimestamp(), sessionId);
    final String data = XmlUtils.marshal(request);

    log.info("\t -> | DVSE request header = {}", header);
    log.info("\t -> | DVSE request data = {}", data);

    final ObjectFactory objFactory = new ObjectFactory();

    final ExecuteXml executeXml = objFactory.createExecuteXml();
    executeXml.setHead(objFactory.createExecuteXmlHead(header));
    executeXml.setData(objFactory.createExecuteXmlData(data));

    final ExecuteXmlResponse response =
        (ExecuteXmlResponse) webServiceTemplate.marshalSendAndReceive(uri, executeXml,
            EXECUTE_XML_SOAP_ACTION_CALLBACK);

    final GetBackXml result = response.getExecuteXmlResult().getValue();

    if (result.getCode() != SUCCESS_RESPONSE_CODE) {
      final String message = MessageFormat.format(DVSE_ERROR_MESSAGE_PATTERN,
          result.getCode(), result.getMessage().getValue());
      log.error(message);
      throw new MdmResponseException(result.getCode(), message);
    }

    final String resultData = result.getData().getValue();
    T returnObject = XmlUtils.unmarshal(responseClazz, resultData);
    log.info("\t -> | DVSE response data = {}", XmlUtils.marshalWithPrettyMode(returnObject));

    return returnObject;
  }

}
