package com.sagag.eshop.repo.api;

import static org.junit.Assert.assertNotNull;

import com.sagag.eshop.repo.app.RepoApplication;
import com.sagag.eshop.repo.entity.EshopUser;
import com.sagag.eshop.repo.utils.RepoDataTests;
import com.sagag.services.common.annotation.EshopIntegrationTest;

import lombok.extern.slf4j.Slf4j;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Integration test class for {@link EshopUserRepository}.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = { RepoApplication.class })
@Slf4j
@EshopIntegrationTest
@Transactional
public class EshopUserRepositoryIT {

  @Autowired
  private EshopUserRepository userRepo;

  @Test
  public void testFindOneByUsername() {
    log.debug("starting UserRepositoryTest");
    final List<EshopUser> users = userRepo.findUsersByUsername(RepoDataTests.ADMIN_USER_NAME);
    Assert.assertNotNull(users);
  }

  @Test
  public void givenUserId_ShouldGetFullName() {
    final String fullName = userRepo.searchFullNameById(5L);
    Assert.assertThat(fullName, Matchers.notNullValue());
  }

  @Test
  public void givenUnknownUserId_ShouldNotGetFullName() {
    final String fullName = userRepo.searchFullNameById(999999L);
    Assert.assertThat(fullName, Matchers.nullValue());
  }

  @Test
  public void givenUserId_ShouldGetNeccessaryUserInfo() {
    final Optional<EshopUser> user = userRepo.findUserByUserId(5L);
    Assert.assertThat(user, Matchers.notNullValue());
    Assert.assertThat(user.isPresent(), Matchers.is(true));
    final EshopUser eshopUser = user.get();
    Assert.assertThat(eshopUser.getLangiso(), Matchers.is("DE"));
    Assert.assertThat(eshopUser.getEmail(), Matchers.is("adriano.sinigoi@derendinger.at"));
    Assert.assertThat(eshopUser.getHourlyRate(), Matchers.is(11.0));
    Assert.assertThat(eshopUser.isVatConfirm(), Matchers.is(true));
  }

  @Test
  public void givenUserId_ShouldGetUsername() {
    final Optional<String> username = userRepo.findUsernameById(5L);
    Assert.assertThat(username.isPresent(), Matchers.is(true));
    Assert.assertThat(username.get(), Matchers.is("sales"));
  }

  @Test
  public void isAadSaleAccount_shouldReturnUser_givenUsername() throws Exception {
    boolean isAadSaleAccount = userRepo.isAadSaleAccountHasNoRole(30603L);
    Assert.assertFalse(isAadSaleAccount);
  }

  @Test
  public void shouldFindUserLoginById() {
    final long userId = 1l;
    Optional<EshopUser> userOpt = userRepo.findUserLoginByUserId(userId);
    Assert.assertThat(userOpt.isPresent(), Matchers.is(true));
  }

  @Test
  public void findUsersByOrgId_shouldReturnUsers_givenOrgId() throws Exception {
    List<EshopUser> users = userRepo.findUsersByOrgId(0);
    assertNotNull(users);
  }
}
