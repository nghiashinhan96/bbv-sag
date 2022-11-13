package com.sagag.services.tools.batch.sales.registration;

import com.sagag.services.tools.domain.target.AadAccounts;
import com.sagag.services.tools.domain.target.EshopUser;
import com.sagag.services.tools.repository.target.AadAccountsRepository;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@StepScope
@Component
public class CreateSaleUserSSOWriter implements ItemWriter<EshopUser> {

  private static final String MALE = "MALE";

  private static final String LEGAL_ENTITY_ID = "30DA";

  private static final String PERMIT_GRUPE = "SALES";

  private static final String PERSONAL_NUMBER = "9999";

  @Autowired
  private AadAccountsRepository aadAccountsRepository;

  @Override
  public void write(List<? extends EshopUser> items) throws Exception {
    if (CollectionUtils.isEmpty(items)) {
      return;
    }
    List<AadAccounts> toSave = items.stream().map(this::toAadAccounts).collect(Collectors.toList());
    aadAccountsRepository.saveAll(toSave);
  }

  private AadAccounts toAadAccounts(EshopUser eshopUser) {
    return AadAccounts.builder()
        .firstName(eshopUser.getFirstName())
        .lastName(eshopUser.getLastName())
        .legalEntityId(LEGAL_ENTITY_ID)
        .permitGroup(PERMIT_GRUPE)
        .primaryContactEmail(eshopUser.getEmail())
        .personalNumber(PERSONAL_NUMBER)
        .gender(MALE)
        .build();
  }
}
