package com.sagag.services.tools.utils;

import com.sagag.services.tools.domain.mdm.DvseKeyValue;
import com.sagag.services.tools.domain.mdm.DvseMainModule;

import lombok.experimental.UtilityClass;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Class to build user module template before create customer user.
 *
 */
@UtilityClass
public final class UserModuleBuilder {

  private static final String DEFAULT_USER_MODULE_ID = "-1";

  private static final String USER_NAME_KEY = "1";

  private static final String USER_NAME_DESCRIPTION = "Benutzername";

  private static final String PASS_KEY = "2";

  private static final String PASS_DESCRIPTION = "Passwort";

  private static final String ACTIVE_STATUS_KEY = "4";

  private static final String ACTIVE_STATUS_VALUE = "1";

  private static final String ACTIVE_STATUS_DESCRIPTION = "Aktiv";

  private static final String USER_DESCRIPTION = "Benutzer";

  public static DvseMainModule build(String username, String password) {

    final DvseMainModule module = new DvseMainModule();
    module.setId(DEFAULT_USER_MODULE_ID);
    module.setDescription(USER_DESCRIPTION);
    module.setKeyValues(getDefaultKeyValues(username, password));

    return module;
  }

  private static List<DvseKeyValue> getDefaultKeyValues(String username, String password) {
    return Arrays.asList(new DvseKeyValue(USER_NAME_KEY, username, USER_NAME_DESCRIPTION), new DvseKeyValue(PASS_KEY, password, PASS_DESCRIPTION),
        new DvseKeyValue(ACTIVE_STATUS_KEY, ACTIVE_STATUS_VALUE, ACTIVE_STATUS_DESCRIPTION));
  }

  public static boolean isUserModule(DvseMainModule module) {
    return StringUtils.equals(DEFAULT_USER_MODULE_ID, module.getId());
  }

  public static Optional<DvseMainModule> filterUserInfoByList(final List<DvseMainModule> modules) {
    if (CollectionUtils.isEmpty(modules)) {
      return Optional.empty();
    }
    return modules.stream().filter(item -> {
      return USER_DESCRIPTION.equalsIgnoreCase(item.getDescription()) && DEFAULT_USER_MODULE_ID.equalsIgnoreCase(item.getId());
    }).findFirst();
  }
  
  public static Optional<String> getUsernameByKey(List<DvseKeyValue> keyValues) {
    if (CollectionUtils.isEmpty(keyValues)) {
      return Optional.empty();
    }
    return keyValues.stream().filter(item -> USER_NAME_KEY.equalsIgnoreCase(item.getKey())).map(found -> found.getValue()).findFirst();
  }
  
  public static Optional<String> getPasswordByKey(List<DvseKeyValue> keyValues) {
    if (CollectionUtils.isEmpty(keyValues)) {
      return Optional.empty();
    }
    return keyValues.stream().filter(item -> PASS_KEY.equalsIgnoreCase(item.getKey())).map(found -> found.getValue()).findFirst();
  }
}
