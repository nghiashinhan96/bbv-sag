package com.sagag.eshop.service.converter.client;

import com.sagag.eshop.repo.entity.client.VEshopClient;
import com.sagag.eshop.service.dto.client.EshopClientDto;
import com.sagag.services.domain.eshop.common.EshopAuthority;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class VEshopClientConverter implements Function<VEshopClient, EshopClientDto> {

  private static final String[] SCOPES = new String[] { "read", "write" };

  private static final String[] AUTHORIZED_GRANT_TYPES =
      new String[] { "password", "authorization_code", "refresh_token" };

  private static final int ACCESS_TOKEN_VALIDITY_SECONDS = 28800;

  private static final int REFRESH_TOKEN_VALIDITY_SECONDS = 2592000;

  @Override
  public EshopClientDto apply(VEshopClient eshopClient) {
    final EshopClientDto client = new EshopClientDto();
    client.setId(eshopClient.getId());
    client.setClientName(eshopClient.getClientName());
    client.setClientSecret(eshopClient.getClientSecret());
    client.setAuthorities(eshopClient.getEshopAuthorities().stream().map(EshopAuthority::name)
        .toArray(String[]::new));
    client.setResourceIds(new String[] { StringUtils.defaultString(eshopClient.getResourceId()) });
    client.setScopes(SCOPES);
    client.setAuthorizedGrantTypes(AUTHORIZED_GRANT_TYPES);
    client.setAccessTokenValiditySeconds(ACCESS_TOKEN_VALIDITY_SECONDS);
    client.setRefreshTokenValiditySeconds(REFRESH_TOKEN_VALIDITY_SECONDS);
    return client;
  }

}
