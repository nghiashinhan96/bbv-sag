package com.sagag.services.ax.request;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sagag.services.article.api.request.ArticleRequest;
import com.sagag.services.article.api.request.AvailabilityRequest;
import com.sagag.services.article.api.request.BasketPosition;
import com.sagag.services.article.api.request.BasketPositionRequest;
import com.sagag.services.ax.utils.AxBranchUtils;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * The class to build AX availability request.
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class AxAvailabilityRequest extends AvailabilityRequest implements
    IAxBasketPositionCreator {

  private static final long serialVersionUID = 6000809782898006145L;

  @JsonIgnore
  private String availabilityUrl;

  private String customerNr;

  private String pickupBranchId;

  /**
   * Constructs the {@link AxAvailabilityRequest} from the builder.
   *
   * @param builder the {@link AxAvailabilityRequestBuilder}
   */
  protected AxAvailabilityRequest(final AxAvailabilityRequestBuilder builder) {
    setAvailabilityUrl(builder.getAvailabilityUrl());
    setDeliveryAddressId(builder.getAddressId());
    setSendMethodCode(builder.getSendMethod());
    setPartialDelivery(builder.isPartialDelivery());
    setIsTourTimetable(builder.isTourTimetable());
    setPickupBranchAbbreviation(StringUtils.EMPTY);
    setCalcSpecificArticleIds(true); // #1277, Only set true if the ArtID was sent
    setIncludeBackOrderTime(true); // IMPORTANT
    setBasketPositions(createErpBasketPositions(builder.getBaskets()));
    setCustomerNr(String.valueOf(builder.getCustomerNr()));
    setPickupBranchId(AxBranchUtils.getDefaultBranchIfNull(builder.getPickupBranchId()));
  }

  @Override
  public boolean allowUpdateVehicleInfo() {
    return false;
  }

  @Override
  public List<BasketPosition> createErpBasketPositions(Collection<BasketPositionRequest> baskets) {
    final Map<String, BasketPosition> basketPositionsMap = new HashMap<>();
    for (BasketPositionRequest position : baskets) {
      for (ArticleRequest article : position.getArticles()) {
        final BasketPosition basket = this.createBasketPosition(article, position.getVehicle());
        basketPositionsMap.compute(article.getId(), sumQuantitiesIfPresent(basket));
      }
    }
    return basketPositionsMap.values().stream().collect(Collectors.toList());
  }

  private static BiFunction<String, BasketPosition, BasketPosition> sumQuantitiesIfPresent(
      final BasketPosition basketPosition) {
    return (key, item) -> {
      if (item == null) {
        return basketPosition;
      }
      item.addQuantity(basketPosition.getQuantity());
      return item;
    };
  }
}
