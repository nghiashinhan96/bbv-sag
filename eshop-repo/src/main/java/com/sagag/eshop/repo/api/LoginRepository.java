package com.sagag.eshop.repo.api;

import com.sagag.eshop.repo.entity.EshopUser;
import com.sagag.eshop.repo.entity.Login;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface LoginRepository extends JpaRepository<Login, Integer> {

  Optional<Login> findFirstByEshopUser(EshopUser eshopUser);

  Optional<Login> findByUserId(Long userId);

  /**
   * Updates last on behalf of date for user.
   *
   * @param lastOnBehalfOfDate last on behalf of date
   * @param id the id of login instance
   */
  @Modifying
  @Query(value = "update Login l set l.lastOnBehalfOfDate = :lastOnBehalfOfDate where l.id=:id")
  void updateLastOnBehalfOfDate(@Param("lastOnBehalfOfDate") Date lastOnBehalfOfDate, @Param("id") int id);

  /**
   * Updates first login date for user.
   *
   * @param firstLogInDate first login date
   * @param id the id of login instance
   */
  @Modifying
  @Query(value = "update Login l set l.firstLoginDate = :firstLoginDate where l.id=:id")
  void updateFirstLoginDate(@Param("firstLoginDate") Date firstLoginDate, @Param("id") int id);

  /**
   * Removes Login by userIds.
   *
   * @param userIds list of identified id
   */
  @Modifying
  @Query(value = "DELETE FROM LOGIN WHERE USER_ID IN :userIds", nativeQuery = true)
  void removeLoginByUserIds(@Param("userIds") List<Long> userIds);
}
