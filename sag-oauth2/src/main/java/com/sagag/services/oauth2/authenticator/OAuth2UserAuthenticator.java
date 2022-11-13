package com.sagag.services.oauth2.authenticator;

import com.sagag.services.common.authenticator.IAuthenticator;
import com.sagag.services.oauth2.helper.EshopAuthHelper;
import com.sagag.services.oauth2.model.VisitRegistration;
import com.sagag.services.oauth2.validator.ExternalUserAuthenticationDataValidator;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidClientException;
import org.springframework.security.oauth2.common.exceptions.UnsupportedGrantTypeException;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerEndpointsConfiguration;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.*;
import org.springframework.util.Assert;

import java.util.Base64;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

public abstract class OAuth2UserAuthenticator<T extends VisitRegistration>
    implements IAuthenticator<T, OAuth2AccessToken>, InitializingBean {

  @Setter
  @Getter
  private TokenGranter tokenGranter;

  @Setter
  @Getter
  private OAuth2RequestFactory oAuth2RequestFactory;

  @Autowired
  @Getter
  private PasswordEncoder passwordEncoder;

  @Autowired
  private ClientDetailsService clientDetailsService;

  @Autowired
  protected EshopAuthHelper eshopAuthHelper;

  @Autowired
  protected ExternalUserAuthenticationDataValidator externalUserAuthenticationDataValidator;

  @Autowired
  private AuthorizationServerEndpointsConfiguration authorizationServerEndpointsConfiguration;

  @Override
  public void afterPropertiesSet() throws Exception {
    final AuthorizationServerEndpointsConfigurer configurer =
        authorizationServerEndpointsConfiguration.getEndpointsConfigurer();
    setOAuth2RequestFactory(configurer.getOAuth2RequestFactory());
    setTokenGranter(configurer.getTokenGranter());
    Assert.notNull(tokenGranter, "The token granter must not be null");
    Assert.notNull(oAuth2RequestFactory, "The OAuth2 request factory must not be null");
    Assert.notNull(passwordEncoder, "The password encoder must not be null");
  }

  public ClientDetails loadClientByClientId(String clientId) {
    return clientDetailsService.loadClientByClientId(clientId);
  }

  protected Function<VisitRegistration, String> companyPasswordExtractor() {
    return requestBody -> {
      // CompanyID + CompanyPassword + Username + CustomerID + TimeStamp
      final String companyIDCompanyPasswordUsernameCustomerIDTimeStamp =
          new String(Base64.getDecoder().decode(requestBody.getVisitRequestKey()));

      // CompanyID + CompanyPassword + Username + CustomerID
      final String companyIDCompanyPasswordUsernameCustomerID =
          companyIDCompanyPasswordUsernameCustomerIdExtractor().apply(
              companyIDCompanyPasswordUsernameCustomerIDTimeStamp, requestBody.getTimeStamp());

      // CompanyID + CompanyPassword + Username
      final String companyIDCompanyPasswordUsername = companyIDCompanyPasswordUsernameExtractor()
          .apply(companyIDCompanyPasswordUsernameCustomerID, requestBody.getCustomerID());

      // CompanyID + CompanyPassword
      final String companyIDCompanyPassword = companyIDCompanyPasswordExtractor()
          .apply(companyIDCompanyPasswordUsername, requestBody.getUsername());

      // CompanyPassword
      return StringUtils.substring(companyIDCompanyPassword,
          StringUtils.length(requestBody.getCompanyID()));
    };
  }

  private BiFunction<String, String, String> companyIDCompanyPasswordUsernameCustomerIdExtractor() {
    return (companyIDCompanyPasswordUsernameCustomerIDTimeStamp, timeStamp) -> {
      final int lengthOfCompanyIDCompanyPasswordUsernameCustomerIDTimeStamp =
          StringUtils.length(companyIDCompanyPasswordUsernameCustomerIDTimeStamp);
      final int lengthOfTimeStamp = StringUtils.length(timeStamp);

      final int endIndexOfCompanyIDCompanyPasswordUsernameCustomerID =
          lengthOfCompanyIDCompanyPasswordUsernameCustomerIDTimeStamp - lengthOfTimeStamp;
      return StringUtils.substring(companyIDCompanyPasswordUsernameCustomerIDTimeStamp, 0,
          endIndexOfCompanyIDCompanyPasswordUsernameCustomerID);
    };
  }

  private BiFunction<String, String, String> companyIDCompanyPasswordUsernameExtractor() {
    return (companyIDCompanyPasswordUsernameCustomerID, customerID) -> {
      final int lengthOfCompanyIDCompanyPasswordUsernameCustomerID =
          StringUtils.length(companyIDCompanyPasswordUsernameCustomerID);
      final int lengthOfCustomerId = StringUtils.length(customerID);

      final int endIndexOfCompanyIDCompanyPasswordUsername =
          lengthOfCompanyIDCompanyPasswordUsernameCustomerID - lengthOfCustomerId;
      return StringUtils.substring(companyIDCompanyPasswordUsernameCustomerID, 0,
          endIndexOfCompanyIDCompanyPasswordUsername);
    };
  }

  private BiFunction<String, String, String> companyIDCompanyPasswordExtractor() {
    return (companyIDCompanyPasswordUsername, username) -> {
      final int endIndexOfCompanyIDCompanyPassword =
          StringUtils.length(companyIDCompanyPasswordUsername) - StringUtils.length(username);
      return StringUtils.substring(companyIDCompanyPasswordUsername, 0,
          endIndexOfCompanyIDCompanyPassword);
    };
  }

  protected OAuth2AccessToken requestOAuth2AccessToken(final Map<String, String> parameters,
    final ClientDetails authenticatedClient, String requestClientId) {
    final TokenRequest tokenRequest =
      getOAuth2RequestFactory().createTokenRequest(parameters, authenticatedClient);
    if (!StringUtils.equals(requestClientId, tokenRequest.getClientId())) {
      throw new InvalidClientException("Given client ID does not match authenticated client");
    }
    final OAuth2AccessToken accessToken =
      getTokenGranter().grant(tokenRequest.getGrantType(), tokenRequest);
    return Optional.ofNullable(accessToken).orElseThrow(() -> new UnsupportedGrantTypeException(
      String.format("Unsupported grant type: %s", tokenRequest.getGrantType())));
  }
}
