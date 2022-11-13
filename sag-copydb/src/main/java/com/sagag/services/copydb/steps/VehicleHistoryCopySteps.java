package com.sagag.services.copydb.steps;

import com.sagag.services.copydb.batch.AbstractJobConfig;
import com.sagag.services.copydb.config.CopyDbProfile;
import com.sagag.services.copydb.domain.src.VehicleHistory;
import com.sagag.services.copydb.domain.dest.DestVehicleHistory;
import com.sagag.services.copydb.utils.Constants;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@CopyDbProfile
public class VehicleHistoryCopySteps extends AbstractJobConfig {

  @Autowired
  private VehicleHistoryProcessor vehicleHistoryProcessor;

  @Autowired
  private DestVehicleHistoryWriter vehicleHistoryWriter;

  @Bean(name = "copyVehicleHistory")
  public Step copyVehicleHistory() {
    return stepBuilderFactory.get("copyVehicleHistory").<VehicleHistory, DestVehicleHistory>chunk(DF_CHUNK)
        .reader(vehicleHistoryReader())
        .processor(vehicleHistoryProcessor)
        .writer(vehicleHistoryWriter)
        .transactionManager(targetTransactionManager)
        .build();
  }

  @Bean
  @StepScope
  public JpaPagingItemReader<VehicleHistory> vehicleHistoryReader() {
    final JpaPagingItemReader<VehicleHistory> reader = new JpaPagingItemReader<>();
    reader.setTransacted(true);
    reader.setEntityManagerFactory(getEntityManagerFactory());
    reader.setPageSize(Constants.MAX_PAGE_SIZE);
    reader.setQueryString("select e from VehicleHistory e");
    reader.setName("vehicleHistoryReader");
    return reader;
  }

}
