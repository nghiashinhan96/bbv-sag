package com.sagag.eshop.service.api;

import com.sagag.eshop.repo.criteria.offer.OfferSearchCriteria;
import com.sagag.eshop.repo.entity.offer.Offer;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.eshop.service.dto.offer.OfferDto;
import com.sagag.eshop.service.dto.offer.OfferGeneralDto;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface OfferService {

  Optional<OfferDto> getOfferDetails(final UserInfo user, final Long offerId);

  /**
   * Searches offers by criteria.
   *
   * @param criteria
   * @param pageable
   * @return instant of {OfferGeneralDto}
   */
  Page<OfferGeneralDto> searchOffers(final OfferSearchCriteria criteria, final Pageable pageable);

  /**
   * Removes offer by offerId
   *
   * @param user
   * @param offerId
   */
  void remove(final UserInfo user, final Long offerId);

  /**
   * Creates new offer
   *
   * @param user the logged in user
   *
   * @return instant of {OfferDto}
   */
  OfferDto create(final UserInfo user);

  /**
   * Creates new offer entity
   *
   * @param user the logged in user
   *
   * @return instant of {Offer}
   */
  Offer createNewOffer(final UserInfo user);

  /**
   * Returns offer by offer id & organisation id
   *
   * @param orgId
   * @param offerId
   * @return
   */
  Optional<Offer> getOne(final Integer orgId, final Long offerId);

}
