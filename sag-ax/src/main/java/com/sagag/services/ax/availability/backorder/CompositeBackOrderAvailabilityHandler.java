package com.sagag.services.ax.availability.backorder;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import com.sagag.services.ax.availability.filter.AvailabilityCriteria;
import com.sagag.services.common.enums.ErpSendMethodEnum;
import com.sagag.services.common.profiles.AxProfile;
import com.sagag.services.domain.sag.erp.Availability;

@Component
@AxProfile
@Primary
public class CompositeBackOrderAvailabilityHandler implements IBackOrderAvailablityHandler {

  @Value("${backOrder.tourTimeTable}")
  private boolean tourTimeTableMode;

  @Autowired
  private DefaultBackOrderAvailabilityHandler defaultBackOrderAvailabilityHandler;

  @Autowired
  private TourTimeTableBackOrderAvailabilityHandler tourTimeTableBackOrderAvailabilityHandler;

  @Override
  public boolean handle(Availability availability, Object... objects) {
    final AvailabilityCriteria availabilityCriteria = (AvailabilityCriteria) objects[0];
    // #2915: From this ticket, arrival time should be calculated,
    // so we will call tour time table calculation as before.
    // after we will run with new business
    boolean result =
        tourTimeTableBackOrderAvailabilityHandler.handle(availability, availabilityCriteria);
    if (tourTimeTableMode) {
      return result;
    }
    return defaultBackOrderAvailabilityHandler.handle(availability,
        availabilityCriteria.getSendMethodEnum(), availabilityCriteria.getLastTourTime());
  }

  public void updateAvailStateInfoIfCartMode(final Availability availability,
      final DateTime lastTourTime, final ErpSendMethodEnum sendMethod, final boolean isCartMode) {
    if (!isCartMode) {
      return;
    }
    // Update avail state
    defaultBackOrderAvailabilityHandler.handle(availability, sendMethod, lastTourTime);
  }

}
