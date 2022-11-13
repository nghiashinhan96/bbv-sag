package com.sagag.eshop.repo.api;

import com.sagag.eshop.repo.app.RepoApplication;
import com.sagag.eshop.repo.entity.EshopUser;
import com.sagag.eshop.repo.entity.GroupUser;
import com.sagag.eshop.repo.utils.RepoDataTests;
import com.sagag.services.common.annotation.EshopIntegrationTest;

import lombok.extern.slf4j.Slf4j;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Integration test class for {@link GroupUserRepository}.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = { RepoApplication.class })
@Slf4j
@EshopIntegrationTest
@Transactional
public class GroupUserRepositoryIT {

  @Autowired
  private GroupUserRepository groupUserRepo;

  @Autowired
  private EshopUserRepository eshopUserRepo;

  @Test
  public void testFindOneByUserId() {
    log.debug("starting UserRepositoryTest");
    Optional<EshopUser> user = eshopUserRepo.findById(RepoDataTests.ADMIN_USER_ID);
    final Optional<GroupUser> users = groupUserRepo.findOneByEshopUser(user.get());
    Assert.assertTrue(users.isPresent());
  }
}
