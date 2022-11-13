package com.sagag.services.ax.availability.backorder;

import com.sagag.services.domain.sag.erp.Availability;

/**
 * Interface to provide functional method handle availability response.
 */
@FunctionalInterface
public interface IBackOrderAvailablityHandler {

  /**
   * Handles binding the availability.
   *
   * @param availability the original availability
   * @return <code>true</code> if the good to binding availability,
   *   otherwise <code>false</code> if not good for update
   */
  boolean handle(Availability availability, Object... objects);

}
