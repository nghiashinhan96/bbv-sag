package com.sagag.services.tools.repository.target;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sagag.services.tools.domain.target.GroupPermission;

public interface TargetGroupPermissionRepository extends JpaRepository<GroupPermission, Integer> {

  @Query(value = "select gp from GroupPermission gp where gp.eshopGroup.id =:groupId and gp.eshopPermission.id = :permissionId")
  Optional<GroupPermission> findByGroupIdAndPermissionId(@Param("groupId") Integer groupId, @Param("permissionId") int permissionId);
}
