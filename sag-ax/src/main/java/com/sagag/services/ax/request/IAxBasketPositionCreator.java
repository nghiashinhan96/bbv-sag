package com.sagag.services.ax.request;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sagag.services.article.api.request.ArticleRequest;
import com.sagag.services.article.api.request.BasketPosition;
import com.sagag.services.article.api.request.BasketPositionRequest;
import com.sagag.services.article.api.request.IBasketPositionCreator;
import com.sagag.services.article.api.request.VehicleRequest;

/**
 * The interface of AX basket position creator.
 *
 */
public interface IAxBasketPositionCreator extends IBasketPositionCreator {

  /**
   * Constructs the basket position for Ax request.
   *
   * @param article the article information to request
   * @param vehicle the vehicle information to request
   * @return the {@link BasketPosition}
   */
  @Override
  default BasketPosition createBasketPosition(final ArticleRequest article,
      Optional<VehicleRequest> vehicle) {
    final BasketPosition basketPosition = new BasketPosition();
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
    Optional.ofNullable(article.getBrandId()).ifPresent(basketPosition::setBrandId);

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

    return basketPosition;
  }

  /**
   * Checks if the request for price or not.
   *
   * @return <code>true</code> if the basket position created for price request, <code>false</code>
   *         for availability request or others
   */
  @JsonIgnore
  boolean allowUpdateVehicleInfo();

  /**
   * Creates ERP {@link BasketPosition} list.
   *
   * @param baskets the basket positions requests
   * @return a list of basket position
   */
  default List<BasketPosition> createErpBasketPositions(Collection<BasketPositionRequest> baskets) {
    final List<BasketPosition> basketPositions = new ArrayList<>();
    baskets.forEach(basket -> basketPositions.addAll(basket.getArticles().stream()
        .map(artReq -> this.createBasketPosition(artReq, basket.getVehicle()))
        .collect(Collectors.toList())));
    return basketPositions;
  }
}
