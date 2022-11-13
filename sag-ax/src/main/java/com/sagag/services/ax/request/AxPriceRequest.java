package com.sagag.services.ax.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sagag.services.article.api.request.PriceRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * The class to build price request in e-Connect.
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class AxPriceRequest extends PriceRequest implements IAxBasketPositionCreator {

  private static final long serialVersionUID = -8095708149659237656L;

  @JsonIgnore
  private String customerNr;

  @Override
  public boolean allowUpdateVehicleInfo() {
    return true;
  }

  /**
   * Constructs the {@link AxPriceRequest} instance.
   *
   * @param builder the {@link AxPriceRequestBuilder}
   */
  public AxPriceRequest(AxPriceRequestBuilder builder) {
    setGrossPrice(builder.isGrossPrice());
    setSpecialNetPriceArticleGroup(builder.getSpecialNetPriceArticleGroup());
    setBasketPositions(createErpBasketPositions(builder.getBaskets()));
    setCustomerNr(builder.getCustomerNr());
    setPriceTypeDisplayEnum(builder.getPriceTypeDisplayEnum());
  }

}
