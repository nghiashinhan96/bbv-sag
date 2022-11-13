package com.sagag.services.sso.tasks;

/**
 * Enumeration represents failed authorization off different reasons. With each reason the HTTP
 * status code and a description is related. The description can be placed into the
 * "WWW-Authenticate" header of the response.
 */
public enum AuthorizationFailure {

  UNAUTHORIZED(401, "Bearer realm=\"{0}\""), WRONG_AUTH_METHOD(401, "Bearer realm=\"{0}\""), WRONG_AUDIENCE(
      401, "Bearer realm=\"{0}\""), TOKEN_EXPIRED(401,
      "Bearer realm=\"{0}\" error=\"invalid_token\" error_description=\"The access token expired\""), WRONG_SIGNATURE(
      401,
      "Bearer realm=\"{0}\" error=\"invalid_token\" error_description=\"Access token signature invalid\""), MISSING_TOKEN(
      401, "No access token present"), TOKEN_PARSE_ERROR(401, "Token could not be parsed");

  private final int status;
  private final String authenticateTemplate;

  private AuthorizationFailure(int status, String template) {
    this.status = status;
    this.authenticateTemplate = template;
  }

  /**
   * @return the HTTP status code
   */
  int getStatus() {
    return status;
  }

  /**
   * Returns the template string of reason description. The placeholder '{0}' will be substituted by
   * application realm.
   *
   * @return the template string
   */
  String getAuthenticateTemplate() {
    return authenticateTemplate;
  }
}
