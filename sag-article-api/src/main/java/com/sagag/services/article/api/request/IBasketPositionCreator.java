package com.sagag.services.article.api.request;

import java.util.Optional;

/**
 * Interface to provide the implementation of basket position creator.
 *
 */
@FunctionalInterface
public interface IBasketPositionCreator {

  /**
   * Creates the basket position for request.
   *
   * @param article the article request, required
   * @param vehicle the vehicle to which the article request belongs, optional
   * @return {@link BasketPosition}
   */
  BasketPosition createBasketPosition(final ArticleRequest article,
      final Optional<VehicleRequest> vehicle);
}
