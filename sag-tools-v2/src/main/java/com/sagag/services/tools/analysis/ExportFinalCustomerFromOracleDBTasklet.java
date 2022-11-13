package com.sagag.services.tools.analysis;

import com.sagag.services.tools.batch.AbstractTasklet;
import com.sagag.services.tools.config.OracleProfile;
import com.sagag.services.tools.domain.csv.CsvFinalCustomer;
import com.sagag.services.tools.domain.source.SourceOrganisation;
import com.sagag.services.tools.exception.BatchJobException;
import com.sagag.services.tools.repository.source.SourceOrganisationRepository;
import com.sagag.services.tools.support.Folders;
import com.sagag.services.tools.utils.CsvUtils;
import com.sagag.services.tools.utils.ToolConstants;

import lombok.extern.slf4j.Slf4j;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@OracleProfile
@Slf4j
public class ExportFinalCustomerFromOracleDBTasklet extends AbstractTasklet {

  private static final String FILE_NAME = "FINAL_CUSTOMER_";

  @Value("${page.start:0}")
  private int startPage;

  @Autowired(required = false)
  private SourceOrganisationRepository sourceOrgRepo;

  @Override
  public RepeatStatus execute(StepContribution contribution,
      ChunkContext chunkContext) throws BatchJobException {

    int size = ToolConstants.MAX_SIZE;
    long total = sourceOrgRepo.countFinalCustomers();
    int totalPage = (int) total / size;
    log.info("Total record = {} - total page = {}", total, totalPage);

    for (int curPage = startPage; curPage < totalPage; curPage++) {
      final Page<SourceOrganisation> orgPage =
        sourceOrgRepo.findFinalCustomers(PageRequest.of(curPage, size));
      if (!orgPage.hasContent()) {
        continue;
      }

      final List<CsvFinalCustomer> customers = new ArrayList<>();
      orgPage.forEach(src -> customers.add(CsvFinalCustomer.of(src)));

      final String filePath = Folders.EXPORT_FINAL_CUSTOMER_DIR_PATH.getPath() +
        FILE_NAME + curPage + ToolConstants.CSV_SUFFIX;
      CsvUtils.write(filePath, customers);
    }

    return finish(contribution);
  }


}
