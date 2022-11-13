package com.sagag.services.service.mail.orderconfirmation;

import com.sagag.services.common.profiles.CzProfile;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.domain.sag.erp.Availability;
import com.sagag.services.stakis.erp.enums.StakisArticleAvailabilityState;

import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.TimeZone;

@Component
@CzProfile
public class CzOrderConfirmationMailSenderByAffDataBuilder
    extends OrderConfirmationMailSenderCustomByAffDataBuilder {

  @Override
  protected Availability findShowingAvail(ArticleDocDto artDoc) {
    return artDoc.findFirstAvailability();
  }

  @Override
  protected String getDisplayedDeliveryTime(Availability avail, TimeZone timeZone, Locale locale) {
    return messageSource.getMessage(
        StakisArticleAvailabilityState.fromCode(avail.getAvailState()).getMsgCode(), null, locale);
  }

}
