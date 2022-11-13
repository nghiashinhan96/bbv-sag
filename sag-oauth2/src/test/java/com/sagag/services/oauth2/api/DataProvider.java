package com.sagag.services.oauth2.api;

import com.sagag.eshop.repo.entity.CustomerSettings;
import com.sagag.eshop.repo.entity.EshopGroup;
import com.sagag.eshop.repo.entity.EshopRole;
import com.sagag.eshop.repo.entity.EshopUser;
import com.sagag.eshop.repo.entity.GroupRole;
import com.sagag.eshop.repo.entity.GroupUser;
import com.sagag.eshop.repo.entity.Login;
import com.sagag.eshop.repo.entity.Organisation;
import com.sagag.eshop.repo.entity.OrganisationGroup;
import com.sagag.eshop.repo.entity.OrganisationType;
import com.sagag.eshop.repo.entity.PaymentMethod;
import com.sagag.eshop.repo.entity.UserSettings;
import com.sagag.services.common.enums.PaymentMethodType;
import com.sagag.services.domain.sag.external.Customer;
import com.sagag.services.oauth2.settings.UpdatedCustomerSettingsDto;

import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@UtilityClass
public class DataProvider {

  public EshopUser buildActiveEshopUser(long userId, String username, String role) {
    Login login = new Login();
    login.setId(1);
    login.setUserActive(true);
    EshopRole eshopRole = new EshopRole();
    eshopRole.setName(role);

    GroupRole groupRole = new GroupRole();
    groupRole.setEshopRole(eshopRole);

    final EshopUser eshopUser = new EshopUser();
    eshopUser.setId(userId);

    eshopUser.setUsername(username);


    final OrganisationGroup orgGroup = new OrganisationGroup();
    orgGroup.setOrganisation(builOrganization());

    final EshopGroup eshopGroup = new EshopGroup();
    eshopGroup.setOrganisationGroups(Arrays.asList(orgGroup));
    eshopGroup.setName(role);
    groupRole.setEshopGroup(eshopGroup);
    groupRole.setEshopRole(eshopRole);
    List<GroupRole> groupRoles = new ArrayList<>();
    groupRoles.add(groupRole);
    final GroupUser groupUser = new GroupUser();
    groupUser.setEshopGroup(eshopGroup);

    eshopUser.setGroupUsers(Arrays.asList(groupUser));

    UserSettings userSettings = new UserSettings();
    userSettings.setId(1);

    eshopUser.setUserSetting(userSettings);

    eshopGroup.setGroupRoles(groupRoles);
    eshopUser.setLogin(login);
    return eshopUser;
  }

  public Organisation builOrganization() {
    Organisation organisation = new Organisation();
    organisation.setParentId(1);
    organisation.setShortname("derendinger-at");
    organisation.setOrgCode("1100005");
    organisation.setName("org name");
    organisation.setOrganisationType(OrganisationType.builder().level(1).build());
    final CustomerSettings cusSettings = new CustomerSettings();
    PaymentMethod paymentMethod = new PaymentMethod();
    paymentMethod.setDescCode(PaymentMethodType.CASH.name());
    cusSettings.setPaymentMethod(paymentMethod);

    organisation.setCustomerSettings(cusSettings);
    return organisation;
  }

  public UpdatedCustomerSettingsDto buildCustomerSettings() {
    final UpdatedCustomerSettingsDto dto = new UpdatedCustomerSettingsDto();

    final CustomerSettings cusSettings = new CustomerSettings();
    PaymentMethod paymentMethod = new PaymentMethod();
    paymentMethod.setDescCode(PaymentMethodType.CASH.name());
    cusSettings.setPaymentMethod(paymentMethod);
    dto.setCustomerSettings(cusSettings);

    Customer customer = new Customer();
    customer.setActive(true);
    dto.setErpCustomer(customer);
    return dto;
  }
}
