package com.sagag.eshop.repo.api.offer;

import com.sagag.eshop.repo.entity.offer.OfferPersonProperty;
import com.sagag.eshop.repo.entity.offer.OfferPersonPropertyId;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OfferPersonPropertyRepository extends
    JpaRepository<OfferPersonProperty, OfferPersonPropertyId> {

  Optional<OfferPersonProperty> findByPersonIdAndType(final Long personId, final String type);

}
