package com.sagag.services.tools.batch.sales.default_email;

import com.sagag.services.tools.domain.target.EshopUser;
import com.sagag.services.tools.repository.target.EshopUserRepository;

import lombok.extern.slf4j.Slf4j;

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
@Slf4j
public class ModifySalesUserItemWriter implements ItemWriter<Optional<EshopUser>> {


  @Autowired
  private EshopUserRepository eshopUserRepo;

  @Override
  @Transactional
  public void write(List<? extends Optional<EshopUser>> sales) throws Exception {
    if (CollectionUtils.isEmpty(sales)) {
      return;
    }

    List<EshopUser> toSave = sales.stream()
        .filter(Optional::isPresent)
        .map(Optional::get)
        .collect(Collectors.toList());
    String ids = toSave.stream().map(EshopUser::getId).map(item -> item + "").collect(Collectors.joining(","));
    log.debug("eshop ID updated {}", ids);

    eshopUserRepo.saveAll(toSave);

  }
}
