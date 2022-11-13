package com.sagag.eshop.repo.api.offer;

import com.sagag.eshop.repo.entity.offer.Offer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface OfferRepository
    extends JpaRepository<Offer, Long>, JpaSpecificationExecutor<Offer> {

  @Query("select o from Offer o "
      + "where o.id = :offerId and o.tecstate = 'ACTIVE' and o.organisation.id = :orgId")
  Optional<Offer> findByActiveOfferId(@Param("offerId") Long offerId,
      @Param("orgId") Integer orgId);

  @Query("select o from Offer o where o.createdUserId IN :userIds")
  List<Offer> findByCreatedUserIds(@Param("userIds") Set<Long> userIds);

  @Query("select o from Offer o where o.modifiedUserId IN :userIds")
  List<Offer> findByModifiedUserIds(@Param("userIds") Set<Long> userIds);
}
