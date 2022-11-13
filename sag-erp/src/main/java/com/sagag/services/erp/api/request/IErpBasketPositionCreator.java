package com.sagag.services.erp.api.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sagag.services.article.api.request.ArticleRequest;
import com.sagag.services.article.api.request.BasketPosition;
import com.sagag.services.article.api.request.BasketPositionRequest;
import com.sagag.services.article.api.request.IBasketPostionCreator;
import com.sagag.services.article.api.request.VehicleRequest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public interface IErpBasketPositionCreator extends IBasketPostionCreator {

  /**
   * Constructs the basket position for ERP request.
   * 
   * @param article the article information to request
   * @param vehicle the vehicle information to request
   * @return the {@link BasketPosition}
   */
  @Override
  default BasketPosition createBasketPosition(final ArticleRequest article,
      Optional<VehicleRequest> vehicle) {
    final BasketPosition basketPosition = new BasketPosition();
    basketPosition.setAdditionalTextDoc(article.getAdditionalTextDoc());
    basketPosition.setAdditionalTextDocPrinters(null);
    basketPosition.setRegistrationDocNr(null);
    basketPosition.setArticleId(article.getId()); // version for article id, ticket #1277
    basketPosition.setQuantity(article.getQuantity());
    if (!vehicle.isPresent() || !isBasketPositionForPrice()) {
      return basketPosition;
    }
    final VehicleRequest vehReq = vehicle.get();
    if (!Objects.isNull(vehReq.getBrandId())) {
      basketPosition.setBrandId(vehReq.getBrandId());
    }
    if (!Objects.isNull(vehReq.getModelId())) {
      basketPosition.setModelId(vehReq.getModelId());
    }
    if (!Objects.isNull(vehReq.getVehicleId())) {
      basketPosition.setVehicleId(vehReq.getVehicleId());
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
  boolean isBasketPositionForPrice();

  /**
   * Creates ERP {@link BasketPosition} list.
   * 
   * @param baskets the basket positions requests
   * @return a list of basket position
   */
  default List<BasketPosition> createErpBasketPositions(Collection<BasketPositionRequest> baskets) {
    final List<BasketPosition> basketPositions = new ArrayList<>();
    baskets.forEach(basket -> {
      basketPositions.addAll(basket.getArticles().stream()
          .map(artReq -> this.createBasketPosition(artReq, basket.getVehicle()))
          .collect(Collectors.toList()));
    });
    return basketPositions;
  }
}
