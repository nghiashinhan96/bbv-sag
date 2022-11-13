package com.sagag.services.oauth2.authenticator.impl;

import com.sagag.services.common.aspect.LogExecutionTime;
import com.sagag.services.common.enums.LoginMode;
import com.sagag.services.common.exception.ValidationException;
import com.sagag.services.common.utils.SagJSONUtil;
import com.sagag.services.oauth2.authenticator.OAuth2UserAuthenticator;
import com.sagag.services.oauth2.helper.EshopAuthHelper;
import com.sagag.services.oauth2.model.VisitRegistration;
import com.sagag.services.oauth2.profiles.AutonetExternalAuthenticatorMode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.Map;

@Component
@AutonetExternalAuthenticatorMode
@Slf4j
public class AutonetUserAuthenticator extends OAuth2UserAuthenticator<VisitRegistration> {

  private static final String PERFORMANCE_MSG =
    "AutonetUserAuthenticator -> authenticate -> Execute Autonet authenticate user in {} ms";

  private static final String AFFILIATE = EshopAuthHelper.AFFILIATE;

  @Override
  @Transactional
  @LogExecutionTime(infoMode = true, value = PERFORMANCE_MSG)
  public OAuth2AccessToken authenticate(VisitRegistration requestBody, String... args) {
    log.debug("Visit Registration Request = {}", SagJSONUtil.convertObjectToPrettyJson(requestBody));
    try {
      externalUserAuthenticationDataValidator.validate(requestBody);
    } catch (ValidationException ex) {
      log.error("Validation Exception:", ex);
      throw new BadCredentialsException("Got validation exception", ex);
    } catch (RuntimeException ex) {
      throw ex;
    }

    final String companyID = requestBody.getCompanyID();
    final ClientDetails authedClientDetails = loadClientByClientId(companyID);
    final String companyPassword = companyPasswordExtractor().apply(requestBody);
    if (!getPasswordEncoder().matches(companyPassword, authedClientDetails.getClientSecret())) {
      throw new BadCredentialsException("Invalid password.");
    }

    eshopAuthHelper.setAuthBody(requestBody);
    return requestOAuth2AccessToken(
      buildGrantAccessTokenRequest(companyID, requestBody.getUsername()), authedClientDetails,
      companyID);
  }

  private static Map<String, String> buildGrantAccessTokenRequest(String companyId,
    String username) {
    final Map<String, String> parameters = new HashMap<>();
    parameters.put("grant_type", "password");
    parameters.put("scope", "read write");
    parameters.put(AFFILIATE, companyId);
    parameters.put("username", username);
    parameters.put(EshopAuthHelper.LOGIN_MODE, LoginMode.NORMAL.name());
    return parameters;
  }

}
