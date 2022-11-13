package com.sagag.eshop.repo.api;

import com.sagag.eshop.repo.entity.GroupPermission;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * JPA repository to find the customized group permissions.
 */
public interface GroupPermissionRepository extends JpaRepository<GroupPermission, Integer> {

  //@formatter:off
  @Query(
      value = "select new com.sagag.eshop.repo.entity.GroupPermission(gp.id, gp.eshopPermission.id, gp.allowed) "
          + "from GroupPermission gp "
          + "join gp.eshopGroup g "
          + "join g.groupUsers gu "
          + "join gu.eshopUser u "
          + "where u.id = :userId")
  List<GroupPermission> findAllUserCustomizedPermissions(@Param("userId") Long userId);
  //@formatter:on

  @Query(
      value = "select new com.sagag.eshop.repo.entity.GroupPermission(gp.id, gp.eshopPermission.id, gp.allowed) "
          + "from GroupPermission gp "
          + "join gp.eshopGroup g "
          + "where g.id = :id")
  List<GroupPermission> findAllUserCustomizedPermissionsByGroupId(@Param("id") int groupId);

  @Query(value = "select gp from GroupPermission gp "
      + "where gp.eshopGroup.id = :groupId and gp.eshopPermission.id = :permissionId")
  Optional<GroupPermission> findByGroupIdAndPermissionId(@Param("groupId") int groupId,
      @Param("permissionId") int permissionId);

  @Query(value = "select gp from GroupPermission gp "
      + "where (gp.eshopGroup.id in :groupIds and gp.eshopPermission.id = :permissionId)")
  List<GroupPermission> findByGroupIdsAndPermissionId(@Param("groupIds") List<Integer> groupId,
      @Param("permissionId") int permissionId);

  @Query(value = "select gp from GroupPermission gp where gp.eshopGroup.id = :groupId")
  List<GroupPermission> findByGroupId(@Param("groupId") int groupId);
}
