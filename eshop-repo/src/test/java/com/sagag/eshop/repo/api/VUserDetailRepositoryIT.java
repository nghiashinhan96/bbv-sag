package com.sagag.eshop.repo.api;

import static org.junit.Assert.assertThat;

import com.sagag.eshop.repo.app.RepoApplication;
import com.sagag.eshop.repo.entity.VUserDetail;
import com.sagag.services.common.annotation.EshopIntegrationTest;

import lombok.extern.slf4j.Slf4j;

import org.hamcrest.CoreMatchers;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Integration test class for repository {@link VUserDetailRepository}.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = { RepoApplication.class })
@EshopIntegrationTest
@Transactional
@Slf4j
public class VUserDetailRepositoryIT {

  private static final int ORG_ID_17 = 17;
  @Autowired
  private VUserDetailRepository repository;

  @Test
  public void shouldReturnFullUserInfoByUserId() {

    // username: tuan1.ax
    final long userId = 27L;

    final Optional<VUserDetail> opt = repository.findByUserId(userId);
    Assert.assertThat(opt.isPresent(), Matchers.is(true));
    Assert.assertThat(opt.get().getUserName(), Matchers.equalTo("tuan1.ax"));
    log.info("info = {}", opt.get());
  }

  @Test
  public void givenCustomerNumber_ShouldGetAllCustomersUsers() {
    final long custNr = 1100005L;
    final List<VUserDetail> allUsernamesBelongsToCustomer =
        repository.findUsersByCustomer(String.valueOf(custNr));
    Assert.assertThat(allUsernamesBelongsToCustomer, Matchers.notNullValue());
    Assert.assertThat(allUsernamesBelongsToCustomer.size(), Matchers.greaterThanOrEqualTo(1));
  }

  @Test
  public void givenUnknownCustomerNumber_ShouldGetNoCustomersUsers() {
    final long custNr = 9999999L;
    final List<VUserDetail> allUsernamesBelongsToCustomer =
        repository.findUsersByCustomer(String.valueOf(custNr));
    Assert.assertThat(allUsernamesBelongsToCustomer, Matchers.notNullValue());
    Assert.assertThat(allUsernamesBelongsToCustomer.isEmpty(), Matchers.is(true));
  }

  @Test
  public void givenUserId_ShouldGetBelongedOrgId() {
    final long userId = 27L;
    final Optional<Integer> orgIdOpt = repository.findOrgIdByUserId(userId);
    Assert.assertThat(orgIdOpt.isPresent(), Matchers.is(true));
    Assert.assertThat(orgIdOpt.get(), Matchers.is(ORG_ID_17));
  }

  @Test
  public void givenUnknownUserId_ShouldGetNoBelongedOrgId() {
    final long userId = 99999999L;
    final Optional<Integer> orgIdOpt = repository.findOrgIdByUserId(userId);
    Assert.assertThat(orgIdOpt.isPresent(), Matchers.is(false));
  }

  @Ignore("only work with sagtest dev schema, negative case")
  @Test(expected = IncorrectResultSizeDataAccessException.class)
  public void givenUserId_ShouldExceptionWhenMoreThanOneOrgId() {
    final long userId = 4L;
    final Optional<Integer> orgIdOpt = repository.findOrgIdByUserId(userId);
    Assert.assertThat(orgIdOpt.isPresent(), Matchers.is(false));
  }

  @Test
  public void givenOrgId_ShouldGetAllBelongedUsernames() {
    final List<String> allUsernames = repository.findUsernamesByOrgId(ORG_ID_17);
    Assert.assertThat(allUsernames, Matchers.notNullValue());
    Assert.assertThat(allUsernames.size(), Matchers.greaterThanOrEqualTo(1));
  }

  @Test
  public void givenInvalidOrgId_ShouldGetNoBelongedUsernames() {
    final int orgId = 999999; // unknown orgId, case negative
    final List<String> allUsernames = repository.findUsernamesByOrgId(orgId);
    Assert.assertThat(allUsernames, Matchers.empty());
  }

  @Test
  public void givenOrgId_ShouldGetAllBelongedUsers() {
    final List<VUserDetail> allUsers = repository.findUsersByOrgId(ORG_ID_17);
    Assert.assertThat(allUsers.size(), Matchers.greaterThanOrEqualTo(1));
  }

  @Test
  public void findOrgCodeByUserId_shouldGetOrgCode_givenUserId() {
    String orgCode = repository.findOrgCodeByUserId(51L).get();
    Assert.assertThat(orgCode, CoreMatchers.is("1100017"));
  }

  @Test
  public void givenUserId_shouldGetUserName() {
    final long userId = 27L; // tuan1.ax
    Optional<String> userNameOpt = repository.findUsernameByUserId(userId);
    Assert.assertTrue(userNameOpt.isPresent());
    Assert.assertThat(userNameOpt.get(), Matchers.equalTo("tuan1.ax"));
  }

  @Test
  public void findAffiliateShortNameById_shouldReturnAffiliateShortName_givenUserId()
      throws Exception {
    String orgShortName = repository.findAffiliateShortNameByUserId(46L).get();
    assertThat(orgShortName, Matchers.is("derendinger-at"));
  }

}
