package com.sagag.services.ax.exception;

import org.springframework.web.client.RestClientResponseException;

public interface IAxErrorMatcher {

  boolean isMatchError(RestClientResponseException ex);

}
