package com.sagag.services.article.api.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sagag.services.common.enums.PriceDisplayTypeEnum;

import lombok.Data;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

/**
 * Class to contain price request to send to ERP.
 *
 */
@Data
public abstract class PriceRequest implements Serializable {

  private static final long serialVersionUID = -8654596869497751463L;

  private Boolean grossPrice;

  private Boolean specialNetPriceArticleGroup;

  @JsonIgnore
  private PriceDisplayTypeEnum priceTypeDisplayEnum;

  /**
   * List of article positions for which prices are requested. The list must contain at least one
   * item.
   */
  private List<BasketPosition> basketPositions;

  @JsonIgnore
  public Optional<Integer> findQuantityByArticleId(final String articleId) {
    if (CollectionUtils.isEmpty(basketPositions) || StringUtils.isBlank(articleId)) {
      return Optional.empty();
    }
    return basketPositions.stream()
        .filter(position -> position.getArticleId().equals(articleId))
        .findFirst().map(BasketPosition::getQuantity);
  }
}
