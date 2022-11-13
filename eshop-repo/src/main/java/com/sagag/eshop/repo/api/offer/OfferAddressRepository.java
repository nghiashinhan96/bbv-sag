package com.sagag.eshop.repo.api.offer;

import com.sagag.eshop.repo.entity.offer.OfferAddress;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface OfferAddressRepository extends JpaRepository<OfferAddress, Long> {

  Optional<OfferAddress> findOneByPersonId(final Long personId);

  @Query("select o from OfferAddress o where o.createdUserId IN :userIds")
  List<OfferAddress> findByCreatedUserIds(@Param("userIds") Set<Long> userIds);

  @Query("select o from OfferAddress o where o.modifiedUserId IN :userIds")
  List<OfferAddress> findByModifiedUserIds(@Param("userIds") Set<Long> userIds);
}
