package com.sagag.services.copydb.steps;

import com.sagag.services.copydb.batch.AbstractJobConfig;
import com.sagag.services.copydb.config.CopyDbProfile;
import com.sagag.services.copydb.domain.src.UserVehicleHistory;
import com.sagag.services.copydb.domain.dest.DestUserVehicleHistory;
import com.sagag.services.copydb.utils.Constants;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@CopyDbProfile
public class UserVehicleHistoryCopySteps extends AbstractJobConfig {

  @Autowired
  private UserVehicleHistoryProcessor userVehicleHistoryProcessor;

  @Autowired
  private DestUserVehicleHistoryWriter userVehicleHistoryWriter;

  @Bean(name = "copyUserVehicleHistory")
  public Step copyUserVehicleHistory() {
    return stepBuilderFactory.get("copyUserVehicleHistory").<UserVehicleHistory, DestUserVehicleHistory>chunk(DF_CHUNK)
        .reader(userVehicleHistoryReader())
        .processor(userVehicleHistoryProcessor)
        .writer(userVehicleHistoryWriter)
        .transactionManager(targetTransactionManager)
        .build();
  }

  @Bean
  @StepScope
  public JpaPagingItemReader<UserVehicleHistory> userVehicleHistoryReader() {
    final JpaPagingItemReader<UserVehicleHistory> reader = new JpaPagingItemReader<>();
    reader.setTransacted(true);
    reader.setEntityManagerFactory(getEntityManagerFactory());
    reader.setPageSize(Constants.MAX_PAGE_SIZE);
    reader.setQueryString("select e from UserVehicleHistory e");
    reader.setName("userVehicleHistoryReader");
    return reader;
  }

}
