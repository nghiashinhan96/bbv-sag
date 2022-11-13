package com.sagag.eshop.service.api.impl;

import com.sagag.eshop.repo.api.EshopGroupRepository;
import com.sagag.eshop.repo.api.GroupUserRepository;
import com.sagag.eshop.repo.entity.EshopGroup;
import com.sagag.eshop.repo.entity.EshopUser;
import com.sagag.eshop.repo.entity.GroupUser;
import com.sagag.eshop.service.api.GroupUserService;

import org.apache.http.util.Asserts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import javax.validation.ValidationException;

@Service
@Transactional
public class GroupUserServiceImpl implements GroupUserService {

  @Autowired
  private GroupUserRepository groupUserRepository;

  @Autowired
  private EshopGroupRepository eshopGroupRepository;

  @Override
  public GroupUser findOneByEshopUser(final EshopUser eshopUser) {
    return groupUserRepository.findOneByEshopUser(eshopUser).orElse(null);
  }

  @Override
  public void saveGroupUser(GroupUser groupUser) {
    groupUserRepository.save(groupUser);
  }

  @Override
  public GroupUser createGroupUser(EshopUser eshopUser, String orgCode, int roleId) {
    Optional<EshopGroup> eshopGroup =
        eshopGroupRepository.findOneByOrgCodeAndRoleId(orgCode, roleId);
    if (!eshopGroup.isPresent()) {
      throw new ValidationException("Can not find eshop group information");
    }
    // Insert group user
    GroupUser groupUser = new GroupUser();
    groupUser.setEshopGroup(eshopGroup.get());
    groupUser.setEshopUser(eshopUser);
    return groupUserRepository.save(groupUser);
  }

  @Override
  public GroupUser createGroupUser(final EshopUser eshopUser, final EshopGroup eshopGroup) {
    Asserts.notNull(eshopUser, "eshopUser should not be null");
    Asserts.notNull(eshopGroup, "eshopGroup should not be null");
    GroupUser groupUser = new GroupUser();
    groupUser.setEshopGroup(eshopGroup);
    groupUser.setEshopUser(eshopUser);
    return groupUserRepository.save(groupUser);
  }

}
