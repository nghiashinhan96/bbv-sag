package com.sagag.services.tools.analysis;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.SystemUtils;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

import com.sagag.services.tools.batch.AbstractTasklet;
import com.sagag.services.tools.config.OracleProfile;
import com.sagag.services.tools.domain.csv.CsvFinalCustomer;
import com.sagag.services.tools.exception.BatchJobException;
import com.sagag.services.tools.support.Folders;
import com.sagag.services.tools.utils.CsvUtils;
import com.sagag.services.tools.utils.ToolConstants;

@Component
@OracleProfile
public class FilteringFinalCustomerFromCsvTasklet extends AbstractTasklet {

  private static final String FILE_NAME = "FINAL_CUSTOMER_";

  @Override
  public RepeatStatus execute(StepContribution contribution,
    ChunkContext chunkContext) throws BatchJobException {
    int size = ToolConstants.MAX_SIZE;

    File file = new File(SystemUtils.getUserDir() + "/csv/ORGANISATION_201711221625.csv");
    final List<CsvFinalCustomer> customers = CsvUtils.read(file, CsvFinalCustomer.class);
    final List<CsvFinalCustomer> filteredFinalCustomers = customers.stream()
      .filter(item -> "FINALCUSTOMER".equalsIgnoreCase(item.getType())).collect(Collectors.toList());
    final List<List<CsvFinalCustomer>> customerPartitions = ListUtils.partition(filteredFinalCustomers, size);
    for (int curPage = 0; curPage < customerPartitions.size(); curPage++) {
      List<CsvFinalCustomer> tmpCustomerList = customerPartitions.get(curPage);
      if (CollectionUtils.isEmpty(tmpCustomerList)) {
        continue;
      }

      final String filePath = Folders.EXPORT_FINAL_CUSTOMER_DIR_PATH.getPath() +
        FILE_NAME + curPage + ToolConstants.CSV_SUFFIX;
      CsvUtils.write(filePath, tmpCustomerList);
    }

    return finish(contribution);
  }
}
