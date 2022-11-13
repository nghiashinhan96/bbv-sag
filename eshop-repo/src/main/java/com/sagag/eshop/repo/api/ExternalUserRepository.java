package com.sagag.eshop.repo.api;

import com.sagag.eshop.repo.entity.ExternalUser;
import com.sagag.services.common.enums.ExternalApp;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ExternalUserRepository extends JpaRepository<ExternalUser, Long> {

  Optional<ExternalUser> findFirstByEshopUserIdAndExternalApp(Long eshopUserId, ExternalApp app);

  Optional<ExternalUser> findFirstByUsernameAndPasswordAndExternalApp(String username,
      String password, ExternalApp app);

  Page<ExternalUser> findByActiveAndExternalApp(Boolean active, ExternalApp app, Pageable pageable);

  /**
   * Returns the first found external user by its username and the external app.
   *
   * @param username the username
   * @param app the external app to which the user belongs
   * @return the {@link ExternalUser}
   */
  Optional<ExternalUser> findFirstByUsernameAndExternalApp(String username, ExternalApp app);

  /**
   * Removes the external user by username and external app.
   *
   * @param username the username to delete
   * @param app the external app to which the user belongs
   */
  @Modifying
  @Query("delete from ExternalUser eu where eu.username = ?1 and eu.externalApp = ?2")
  void deleteUserByUsernameAndExternalApp(String username, ExternalApp app);

  @Query("select eu.username from ExternalUser eu where eu.eshopUserId =:userId")
  Optional<String> findDVSEUsernameByUserId(@Param("userId") Long userId);

  @Query("select case when count(e) > 0 then 'true' else 'false' end "
      + "from ExternalUser e where e.username = :externalUsername")
  boolean isUsernameExisted(@Param("externalUsername") String externalUsername);

  @Query("select count(u) from ExternalUser u "
      + "where u.lockVirtualUser LIKE concat('%',:orgCode,'_%') ")
  int countVirtualUserExisted(@Param("orgCode") String orgCode);

  @Query(value = "SELECT TOP 1 * FROM EXTERNAL_USER u "
      + "WHERE u.LOCK_VIRTUAL_USER = concat( :orgCode ,'_') "
      + "and u.ESHOP_USER_ID IS NULL ",
      nativeQuery = true)
  Optional<ExternalUser> getAvailableVirtualUser(@Param(value = "orgCode") String orgCode);

  @Query(value = "UPDATE EXTERNAL_USER "
      + "SET ESHOP_USER_ID = null, "
      + "LOCK_VIRTUAL_USER = LEFT(LOCK_VIRTUAL_USER, CHARINDEX('_', LOCK_VIRTUAL_USER)) "
      + "WHERE ESHOP_USER_ID in ( :eshopUserIds)",
  nativeQuery = true)
  @Modifying
  void releaseVirtualUsers(@Param (value = "eshopUserIds") List<Long>  eshopUserIds);

  /**
   * Returns all external users of selected user id.
   *
   * @param eshopUserId
   * @return the list of external account
   */
  List<ExternalUser> findByEshopUserId(Long eshopUserId);
}
