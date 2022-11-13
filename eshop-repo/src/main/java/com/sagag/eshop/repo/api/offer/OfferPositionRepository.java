package com.sagag.eshop.repo.api.offer;

import com.sagag.eshop.repo.entity.offer.OfferPosition;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface OfferPositionRepository extends JpaRepository<OfferPosition, Long> {

  @Query("select o from OfferPosition o where o.modifiedUserId IN :userIds")
  List<OfferPosition> findByModifiedUserIds(@Param("userIds") Set<Long> userIds);

  @Query("select o from OfferPosition o where o.createdUserId IN :userIds")
  List<OfferPosition> findByCreatedUserIds(@Param("userIds") Set<Long> virtualUserIds);
}
