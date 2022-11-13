package com.sagag.eshop.repo.api;

import com.google.common.collect.Sets;
import com.sagag.eshop.repo.app.RepoApplication;
import com.sagag.eshop.repo.entity.EshopFunction;
import com.sagag.eshop.repo.entity.EshopPermission;
import com.sagag.eshop.repo.entity.PermFunction;
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
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Integration test class for {@link EshopPermissionRepository}.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = { RepoApplication.class })
@EshopIntegrationTest
@Transactional
public class EshopPermissionRepositoryIT {

  @Autowired
  private EshopPermissionRepository repository;

  @Test
  public void givenUserShouldGetAllUserDefaultPermissions() {
    List<Integer> permissions = repository.findAllUserDefaultPermissions(1L);
    Assert.assertThat(permissions.isEmpty(), Matchers.is(true));
  }

  @Test
  public void givenPermissionIdsShouldGetAllPermissions() {
    final Set<Integer> permissionIds = Sets.newHashSet(1, 2);
    List<EshopPermission> permissions = repository.findAllPermissionsIn(permissionIds);
    Assert.assertThat(permissions.isEmpty(), Matchers.is(true));

    List<EshopFunction> functions = permissions.stream().flatMap(p -> p.getPermFunctions().stream())
        .map(PermFunction::getEshopFunction).collect(Collectors.toList());
    Assert.assertThat(functions.isEmpty(), Matchers.is(true));
  }
}
