package com.sagag.services.tools.batch.sales.email;

import com.sagag.services.tools.config.OracleProfile;
import com.sagag.services.tools.domain.target.EshopUser;
import com.sagag.services.tools.domain.target.VUserDetail;
import com.sagag.services.tools.repository.target.EshopUserRepository;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
@StepScope
@Slf4j
@OracleProfile
public class SalesUserRegistrationItemProcessor implements ItemProcessor<VUserDetail, Optional<EshopUser>> {

  private static final String SALES_EMAIL_PATTERN = "ax%s@sag-ag.ch";

  private static final String SALES_PREFIX = "sales";

  @Autowired
  private EshopUserRepository eshopUserRepo;

  @Override
  @Transactional(readOnly = true)
  public Optional<EshopUser> process(VUserDetail salesUser) throws Exception {
    Optional<EshopUser> eshopUser = eshopUserRepo.findById(salesUser.getUserId());
    return eshopUser.map(user -> {
      String[] strs = StringUtils.split(user.getUsername(), SALES_PREFIX);
      if (strs.length == 0) {
        return null;
      }

      user.setEmail(String.format(SALES_EMAIL_PATTERN, strs[0]));
      log.debug("{}", user.getEmail());
      return user;
    });
  }

}
