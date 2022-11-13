package com.sagag.services.tools.batch.sales.sso.importing;

import com.sagag.services.tools.config.OracleProfile;
import com.sagag.services.tools.domain.SsoSalesUserOutput;
import com.sagag.services.tools.domain.target.EshopGroup;
import com.sagag.services.tools.domain.target.EshopUser;
import com.sagag.services.tools.domain.target.ExternalUser;
import com.sagag.services.tools.domain.target.GroupUser;
import com.sagag.services.tools.domain.target.Login;
import com.sagag.services.tools.domain.target.UserSettings;
import com.sagag.services.tools.repository.target.AadAccountsRepository;
import com.sagag.services.tools.repository.target.EshopGroupRepository;
import com.sagag.services.tools.repository.target.EshopUserRepository;
import com.sagag.services.tools.repository.target.ExternalUserRepository;
import com.sagag.services.tools.repository.target.GroupUserRepository;
import com.sagag.services.tools.repository.target.LoginRepository;
import com.sagag.services.tools.repository.target.UserSettingsRepository;
import com.sagag.services.tools.support.EshopAuthority;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Component
@StepScope
@Transactional
@OracleProfile
public class SsoSalesUserImportingItemWriter implements ItemWriter<SsoSalesUserOutput>, InitializingBean {

  @Autowired
  private UserSettingsRepository userSettingsRepo;

  @Autowired
  private EshopUserRepository eshopUserRepo;

  @Autowired
  private LoginRepository loginRepo;

  @Autowired
  private GroupUserRepository groupUserRepo;

  @Autowired
  private EshopGroupRepository eshopGroupRepo;

  @Autowired
  private ExternalUserRepository externalUserRepo;

  @Autowired
  private AadAccountsRepository aadAccountsRepo;

  private EshopGroup salesGroup;

  @Override
  public void afterPropertiesSet() throws Exception {
    salesGroup = eshopGroupRepo.findByName(EshopAuthority.SALES_ASSISTANT.name()).orElseThrow(() -> new NoSuchElementException("Not found sales group"));
  }

  @Override
  public void write(List<? extends SsoSalesUserOutput> items) throws Exception {
    items.stream().forEach(this::writeEachItem);
  }

  private void writeEachItem(SsoSalesUserOutput item) {
    UserSettings createdUserSettings = userSettingsRepo.save(item.getUserSettings());
    EshopUser eshopUser = item.getEshopUser();
    eshopUser.setSetting(createdUserSettings.getId());
    EshopUser createdEshopUser = eshopUserRepo.save(eshopUser);

    Login login = item.getLogin();
    login.setEshopUser(createdEshopUser);
    loginRepo.save(login);

    assignSalesRole(createdEshopUser);

    ExternalUser externalUser = item.getExternalUser();
    externalUser.setEshopUserId(createdEshopUser.getId());
    externalUserRepo.save(externalUser);

    aadAccountsRepo.save(item.getAadAccounts());
  }

  private void assignSalesRole(EshopUser user) {
    final GroupUser groupUser = new GroupUser();
    groupUser.setEshopGroup(salesGroup);
    groupUser.setEshopUser(user);
    groupUserRepo.save(groupUser);
  }
}
