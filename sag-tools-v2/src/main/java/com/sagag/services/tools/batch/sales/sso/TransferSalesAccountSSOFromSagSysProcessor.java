package com.sagag.services.tools.batch.sales.sso;

import com.sagag.services.tools.config.SagsysProfile;
import com.sagag.services.tools.domain.target.AadAccounts;
import com.sagag.services.tools.repository.target.AadAccountsRepository;
import com.sagag.services.tools.utils.SagBeanUtils;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Component
@StepScope
@SagsysProfile
public class TransferSalesAccountSSOFromSagSysProcessor implements ItemProcessor<com.sagag.services.tools.domain.sagsys.AadAccounts, Optional<AadAccounts>> {

  @Autowired
  private AadAccountsRepository aadAccountsRepository;

  @Override
  @Transactional(readOnly = true)
  public Optional<AadAccounts> process(com.sagag.services.tools.domain.sagsys.AadAccounts sourceAccount) throws Exception {
    if (sourceAccount == null || StringUtils.isBlank(sourceAccount.getPrimaryContactEmail())) {
      return Optional.empty();
    }

    List<AadAccounts> duplicatedAccountOnTargetRepo = aadAccountsRepository.findByPrimaryContactEmail(sourceAccount.getPrimaryContactEmail());

    if (CollectionUtils.isNotEmpty(duplicatedAccountOnTargetRepo)) {
      return Optional.empty();
    }

    return Optional.ofNullable(SagBeanUtils.map(sourceAccount, AadAccounts.class));
  }

}
