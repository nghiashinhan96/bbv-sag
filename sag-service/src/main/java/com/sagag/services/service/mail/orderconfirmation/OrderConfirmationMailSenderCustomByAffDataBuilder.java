package com.sagag.services.service.mail.orderconfirmation;

import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.domain.sag.erp.Availability;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

import java.util.Locale;
import java.util.TimeZone;

public abstract class OrderConfirmationMailSenderCustomByAffDataBuilder {

  @Autowired
  protected MessageSource messageSource;

  protected abstract Availability findShowingAvail(ArticleDocDto artDoc);

  protected abstract String getDisplayedDeliveryTime(Availability avail, TimeZone timeZone,
      Locale locale);

}
