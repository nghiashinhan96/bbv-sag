package com.sagag.eshop.repo.api.offer;

import com.sagag.eshop.repo.entity.offer.OfferPerson;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface OfferPersonRepository extends JpaRepository<OfferPerson, Long>,
    JpaSpecificationExecutor<OfferPerson> {

  @Query("select o from OfferPerson o where o.modifiedUserId IN :userIds")
  List<OfferPerson> findByModifiedUserIds(@Param("userIds") Set<Long> userIds);

  @Query("select o from OfferPerson o where o.createdUserId IN :userIds")
  List<OfferPerson> findByCreatedUserIds(@Param("userIds") Set<Long> userIds);
}
