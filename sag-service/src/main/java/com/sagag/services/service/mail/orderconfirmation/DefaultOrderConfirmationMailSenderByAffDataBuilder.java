package com.sagag.services.service.mail.orderconfirmation;

import com.sagag.services.ax.utils.AxConstants;
import com.sagag.services.common.utils.DateUtils;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.domain.sag.erp.Availability;

import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.TimeZone;

@Component
public class DefaultOrderConfirmationMailSenderByAffDataBuilder
  extends OrderConfirmationMailSenderCustomByAffDataBuilder {

  @Override
  protected final Availability findShowingAvail(ArticleDocDto artDoc) {
    return artDoc.findAvailWithLatestTime();
  }

  @Override
  protected String getDisplayedDeliveryTime(Availability avail, TimeZone timeZone, Locale locale) {
    if (avail.isDelivery24Hours()) {
      return messageSource.getMessage(AxConstants.DELIVERY_24HOURS, null, locale);
    }
    if (avail.isPickupMode() && avail.isSofort()) {
      return messageSource.getMessage(AxConstants.DELIVERY_IMMEDIATE, null, locale);
    }
    return DateUtils.formatDateStrWithTimeZone(avail.getArrivalTime(),
        DateUtils.SWISS_DATE_PATTERN_2, timeZone);
  }
}
