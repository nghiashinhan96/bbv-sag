package com.sagag.eshop.service.api.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sagag.eshop.repo.api.EshopRoleRepository;
import com.sagag.eshop.repo.entity.EshopRole;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

/**
 * IT to verify for {@link RoleServiceImpl}.
 */
@RunWith(MockitoJUnitRunner.class)
public class RoleServiceImplTest {

  @Before
  public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);
  }

  @Mock
  private EshopRoleRepository roleRepository;

  @InjectMocks
  private RoleServiceImpl roleService;

  @Test
  public void findRoleByName() {

    // Expected objects
    int id = 1;
    EshopRole expectedEshopRole = new EshopRole();
    expectedEshopRole.setId(id);
    expectedEshopRole.setName("SALES_ASSISTANT");
    expectedEshopRole.setDescription("Sales assistant role");
    // mock role repo
    when(roleRepository.findOneByName(expectedEshopRole.getName()))
        .thenReturn(Optional.of(expectedEshopRole));

    EshopRole role = roleService.findRoleByName(expectedEshopRole.getName()).get();

    assertNotNull(role);
    assertEquals(expectedEshopRole.getId(), role.getId());
    assertEquals(expectedEshopRole.getName(), role.getName());
    assertEquals(expectedEshopRole.getDescription(), role.getDescription());

    // make sure method is invoked
    verify(roleRepository).findOneByName(expectedEshopRole.getName());
  }
}
