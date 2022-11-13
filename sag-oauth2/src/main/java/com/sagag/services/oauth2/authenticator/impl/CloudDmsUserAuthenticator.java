package com.sagag.services.oauth2.authenticator.impl;

import com.sagag.services.common.aspect.LogExecutionTime;
import com.sagag.services.common.enums.LoginMode;
import com.sagag.services.common.exception.ValidationException;
import com.sagag.services.oauth2.authenticator.OAuth2UserAuthenticator;
import com.sagag.services.oauth2.helper.EshopAuthHelper;
import com.sagag.services.oauth2.model.VisitRegistration;
import com.sagag.services.oauth2.profiles.CloudDmsExternalAuthenticatorMode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.Map;

@Component
@CloudDmsExternalAuthenticatorMode
@Slf4j
public class CloudDmsUserAuthenticator extends OAuth2UserAuthenticator<VisitRegistration> {

  private static final String ESHOP_CLOUD_DMS_CLIENT_PREFIX = "cloud-dms-";
  private static final String PERFORMANCE_MSG =
      "CloudDmsUserAuthenticator -> authenticate -> Execute cloud Dms authenticate user in {} ms";

  private static final String ESHOP_CLOUD_DMS_CLIENT_ID = "ESHOP_CLOUD_DMS_CLIENT_ID";

  @Override
  @Transactional
  @LogExecutionTime(infoMode = true, value = PERFORMANCE_MSG)
  public OAuth2AccessToken authenticate(VisitRegistration requestBody, String... args) {
    try {
      externalUserAuthenticationDataValidator.validate(requestBody);
    } catch (ValidationException ex) {
      log.error("Validation Exception:", ex);
      throw new BadCredentialsException("Got validation exception", ex);
    } catch (RuntimeException ex) {
      throw ex;
    }

    final String companyID = requestBody.getCompanyID();
    final String clientId = ESHOP_CLOUD_DMS_CLIENT_PREFIX + companyID;
    final ClientDetails authedClientDetails = loadClientByClientId(clientId);
    final String companyPassword = companyPasswordExtractor().apply(requestBody);
    if (!getPasswordEncoder().matches(companyPassword, authedClientDetails.getClientSecret())) {
      throw new BadCredentialsException("Invalid company password.");
    }

    eshopAuthHelper.setCloudDmsBody(requestBody);
    return requestOAuth2AccessToken(
      buildGrantAccessTokenRequest(clientId, requestBody.getUsername()), authedClientDetails,
      clientId);
  }

  private static Map<String, String> buildGrantAccessTokenRequest(String clientId,
      String username) {
    final Map<String, String> parameters = new HashMap<>();
    parameters.put("grant_type", "password");
    parameters.put("scope", "read write");
    parameters.put(ESHOP_CLOUD_DMS_CLIENT_ID, clientId);
    parameters.put("username", username);
    parameters.put(EshopAuthHelper.LOGIN_MODE, LoginMode.CLOUD_DMS.name());
    return parameters;
  }

}
