package com.sagag.services.oauth2.model.client;

import com.sagag.eshop.service.dto.client.EshopClientDto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;

import java.util.Arrays;
import java.util.Collections;

@Data
@EqualsAndHashCode(callSuper = true)
public class EshopClientDetails extends BaseClientDetails {

  private static final long serialVersionUID = -1615482762324444725L;

  public EshopClientDetails(EshopClientDto eshopClient) {
    setClientId(eshopClient.getClientName());
    setClientSecret(eshopClient.getClientSecret());
    setAuthorizedGrantTypes(Arrays.asList(eshopClient.getAuthorizedGrantTypes()));
    setAccessTokenValiditySeconds(eshopClient.getAccessTokenValiditySeconds());
    setRefreshTokenValiditySeconds(eshopClient.getRefreshTokenValiditySeconds());
    setScope(Arrays.asList(eshopClient.getScopes()));
    setAuthorities(AuthorityUtils.createAuthorityList(eshopClient.getAuthorities()));
    setResourceIds(Arrays.asList(eshopClient.getResourceIds()));
    if (eshopClient.isAutoApprove()) {
      setAutoApproveScopes(getScope());
    } else {
      setAutoApproveScopes(Collections.emptySet());
    }
  }
}
