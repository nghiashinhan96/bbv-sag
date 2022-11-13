package com.sagag.services.tools.repository.target;

import com.sagag.services.tools.domain.target.UserSettings;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserSettingsRepository extends CrudRepository<UserSettings, Integer> {

  @Query(value = "SELECT us.* FROM USER_SETTINGS us "
      + "JOIN ESHOP_USER eu ON eu.SETTING = us.ID "
      + "JOIN GROUP_USER gu ON gu.USER_ID = eu.ID "
      + "JOIN ORGANISATION_GROUP og ON og.ID = gu.GROUP_ID "
      + "JOIN ORGANISATION o ON o.ID = og.ORGANISATION_ID "
      + "WHERE o.ORG_CODE = :orgCode AND eu.USERNAME = :username AND us.SHOW_HAPPY_POINTS = :hasPartnerProgram",
      nativeQuery = true)
  List<UserSettings> findUserSettingByPartnerProgram(
      @Param("hasPartnerProgram") boolean hasPartnerProgram, @Param("username") String username,
      @Param("orgCode") String orgCode);

}
