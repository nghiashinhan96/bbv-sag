package com.sagag.eshop.repo.api.offer;

import com.sagag.eshop.repo.entity.offer.ShopArticle;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface ShopArticleRepository extends JpaRepository<ShopArticle, Long>, JpaSpecificationExecutor<ShopArticle> {

  @Query("select s from ShopArticle s where s.id IN :ids and s.tecstate = 'ACTIVE' and s.organisationId= :organisationId")
  List<ShopArticle> findByIdsAndOrganisationId(@Param("ids") Set<Long> ids, @Param("organisationId") Integer organisationId);

}
