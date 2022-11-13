package com.sagag.eshop.repo.api;

import com.sagag.eshop.repo.entity.EshopRole;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EshopRoleRepository extends JpaRepository<EshopRole, Integer> {

  List<EshopRole> findByIdIn(List<Integer> idList);

  Optional<EshopRole> findOneByName(String roleName);

  @Query(value = "select e from EshopRole e where e.name in (:name)")
  List<EshopRole> findEshopRolesByName(@Param("name") final List<String> name);

  @Query(
      value = "select r.name from EshopRole r "
          + "join r.groupRoles gr join gr.eshopGroup g join g.groupUsers gu join gu.eshopUser u "
          + "where u.id = :userId")
  List<String> findRolesFromUserId(@Param("userId") Long userId);

  @Query(value = "select e.id from EshopRole e where e.name = :name")
  Optional<Integer> findIdByName(@Param("name") final String name);
}
