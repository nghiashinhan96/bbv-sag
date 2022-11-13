package com.sagag.services.dvse.authenticator.mdm;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.access.AccessDeniedException;

import com.sagag.eshop.service.api.ExternalUserService;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.common.enums.ExternalApp;
import com.sagag.services.common.locale.LocaleContextHelper;
import com.sagag.services.common.utils.SagJSONUtil;
import com.sagag.services.domain.eshop.dto.ExternalUserDto;
import com.sagag.services.dvse.dto.dvse.ConnectUser;
import com.sagag.services.hazelcast.api.UserCacheService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AbstractDvseUserAuthenticator {

  private static final String LOGIN_REQUEST_MSG = "Authenticate MDM user info customerId= {} "
      + "- username = {} - password = {} - externalSesssionId";

  @Autowired
  private DvseUserManager dvseUserManager;

  @Autowired
  private ExternalUserService externalUserService;

  @Autowired
  private UserCacheService userCacheService;

  @Autowired
  private LocaleContextHelper localeContextHelper;

  protected ConnectUser findConnectUser(String customerId, String username, String password,
      String extSessionId) {
    log.debug(LOGIN_REQUEST_MSG, customerId, username, password, extSessionId);

    final Optional<ConnectUser> connectUserOpt =
        dvseUserManager.findConnectUser(customerId, username, extSessionId);
    final ConnectUser connectUser = connectUserOpt
        .orElseGet(updatedConnectUserSupplier(customerId, username, password, extSessionId));

    updateUserLocaleInContext(connectUser);

    log.debug("Authenticated User = {}", SagJSONUtil.convertObjectToPrettyJson(connectUser));
    return connectUser;
  }

  private Supplier<ConnectUser> updatedConnectUserSupplier(String customerId, String username,
      String password, String extSessionId) {
    return () -> {
      final ConnectUser connectUser = findUpdatedConnectUser(username, password, extSessionId);
      if (isPoolMdmUser(connectUser)) {
        // Not save poolMdmUser to dvse user map, it cause wrong mapping when mdmUser released
        return connectUser;
      }
      // Save on cache in 60 minutes, after fetch new once session
      dvseUserManager.add(customerId, username, extSessionId, connectUser);
      return connectUser;
    };
  }

  private ConnectUser findUpdatedConnectUser(String username, String password,
      String extSessionId) {

    // Note that this xml external session id store from the (sample) login url:
    // https://web2.carparts-cat.com/loginh.aspx?SID=435001&user=1F255CF3F5337E5F&pw=31D7F6939DA6EC14&ESID=5
    // it should be more security but for now the DVSE team doesn't support for user and pw
    // eventually,
    // so ESID should be simple the same prior DVSE interface. Extra effort will be considered after
    // discussing with DVSE team.
    log.debug("Request User info: username = {}, password = {}, external session Id {}", username,
        password, extSessionId);

    // Check existing user when user access to web services
    // Note: a problem with migration data duplication issue occurred in this case.
    // it will get the first record. Make sure it is unique result
    // and we will make sure it the db column is unique in another ticket
    final ExternalUserDto externalUser = externalUserService
        .getExternalUser(username, password, ExternalApp.DVSE).orElseThrow(
            () -> new NoSuchElementException(String.format("Not found user = %s", username)));

    final Long userId = externalUser.getEshopUserId();
    final UserInfo userInfo = userCacheService.get(userId);
    if (Objects.isNull(userInfo)) {
      final String msg = String.format("The user info of userId = %s is not stored in cache, "
          + "please re-login with e-Connect application", userId);
      log.error(msg);
      throw new AccessDeniedException(msg);
    }

    // External session Id param will be only sale Id since DB userId got it from xml dvse user and
    // pwd
    // normal user login meant blank external session Id
    // for multiple sales login, the sales session key is mdmUser.customerId + mdmUser.userName +
    // mdmUser.externalSessionI
    if (StringUtils.isNotBlank(extSessionId)) {
      Long saleId = Long.valueOf(extSessionId);
      userInfo.setSalesId(saleId);
      userInfo.setSalesUsername(userCacheService.get(saleId).getUsername());
    } else { // for normal user login
      userInfo.setSalesId(null);
      userInfo.setSalesUsername(null);
    }

    return new ConnectUser(userInfo, externalUser);
  }

  private static boolean isPoolMdmUser(ConnectUser connectUser) {
    return Optional.ofNullable(connectUser).map(ConnectUser::getExternalUser)
        .map(ExternalUserDto::getLockVirtualUser).filter(StringUtils::isNotBlank).isPresent();
  }

  private void updateUserLocaleInContext(final ConnectUser user) {
    if (user == null || user.getUserLocale() == null) {
      LocaleContextHolder.setLocale(localeContextHelper.defaultAppLocale());
      return;
    }
    LocaleContextHolder.setLocale(user.getUserLocale());
  }
}
