package com.sagag.services.oauth2.api.impl.client;

import com.sagag.services.hazelcast.api.EshopClientCacheService;
import com.sagag.services.oauth2.model.client.EshopClientDetails;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ClientDetailsServiceImpl implements ClientDetailsService {

  private EshopClientCacheService clientDetailCacheService;

  @Autowired
  public ClientDetailsServiceImpl(EshopClientCacheService eshopClientCacheService) {
    this.clientDetailCacheService = eshopClientCacheService;
  }

  @Override
  public ClientDetails loadClientByClientId(String clientId) {
    log.debug("Returning the client detail from clientId = {}", clientId);
    if (StringUtils.isBlank(clientId)) {
      throw new ClientRegistrationException("Not found client details with invalid argument");
    }
    return clientDetailCacheService.findByClientId(clientId).map(EshopClientDetails::new)
        .orElseThrow(() -> new ClientRegistrationException(
            String.format("Not found client details with client id = %s", clientId)));
  }

}
