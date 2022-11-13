package com.sagag.services.stakis.erp.domain;

import com.sagag.services.article.api.domain.article.AdditionalSearchCriteria;
import com.sagag.services.article.api.request.ExternalOrderRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class TmSendOrderExternalRequest extends ExternalOrderRequest {

  private static final long serialVersionUID = -4906441200626062915L;

  private String username;

  private String customerId;

  private String securityToken;

  private String language;

  private AdditionalSearchCriteria additional;

}
