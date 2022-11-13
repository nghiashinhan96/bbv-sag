package com.sagag.services.tools.repository.target;


import com.sagag.services.tools.domain.target.ExternalUser;
import com.sagag.services.tools.support.ExternalApp;

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

  Optional<ExternalUser> findFirstByUsernameAndPasswordAndExternalApp(String username, String password, ExternalApp app);

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

  @Query("select case when count(e) > 0 then 'true' else 'false' end " + "from ExternalUser e where e.username = :externalUsername")
  boolean isUsernameExisted(@Param("externalUsername") String externalUsername);

  @Query("select exU from ExternalUser exU, VUserDetail vU where vU.orgId = :orgId and exU.eshopUserId = vU.userId")
  List<ExternalUser> findByOrgId(@Param("orgId") Integer orgId);

}
