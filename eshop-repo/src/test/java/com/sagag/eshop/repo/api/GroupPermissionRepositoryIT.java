package com.sagag.eshop.repo.api;

import static org.junit.Assert.assertNotNull;

import com.sagag.eshop.repo.app.RepoApplication;
import com.sagag.eshop.repo.entity.GroupPermission;
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

import java.util.Arrays;
import java.util.List;

/**
 * Integration test class for {@link GroupPermissionRepository}.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = { RepoApplication.class })
@EshopIntegrationTest
@Transactional
public class GroupPermissionRepositoryIT {

  @Autowired
  private GroupPermissionRepository groupPermRepo;

  @Test
  public void givenUser_ShouldGetAllUserCustomizedPermissions() {
    List<GroupPermission> customizedPerms =
        groupPermRepo.findAllUserCustomizedPermissions(RepoDataTests.USER_AX_AD_USER_ID);
    Assert.assertThat(customizedPerms, Matchers.not(Matchers.empty()));
  }

  @Test
  public void findByGroupIdsAndPermissionId_shouldReturnGroupPerms_givenGroupIdsAndPermId()
      throws Exception {
    List<GroupPermission> groupPerms =
        groupPermRepo.findByGroupIdsAndPermissionId(Arrays.asList(27), 22);
    assertNotNull(groupPerms);
  }
}
