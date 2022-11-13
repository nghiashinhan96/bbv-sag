package com.sagag.services.ax.availability.backorder;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sagag.services.article.api.availability.ArticleAvailabilityResult;
import com.sagag.services.article.api.availability.IArticleAvailabilityProcessor;
import com.sagag.services.common.enums.ErpSendMethodEnum;
import com.sagag.services.common.profiles.AxProfile;
import com.sagag.services.domain.sag.erp.Availability;

@Component
@AxProfile
public class DefaultBackOrderAvailabilityHandler implements IBackOrderAvailablityHandler {

  @Autowired
  private IArticleAvailabilityProcessor availabilityProcessor;

  @Override
  public boolean handle(Availability availability, Object... objects) {
    final ErpSendMethodEnum sendMethodEnum = (ErpSendMethodEnum) objects[0];
    final DateTime lastTourTime = (DateTime) objects[1];
    final ArticleAvailabilityResult result =
        availabilityProcessor.process(availability, sendMethodEnum, lastTourTime);
    availability.setAvailState(result.getAvailablityStateCode());
    availability.setAvailStateColor(result.getAvailablityStateColor());
    return true;
  }

}
