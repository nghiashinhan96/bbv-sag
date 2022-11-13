package com.sagag.services.haynespro.client;


import com.sagag.services.common.soap.SoapWsExchangeClient;
import com.sagag.services.common.utils.SagEshopUserUtils;
import com.sagag.services.haynespro.builder.HaynesProAccessUrlRequestBuilder;
import com.sagag.services.haynespro.config.HaynesProInternalProperties;
import com.sagag.services.haynespro.config.HaynesProProfile;
import com.sagag.services.haynespro.config.HaynesProProperties;
import com.sagag.services.haynespro.domain.HaynesProItem;
import com.sagag.services.haynespro.domain.HaynesProMap;
import com.sagag.services.haynespro.domain.ObjectFactory;
import com.sagag.services.haynespro.domain.RegisterVisitByDistributor;
import com.sagag.services.haynespro.domain.RegisterVisitByDistributorResponse;
import com.sagag.services.haynespro.domain.RegisterVisitResult;
import com.sagag.services.haynespro.request.HaynesProAccessUrlRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.soap.client.core.SoapActionCallback;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Function;

@Component
@HaynesProProfile
public class HaynesProClient {

  @Autowired
  private HaynesProInternalProperties hpInternalProps;
  @Autowired
  private HaynesProProperties hpProps;
  @Autowired
  @Qualifier("haynesProWebServiceTemplate")
  private WebServiceTemplate webServiceTemplate;
  @Autowired
  private SoapWsExchangeClient exchangeClient;
  @Autowired
  private Jaxb2Marshaller haynesProMarshaller;

  private ObjectFactory objectFactory = new ObjectFactory();

  public RegisterVisitResult getHaynesProAccessUrl(final HaynesProAccessUrlRequest request) {
    Assert.notNull(request, "The given haynes pro request must not be null");
    request.setCallbackUrl(hpInternalProps.getCallbackUrl() + hpProps.getCallbackApi());

    final RegisterVisitByDistributor distributor = objectFactory.createRegisterVisitByDistributor();
    distributor.setCompanyIdentificaton(hpProps.getCompanyIdentificaton());
    distributor.setDistributorPassword(hpProps.getDistributorPassword());
    distributor.setUsername(SagEshopUserUtils.defaultHaynesProUsername(request.getUsername()));

    final HaynesProAccessUrlRequestBuilder builder = new HaynesProAccessUrlRequestBuilder(request);
    distributor.setProperties(propertiesConverter().apply(builder.build()));

    final SoapActionCallback soapAction =
        new SoapActionCallback(hpProps.getUri() + "/registerVisitByDistributor");
    final RegisterVisitByDistributorResponse response = exchangeClient.exchange(webServiceTemplate,
        haynesProMarshaller.getJaxbContext(), hpProps.getUri(), distributor, soapAction);

    return Optional.ofNullable(response)
        .map(RegisterVisitByDistributorResponse::getRegisterVisitByDistributorReturn)
        .orElseThrow(() -> new NoSuchElementException("Not found response"));
  }

  private Function<Map<String, String>, HaynesProMap> propertiesConverter() {
    return properties -> {
      final HaynesProMap props = objectFactory.createMap();
      properties.forEach((key, value) -> {
        HaynesProItem item = objectFactory.createItem();
        item.setKey(key);
        item.setValue(value);
        props.getItem().add(item);
      });
      return props;
    };
  }

}
