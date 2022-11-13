package com.sagag.services.tools.exception;

import org.springframework.web.client.RestClientResponseException;

public interface IAxErrorMatcher {

  boolean isMatchError(RestClientResponseException ex);

}
