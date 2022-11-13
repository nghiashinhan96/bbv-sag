package com.sagag.eshop.service.api.impl;

import com.sagag.eshop.repo.api.EshopPermissionRepository;
import com.sagag.eshop.repo.api.GroupPermissionRepository;
import com.sagag.eshop.repo.entity.EshopGroup;
import com.sagag.eshop.repo.entity.EshopPermission;
import com.sagag.eshop.repo.entity.GroupPermission;
import com.sagag.services.common.annotation.EshopMockitoJUnitRunner;
import com.sagag.services.common.enums.PermissionEnum;

import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Optional;

@EshopMockitoJUnitRunner
public class PermissionServiceImplTest {

  @InjectMocks
  private PermissionServiceImpl permissionService;

  @Mock
  private EshopPermissionRepository permissionRepo;

  @Mock
  private GroupPermissionRepository groupPermissionRepo;

  @Before
  public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void setPermissions_shouldEnablePermission_givenGroupsAndPermissions() throws Exception {
    Mockito.when(permissionRepo.findPermissionIdByName(anyString())).thenReturn(Optional.of(1));

    EshopGroup adminGroup = EshopGroup.builder().name("ADMIN_G").id(1).build();
    EshopPermission batteryPerm = EshopPermission.builder().permission("BATTERY").build();
    GroupPermission groupPerm =
        GroupPermission.builder().eshopGroup(adminGroup).eshopPermission(batteryPerm).build();

    Mockito.when(groupPermissionRepo.findByGroupIdAndPermissionId(anyInt(), anyInt()))
        .thenReturn(Optional.of(groupPerm));
    Mockito.when(groupPermissionRepo.save(any())).thenReturn(groupPerm);
    Mockito.when(permissionRepo.findById(anyInt())).thenReturn(Optional.of(batteryPerm));
    this.permissionService.setPermissions(Arrays.asList(adminGroup),
        Arrays.asList(PermissionEnum.BATTERY), true);

    assertTrue(groupPerm.isAllowed());
  }

  @Test(expected = IllegalArgumentException.class)
  public void setPermissions_shouldEnablePermissionFail_givenGroupsAndNotFoundPermissions()
      throws Exception {
    Mockito.when(permissionRepo.findPermissionIdByName(anyString())).thenReturn(Optional.of(1));

    EshopGroup adminGroup = EshopGroup.builder().name("ADMIN_G").id(1).build();
    EshopPermission batteryPerm = EshopPermission.builder().permission("BATTERY").build();
    GroupPermission groupPerm =
        GroupPermission.builder().eshopGroup(adminGroup).eshopPermission(batteryPerm).build();

    Mockito.when(groupPermissionRepo.findByGroupIdAndPermissionId(anyInt(), anyInt()))
        .thenReturn(Optional.of(groupPerm));
    Mockito.when(groupPermissionRepo.save(any())).thenReturn(groupPerm);
    Mockito.when(permissionRepo.findById(anyInt())).thenReturn(Optional.empty());
    this.permissionService.setPermissions(Arrays.asList(adminGroup),
        Arrays.asList(PermissionEnum.BATTERY), true);

  }
}
