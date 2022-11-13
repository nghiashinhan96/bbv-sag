package com.sagag.services.tools.repository.target;

import com.sagag.services.tools.domain.target.EshopGroup;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EshopGroupRepository extends JpaRepository<EshopGroup, Integer> {

  @Query(
      value = "SELECT g.* from ESHOP_GROUP g " +
          "JOIN ORGANISATION_GROUP og ON g.ID = og.GROUP_ID " +
          "JOIN ORGANISATION o ON o.ID = og.ORGANISATION_ID AND o.ORG_CODE = :orgCode " +
          "JOIN GROUP_ROLE gr ON gr.GROUP_ID = g.ID AND gr.ROLE_ID = :roleId",
      nativeQuery = true)
  Optional<EshopGroup> findOneByOrgCodeAndRoleId(@Param("orgCode") String orgCode,
      @Param("roleId") int roleId);

  @Query(
      value = "SELECT eg FROM EshopGroup eg "
      + "INNER JOIN eg.organisationGroups og "
      + "INNER JOIN og.organisation o "
      + "INNER JOIN o.customerSettings cs "
      + "WHERE cs.id= :id")
  List<EshopGroup> findByCustomerSettingId(@Param("id") Integer id);

  @Query(value = "select eg.id from EshopGroup eg where eg.name = :name")
  Optional<Integer> findEshopGroupIdByName(@Param("name") String name);

  Optional<EshopGroup> findByName(String name);

  Optional<EshopGroup> findById(int id);
}
