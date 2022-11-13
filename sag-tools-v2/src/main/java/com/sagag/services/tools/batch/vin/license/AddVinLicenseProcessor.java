package com.sagag.services.tools.batch.vin.license;

import com.sagag.services.tools.config.OracleProfile;
import com.sagag.services.tools.domain.target.License;
import com.sagag.services.tools.repository.target.LicenseRepository;
import com.sagag.services.tools.utils.DateUtils;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.Calendar;
import java.util.Date;

@Component
@StepScope
@OracleProfile
@Slf4j
public class AddVinLicenseProcessor implements ItemProcessor<String, String> {

  @Autowired
  private LicenseRepository licenseRepo;

  @Override
  public String process(final String customerNr) throws Exception {

    Assert.hasText(customerNr, "customerNr is required");
    if (customerNr.startsWith("0")) {
      log.warn(String.format("Skip customer starts with 0 %s", customerNr));
      return StringUtils.EMPTY;
    }

    log.info(String.format("Start add vin-10 for customerNr %s", customerNr));
    Date now = Calendar.getInstance().getTime();
    Date endDate = DateUtils.toDateByPattern("2099-12-31 22:59:59.000", DateUtils.SWISS_DATE_PATTERN);
    License license = License.builder()
        .beginDate(now)
        .typeOfLicense("VIN")
        .packId(3L)
        .packName("VIN-10")
        .customerNr(Long.valueOf(customerNr))
        .userId(1L)
        .quantity(10)
        .quantityUsed(0)
        .lastUpdate(now)
        .lastUpdateBy(1L)
        .endDate(endDate)
        .build();

    licenseRepo.save(license);
    return String.format("End Add vin-10 for customerNr %s", customerNr);
  }
}
