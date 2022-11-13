package com.sagag.eshop.service.finalcustomer;

import com.sagag.eshop.repo.api.EshopUserRepository;
import com.sagag.eshop.repo.api.LoginRepository;
import com.sagag.eshop.repo.entity.EshopUser;
import com.sagag.eshop.repo.entity.Login;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class FinalCustomerUserLoginStatusProcessor extends AbstractFinalCustomerProcessor {

  @Autowired
  private EshopUserRepository eshopUserRepo;

  @Autowired
  private LoginRepository loginRepo;

  public void process(Integer finalCustomerOrgId, boolean active) {
    List<Login> logins =
        CollectionUtils.emptyIfNull(eshopUserRepo.findUsersByOrgId(finalCustomerOrgId)).stream()
            .map(EshopUser::getLogin).collect(Collectors.toList());
    if (CollectionUtils.isEmpty(logins)) {
      return;
    }

    logins.stream().filter(Login::isUserActive).forEach(item -> item.setUserActive(active));
    loginRepo.saveAll(logins);
  }
}
