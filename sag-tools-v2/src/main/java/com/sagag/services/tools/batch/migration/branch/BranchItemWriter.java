package com.sagag.services.tools.batch.migration.branch;

import com.sagag.services.tools.domain.target.Branch;
import com.sagag.services.tools.repository.target.BranchRepository;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
@StepScope
public class BranchItemWriter implements ItemWriter<Branch> {

  @Autowired
  private BranchRepository branchRepository;

  @Override
  public void write(List<? extends Branch> items) throws Exception {
    branchRepository.saveAll(items.stream().filter(Objects::nonNull).collect(Collectors.toList()));
  }
}
