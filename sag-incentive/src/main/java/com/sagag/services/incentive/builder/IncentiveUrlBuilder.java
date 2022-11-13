package com.sagag.services.incentive.builder;

import com.sagag.services.common.exception.ServiceException;

@FunctionalInterface
public interface IncentiveUrlBuilder {

  /**
   * Builds incentive URL.
   *
   * @return the outlet URL.
   */
  String buildUrl() throws ServiceException;

}
