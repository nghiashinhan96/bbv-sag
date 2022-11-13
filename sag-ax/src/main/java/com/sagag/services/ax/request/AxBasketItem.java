package com.sagag.services.ax.request;

import java.io.Serializable;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sagag.services.article.api.request.BasketPosition;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The class to build ax basket item.
 *
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AxBasketItem implements Serializable {

  private static final long serialVersionUID = -6688843944948048309L;

  private String articleId;

  private Integer quantity;

  private Integer brandId;

  private String brand;

  private String model;

  private String type;

  private String registrationDocNr;

  private String sourcingType;

  private String vendorId;

  private String arrivalTime;

  public static AxBasketItem copy(BasketPosition position) {
    AxBasketItem basketItem = new AxBasketItem();
    basketItem.setArticleId(position.getArticleId());
    basketItem.setQuantity(position.getQuantity());
    return basketItem;
  }

  /**
   * The utility method to copy position request to AX client request
   * ignore brandId for availability request.
   *
   */
  public static AxBasketItem copyWithGetPrices(BasketPosition position) {
    AxBasketItem basketItem = new AxBasketItem();
    basketItem.setArticleId(position.getArticleId());
    basketItem.setQuantity(position.getQuantity());
    if (Objects.nonNull(position.getBrandId())) {
      basketItem.setBrandId(position.getBrandId().intValue());
    }
    return basketItem;
  }

}
