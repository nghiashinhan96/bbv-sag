package com.sagag.eshop.service.tests.utils;

import static org.junit.Assert.assertThat;

import com.sagag.eshop.repo.entity.EshopFunction;
import com.sagag.eshop.repo.entity.EshopGroup;
import com.sagag.eshop.repo.entity.EshopPermission;
import com.sagag.eshop.repo.entity.EshopRole;
import com.sagag.eshop.repo.entity.EshopUser;
import com.sagag.eshop.repo.entity.GroupRole;
import com.sagag.eshop.repo.entity.GroupUser;
import com.sagag.eshop.repo.entity.Language;
import com.sagag.eshop.repo.entity.Login;
import com.sagag.eshop.repo.entity.OrganisationGroup;
import com.sagag.eshop.repo.entity.PasswordHash;
import com.sagag.eshop.repo.entity.PermFunction;
import com.sagag.eshop.repo.entity.RolePermission;
import com.sagag.eshop.repo.entity.RoleType;
import com.sagag.eshop.repo.entity.Salutation;
import com.sagag.eshop.repo.entity.UserSettings;
import com.sagag.services.common.enums.HashType;
import com.sagag.services.common.enums.RoleTypeEnum;
import com.sagag.services.domain.eshop.common.EshopAuthority;

import lombok.experimental.UtilityClass;

import org.hamcrest.Matchers;

import java.util.List;

@UtilityClass
public class UserAssertions {

  public static void assertFoundUser(EshopUser foundUser, final EshopAuthority authority) {
    assertThat(foundUser, Matchers.notNullValue());
    assertThat(foundUser.getId(), Matchers.is(TestsConstants.USER_ID_LONG));
    assertThat(foundUser.getUsername(), Matchers.is(TestsConstants.USER_NAME));
    assertThat(foundUser.getEmail(), Matchers.is(TestsConstants.EMAIL));
    assertThat(foundUser.getSetting(), Matchers.is(TestsConstants.USER_SETTING_ID));
    assertThat(foundUser.getHourlyRate(), Matchers.is(TestsConstants.HOUR_RATE));
    assertThat(foundUser.getFirstName(), Matchers.is(TestsConstants.FIRST_NAME));
    assertThat(foundUser.getLastName(), Matchers.is(TestsConstants.LAST_NAME));
    assertThat(foundUser.getType(), Matchers.is(TestsConstants.USER_TYPE));
    assertThat(foundUser.getPhone(), Matchers.is(TestsConstants.PHONE_NR));
    assertThat(foundUser.isEmailConfirmation(), Matchers.is(true));
    assertThat(foundUser.isNewsletter(), Matchers.is(false));

    assertLogin(foundUser.getLogin());
    assertSalutation(foundUser.getSalutation());
    assertLanguage(foundUser.getLanguage());
    assertUserSetting(foundUser.getUserSetting());

    final List<GroupUser> groupUsers = foundUser.getGroupUsers();
    assertThat(groupUsers, Matchers.hasSize(1));
    assertGroupUser(groupUsers.get(0), authority);

  }

  private static void assertLogin(Login login) {
    assertThat(login.getId(), Matchers.is(TestsConstants.LOGIN_ID));
    assertThat(login.getUserId(), Matchers.is(TestsConstants.USER_ID_LONG));
    CommonAssertions.assertDate(login.getFirstLoginDate());
    CommonAssertions.assertDate(login.getLastOnBehalfOfDate());
    assertPasswordHash(login.getPasswordHash());
    assertThat(login.getEshopUser().getId(), Matchers.is(TestsConstants.USER_ID_LONG));
  }

  private static void assertPasswordHash(PasswordHash passwordHash) {
    assertThat(passwordHash.getPassword(), Matchers.is(TestsConstants.PWD_RAW));
    assertThat(passwordHash.getHashType(), Matchers.is(HashType.BLCK_VAR));
  }

  private static void assertUserSetting(UserSettings settings) {
    assertThat(settings.getId(), Matchers.is(TestsConstants.USER_SETTING_ID));
    assertThat(settings.isAcceptHappyPointTerm(), Matchers.is(true));
    assertThat(settings.getAddressId(), Matchers.is(TestsConstants.ADDRESS_ID));
    assertThat(settings.getAllocationId(), Matchers.is(TestsConstants.ALLOCATION_ID));
    assertThat(settings.getBillingAddressId(),
        Matchers.is(String.valueOf(TestsConstants.BILLING_ADDR_ID)));
    assertThat(settings.getClassicCategoryView(), Matchers.is(true));
    assertThat(settings.getSingleSelectMode(), Matchers.is(true));
    assertThat(settings.getCollectiveDelivery(), Matchers.is(TestsConstants.COLLECTIVE_DELIVERY));
    assertThat(settings.isCurrentStateNetPriceView(), Matchers.is(true));
    assertThat(settings.getDeliveryAddressId(), Matchers.is(TestsConstants.DELIVERY_ADDR_ID));
    assertThat(settings.getDeliveryId(), Matchers.is(TestsConstants.DELIVERY_ID));
    assertThat(settings.isEmailNotificationOrder(), Matchers.is(true));
    CommonAssertions.assertInvoiceType(settings.getInvoiceType());
    assertThat(settings.isNetPriceConfirm(), Matchers.is(true));
    assertThat(settings.isNetPriceView(), Matchers.is(true));
    CommonAssertions.assertPaymentMethod(settings.getPaymentMethod());
    assertThat(settings.isSaleOnBehalfOf(), Matchers.is(false));
    assertThat(settings.isShowDiscount(), Matchers.is(false));
    assertThat(settings.isShowHappyPoints(), Matchers.is(false));
    assertThat(settings.isShowRecommendedRetailPrice(), Matchers.is(true));
    assertThat(settings.isViewBilling(), Matchers.is(true));

  }

  private static void assertGroupUser(GroupUser groupUser, final EshopAuthority authority) {
    assertThat(groupUser.getId(), Matchers.is(TestsConstants.GROUPUSER_ID));
    assertThat(groupUser.getEshopUser().getId(), Matchers.is(TestsConstants.USER_ID_LONG));
    assertGroup(groupUser.getEshopGroup(), authority);
  }

  private static void assertGroup(EshopGroup eshopGroup, EshopAuthority authority) {
    assertThat(eshopGroup.getId(), Matchers.is(TestsConstants.GROUP_ID));
    final List<GroupRole> groupRoles = eshopGroup.getGroupRoles();
    assertThat(groupRoles, Matchers.hasSize(1));
    final GroupRole groupRole = groupRoles.get(0);
    assertThat(groupRole.getEshopGroup().getId(), Matchers.is(TestsConstants.GROUP_ID));
    final EshopRole role = groupRole.getEshopRole();
    assertThat(role.getId(), Matchers.is(TestsConstants.ROLE_ID));
    if (authority == EshopAuthority.GROUP_ADMIN) {
      assertThat(eshopGroup.getDescription(), Matchers.is(TestsConstants.GROUP_ADMIN_DESC));
      assertThat(eshopGroup.getName(), Matchers.is(TestsConstants.GROUP_ADMIN_NAME));
    } else if (authority == EshopAuthority.MARKETING_ASSISTANT) {
      assertThat(eshopGroup.getDescription(), Matchers.is(TestsConstants.MARKETING_GROUP_DESC));
      assertThat(eshopGroup.getName(), Matchers.is(TestsConstants.MARKETING_GROUP_NAME));
    } else if (authority == EshopAuthority.NORMAL_USER) {
      assertThat(eshopGroup.getDescription(), Matchers.is(TestsConstants.NORMALUSER_GROUP_DESC));
      assertThat(eshopGroup.getName(), Matchers.is(TestsConstants.NORMALUSER_GROUP_NAME));
    } else if (authority == EshopAuthority.SALES_ASSISTANT) {
      assertThat(eshopGroup.getDescription(), Matchers.is(TestsConstants.ASSISTANT_GROUP_DESC));
      assertThat(eshopGroup.getName(), Matchers.is(TestsConstants.ASSISTANT_GROUP_NAME));
    } else if (authority == EshopAuthority.SYSTEM_ADMIN) {
      assertThat(eshopGroup.getDescription(), Matchers.is(TestsConstants.SYSTEMADMIN_GROUP_DESC));
      assertThat(eshopGroup.getName(), Matchers.is(TestsConstants.SYSTEMADMIN_GROUP_NAME));
    } else if (authority == EshopAuthority.USER_ADMIN) {
      assertThat(eshopGroup.getDescription(), Matchers.is(TestsConstants.USERADMIN_GROUP_DESC));
      assertThat(eshopGroup.getName(), Matchers.is(TestsConstants.USERADMIN_GROUP_NAME));
    }
    assertRole(role, authority);
    final List<RolePermission> rolePerms = role.getRolePermissions();
    assertThat(rolePerms, Matchers.hasSize(1));
    final RolePermission rolePerm = rolePerms.get(0);
    assertRolePermission(rolePerm);
    final List<OrganisationGroup> orgGroups = eshopGroup.getOrganisationGroups();
    assertThat(orgGroups, Matchers.hasSize(1));
    final OrganisationGroup orgGroup = orgGroups.get(0);
    assertThat(orgGroup.getEshopGroup().getId(), Matchers.is(TestsConstants.GROUP_ID));
    OrganisationAssertions.assertFoundOrganisation(orgGroup.getOrganisation());
  }

  private static void assertRole(final EshopRole role, EshopAuthority authority) {
    if (authority == EshopAuthority.GROUP_ADMIN) {
      assertThat(role.getName(), Matchers.is(EshopAuthority.GROUP_ADMIN.name()));
      assertThat(role.getDescription(), Matchers.is(TestsConstants.GROUP_ADMIN_ROLE_DESC));
      assertRoleType(role.getRoleType(), RoleTypeEnum.ADMIN);
    } else if (authority == EshopAuthority.MARKETING_ASSISTANT) {
      assertThat(role.getName(), Matchers.is(EshopAuthority.MARKETING_ASSISTANT.name()));
      assertThat(role.getDescription(), Matchers.is(TestsConstants.MARKETING_ASSISTANT_DESC));
      assertRoleType(role.getRoleType(), RoleTypeEnum.ASSISTANT);
    } else if (authority == EshopAuthority.NORMAL_USER) {
      assertThat(role.getName(), Matchers.is(EshopAuthority.NORMAL_USER.name()));
      assertThat(role.getDescription(), Matchers.is(TestsConstants.NORMAL_USER_DESC));
      assertRoleType(role.getRoleType(), RoleTypeEnum.NORMAL_USER);
    } else if (authority == EshopAuthority.SALES_ASSISTANT) {
      assertThat(role.getName(), Matchers.is(EshopAuthority.SALES_ASSISTANT.name()));
      assertThat(role.getDescription(), Matchers.is(TestsConstants.SALES_ASSISTANT_DESC));
      assertRoleType(role.getRoleType(), RoleTypeEnum.ASSISTANT);
    } else if (authority == EshopAuthority.SYSTEM_ADMIN) {
      assertThat(role.getName(), Matchers.is(EshopAuthority.SYSTEM_ADMIN.name()));
      assertThat(role.getDescription(), Matchers.is(TestsConstants.SYSTEM_ADMIN_DESC));
      assertRoleType(role.getRoleType(), RoleTypeEnum.ADMIN);
    } else if (authority == EshopAuthority.USER_ADMIN) {
      assertThat(role.getName(), Matchers.is(EshopAuthority.USER_ADMIN.name()));
      assertThat(role.getDescription(), Matchers.is(TestsConstants.USER_ADMIN_DESC));
      assertRoleType(role.getRoleType(), RoleTypeEnum.ADMIN);
    }
  }

  private static void assertRolePermission(RolePermission rolePerm) {
    assertThat(rolePerm.getEshopRole().getId(), Matchers.is(TestsConstants.ROLE_ID));
    final EshopPermission perm = rolePerm.getEshopPermission();
    assertThat(perm.getId(), Matchers.is(TestsConstants.PERM_ID));
    assertThat(perm.getCreatedBy(), Matchers.is(TestsConstants.USER_ID_LONG));
    assertThat(perm.getDescription(), Matchers.is(TestsConstants.PERM_DESC));
    assertThat(perm.getModifiedBy(), Matchers.is(TestsConstants.USER_ID_LONG));
    assertThat(perm.getPermissisonKey(), Matchers.is(TestsConstants.PERM_KEY_VIEW_PROFILE));
    assertThat(perm.getPermission(), Matchers.is(TestsConstants.PERM_NAME));

    final List<PermFunction> permFuncs = perm.getPermFunctions();
    assertThat(permFuncs, Matchers.hasSize(1));
    final PermFunction permFunc = permFuncs.get(0);
    assertThat(permFunc.getId(), Matchers.is(TestsConstants.PERM_FUNC_ID));
    assertThat(permFunc.getEshopPermission().getId(), Matchers.is(TestsConstants.PERM_ID));
    assertFunction(permFunc.getEshopFunction());
  }

  private static void assertFunction(EshopFunction func) {
    assertThat(func.getId(), Matchers.is(TestsConstants.FUNC_ID));
    assertThat(func.getFunctionName(), Matchers.is(TestsConstants.PERM_NAME));
    assertThat(func.getRelativeUrl(), Matchers.is(TestsConstants.FUNC_URL));
    assertThat(func.getDescription(), Matchers.is(TestsConstants.FUNC_DESC));
  }

  private static void assertRoleType(RoleType roleType, RoleTypeEnum roleTypeEnum) {
    if (roleTypeEnum == RoleTypeEnum.ADMIN) {
      assertThat(roleType.getName(), Matchers.is(RoleTypeEnum.ADMIN.name()));
      assertThat(roleType.getDescription(), Matchers.is(TestsConstants.ROLETYPE_ADMIN_DESC));
    } else if (roleTypeEnum == RoleTypeEnum.ASSISTANT) {
      assertThat(roleType.getName(), Matchers.is(RoleTypeEnum.ASSISTANT.name()));
      assertThat(roleType.getDescription(), Matchers.is(TestsConstants.ROLETYPE_ASSISTANT_DESC));
    } else if (roleTypeEnum == RoleTypeEnum.NORMAL_USER) {
      assertThat(roleType.getName(), Matchers.is(RoleTypeEnum.NORMAL_USER.name()));
      assertThat(roleType.getDescription(), Matchers.is(TestsConstants.ROLETYPE_NORMAL_USER_DESC));
    }
  }

  private static void assertLanguage(Language language) {
    assertThat(language.getId(), Matchers.is(TestsConstants.LANG_ID));
    assertThat(language.getLangiso(), Matchers.is(TestsConstants.LANG_DE));
    assertThat(language.getLangcode(), Matchers.is(TestsConstants.LANG_CODE_DE));
    assertThat(language.getDescription(), Matchers.is(TestsConstants.LANG_DESC));
  }

  private static void assertSalutation(final Salutation salutation) {
    assertThat(salutation.getId(), Matchers.is(TestsConstants.SALUT_ID));
    assertThat(salutation.getCode(), Matchers.is(TestsConstants.SALUT_CODE));
    assertThat(salutation.getType(), Matchers.is(TestsConstants.SALUT_TYPE));
    assertThat(salutation.getDescription(), Matchers.is(TestsConstants.SALUT_DESC));
  }
}
