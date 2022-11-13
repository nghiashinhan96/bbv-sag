package com.sagag.eshop.repo.api;

import com.sagag.eshop.repo.entity.EshopGroup;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EshopGroupRepository extends JpaRepository<EshopGroup, Integer> {

  @Query(
      value = "SELECT g.* from ESHOP_GROUP g "
          + "JOIN ORGANISATION_GROUP og ON g.ID = og.GROUP_ID "
          + "JOIN ORGANISATION o ON o.ID = og.ORGANISATION_ID AND o.ORG_CODE = :orgCode "
          + "JOIN GROUP_ROLE gr ON gr.GROUP_ID = g.ID AND gr.ROLE_ID = :roleId",
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

  @Query(
      value = "SELECT eg FROM EshopGroup eg "
          + "INNER JOIN eg.organisationGroups og "
          + "INNER JOIN og.organisation o "
          + "INNER JOIN o.customerSettings cs "
          + "WHERE cs.id in(:ids)")
  List<EshopGroup> findByCustomerSettingIds(@Param("ids") List<Integer> ids);

  @Query(
      value = "SELECT g from EshopGroup g join g.groupRoles gr join g.organisationGroups og join og.organisation o "
          + "WHERE gr.eshopRole.id = :roleId and o.id = :orgId")
  Optional<EshopGroup> findByOrgIdAndRoleId(@Param("orgId") int orgId, @Param("roleId") int roleId);

  @Query(value = "select eg.id from EshopGroup eg where eg.name = :name")
  Optional<Integer> findEshopGroupIdByName(@Param("name") String name);

  @Query(
      value = "SELECT g from EshopGroup g "
          + "join g.groupRoles gr "
          + "join g.organisationGroups og "
          + "join og.organisation o "
          + "WHERE gr.eshopRole.name = :roleName and o.id = :orgId")
  Optional<EshopGroup> findByOrgIdAndRoleName(@Param("orgId") int orgId,
      @Param("roleName") String roleName);

  @Query(
      value = "SELECT g from EshopGroup g "
          + "JOIN g.groupRoles gr "
          + "JOIN g.organisationGroups og "
          + "JOIN og.organisation o "
          + "WHERE o.id = :orgId")
  List<EshopGroup> findByOrgId(@Param("orgId") int orgId);

  @Query(
      value = "SELECT eg FROM EshopGroup eg "
          + "INNER JOIN eg.organisationGroups og "
          + "INNER JOIN og.organisation o "
          + "WHERE o.id in(:orgIds)")
  Optional<List<EshopGroup>> findByOrgIds(@Param("orgIds") List<Integer> orgIds);
}
