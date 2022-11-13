package com.sagag.services.ax.request;

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sagag.services.article.api.request.ArticleRequest;
import com.sagag.services.article.api.request.BasketPosition;
import com.sagag.services.article.api.request.BasketPositionRequest;
import com.sagag.services.article.api.request.ExternalOrderRequest;
import com.sagag.services.article.api.request.VehicleRequest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * The class to build order request from e-Connect.
 *
 */
@Builder
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AxOrderRequest extends ExternalOrderRequest implements IAxBasketPositionCreator {

  private static final long serialVersionUID = 8171269601999426151L;

  private String customerNr;

  private String pickupBranchId;

  private String personalNumber;

  private String orderType;

  public AxOrderRequest(final String customerNr, final Collection<BasketPositionRequest> baskets) {
    setCustomerNr(customerNr);
    setBasketPositions(createErpBasketPositions(baskets));
  }

  @Override
  public boolean allowUpdateVehicleInfo() {
    return true;
  }

  /**
   * Constructs the basket position for Ax request.
   *
   * @param article the article information to request
   * @param vehicle the vehicle information to request
   * @return the {@link BasketPosition}
   */
  @Override
  public BasketPosition createBasketPosition(final ArticleRequest article,
      Optional<VehicleRequest> vehicle) {
    final BasketPosition basketPosition = new BasketPosition();
    basketPosition.setAdditionalTextDoc(article.getAdditionalTextDoc());
    basketPosition.setAdditionalTextDocPrinters(article.getAdditionalTextDocPrinters());
    basketPosition.setRegistrationDocNr(null);
    basketPosition.setArticleId(article.getId()); // version for article id, ticket #1277
    basketPosition.setQuantity(article.getQuantity());

    if (StringUtils.isNotBlank(article.getRegistrationDocNr())) {
      basketPosition.setRegistrationDocNr(article.getRegistrationDocNr());
    }

    Optional.ofNullable(article.getSourcingType()).filter(StringUtils::isNotBlank)
        .ifPresent(basketPosition::setSourcingType);
    Optional.ofNullable(article.getVendorId()).filter(StringUtils::isNotBlank)
        .ifPresent(basketPosition::setVendorId);
    Optional.ofNullable(article.getArrivalTime()).filter(StringUtils::isNotBlank)
        .ifPresent(basketPosition::setArrivalTime);
    Optional.ofNullable(article.getBrandId())
        .ifPresent(basketPosition::setBrandId);

    if (!vehicle.isPresent() || !allowUpdateVehicleInfo()) {
      return basketPosition;
    }
    final VehicleRequest vehReq = vehicle.get();
    if (!Objects.isNull(vehReq.getBrandId())) {
      basketPosition.setBrandId(vehReq.getBrandId());
    }
    if (!StringUtils.isBlank(vehReq.getBrand())) {
      basketPosition.setBrand(vehReq.getBrand());
    }
    if (!StringUtils.isBlank(vehReq.getModel())) {
      basketPosition.setModel(vehReq.getModel());
    }
    if (!StringUtils.isBlank(vehReq.getType())) {
      basketPosition.setType(vehReq.getType());
    }

    Optional.ofNullable(article.getPriceDiscTypeId()).ifPresent(basketPosition::setPriceDiscTypeId);
    Optional.ofNullable(article.getBrand()).ifPresent(basketPosition::setBrand);

    return basketPosition;
  }

}
