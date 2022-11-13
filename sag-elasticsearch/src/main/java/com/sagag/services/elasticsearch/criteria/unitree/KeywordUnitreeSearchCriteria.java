package com.sagag.services.elasticsearch.criteria.unitree;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import org.apache.commons.lang3.StringUtils;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false)
public class KeywordUnitreeSearchCriteria {

  @NonNull
  private String text;

  private boolean perfectMatched;

  public boolean hasText() {
    return !StringUtils.isBlank(text);
  }
}
