package com.sagag.services.oauth2.endpoint;

import com.sagag.services.oauth2.authenticator.OAuth2UserAuthenticator;
import com.sagag.services.oauth2.model.VisitRegistration;

import lombok.Setter;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.endpoint.FrameworkEndpoint;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;

@FrameworkEndpoint
public class ExternalUserVisitEndpoint implements InitializingBean {

  @Setter
  private OAuth2UserAuthenticator<VisitRegistration> userAuthenticator;

  @Override
  public void afterPropertiesSet() throws Exception {
    Assert.notNull(userAuthenticator, "The given authenticator must not be null");
  }

  @PostMapping(value = "/visit/{companyID}")
  public ResponseEntity<OAuth2AccessToken> visit(HttpServletRequest request,
      @PathVariable("companyID") final String companyID,
      @RequestBody VisitRegistration body) {
    final BodyBuilder bodyBuilder = ResponseEntity.ok()
        .header(HttpHeaders.CACHE_CONTROL, CacheControl.noStore().getHeaderValue())
        .header(HttpHeaders.PRAGMA, CacheControl.noCache().getHeaderValue())
        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE);

    body.setCompanyID(companyID);
    return bodyBuilder.body(userAuthenticator.authenticate(body));
  }
}
