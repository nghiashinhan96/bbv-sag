package com.sagag.services.oauth2.validator;

import com.sagag.services.common.exception.ValidationException;
import com.sagag.services.common.utils.DateUtils;
import com.sagag.services.common.validator.IDataValidator;
import com.sagag.services.oauth2.model.VisitRegistration;

import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Component
public class ExternalUserAuthenticationDataValidator implements IDataValidator<VisitRegistration> {

  private static final String UTC_PATTERN = DateUtils.UTC_DATE_PATTERN_2;

  private static final long TEN_MINUTES = 10l;

  @Override
  public boolean validate(VisitRegistration requestBody) throws ValidationException {
    Assert.hasText(requestBody.getCompanyID(), "companyID is required");
    Assert.hasText(requestBody.getTimeStamp(), "timeStamp is required");
    Assert.hasText(requestBody.getVisitRequestKey(), "visitRequestKey is required");
    Assert.hasText(requestBody.getUsername(), "username is required");
    Assert.hasText(requestBody.getCustomerID(), "customerId is required");
    Assert.state(checkTimeOfRequest(requestBody.getTimeStamp()), "The request time is invalid");
    return true;
  }

  private boolean checkTimeOfRequest(String utcRequestTime) {
    final LocalDateTime requestTime = parseSafeUTCDateStr(utcRequestTime);
    final LocalDateTime tenAgoTime = LocalDateTime.now().minusMinutes(TEN_MINUTES);
    final LocalDateTime tenLaterTime = LocalDateTime.now().plusMinutes(TEN_MINUTES);
    return !(requestTime.isBefore(tenAgoTime) || requestTime.isAfter(tenLaterTime));
  }

  private LocalDateTime parseSafeUTCDateStr(String utcTimestamp) {
    Date requestDate = null;
    try {
      requestDate = DateUtils.parseUTCDateFromString(utcTimestamp, UTC_PATTERN);
    } catch (Exception e) {
      throw new IllegalArgumentException(
          String.format("Timestamp should be UTC format %s", UTC_PATTERN));
    }
    return LocalDateTime.ofInstant(Instant.ofEpochMilli(requestDate.getTime()),
        ZoneId.systemDefault());
  }
}
