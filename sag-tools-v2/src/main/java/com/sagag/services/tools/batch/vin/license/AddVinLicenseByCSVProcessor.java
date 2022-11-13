package com.sagag.services.tools.batch.vin.license;

import com.sagag.services.tools.config.OracleProfile;
import com.sagag.services.tools.domain.target.License;
import com.sagag.services.tools.domain.target.LicenseSettings;
import com.sagag.services.tools.repository.target.LicenseSettingsRepository;
import com.sagag.services.tools.utils.DateUtils;

import lombok.extern.slf4j.Slf4j;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

@Component
@StepScope
@OracleProfile
@Slf4j
public class AddVinLicenseByCSVProcessor implements ItemProcessor<String, Optional<License>> {

  @Value("${vinlicense.packName:VIN-10}")
  private String packName;

  @Value("${vinlicense.quantity:10}")
  private int quantity;

  @Autowired
  private LicenseSettingsRepository licenseSettingsRepo;

  @Override
  public Optional<License> process(final String customerNr) throws Exception {

    Assert.hasText(customerNr, "customerNr is required");
    if (customerNr.startsWith("0")) {
      log.warn(String.format("Skip customer starts with 0 %s", customerNr));
      return Optional.empty();
    }

    LicenseSettings settings = licenseSettingsRepo.findOneByPackName(packName)
        .orElseThrow(() -> new IllegalArgumentException("Cannot find valid license settings with packname: " + packName));

    Date now = Calendar.getInstance().getTime();
    Date endDate = DateUtils.toDateByPattern("2099-12-31 22:59:59.000", DateUtils.SWISS_DATE_PATTERN);
    return Optional.of(License.builder()
        .beginDate(now)
        .typeOfLicense("VIN")
        .packId(settings.getPackId())
        .packName(packName)
        .customerNr(Long.valueOf(customerNr))
        .userId(1L)
        .quantity(quantity)
        .quantityUsed(0)
        .lastUpdate(now)
        .lastUpdateBy(1L)
        .endDate(endDate)
        .build());
  }
}
