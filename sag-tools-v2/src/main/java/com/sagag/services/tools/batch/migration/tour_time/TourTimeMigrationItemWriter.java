package com.sagag.services.tools.batch.migration.tour_time;

import com.sagag.services.tools.domain.target.TargetTourTime;
import com.sagag.services.tools.repository.target.TargetTourTimeRepository;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@StepScope
@Component
public class TourTimeMigrationItemWriter implements ItemWriter<TargetTourTime> {

  @Autowired
  private TargetTourTimeRepository repository;

  @Override
  public void write(List<? extends TargetTourTime> items) throws Exception {

    final Set<String> filteredSet = items.stream().map(uniqueKeyConverter())
        .collect(Collectors.toSet());

    final List<TargetTourTime> finalList = items.stream()
        .filter(item -> filteredSet.contains(uniqueKeyConverter().apply(item)))
        .collect(Collectors.toList());
    if (CollectionUtils.isEmpty(finalList)) {
      return;
    }
    repository.saveAll(items);
  }

  private static Function<TargetTourTime, String> uniqueKeyConverter() {
    return item -> item.getCustomerNumber() + "_" + item.getBranchId() + "_" + item.getTourName();
  }

}
