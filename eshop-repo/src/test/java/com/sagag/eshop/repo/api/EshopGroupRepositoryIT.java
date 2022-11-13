package com.sagag.eshop.repo.api;

import static org.junit.Assert.assertNotNull;

import com.sagag.eshop.repo.app.RepoApplication;
import com.sagag.eshop.repo.entity.EshopGroup;
import com.sagag.services.common.annotation.EshopIntegrationTest;
import com.sagag.services.common.enums.RoleTypeEnum;

import lombok.extern.slf4j.Slf4j;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Integration test class for {@link EshopFunctionRepository}.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = { RepoApplication.class })
@Slf4j
@EshopIntegrationTest
@Transactional
public class EshopGroupRepositoryIT {

  @Autowired
  private EshopGroupRepository eshopGroupRepository;

  @Test
  public void testFindOneByOrgCodeAndRoleId() {
    log.debug("starting testFindOneByOrgCodeAndRoleId");
    final Optional<EshopGroup> eshopGroup =
        eshopGroupRepository.findOneByOrgCodeAndRoleId("1100005", 3);
    Assert.assertTrue(eshopGroup.isPresent());

  }

  @Test
  public void givenOrgIdAndRoleId_ShouldGetUserGroup() {
    final Optional<EshopGroup> eshopGroup = eshopGroupRepository.findByOrgIdAndRoleId(17, 3);
    Assert.assertTrue(eshopGroup.isPresent());
  }

  @Test
  public void findByOrgIdAndRoleName_shouldReturnGroup_givenOrgIdAndRoleName() throws Exception {
    EshopGroup group =
        eshopGroupRepository.findByOrgIdAndRoleName(17, RoleTypeEnum.NORMAL_USER.name()).get();
    assertNotNull(group);
  }

  @Test
  public void findByOrgId_shouldReturnEshopGroups_giveOrgId() throws Exception {
    List<EshopGroup> groups = eshopGroupRepository.findByOrgId(17);
    assertNotNull(groups);
  }

  @Test
  public void findByOrgIds_shouldReturnEshopGroups_giveOrgIds() throws Exception {
    List<EshopGroup> groups = eshopGroupRepository.findByOrgIds(Arrays.asList(17, 18)).get();
    assertNotNull(groups);
  }
}
