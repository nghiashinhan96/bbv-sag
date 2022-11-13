package com.sagag.services.rest.authorization;

import com.sagag.services.rest.authorization.impl.SecretKeyAuthorizationImpl;

import org.apache.commons.lang3.StringUtils;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.authentication.TestingAuthenticationToken;

@RunWith(MockitoJUnitRunner.class)
public class SecretKeyAuthorizationTest {

  private static final String ROLE_ANONYMOUS = "ROLE_ANONYMOUS";

  @InjectMocks
  private SecretKeyAuthorizationImpl authorization;

  @Test
  public void shouldAuthorize_CorrectSecretKeyAndRoleAnonymous() {
    final TestingAuthenticationToken authed = new TestingAuthenticationToken(StringUtils.EMPTY,
        StringUtils.EMPTY, ROLE_ANONYMOUS);
    final String hashKey = "ZXNob3Atd2ViOmVzaG9wLXdlYi15enRBaEdwRlc=";

    Assert.assertThat(authorization.authorize(authed, hashKey), Matchers.is(true));
  }

  @Test
  public void shouldAuthorize_CorrectSecretKeyAndMultipleRoles() {
    final TestingAuthenticationToken authed = new TestingAuthenticationToken(StringUtils.EMPTY,
        StringUtils.EMPTY, ROLE_ANONYMOUS, "ROLE_ADMIN");
    final String hashKey = "ZXNob3Atd2ViOmVzaG9wLXdlYi15enRBaEdwRlc=";

    Assert.assertThat(authorization.authorize(authed, hashKey), Matchers.is(false));
  }

  @Test
  public void shouldAuthorize_InCorrectSecretKeyAndRoleAnonymous() {
    final TestingAuthenticationToken authed = new TestingAuthenticationToken(StringUtils.EMPTY,
        StringUtils.EMPTY, ROLE_ANONYMOUS);
    final String[] hashKeys = { "ZXNob3Atd2ViOmVzaG9wLXdlYi15enRBaEdwRlcxMjM=",
        "ZXNob3Atd2ViLTEyMzplc2hvcC13ZWIteXp0QWhHcEZX",
        "ZXNob3Atd2ViOg==",
        StringUtils.EMPTY,
        null };
    for (String hashKey : hashKeys) {
      Assert.assertThat(authorization.authorize(authed, hashKey), Matchers.is(false));
    }
  }
}
