package com.sagag.eshop.repo.api;

import com.sagag.eshop.repo.entity.EshopPermission;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * JPA repository interfacing for Permissions.
 */
public interface EshopPermissionRepository extends JpaRepository<EshopPermission, Integer> {

  // @formatter:off
  @Query(
      value = "select p.id from EshopPermission p "
          + "join p.rolePermissions rp "
          + "join rp.eshopRole r "
          + "join r.groupRoles gr "
          + "join gr.eshopGroup g "
          + "join g.groupUsers gu "
          + "join gu.eshopUser u "
          + "where u.id = :userId")
  List<Integer> findAllUserDefaultPermissions(@Param("userId") Long userId);
  // @formatter:on

  @Query(value = "select p.id from EshopPermission p " + "join p.rolePermissions rp "
      + "join rp.eshopRole r " + "join r.groupRoles gr " + "where gr.id= :id")
  List<Integer> findAllUserDefaultPermissionsByGroupId(@Param("id") int groupId);

  @Query(value = "SELECT p FROM EshopPermission p " + "inner join p.permFunctions pf "
      + "inner join pf.eshopFunction f " + "where f.relativeUrl = :url")
  Optional<List<EshopPermission>> findByFunctionUrl(@Param("url") String url);


  /**
   * Returns all permissions and its functions in a list of permission ids.
   *
   * @param actualPermIds a list of permission ids
   * @return a list of permissions with its accessible functions
   */
  @Query(value = "select p from EshopPermission p where p.id in (:permIds)")
  List<EshopPermission> findAllPermissionsIn(@Param("permIds") Collection<Integer> actualPermIds);

  @Query(value = "select p.id from EshopPermission p where p.permission =:permissionName")
  Optional<Integer> findPermissionIdByName(@Param("permissionName") String permissionName);

  Optional<EshopPermission> findByPermission(String permission);
}
