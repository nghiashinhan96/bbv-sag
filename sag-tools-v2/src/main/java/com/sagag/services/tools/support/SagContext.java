package com.sagag.services.tools.support;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.apache.commons.collections.CollectionUtils;

import java.util.List;

/**
 * The SAG context entity.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SagContext {

  private String userAffiliate;

  private List<String> salesAffiliates;

  private String requestId;

  public boolean hasSalseAffiliates() {
    return CollectionUtils.isNotEmpty(salesAffiliates);
  }
}
