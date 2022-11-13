package com.sagag.services.article.api.availability.finalcustomer;

import com.sagag.services.common.enums.ErpSendMethodEnum;
import com.sagag.services.domain.eshop.dto.WssDeliveryProfileDto;
import com.sagag.services.domain.sag.erp.Availability;

public interface FinalCustomerAvailabilityFilter {

  /**
   * Filters availability for final customer
   *
   * @param lastestAvailability
   * @param wssDeliveryProfile
   * @param maxAvailabilityDayRange
   * @return availability for final customer {@link Availability}
   */
  Availability filterAvailability(Availability lastestAvailability,
      WssDeliveryProfileDto wssDeliveryProfile, Integer maxAvailabilityDayRange);

  /**
   * Retrieves filter supported send method
   * @return send method supported by filter {@link ErpSendMethodEnum}
   */
  ErpSendMethodEnum sendMethod();

}
