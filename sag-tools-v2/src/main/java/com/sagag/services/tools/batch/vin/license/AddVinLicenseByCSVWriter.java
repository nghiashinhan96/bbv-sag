package com.sagag.services.tools.batch.vin.license;

import com.sagag.services.tools.config.OracleProfile;
import com.sagag.services.tools.domain.target.License;
import com.sagag.services.tools.repository.target.LicenseRepository;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@StepScope
@Component
@OracleProfile
public class AddVinLicenseByCSVWriter implements ItemWriter<Optional<License>> {

  @Autowired
  private LicenseRepository licenseRepo;

  @Override
  @Transactional
  public void write(List<? extends Optional<License>> licenses) throws Exception {
    if (CollectionUtils.isEmpty(licenses)) {
      return;
    }
    List<License> account = licenses.stream()
        .filter(Optional::isPresent)
        .map(Optional::get)
        .collect(Collectors.toList());

    licenseRepo.saveAll(account);
  }
}
