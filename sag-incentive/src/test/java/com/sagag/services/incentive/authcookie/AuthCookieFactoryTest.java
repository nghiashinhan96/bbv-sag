package com.sagag.services.incentive.authcookie;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.sagag.services.common.enums.HashType;
import com.sagag.services.incentive.domain.IncentiveLoginDto;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.servlet.http.Cookie;

/**
 * UT for {@link AuthCookieFactory}.
 */
@RunWith(MockitoJUnitRunner.class)
public class AuthCookieFactoryTest {

  private static final String INVALID_TOKEN =
      "krZhGEd47NSYMk2QieZYYbVDMf75nDr79AhEonfamqWuNWW8LU5CszFUMKNTiNZfd%2FfjmH1YqoOMV1Exl8gituE3Zi"
          + "Qe%2BnknTdqgiYJ8M1KOF9KQilgn%2FMrZFhoAx0QY";

  private Date now;

  private IncentiveLoginDto login;

  @Before
  public void setUp() throws Exception {
    login = new IncentiveLoginDto();
    login.setType(HashType.BLCK_VAR);
    login.setPassword("Fdasdfghrsdff");
    login.setUserName("leonhardt");

    now = Calendar.getInstance().getTime();
  }

  @Test
  public void testCreate() throws Exception {
    testCookie();
  }

  private void testCookie() throws CookiePrivacyException {
    Cookie cookie = AuthCookieFactory.createCookie(login, "dch", 60 * 60 * 1000, now);
    String cookieValue1 = cookie.getValue();
    AuthCookie authCookie = AuthCookieFactory.decodeAndValidateLoginToken(cookieValue1, "dch", now);
    assertTrue(authCookie.isValid());
    assertEquals(login.getUserName(), authCookie.getPayload(CookieField.USER_NAME));
    assertEquals("dch", authCookie.getPayload(CookieField.AP_KEY));
    String cookieValue = cookie.getValue();
    assertFalse(AuthCookieFactory.decodeAndValidateLoginToken(cookieValue, "dat", now).isValid());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreateNull() throws Exception {
    login.setType(null);
    testCookie();

    login.setPassword(null);
    testCookie();
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreateNull2() throws Exception {
    login.setPassword(null);
    login.setType(null);
    AuthCookieFactory.createCookie(login, "dch", 60 * 60 * 1000, now);
  }

  @Test
  public void testCreateLong() throws Exception {
    login.setPassword("FdasdfghrsdffFdasdfghrsdff");
    login.setType(HashType.BLCK_VAR);
    login.setUserName("leonhardtleonhardt");

    Cookie cookie = AuthCookieFactory.createCookie(login, "ehaub", 60 * 60 * 1000, now);

    String cookieValue1 = cookie.getValue();
    AuthCookie authCookie =
        AuthCookieFactory.decodeAndValidateLoginToken(cookieValue1, "ehaub", now);
    assertTrue(authCookie.isValid());
    assertEquals(login.getUserName(), authCookie.getPayload(CookieField.USER_NAME));
    assertEquals("ehaub", authCookie.getPayload(CookieField.AP_KEY));
    String cookieValue = cookie.getValue();
    assertFalse(AuthCookieFactory.decodeAndValidateLoginToken(cookieValue, "dat", now).isValid());
  }

  @Test
  public void testInvalid1() throws Exception {
    Cookie cookie = new Cookie("blah", "123456789");
    String cookieValue = cookie.getValue();
    assertFalse(AuthCookieFactory.decodeAndValidateLoginToken(cookieValue, "dch", now).isValid());
  }

  @Test
  public void testInvalid2() throws Exception {
    Cookie cookie = new Cookie("login_info", "123456789");
    String cookieValue = cookie.getValue();
    assertFalse(AuthCookieFactory.decodeAndValidateLoginToken(cookieValue, "dch", now).isValid());
  }

  @Test
  public void testInvalid3() throws Exception {
    Date past = new GregorianCalendar(2013, 11, 1).getTime();
    Cookie cookie = AuthCookieFactory.createCookie(login, "dch", 60, past);
    String cookieValue = cookie.getValue();
    assertFalse(AuthCookieFactory.decodeAndValidateLoginToken(cookieValue, "dch", now).isValid());
  }

  @Test
  public void validateInvalidTokenSinceExpiredTime() {
    AuthCookie authCookie = AuthCookieFactory.decodeAndValidateLoginToken(INVALID_TOKEN);
    Assert.assertEquals(false, authCookie.isValid());
  }
}
