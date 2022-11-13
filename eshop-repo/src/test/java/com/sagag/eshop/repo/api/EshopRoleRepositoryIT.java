package com.sagag.eshop.repo.api;

import com.sagag.eshop.repo.app.RepoApplication;
import com.sagag.eshop.repo.utils.RepoDataTests;
import com.sagag.services.common.annotation.EshopIntegrationTest;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Integration test class for {@link EshopRoleRepository}.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = { RepoApplication.class })
@EshopIntegrationTest
@Transactional
public class EshopRoleRepositoryIT {

  @Autowired
  private EshopRoleRepository roleRepo;

  @Test
  public void givenUserId_ShouldGetAllRoles() {
    final List<String> roles = roleRepo.findRolesFromUserId(RepoDataTests.USER_AX_AD_USER_ID);
    Assert.assertThat(roles, Matchers.notNullValue());
    Assert.assertThat(roles.isEmpty(), Matchers.is(false));
  }
}
