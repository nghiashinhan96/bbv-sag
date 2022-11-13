package com.sagag.services.tools.batch.sales.sso;

import com.sagag.services.tools.config.SagsysProfile;
import com.sagag.services.tools.domain.target.AadAccounts;
import com.sagag.services.tools.repository.target.AadAccountsRepository;

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
@SagsysProfile
public class TransferSalesAccountSSOFromSagSysWriter implements ItemWriter<Optional<AadAccounts>> {

  @Autowired
  private AadAccountsRepository aadAccountsRepository;

  @Override
  @Transactional
  public void write(List<? extends Optional<AadAccounts>> aadAccounts) throws Exception {
    if (CollectionUtils.isEmpty(aadAccounts)) {
      return;
    }
    List<AadAccounts> account = aadAccounts.stream()
        .filter(Optional::isPresent)
        .map(Optional::get)
        .collect(Collectors.toList());

    aadAccountsRepository.saveAll(account);
  }
}
