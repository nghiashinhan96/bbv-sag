package com.sagag.services.incentive.points;

public interface IncentivePointsGetter {

  /**
   * Returns incentive point info by custNr
   *
   */
  Long get(String custNr);

}
