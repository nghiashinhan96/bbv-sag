package com.sagag.services.tools.batch.migration.tour_time;

import com.sagag.services.tools.domain.csv.CsvTourTime;
import com.sagag.services.tools.domain.target.TargetTourTime;
import com.sagag.services.tools.repository.target.TargetTourTimeRepository;

import lombok.extern.slf4j.Slf4j;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@StepScope
@Component
@Slf4j
public class TourTimeMigrationItemProcessor implements ItemProcessor<CsvTourTime, TargetTourTime> {

  @Autowired
  private TargetTourTimeRepository repository;

  @Override
  public TargetTourTime process(CsvTourTime item) throws Exception {
    log.debug("csv tour time branch = {} - customer = {} - tourName = {}",
        item.getBranchId(), item.getCustomer(), item.getTourName());
    TargetTourTime targetItem = targetTourTimeConverter().apply(item);
    if (isPresent(targetItem)) {
      log.warn("This tour time table record is exist");
      return null;
    }
    return targetItem;
  }

  private boolean isPresent(TargetTourTime item) {
    return repository.findByCustomerNumberAndBranchIdAndTourName(
        item.getCustomerNumber(), item.getBranchId(), item.getTourName()).isPresent();
  }

  private static Function<CsvTourTime, TargetTourTime> targetTourTimeConverter() {
    return csvTourTime -> {
      TargetTourTime tourTime = new TargetTourTime();
      tourTime.setBranchId(csvTourTime.getBranchId());
      tourTime.setCustomerNumber(csvTourTime.getCustomer());
      tourTime.setTourName(csvTourTime.getTourName());
      return tourTime;
    };
  }

}
