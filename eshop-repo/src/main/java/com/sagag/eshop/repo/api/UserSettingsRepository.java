package com.sagag.eshop.repo.api;

import com.sagag.eshop.repo.entity.UserSettings;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserSettingsRepository extends CrudRepository<UserSettings, Integer> {

  /**
   * Returns a {@link UserSettings}.
   *
   * @return an UserSettings.
   */
  Optional<UserSettings> findOneById(int id);

  /**
   * Return UserSettings by given user id
   *
   * @param id
   * @return
   */
  @Query(value = "SELECT us from UserSettings us WHERE us.id = "
      + " (SELECT u.setting FROM EshopUser u WHERE u.id=:userId)")
  Optional<UserSettings> findByUserId(@Param("userId") Long id);

  /**
   * Returns the user settings from user id.
   *
   * @param userId the specific user id to get its settings
   * @return the user settings
   */
  @Query(value = "select us from EshopUser u join u.userSetting us where u.id = :userId ")
  UserSettings findSettingsByUserId(@Param("userId") Long userId);

  //@formatter:off
  @Query(
      value = "select st from UserSettings st "
          + "join st.eshopUser u "
          + "join u.login l "
          + "join u.groupUsers uGr "
          + "join uGr.eshopGroup gr "
          + "join gr.organisationGroups orgGr "
          + "join orgGr.organisation org "
          + "where (org.id=:orgId and l.isUserActive = 1)")
  Optional<List<UserSettings>> findActiveUserSettingsByOrgId(@Param("orgId") Integer orgId);
  //@formatter:on

  /**
   * Remove settings by Ids.
   *
   * @param ids list of identified id
   */
  @Modifying
  @Query(value = "DELETE FROM USER_SETTINGS WHERE ID IN :ids", nativeQuery = true)
  void removeSettingsByIds(@Param("ids") List<Integer> ids);
}
