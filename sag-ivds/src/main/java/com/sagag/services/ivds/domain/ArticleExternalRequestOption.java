package com.sagag.services.ivds.domain;

import com.sagag.services.article.api.domain.article.AdditionalSearchCriteria;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Class to provide the options to get Ax info in execution.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticleExternalRequestOption implements Serializable {

  private static final long serialVersionUID = -411247516238984784L;

  private boolean callErpRequest;

  private boolean filterArticleBefore;

  private boolean updatePrice;

  private boolean updateStock;

  private boolean updateAvailability;

  private boolean ignoredArtElasticsearch;

  private Integer finalCustomerOrgId;

  private AdditionalSearchCriteria additional;

}
