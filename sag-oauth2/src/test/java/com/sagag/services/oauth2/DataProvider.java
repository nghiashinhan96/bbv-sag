package com.sagag.services.oauth2;

import com.sagag.services.common.utils.DateUtils;
import com.sagag.services.oauth2.model.VisitRegistration;

import lombok.experimental.UtilityClass;

import org.apache.commons.lang3.StringUtils;

import java.util.Calendar;
import java.util.Date;

@UtilityClass
public class DataProvider {

  public VisitRegistration buildVisitRegistration() {
    final VisitRegistration requestBody = new VisitRegistration();
    requestBody.setCompanyID("autonet-hungary");
    requestBody.setCustomerID("11030535");
    requestBody.setLanguage("1");
    requestBody.setReturnURL(StringUtils.EMPTY);

    Date utcRequestDate = Calendar.getInstance().getTime();

    requestBody.setTimeStamp(DateUtils.getUTCDateString(utcRequestDate, DateUtils.UTC_DATE_PATTERN_2));
    requestBody.setVisitRequestKey(
        "YXV0b25ldC1odW5nYXJ5YXV0b25ldC1odW5nYXJ5LXl6dEFoMTE1MWNvbm5lY3QzX3Rlc3QxMTAzMDUzNQ==");
    requestBody.setUsername("connect3_test");
    return requestBody;
  }

  public VisitRegistration buildSampleVisitRegistration() {
    VisitRegistration sample = buildVisitRegistration();
    sample.setTimeStamp("2020-05-05T07:15:00.000Z");
    return sample;
  }

}
