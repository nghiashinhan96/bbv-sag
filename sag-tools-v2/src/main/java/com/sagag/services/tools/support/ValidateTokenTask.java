package com.sagag.services.tools.support;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.jwk.source.RemoteJWKSet;
import com.nimbusds.jose.proc.BadJOSEException;
import com.nimbusds.jose.proc.JWSKeySelector;
import com.nimbusds.jose.proc.JWSVerificationKeySelector;
import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.proc.ConfigurableJWTProcessor;
import com.nimbusds.jwt.proc.DefaultJWTProcessor;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ValidateTokenTask {
  protected static ConfigurableJWTProcessor<SecurityContext> jwtProcessor;
  protected static JWKSource<SecurityContext> keySource;
  protected static JWSAlgorithm expectedJWSAlg;
  protected static JWSKeySelector<SecurityContext> keySelector;
  protected static SecurityContext securityContext;
  private static final String KEY_DISCOVERY_URL = "https://login.windows.net/common/discovery/keys";

  /**
   * Initialize JWK related objects.
   *
   **/
  protected static void postConstruct() {
    // Set up a JWT processor to parse the tokens and then check their signature
    // and validity time window (bounded by the "iat", "nbf" and "exp" claims)
    jwtProcessor = new DefaultJWTProcessor<>();

    // The public RSA keys to validate the signatures will be sourced from the
    // OAuth 2.0 server's JWK set, published at a well-known URL. The RemoteJWKSet
    // object caches the retrieved keys to speed up subsequent look-ups and can
    // also gracefully handle key-rollover

    try {
      keySource = new RemoteJWKSet<>(new URL(KEY_DISCOVERY_URL));
    } catch (MalformedURLException ex) {
      log.error("Error while validating ERP access token: {}", ex.getMessage());
      return;
    }

    // The expected JWS algorithm of the access tokens (agreed out-of-band)
    expectedJWSAlg = JWSAlgorithm.RS256;

    // Configure the JWT processor with a key selector to feed matching public
    // RSA keys sourced from the JWK set URL
    keySelector = new JWSVerificationKeySelector<>(expectedJWSAlg, keySource);
    jwtProcessor.setJWSKeySelector(keySelector);

    // optional context parameter, not required here
    securityContext = null;
  }

  /**
   * Validates the token.
   *
   * @param realm reflects the expected audience value of the token
   * @param accessToken access token received with the request
   *
   * @return empty optional if validation wass successfull, optional conaining an
   *         AuthrizationFailure in case of something went wrong
   */
  public static Optional<AuthorizationFailure> validate(String accessToken, String realm) {
    JWTClaimsSet claimsSet;

    postConstruct();

    if (accessToken.isEmpty()) {
      log.error("Error while validating ERP access token: Token is missing.");
      return Optional.of(AuthorizationFailure.MISSING_TOKEN);
    }

    try {
      claimsSet = jwtProcessor.process(accessToken, securityContext);
    } catch (ParseException | BadJOSEException | JOSEException ex) {
      log.error("Error while validating ERP access token: Token could not be parsed:",
          ex);
      return Optional.of(AuthorizationFailure.TOKEN_PARSE_ERROR);
    }

    if (!validateSignature(claimsSet)) {
      log.error("Error while validating ERP access token: Token has invalid signature");
      return Optional.of(AuthorizationFailure.WRONG_SIGNATURE);
    }

    if (!validateAudience(claimsSet, realm)) {
      log.error("Error while validating ERP access token: Token is not for audience {}", realm);
      return Optional.of(AuthorizationFailure.WRONG_AUDIENCE);
    }

    if (!validateExpiration(claimsSet, realm)) {
      log.error("Error while validating ERP access token: Token is expired for audience {}", realm);
      return Optional.of(AuthorizationFailure.TOKEN_EXPIRED);
    }

    return Optional.empty();
  }

  /**
   * Validate that the signature of the token is correct in order to make sure that the token has
   * not been tampered with.
   *
   * @param claimsSet claimsSet retrieved from the token
   *
   * @return true if signature is valid, false otherwise
   */
  protected static boolean validateSignature(JWTClaimsSet claimsSet) {
    try {
      // This line will throw an exception if it is not a signed JWS (as expected)
      claimsSet.toJSONObject();
      return true;
    } catch (Exception ex) {
      log.error("Error while validating ERP access token: validate signature {}", ex.getMessage());
    }
    return false;
  }

  /**
   * Validate that the token is for this application.
   *
   * @param claimsSet claimsSet retrieved from the token
   * @param realm expected audience to be encoded in the token
   *
   * @return true if audience matches the expeced audience, false otherwise
   */
  protected static boolean validateAudience(JWTClaimsSet claimsSet, String realm) {
    List<String> audienceList = claimsSet.getAudience();
    for (String audience : audienceList) {
      if (audience.contains(realm)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Validate that the token is expired for this application.
   *
   * @param claimsSet claimsSet retrieved from the token
   * @param realm expected audience to be encoded in the token
   *
   * @return true if token expired
   */
  protected static boolean validateExpiration(JWTClaimsSet claimsSet, String realm) {
    if (claimsSet.getExpirationTime().after(new Date())) {
      return true;
    }
    return false;
  }
}
