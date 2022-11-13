package com.sagag.services.service.exception;

import com.sagag.services.common.exception.ValidationException;

import java.util.Collections;
import java.util.Map;

/**
 * Location exception class definition.
 */
public class LocationValidationException extends ValidationException {


  private static final long serialVersionUID = -6129831397808779814L;

  private String locationId;

  /**
   * Construct location exception in case mismatch locationId recognized by shopping basket and
   * create order request
   * 
   * @param locationId
   */
  public LocationValidationException(final String locationId) {
    super(String.format("Invalid location info for this order with locationId= %s", locationId));
    this.locationId = locationId;
    setMoreInfos(buildMoreInfos());
  }

  @Override
  public Map<String, Object> buildMoreInfos() {
    return Collections.singletonMap("LOCATION", locationId);
  }
}
