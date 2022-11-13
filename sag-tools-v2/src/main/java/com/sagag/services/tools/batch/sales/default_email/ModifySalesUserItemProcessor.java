package com.sagag.services.tools.batch.sales.default_email;

import com.sagag.services.tools.domain.target.EshopUser;
import com.sagag.services.tools.repository.target.EshopUserRepository;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

@Component
@StepScope
@Slf4j
public class ModifySalesUserItemProcessor implements ItemProcessor<Integer, Optional<EshopUser>> {

  @Value("${sales.search.emailPrefix:}")
  private String emailPrefixSearch;

  @Value("${sales.search.emailAddress:}")
  private String emailAddressSearch;

  @Value("${sales.update.emailPrefix:}")
  private String emailPrefix;

  @Value("${sales.update.emailAddress:}")
  private String emailAddress;

  @Autowired
  private EshopUserRepository eshopUserRepo;

  @Override
  @Transactional(readOnly = true)
  public Optional<EshopUser> process(Integer item) throws Exception {
    if (StringUtils.isAnyBlank(emailPrefix, emailAddress, emailPrefixSearch, emailAddressSearch)) {
      throw new IllegalArgumentException("Must full fill all needed informations");
    }
    final String emailToSearch = emailPrefixSearch + item + emailAddressSearch;

    EshopUser eshopEntity = eshopUserRepo.findByEmail(emailToSearch).orElse(null);

    if (Objects.isNull(eshopEntity)) {
      log.debug("Cannot find eshop user with email: {}", emailToSearch);
      return Optional.empty();
    }

    final String emailToUpdate = item < 10 ? emailPrefix + "0" + item + emailAddress : emailPrefix + item + emailAddress;
    eshopEntity.setEmail(emailToUpdate);

    return Optional.of(eshopEntity);
  }

}
