package com.sagag.eshop.repo.api.collection;

import com.sagag.eshop.repo.entity.collection.OrganisationCollection;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OrganisationCollectionRepository
    extends JpaRepository<OrganisationCollection, Integer> {

  @Query("select oc from OrganisationCollection oc, CollectionRelation cr "
      + "where cr.organisationId =:orgId and cr.collectionId = oc.id "
      + "and cr.isActive=true")
  Optional<OrganisationCollection> findCollectionByOrgId(@Param("orgId") int orgId);

  @Query("select oc.id from OrganisationCollection oc where oc.shortname =:shortname")
  Optional<Integer> findCollectionIdByShortname(@Param("shortname") String shortname);

  @Query("select org.customerSettings.id from OrganisationCollection oc, CollectionRelation cr, Organisation org "
      + "where oc.shortname =:shortName and cr.collectionId = oc.id and org.id = cr.organisationId "
      + "and org.organisationType.name =:orgType and cr.isActive=true")
  List<Integer> findOrganisationSettingIdBy(@Param("shortName") String shortName, @Param("orgType") String orgType,
      Pageable pageable);

  Optional<OrganisationCollection> findByName(String name);

  Optional<OrganisationCollection> findByShortname(String shortname);

  @Query("select distinct oc.name from CollectionRelation cr "
      + "join Organisation og on og.id = cr.organisationId "
      + "join OrganisationCollection oc on oc.id = cr.collectionId "
      + "where og.orgCode LIKE concat('%',:orgCode,'%') ")
  List<String> findCollectionNameByOrgCode(@Param("orgCode") String orgCode);

  @Query("select og.orgCode from CollectionRelation cr "
      + "join Organisation og on og.id = cr.organisationId "
      + "join OrganisationCollection oc on oc.id = cr.collectionId "
      + "join OrganisationType ot on ot.id = og.organisationType " + "where ot.level ='3' "
      + "and oc.shortname =:collectionShortName")
  Page<String> findCustomersByCollectionShortName(@Param("collectionShortName") String collectionShortName,
      Pageable pageable);

  @Query("select og.orgCode from CollectionRelation cr "
      + "join Organisation og on og.id = cr.organisationId "
      + "join OrganisationCollection oc on oc.id = cr.collectionId "
      + "join OrganisationType ot on ot.id = og.organisationType " + "where ot.level ='3' "
      + "and oc.shortname =:collectionShortName")
  List<String> findCustomersByCollectionShortName(@Param("collectionShortName") String collectionShortName);

  List<OrganisationCollection> findByAffiliateId(Integer affiliateId);
}
