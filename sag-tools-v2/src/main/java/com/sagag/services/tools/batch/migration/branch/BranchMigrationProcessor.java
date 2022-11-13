package com.sagag.services.tools.batch.migration.branch;


import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Optional;

import com.sagag.services.tools.domain.csv.CsvBranchOpeningHour;
import com.sagag.services.tools.domain.elasticsearch.BranchDoc;
import com.sagag.services.tools.domain.target.Branch;
import com.sagag.services.tools.domain.target.Country;
import com.sagag.services.tools.repository.target.BranchRepository;
import com.sagag.services.tools.repository.target.CountryRepository;
import com.sagag.services.tools.service.BranchSearchService;

import lombok.extern.slf4j.Slf4j;

@StepScope
@Slf4j
@Component
public class BranchMigrationProcessor implements ItemProcessor<CsvBranchOpeningHour, Branch> {

  @Autowired
  private BranchSearchService branchSearchService;

  @Autowired
  private CountryRepository countryRepository;
  
  @Value("#{'${es.index.branch:}'}")
  private String indexBranchES;

  @Autowired
  private BranchRepository branchRepository;

  @Override
  public Branch process(final CsvBranchOpeningHour csvBranch) {

    final Optional<BranchDoc> branchESOpt = branchSearchService.searchByBranchNr(csvBranch.getBranchNr(), indexBranchES);

    if (!branchESOpt.isPresent()) {
      log.info("--------ES Branch not exist: " + csvBranch.getBranchNr());
      return null;
    }

    if (branchRepository.checkExistingByBranchNr(csvBranch.getBranchNr())) {
      log.info("--------Branch {} is already exist in DB: ", csvBranch.getBranchNr());
      return null;
    }
    return buildCustomerBranch(csvBranch, branchESOpt.get());
  }

  private Branch buildCustomerBranch(final CsvBranchOpeningHour branchCSV, final BranchDoc branchES) {
    final Branch branch = Branch.builder()
        .branchNr(branchES.getBranchNr())
        .branchCode(branchCSV.getBranchCode())
        .orgId(branchES.getOrgId())
        .zip(branchES.getZip())
        .addressDesc(branchES.getAddressDesc())
        .addressCountry(branchES.getAddressCountry())
        .addressStreet(branchES.getAddressStreet())
        .addressCity(branchES.getAddressCity())
        .regionId(branchES.getRegionId())
        .primaryEmail(branchES.getPrimaryEmail())
        .primaryUrl(branchES.getPrimaryUrl())
        .primaryFax(branchES.getPrimaryFax())
        .primaryPhone(branchES.getPrimaryPhone())
        .validForKSL(branchES.getValidForKSL())
        .openingTime(branchCSV.getOpeningHour())
        .closingTime(branchCSV.getClosingTime())
        .build();

    final Optional<Country> country = countryRepository.findByShortCode(branchES.getCountryCode());
    country.ifPresent(branch::setCountry);
    return branch;
  }
}
