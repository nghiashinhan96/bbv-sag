package com.sagag.services.article.api.availability;

import lombok.Data;

import org.apache.commons.lang3.StringUtils;

@Data
public class ArticleAvailabilityResult {

  private final String availablityStateColor;

  private final int availablityStateCode;

  private final boolean isExtended;

  public boolean hasResult() {
    return StringUtils.isNotBlank(availablityStateColor);
  }

}
