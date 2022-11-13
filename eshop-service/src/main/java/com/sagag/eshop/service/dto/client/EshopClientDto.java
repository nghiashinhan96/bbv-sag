package com.sagag.eshop.service.dto.client;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class EshopClientDto implements Serializable {

  private static final long serialVersionUID = -5900258959730031798L;

  private Integer id;

  private String clientName;

  private String clientSecret;

  private String[] scopes;

  private String[] authorities;

  private String[] resourceIds;

  private String[] authorizedGrantTypes;

  private int accessTokenValiditySeconds;

  private int refreshTokenValiditySeconds;

  private boolean autoApprove;

}
