package com.sagag.eshop.repo.api.collection;

import com.sagag.eshop.repo.entity.collection.CollectionRelation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CollectionRelationRepository extends JpaRepository<CollectionRelation, Integer>{

  Long countByCollectionId(Integer collectionId);

  @Query(value = "select cr from CollectionRelation cr, Organisation org "
      + "where cr.organisationId = org.id and (org.id=:wholesalerOrgId or org.parentId=:wholesalerOrgId)")
  List<CollectionRelation> findAllByWholesalerOrgId(@Param("wholesalerOrgId") Integer wholesalerOrgId);
}
