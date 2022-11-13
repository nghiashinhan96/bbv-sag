package com.sagag.eshop.service.api.impl;

import com.sagag.eshop.service.api.PermissionService;
import com.sagag.eshop.service.app.ServiceApplication;
import com.sagag.eshop.service.dto.PermissionDto;
import com.sagag.services.common.annotation.EshopIntegrationTest;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;


/**
 * Integration test class for Permission service.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { ServiceApplication.class })
@EshopIntegrationTest
public class PermissionServiceImplIT {

  @Autowired
  private PermissionService permService;

  @Test
  public void givenUser_ShouldGetAllActualPermissions() {
    final List<PermissionDto> allPermissions = permService.getUserPermissions(26L);
    Assert.assertThat(allPermissions, Matchers.not(Matchers.empty()));
  }

}
