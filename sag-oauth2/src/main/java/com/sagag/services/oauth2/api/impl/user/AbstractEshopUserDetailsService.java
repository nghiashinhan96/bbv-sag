package com.sagag.services.oauth2.api.impl.user;

import com.sagag.eshop.repo.entity.EshopUser;
import com.sagag.eshop.repo.entity.Login;
import com.sagag.eshop.service.api.LoginService;
import com.sagag.eshop.service.api.UserService;
import com.sagag.eshop.service.exception.UserValidationException;
import com.sagag.eshop.service.user.UserSearchFactory;
import com.sagag.services.oauth2.helper.EshopAuthHelper;
import com.sagag.services.oauth2.model.user.EshopUserDetails;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.common.exceptions.UserDeniedAuthorizationException;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenStore;

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

@Slf4j
public abstract class AbstractEshopUserDetailsService implements UserDetailsService {

  protected static final Long NO_SUPPORTED_SALES_LOGIN = null;

  @Autowired
  protected UserService userService;

  @Autowired
  private LoginService loginService;

  @Autowired
  @Qualifier("tokenStore")
  protected TokenStore tokenStore;

  @Autowired
  protected UserSearchFactory userSearchFactory;

  @Autowired
  protected EshopAuthHelper eshopAuthHelper;

  public abstract boolean support(String affiliate);

  /**
   * Updates date time on login.
   *
   * <p>Should do update date time login</p>
   * @param foundUser found user in db
   * @param salesId login saleId
   */
  protected void doUpdateDateTimeOnLogin(EshopUser foundUser, Long salesId) {
    // Log sign in date for user login analysis
    final Login login = loginService.getLoginForUser(foundUser.getId());
    if (!login.isUserActive()) {
      log.error("User = {} is inactive", foundUser.getUsername());
      throw new UserDeniedAuthorizationException("User is inactive.");
    }
    final Date currentDate = Calendar.getInstance().getTime();
    if (salesId != null) {
      loginService.updateLastOnBehalfOfDate(login.getId(), currentDate);
      return;
    }

    if (Objects.isNull(login.getFirstLoginDate())) {
      login.setFirstLoginDate(currentDate);
      loginService.updateFirstLoginDate(login.getId(), currentDate);
    }
    userService.updateLoginSignInDate(foundUser.getId(), currentDate);
  }

  protected Optional<EshopUser> search(final String username, final String affiliate) {
    log.debug("Returning user info by username = {} - affiliate = {}", username, affiliate);
    try {
      return userSearchFactory.searchUsernameCaseSensitive(username, affiliate);
    } catch (UserValidationException e) {
      throw usernameNotFoundExceptionSupplier(username).get();
    }
  }

  protected Supplier<UsernameNotFoundException> usernameNotFoundExceptionSupplier(
      final String username) {
    return () -> {
      log.warn("Username = {} not found.", username);
      throw new UsernameNotFoundException("Username was not found");
    };
  }

  protected Long retrieveSalesIdFromToken(String salesToken) {
    if (StringUtils.isBlank(salesToken)) {
      return null;
    }
    try {
      OAuth2Authentication salesAuth =
          tokenStore.readAuthentication(tokenStore.readAccessToken(salesToken));
      return ((EshopUserDetails) salesAuth.getPrincipal()).getId();
    } catch (AuthenticationException | InvalidTokenException validateTokenException) {
      log.error("Validation error from sales token {}, {}", salesToken, validateTokenException);
      return null;
    }
  }

  protected boolean isLanguageChanged(EshopUser user, String langIso) {
    if (StringUtils.isBlank(langIso)) {
      return false;
    }
    if (NumberUtils.isDigits(langIso)
        && NumberUtils.compare(NumberUtils.toInt(langIso), user.getLanguage().getTecDoc()) == 0) {
      return false;
    }
    return !langIso.equals(user.getLanguage().getLangiso());
  }
}
