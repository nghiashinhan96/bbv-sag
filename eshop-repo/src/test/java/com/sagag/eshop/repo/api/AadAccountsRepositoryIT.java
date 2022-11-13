package com.sagag.eshop.repo.api;

import com.sagag.eshop.repo.app.RepoApplication;
import com.sagag.eshop.repo.entity.AadAccounts;
import com.sagag.services.common.annotation.EshopIntegrationTest;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Integration test class for role {@link AadAccountsRepository}.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = { RepoApplication.class })
@EshopIntegrationTest
@Transactional
public class AadAccountsRepositoryIT {

  @Autowired
  private AadAccountsRepository aadRepo;

  @Test
  public void givenAccountId_ShouldGetAadAccount() {
    final String primaryContactEmail = "sys-ws-connect-dev@sag-ag.ch";
    final Optional<AadAccounts> account =
        aadRepo.findFirstByPrimaryContactEmailAndPermitGroup(primaryContactEmail, "SALES");
    // assume the account with id have email = sys-ws-connect-dev@sag-ag.ch
    Assert.assertThat(account.get().getPrimaryContactEmail(), Matchers.is(primaryContactEmail));
  }
}
