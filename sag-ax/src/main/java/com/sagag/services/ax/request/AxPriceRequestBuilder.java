package com.sagag.services.ax.request;

import java.util.Collection;

import com.sagag.services.article.api.request.BasketPositionRequest;
import com.sagag.services.common.enums.PriceDisplayTypeEnum;

import lombok.Data;

/**
 * The builder class to build {@link AxPriceRequest}.
 *
 */
@Data
public class AxPriceRequestBuilder {

  private final Collection<BasketPositionRequest> baskets; // required
  private boolean grossPrice; // optional
  private Boolean specialNetPriceArticleGroup; // optional
  private String customerNr; // required
  private PriceDisplayTypeEnum priceTypeDisplayEnum;

  /**
   * Builds the gross price for request.
   *
   * @param grossPrice the gross price
   * @return the {@link AxPriceRequestBuilder}
   */
  public AxPriceRequestBuilder grossPrice(final boolean grossPrice) {
    setGrossPrice(grossPrice);
    return this;
  }

  /**
   * Builds the special net price.
   *
   * @param specialNetPriceArticleGroup the special net price
   * @return the {@link AxPriceRequestBuilder}
   */
  public AxPriceRequestBuilder specialNetPriceArticleGroup(
      final Boolean specialNetPriceArticleGroup) {
    setSpecialNetPriceArticleGroup(specialNetPriceArticleGroup);
    return this;
  }

  public AxPriceRequestBuilder customerNr(String customerNr) {
    this.customerNr = customerNr;
    return this;
  }

  public AxPriceRequestBuilder priceDisplayTypeEnum(PriceDisplayTypeEnum priceTypeDisplayEnum) {
    this.priceTypeDisplayEnum = priceTypeDisplayEnum;
    return this;
  }

  /**
   * Creates the price request to ERP.
   *
   * @return {@link AxPriceRequest}
   */
  public AxPriceRequest build() {
    return new AxPriceRequest(this);
  }
}
