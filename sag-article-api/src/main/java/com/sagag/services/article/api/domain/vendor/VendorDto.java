package com.sagag.services.article.api.domain.vendor;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode(of = { "vendorId", "articleId" })
public class VendorDto implements Serializable {

  private static final long serialVersionUID = 7152676492094452855L;

  private String vendorId;

  private String vendorName;

  private String leadTime;

  private String articleId;

  private String externalArticleId;

  @JsonIgnore
  public Long getVendorIdLong() {
    return Long.valueOf(getVendorId());
  }
}
