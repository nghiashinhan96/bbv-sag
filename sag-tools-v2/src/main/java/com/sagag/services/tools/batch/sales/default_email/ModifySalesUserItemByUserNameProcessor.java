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

import java.util.Optional;

@Component
@StepScope
@Slf4j
public class ModifySalesUserItemByUserNameProcessor implements ItemProcessor<Integer, Optional<EshopUser>> {

  @Value("${sales.search.usernamePrefix:}")
  private String usernamePrefix;

  @Value("${sales.update.emailPrefix:}")
  private String emailPrefix;

  @Value("${sales.update.emailAddress:}")
  private String emailAddress;

  @Autowired
  private EshopUserRepository eshopUserRepo;

  @Override
  public Optional<EshopUser> process(Integer item) {
    if (StringUtils.isAnyBlank(usernamePrefix, emailPrefix, emailAddress)) {
      throw new IllegalArgumentException("Must full fill all needed informations");
    }
    final String userNameToSearch = item < 10 ? usernamePrefix + "0" + item : usernamePrefix + item;
    EshopUser eshopUser = eshopUserRepo.findByUsername(userNameToSearch)
        .orElse(null);
    if(eshopUser == null) {
      log.debug("Cannot find eshop user with user: {}", userNameToSearch);
      return Optional.empty();
    }
    String emailToUpdate = item < 10 ? emailPrefix + "0" + item + emailAddress : emailPrefix + item + emailAddress;
    eshopUser.setEmail(emailToUpdate);
    return Optional.of(eshopUser);
  }
}
