package com.sagag.eshop.service.api;

import com.sagag.eshop.repo.criteria.offer.OfferPersonSearchCriteria;
import com.sagag.eshop.service.dto.offer.OfferPersonDto;
import com.sagag.eshop.service.dto.offer.ViewOfferPersonDto;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Interface class of offer person.
 */
public interface OfferPersonService {

  /**
   * Creates the offer person
   *
   * @param offerPerson
   * @return the object of {@link OfferPersonDto}
   */
  OfferPersonDto createOfferPerson(final OfferPersonDto offerPerson);

  /**
   * Returns the page of offer persons in overview
   *
   * @param criteria the offer person search criteria
   * @param pageable
   * @return the page of {@link OfferPersonDto}
   */
  Page<ViewOfferPersonDto> searchOfferPersons(final OfferPersonSearchCriteria criteria,
      final Pageable pageable);

  /**
   * Edits the offer person
   *
   * @param offerPerson
   * @return the object of {@link OfferPersonDto}
   */
  OfferPersonDto editOfferPerson(final OfferPersonDto offerPerson);

  /**
   * Returns the offer person details.
   *
   * @param offerPersonId the offer person id
   * @return the optional of {@link OfferPersonDto}
   */
  Optional<OfferPersonDto> getOfferPersonDetails(final Long offerPersonId);

  /**
   * Removes the existing offer person.
   *
   * @param currentUserId the current user id
   * @param offerPersonId the offer person id
   * @return the result of {@link OfferPersonDto}
   */
  OfferPersonDto removeOfferPerson(final Long currentUserId, final Long offerPersonId);

}
