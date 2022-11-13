package com.sagag.services.ivds.api;

import com.sagag.services.common.exception.ServiceException;
import com.sagag.services.ivds.response.HaynesProClientResponse;

import java.io.BufferedReader;

/**
 * Interface class for IVDS HaynesPro search services.
 */
public interface IvdsHaynesProSearchService {

  /**
   * Stores the callback response from HaynesPro into user context.
   *
   * @param uuid the user key
   * @param vehId vehicle id
   * @param bufferedReader the current buffer reader
   */
  void handleHaynesProCallback(String userKey, String vehId, BufferedReader bufferedReader);

  /**
   * Returns the HaynesPro response from user context.
   *
   * @param uuid the user key
   * @param vehicleId the selected vehicle id
   * @return HaynesProClientResponse
   */
  HaynesProClientResponse getHaynesProResponse(String userKey, String vehicleId)
      throws ServiceException;
}
