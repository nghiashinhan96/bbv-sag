package com.sagag.services.tools.repository.target;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sagag.services.tools.domain.target.EshopPermission;

public interface TargetEshopPermissionRepository extends JpaRepository<EshopPermission, Integer> {

  Optional<EshopPermission> findOneByPermission(String permission);
}
