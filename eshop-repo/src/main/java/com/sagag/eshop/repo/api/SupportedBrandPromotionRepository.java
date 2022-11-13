package com.sagag.eshop.repo.api;

import com.sagag.eshop.repo.entity.SupportedBrandPromotion;
import com.sagag.eshop.repo.enums.ArticleShopType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface SupportedBrandPromotionRepository
  extends JpaRepository<SupportedBrandPromotion, Integer> {

  @Query(value = "select p.brand from SupportedBrandPromotion p, SupportedAffiliate a "
      + "where p.articleShopType = :articleShopType "
      + "and p.startDate <= :requestDate and p.endDate >= :requestDate "
      + "and p.active is true "
      + "and a.id = p.supportedAffiliateId and a.shortName = :affiliate")
  List<String> findPromotionBrands(@Param("articleShopType") ArticleShopType articleShopType,
      @Param("affiliate") String affiliate, @Param("requestDate") Date requestDate);
}
