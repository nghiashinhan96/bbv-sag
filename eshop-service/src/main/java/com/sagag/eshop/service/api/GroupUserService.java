package com.sagag.eshop.service.api;

import com.sagag.eshop.repo.entity.EshopGroup;
import com.sagag.eshop.repo.entity.EshopUser;
import com.sagag.eshop.repo.entity.GroupUser;


public interface GroupUserService {

  /**
   * Find one by eshop user
   *
   * @param eshopUser
   * @return
   */
  GroupUser findOneByEshopUser(final EshopUser eshopUser);

  /**
   * Save GroupUser info
   * @param groupUser
   */
  void saveGroupUser(GroupUser groupUser);

  /**
   * Inserts groupUser.
   * @param eshopUser
   * @param orgCode
   * @param roleId
   * @return GroupUser
   */
  GroupUser createGroupUser(EshopUser eshopUser, String orgCode, int roleId);

  /**
   * Creates groupUser for eshopUser and eshopGroup
   *
   * @param eshopUser the eshopUser to be saved
   * @param eshopGroup the eshopGroup to be saved
   * @return saved {@link GroupUser}
   */
  GroupUser createGroupUser(EshopUser eshopUser, EshopGroup eshopGroup);
}
