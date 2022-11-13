package com.sagag.eshop.service.api.impl;

import com.sagag.eshop.repo.api.EshopGroupRepository;
import com.sagag.eshop.repo.api.OrganisationRepository;
import com.sagag.eshop.repo.api.SalutationRepository;
import com.sagag.eshop.repo.entity.EshopGroup;
import com.sagag.eshop.repo.entity.EshopUser;
import com.sagag.eshop.repo.entity.GroupUser;
import com.sagag.eshop.repo.entity.Language;
import com.sagag.eshop.repo.entity.Login;
import com.sagag.eshop.repo.entity.Salutation;
import com.sagag.eshop.repo.entity.UserSettings;
import com.sagag.eshop.service.api.AutonetUserService;
import com.sagag.eshop.service.api.GroupUserService;
import com.sagag.eshop.service.helper.UserSettingHelper;
import com.sagag.services.common.enums.SalutationEnum;
import com.sagag.services.common.enums.UserType;
import com.sagag.services.domain.eshop.dto.UserProfileDto;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
public class AutonetUserServiceImpl extends AbstractUserCreation implements AutonetUserService {

  @Autowired
  private EshopGroupRepository eshopGroupRepository;

  @Autowired
  private OrganisationRepository organisationRepo;

  @Autowired
  private SalutationRepository salutationRepo;

  @Autowired
  private GroupUserService groupUserService;

  @Override
  @Transactional
  public EshopUser createAutonetUser(String username, String langIso,
      String affiliate) {
    final Language lang = languageService.findLanguageByLangIsoOrTecDoc(langIso);

    final UserSettings settings = userSettingsRepo.save(UserSettingHelper
        .buildDefaultUserSettingFromCustomerSetting(customerSettingHelper.buildDefaultCustomerSetting()));

    final Salutation defaultSalutation =
        salutationRepo.findOneByCode(SalutationEnum.SALUTATION_MR.toString())
            .orElseThrow(() -> new NoSuchElementException(
                String.format("No Data found for %s", SalutationEnum.SALUTATION_MR.toString())));

    UserProfileDto userProfileDto = UserProfileDto.builder()
        .userName(username)
        .languageId(lang.getId())
        .salutationId(defaultSalutation.getId())
        .userType(UserType.AUTONET.toString())
        .build();
    EshopUser autonetEshopUser = createDefaultUser(userProfileDto, settings.getId());

    final Login login = createLogin(autonetEshopUser);

    GroupUser groupUser = groupUserService.createGroupUser(autonetEshopUser, findAutonetEshopGroup(affiliate));
    autonetEshopUser.setLogin(login);
    autonetEshopUser.setGroupUsers(Arrays.asList(groupUser));
    return autonetEshopUser;
  }

  private EshopGroup findAutonetEshopGroup(String affiliate) {
    int firstOrgIdInAffiliate = findFirstOrgIdInAffiliate(affiliate);
    List<EshopGroup> eshopGroups = eshopGroupRepository.findByOrgId(firstOrgIdInAffiliate);
    if (CollectionUtils.isEmpty(eshopGroups) || eshopGroups.size() > 1) {
      throw new IllegalArgumentException(
          String.format("The autonet customer %s should have 1 group only", affiliate));
    }
    return eshopGroups.get(0);
  }

  private int findFirstOrgIdInAffiliate(String affiliate) {
    List<Integer> orgIds = organisationRepo.findOrgIdInAffiliate(affiliate);
    if (CollectionUtils.isEmpty(orgIds) || orgIds.size() > 1) {
      throw new IllegalArgumentException(
          String.format("The autonet affiliate %s should have 1 customer only", affiliate));
    }
    return orgIds.get(0);
  }
}
