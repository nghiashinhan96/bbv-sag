package com.sagag.services.domain.eshop.dto.price;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.apache.commons.collections.CollectionUtils;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DisplayedPriceResponseItem implements Serializable {

  private static final long serialVersionUID = 6862594447870583707L;

  private String articleId;
  private Integer amount;
  private List<DisplayedPriceDto> prices;

  @JsonIgnore
  public boolean isValidItem() {
    return CollectionUtils.isNotEmpty(prices)
        && prices.stream().anyMatch(DisplayedPriceDto::isValidItem);
  }

  @JsonIgnore
  public DisplayedPriceDto getSinglePrice() {
    if (CollectionUtils.isNotEmpty(prices)) {
      return prices.get(0);
    }
    return null;
  }
}
