package com.sagag.services.tools.repository.target;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sagag.services.tools.domain.target.EshopUser;
import com.sagag.services.tools.domain.target.GroupUser;

import java.util.List;
import java.util.Optional;

public interface GroupUserRepository extends JpaRepository<GroupUser, Integer> {

  Optional<GroupUser> findOneByEshopUser(final EshopUser eshopUser);

  @Modifying
  @Query(value = "DELETE FROM GROUP_USER WHERE USER_ID IN :userIds", nativeQuery = true)
  void removeGroupUserByUserIds(@Param("userIds") List<Long> userIds);
}
