package com.sagag.services.tools.analysis;

import com.sagag.services.tools.batch.AbstractTasklet;
import com.sagag.services.tools.domain.customer.CsvCustomerInfoData;
import com.sagag.services.tools.domain.customer.CsvNonKlausCustomerInfoData;
import com.sagag.services.tools.repository.target.OrganisationRepository;
import com.sagag.services.tools.support.SupportedAffiliate;
import com.sagag.services.tools.utils.CsvUtils;
import com.sagag.services.tools.utils.ToolConstants;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@StepScope
@Slf4j
public class ReportNotYetMigrateNonKlausCustomerAnalyzer extends AbstractTasklet {

  @Value("${sag.csv.support:}")
  private String csvSupportName;

  @Autowired
  private OrganisationRepository orgRepo;

  @Override
  public RepeatStatus execute(StepContribution contribution,
      ChunkContext chunkContext) throws Exception {
    final StringBuilder fileNameBuilder = new StringBuilder();
    fileNameBuilder.append("/csv/");
    fileNameBuilder.append("NON_KLAUS_CUSTOMER");
    if (!StringUtils.isBlank(csvSupportName)) {
      fileNameBuilder.append(csvSupportName);
    }
    fileNameBuilder.append(".csv");

    log.debug("fileNameBuilder = {}", fileNameBuilder);
    final File file = new File(SystemUtils.getUserDir() + fileNameBuilder.toString());
    List<CsvCustomerInfoData> csvCustomerInfoItems = CsvUtils.read(file, ' ', StandardCharsets.UTF_8,
        CsvNonKlausCustomerInfoData.class, true).stream()
        .map(CsvNonKlausCustomerInfoData::build).collect(Collectors.toList());
    csvCustomerInfoItems.forEach(item -> item.setAffiliate(SupportedAffiliate.KLAUS));

    List<String> orgCodes = csvCustomerInfoItems.stream()
        .map(CsvCustomerInfoData::getCustomerNr)
        .collect(Collectors.toList());
    List<List<String>> orgCodesPartions = ListUtils.partition(orgCodes, ToolConstants.MAX_SIZE);

    final List<String> notMigratedCustomerNrs = new ArrayList<>();
    for (List<String> orgCodePartion : orgCodesPartions) {
      final List<String> existedOrgCodes = orgRepo.findExistedOrgCodeByOrgCodes(orgCodePartion);
      notMigratedCustomerNrs.addAll(orgCodePartion.stream()
          .filter(orgCode -> !existedOrgCodes.contains(orgCode))
          .collect(Collectors.toList()));
    }

    log.info("#################################### Report ####################################");
    log.info("################# The list of not migrated customer numbers are #################");
    final Map<String, CsvCustomerInfoData> klausCustomersMap = csvCustomerInfoItems.stream()
        .collect(Collectors.toMap(i -> i.getCustomerNr(), Function.identity()));
    final List<String> warningMessages = notMigratedCustomerNrs.stream()
        .map(notMigratedCustomerNr -> String.format("Customer number = %s - name = %s", notMigratedCustomerNr,
          klausCustomersMap.get(notMigratedCustomerNr).getName())).collect(Collectors.toList());
    for (String warnMsg : warningMessages) {
      System.out.println(warnMsg);
    }
    log.info("#################################### End Report ####################################");
    return finish(contribution);
  }

}
