package com.sagag.eshop.service.api.impl;

import com.sagag.eshop.repo.api.EshopRoleRepository;
import com.sagag.eshop.repo.entity.EshopRole;
import com.sagag.eshop.service.api.RoleService;
import com.sagag.services.domain.eshop.common.EshopAuthority;
import com.sagag.services.domain.eshop.common.EshopUserCreateAuthority;
import com.sagag.services.domain.eshop.dto.EshopRoleDto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Transactional
public class RoleServiceImpl implements RoleService {

  @Autowired
  private EshopRoleRepository roleRepo;

  @Override
  public List<EshopRole> getAllRoles() {
    return roleRepo.findAll();
  }

  @Override
  public List<EshopRole> findAllRolesInIds(List<Integer> ids) {
    return roleRepo.findByIdIn(ids);
  }

  @Override
  public Optional<EshopRole> findRoleByName(String name) {
    return roleRepo.findOneByName(name);
  }

  @Override
  public List<EshopRoleDto> getAllRoleDto() {
      List<EshopRoleDto> eshopRoleDtos = new ArrayList<>();
      List<String> roleNames = new ArrayList<>();
      roleNames.add(EshopUserCreateAuthority.USER_ADMIN.name());
      roleNames.add(EshopUserCreateAuthority.NORMAL_USER.name());
      List<EshopRole> eshopRoles = roleRepo.findEshopRolesByName(roleNames);
    eshopRoles.forEach(eshopRole -> {
      EshopRoleDto eshopRoleDto =
          EshopRoleDto.builder().id(eshopRole.getId()).name(eshopRole.getName())
              .description(eshopRole.getDescription()).build();
      eshopRoleDtos.add(eshopRoleDto);
    });
    return eshopRoleDtos;
  }

  @Override
  public List<EshopRoleDto> getEshopRolesForUserProfile(String userRole, boolean isOtherProfile) {

    // Get Eshop roles based on user role
    final List<String> roleNames = new ArrayList<>();
    if (isOtherProfile || EshopAuthority.isCustomerUserAdmin(userRole)) {
      Stream.of(EshopUserCreateAuthority.frontEndValues(userRole))
          .map(EshopUserCreateAuthority::name)
          .forEach(roleNames::add);
    } else if (EshopAuthority.isSaleAssistant(userRole)) {
      roleNames.add(EshopAuthority.SALES_ASSISTANT.name());
    } else {
      roleNames.add(EshopUserCreateAuthority.normalUser(userRole).name());
    }

    return roleRepo.findEshopRolesByName(roleNames).stream()
        .map(eshopRoleDtoMapper()).collect(Collectors.toList());
  }

  @Override
  public List<EshopRoleDto> getDefaultEshopRoles() {
    return roleRepo.findEshopRolesByName(EshopUserCreateAuthority.normalNames()).stream()
        .map(eshopRoleDtoMapper())
        .collect(Collectors.toList());
  }

  private static Function<EshopRole, EshopRoleDto> eshopRoleDtoMapper() {
    return eshopRole -> EshopRoleDto.builder()
        .id(eshopRole.getId())
        .name(eshopRole.getName())
        .description(eshopRole.getDescription())
        .build();
  }

}
