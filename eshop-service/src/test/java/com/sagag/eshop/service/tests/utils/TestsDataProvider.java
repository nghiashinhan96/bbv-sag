package com.sagag.eshop.service.tests.utils;

import com.sagag.eshop.repo.entity.AddressType;
import com.sagag.eshop.repo.entity.CouponUseLog;
import com.sagag.eshop.repo.entity.CustomerSettings;
import com.sagag.eshop.repo.entity.EshopAddress;
import com.sagag.eshop.repo.entity.EshopFunction;
import com.sagag.eshop.repo.entity.EshopGroup;
import com.sagag.eshop.repo.entity.EshopPermission;
import com.sagag.eshop.repo.entity.EshopRole;
import com.sagag.eshop.repo.entity.EshopUser;
import com.sagag.eshop.repo.entity.GroupRole;
import com.sagag.eshop.repo.entity.GroupUser;
import com.sagag.eshop.repo.entity.InvoiceType;
import com.sagag.eshop.repo.entity.Language;
import com.sagag.eshop.repo.entity.Login;
import com.sagag.eshop.repo.entity.Organisation;
import com.sagag.eshop.repo.entity.OrganisationAddress;
import com.sagag.eshop.repo.entity.OrganisationGroup;
import com.sagag.eshop.repo.entity.OrganisationType;
import com.sagag.eshop.repo.entity.PasswordHash;
import com.sagag.eshop.repo.entity.PaymentMethod;
import com.sagag.eshop.repo.entity.PermFunction;
import com.sagag.eshop.repo.entity.RolePermission;
import com.sagag.eshop.repo.entity.RoleType;
import com.sagag.eshop.repo.entity.Salutation;
import com.sagag.eshop.repo.entity.UserSettings;
import com.sagag.services.common.enums.HashType;
import com.sagag.services.common.enums.RoleTypeEnum;
import com.sagag.services.domain.eshop.common.EshopAuthority;
import com.sagag.services.domain.sag.external.ContactInfo;
import com.sagag.services.domain.sag.external.Customer;
import com.sagag.services.domain.sag.external.CustomerBranch;

import lombok.experimental.UtilityClass;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

@UtilityClass
public class TestsDataProvider {

  public static final String USERNAME_USER_ADMIN = "user.ax.ad_#";

  public static final String EMAIL_USER_ADMIN = "final@gmail.com";

  public static final String FINAL_USER_NAME_WSS_USER_ADMIN = "user.test.final";

  public static final String FINAL_WSS_AFF = "eh-ch";

  public enum ContactType {
    PHONE, EMAIL, FAX;
  }

  /**
   * Builds the eshop user with role authority.
   *
   * @return {@link EshopUser}
   */
  // @formatter:off
  public static EshopUser buildUser(EshopAuthority authority) {
    final EshopUser user = buildUser();
    final EshopGroup group = buildGroup(authority);
    final GroupUser groupUser = GroupUser.builder()
        .id(TestsConstants.GROUPUSER_ID).eshopGroup(group).eshopUser(user).build();
    user.setGroupUsers(Arrays.asList(groupUser));
    user.setUserSetting(buildUserSettings());
    user.setLogin(buildLogin(user));
    return user;
  }

  /**
   * Builds the user organisation.
   *
   * @return the user {@link Organisation}.
   */
  public static Organisation buildOrganisation() {
    final Organisation org = new Organisation(
        TestsConstants.ORG_ID, TestsConstants.ORG_NAME,
        TestsConstants.ORG_CODE, TestsConstants.DDAT_ORG_ID, TestsConstants.ORG_SHORTNAME);
    org.setDescription(TestsConstants.ORG_DESC);
    org.setCouponUseLog(Arrays.asList(buildCouponUseLog()));
    org.setCustomerSettings(buildCustomerSettings(org));
    org.setOrganisationAddresses(Arrays.asList(buildOrganisationAddress(org)));
    org.setOrganisationType(buildOrganisationType());
    return org;
  }

  private static EshopUser buildUser() {
    return EshopUser.builder()
        .id(TestsConstants.USER_ID_LONG)
        .username(TestsConstants.USER_NAME)
        .email(TestsConstants.EMAIL)
        .lastName(TestsConstants.LAST_NAME)
        .firstName(TestsConstants.FIRST_NAME)
        .hourlyRate(TestsConstants.HOUR_RATE)
        .type(TestsConstants.USER_TYPE)
        .newsletter(false)
        .phone(TestsConstants.PHONE_NR)
        .salutation(buildSalutation())
        .setting(TestsConstants.USER_SETTING_ID)
        .emailConfirmation(true)
        .language(buildLanguage()).build();
  }

  private static UserSettings buildUserSettings() {
    final UserSettings userSettings = new UserSettings();
    userSettings.setId(TestsConstants.USER_SETTING_ID);
    userSettings.setAcceptHappyPointTerm(true);
    userSettings.setAddressId(TestsConstants.ADDRESS_ID);
    userSettings.setAllocationId(TestsConstants.ALLOCATION_ID);
    userSettings.setBillingAddressId(String.valueOf(TestsConstants.BILLING_ADDR_ID));
    userSettings.setClassicCategoryView(true);
    userSettings.setSingleSelectMode(true);
    userSettings.setCollectiveDelivery(TestsConstants.COLLECTIVE_DELIVERY);
    userSettings.setCurrentStateNetPriceView(true);
    userSettings.setDeliveryAddressId(TestsConstants.DELIVERY_ADDR_ID);
    userSettings.setDeliveryId(TestsConstants.DELIVERY_ID);
    userSettings.setEmailNotificationOrder(true);
    userSettings.setInvoiceType(buildInvoiceType());
    userSettings.setNetPriceConfirm(true);
    userSettings.setNetPriceView(true);
    userSettings.setPaymentMethod(buildPaymentMethod());
    userSettings.setSaleOnBehalfOf(false);
    userSettings.setShowDiscount(false);
    userSettings.setShowHappyPoints(false);
    userSettings.setShowRecommendedRetailPrice(true);
    userSettings.setViewBilling(true);
    return userSettings;
  }

  private static Salutation buildSalutation() {
    return Salutation.builder()
        .id(TestsConstants.SALUT_ID)
        .code(TestsConstants.SALUT_CODE)
        .type(TestsConstants.SALUT_TYPE)
        .description(TestsConstants.SALUT_DESC).build();
  }

  private static Language buildLanguage() {
    return Language.builder()
        .id(TestsConstants.LANG_ID)
        .langcode(TestsConstants.LANG_CODE_DE)
        .langiso(TestsConstants.LANG_DE)
        .description(TestsConstants.LANG_DESC).build();
  }

  private static Login buildLogin(final EshopUser user) {
    final Login userLogin = new Login();
    userLogin.setId(TestsConstants.LOGIN_ID);
    final Date date = buildDate();
    userLogin.setFirstLoginDate(date);
    userLogin.setLastOnBehalfOfDate(date);
    userLogin.setUserActive(true);
    userLogin.setUserId(TestsConstants.USER_ID_LONG);
    userLogin.setEshopUser(user);
    userLogin.setPasswordHash(
        PasswordHash.ofPasswordAsString(TestsConstants.PWD_RAW, HashType.BLCK_VAR));
    return userLogin;
  }

  private static RolePermission buildRolePermission(EshopPermission perm, EshopRole role) {
    return RolePermission.builder()
        .id(TestsConstants.ROLE_PERM_ID)
        .eshopPermission(perm)
        .eshopRole(role).build();
  }

  private static EshopPermission buildPermission() {
    final EshopPermission perm = EshopPermission.builder()
        .id(TestsConstants.PERM_ID)
        .createdBy(TestsConstants.USER_ID_LONG)
        .description(TestsConstants.PERM_DESC)
        .modifiedBy(TestsConstants.USER_ID_LONG)
        .permissisonKey(TestsConstants.PERM_KEY_VIEW_PROFILE)
        .permission(TestsConstants.PERM_NAME).build();
    perm.setPermFunctions(Arrays.asList(buildPermFunction(buildFunction(), perm)));
    return perm;
  }

  private static PermFunction buildPermFunction(EshopFunction func, EshopPermission perm) {
    return PermFunction.builder()
        .id(TestsConstants.PERM_FUNC_ID)
        .eshopFunction(func)
        .eshopPermission(perm).build();
  }

  private static EshopFunction buildFunction() {
    return EshopFunction.builder()
        .id(TestsConstants.FUNC_ID)
        .functionName(TestsConstants.PERM_NAME)
        .relativeUrl(TestsConstants.FUNC_URL)
        .description(TestsConstants.FUNC_DESC).build();
  }

  // @formatter:on

  private static EshopGroup buildGroup(EshopAuthority authority) {
    final EshopGroup group = new EshopGroup();
    group.setId(TestsConstants.GROUP_ID);
    if (authority == EshopAuthority.GROUP_ADMIN) {
      group.setDescription(TestsConstants.GROUP_ADMIN_DESC);
      group.setName(TestsConstants.GROUP_ADMIN_NAME);
    } else if (authority == EshopAuthority.MARKETING_ASSISTANT) {
      group.setDescription(TestsConstants.MARKETING_GROUP_DESC);
      group.setName(TestsConstants.MARKETING_GROUP_NAME);
    } else if (authority == EshopAuthority.NORMAL_USER) {
      group.setDescription(TestsConstants.NORMALUSER_GROUP_DESC);
      group.setName(TestsConstants.NORMALUSER_GROUP_NAME);
    } else if (authority == EshopAuthority.SALES_ASSISTANT) {
      group.setDescription(TestsConstants.ASSISTANT_GROUP_DESC);
      group.setName(TestsConstants.ASSISTANT_GROUP_NAME);
    } else if (authority == EshopAuthority.SYSTEM_ADMIN) {
      group.setDescription(TestsConstants.SYSTEMADMIN_GROUP_DESC);
      group.setName(TestsConstants.SYSTEMADMIN_GROUP_NAME);
    } else if (authority == EshopAuthority.USER_ADMIN) {
      group.setDescription(TestsConstants.USERADMIN_GROUP_DESC);
      group.setName(TestsConstants.USERADMIN_GROUP_NAME);
    } else if (authority == EshopAuthority.FINAL_USER_ADMIN) {
      group.setDescription(StringUtils.EMPTY);
      group.setName(StringUtils.EMPTY);
    }
    final Organisation org = buildOrganisation();
    final OrganisationGroup orgGroup = buildOrganisationGroup(org, group);
    org.setOrganisationGroups(Arrays.asList(orgGroup));
    final EshopRole role = buildRole(authority);
    final GroupRole groupRole = buildGroupRole(group, role);
    group.setGroupRoles(Arrays.asList(groupRole));
    group.setOrganisationGroups(Arrays.asList(orgGroup));
    return group;
  }

  private static OrganisationGroup buildOrganisationGroup(final Organisation org,
      final EshopGroup group) {
    final OrganisationGroup orgGroup = new OrganisationGroup();
    orgGroup.setId(TestsConstants.ORG_GROUP_ID);
    orgGroup.setEshopGroup(group);
    orgGroup.setOrganisation(org);
    return orgGroup;
  }

  private static GroupRole buildGroupRole(EshopGroup group, EshopRole role) {
    final GroupRole groupRole = new GroupRole();
    groupRole.setEshopGroup(group);
    groupRole.setEshopRole(role);
    return groupRole;
  }

  private static EshopRole buildRole(EshopAuthority roleAuth) {
    final EshopPermission perm = buildPermission();
    final EshopRole role = EshopRole.builder().id(TestsConstants.ROLE_ID).build();
    role.setRolePermissions(Arrays.asList(buildRolePermission(perm, role)));
    if (roleAuth == EshopAuthority.SYSTEM_ADMIN) {
      role.setName(EshopAuthority.SYSTEM_ADMIN.name());
      role.setDescription(TestsConstants.SYSTEM_ADMIN_DESC);
      role.setRoleType(buildRoleType(RoleTypeEnum.ADMIN));
    } else if (roleAuth == EshopAuthority.GROUP_ADMIN) {
      role.setName(EshopAuthority.GROUP_ADMIN.name());
      role.setDescription(TestsConstants.GROUP_ADMIN_DESC);
      role.setRoleType(buildRoleType(RoleTypeEnum.ADMIN));
    } else if (roleAuth == EshopAuthority.USER_ADMIN) {
      role.setName(EshopAuthority.USER_ADMIN.name());
      role.setDescription(TestsConstants.USER_ADMIN_DESC);
      role.setRoleType(buildRoleType(RoleTypeEnum.ADMIN));
    } else if (roleAuth == EshopAuthority.NORMAL_USER) {
      role.setName(EshopAuthority.NORMAL_USER.name());
      role.setDescription(TestsConstants.NORMAL_USER_DESC);
      role.setRoleType(buildRoleType(RoleTypeEnum.NORMAL_USER));
    } else if (roleAuth == EshopAuthority.MARKETING_ASSISTANT) {
      role.setName(EshopAuthority.MARKETING_ASSISTANT.name());
      role.setDescription(TestsConstants.MARKETING_ASSISTANT_DESC);
      role.setRoleType(buildRoleType(RoleTypeEnum.ASSISTANT));
    } else if (roleAuth == EshopAuthority.SALES_ASSISTANT) {
      role.setName(EshopAuthority.SALES_ASSISTANT.name());
      role.setDescription(TestsConstants.SALES_ASSISTANT_DESC);
      role.setRoleType(buildRoleType(RoleTypeEnum.ASSISTANT));
    } else if (roleAuth == EshopAuthority.FINAL_USER_ADMIN) {
      role.setName(EshopAuthority.FINAL_USER_ADMIN.name());
      role.setDescription(EshopAuthority.FINAL_USER_ADMIN.name());
      role.setRoleType(buildRoleType(RoleTypeEnum.NORMAL_USER));
    }

    return role;
  }

  private static RoleType buildRoleType(RoleTypeEnum roleTypeEnum) {
    final RoleType roleType = new RoleType();
    roleType.setId(TestsConstants.ROLE_TYPE_ID);
    if (roleTypeEnum == RoleTypeEnum.ADMIN) {
      roleType.setName(RoleTypeEnum.ADMIN.name());
      roleType.setDescription(TestsConstants.ROLETYPE_ADMIN_DESC);
    } else if (roleTypeEnum == RoleTypeEnum.ASSISTANT) {
      roleType.setName(RoleTypeEnum.ASSISTANT.name());
      roleType.setDescription(TestsConstants.ROLETYPE_ASSISTANT_DESC);
    } else if (roleTypeEnum == RoleTypeEnum.NORMAL_USER) {
      roleType.setName(RoleTypeEnum.NORMAL_USER.name());
      roleType.setDescription(TestsConstants.ROLETYPE_NORMAL_USER_DESC);
    }
    return roleType;
  }

  private static OrganisationType buildOrganisationType() {
    final OrganisationType orgType = new OrganisationType();
    orgType.setId(TestsConstants.ORG_TYPE);
    orgType.setDescription("This is Affiliate organisation");
    orgType.setLevel(TestsConstants.ORG_TYPE_LEVEL);
    orgType.setName(TestsConstants.ORG_TYPE_NAME);
    return orgType;
  }

  private static OrganisationAddress buildOrganisationAddress(final Organisation org) {
    final OrganisationAddress orgAddress = new OrganisationAddress();
    orgAddress.setId(TestsConstants.ORG_ADDRESS_ID);
    orgAddress.setAddress(buildEshopAddress(orgAddress));
    orgAddress.setOrganisation(org);
    return orgAddress;
  }

  private static EshopAddress buildEshopAddress(final OrganisationAddress orgAddress) {
    final EshopAddress eshopAddress = new EshopAddress();
    eshopAddress.setId(TestsConstants.ESHOP_ADDRESS_ID);
    eshopAddress.setAddressType(buildAddressType());
    eshopAddress.setCity(StringUtils.EMPTY);
    eshopAddress.setCountryiso(TestsConstants.COUNTRY_ISO_CODE_AT);
    eshopAddress.setLine1(StringUtils.EMPTY);
    eshopAddress.setLine2(StringUtils.EMPTY);
    eshopAddress.setLine3(StringUtils.EMPTY);
    eshopAddress.setOrganisationAddresses(Arrays.asList(orgAddress));
    eshopAddress.setState(StringUtils.EMPTY);
    eshopAddress.setZipcode(TestsConstants.ZIP_CODE);
    return eshopAddress;
  }

  private static AddressType buildAddressType() {
    final AddressType addressType = new AddressType();

    return addressType;
  }

  private static CustomerSettings buildCustomerSettings(final Organisation org) {
    final CustomerSettings customerSettings = new CustomerSettings();
    customerSettings.setId(TestsConstants.CUST_SETTINGS_ID);
    customerSettings.setAddressId(false);
    customerSettings.setAllocationId(TestsConstants.ALLOCATION_ID);
    customerSettings.setAllowNetPriceChanged(false);
    customerSettings.setBillingAddressId(TestsConstants.BILLING_ADDR_ID);
    customerSettings.setCollectiveDelivery(TestsConstants.COLLECTIVE_DELIVERY);
    customerSettings.setDeliveryAddressId(TestsConstants.BILLING_ADDR_ID);
    customerSettings.setDeliveryId(TestsConstants.DELIVERY_ID);
    customerSettings.setEmailNotificationOrder(false);
    customerSettings.setInvoiceType(buildInvoiceType());
    customerSettings.setNetPriceConfirm(true);
    customerSettings.setNetPriceView(true);
    customerSettings.setOrganisation(org);
    customerSettings.setPaymentMethod(buildPaymentMethod());
    customerSettings.setSessionTimeoutSeconds(TestsConstants.TIMEOUT_1HOUR); // one hour
    customerSettings.setShowDiscount(false);
    customerSettings.setUseDefaultSetting(false);
    customerSettings.setViewBilling(false);
    return customerSettings;
  }

  public static Customer buildCustomer() {
    return Customer.builder() // @formatter:off
        .active(true)
        .addressSalutation(StringUtils.EMPTY)
        .affiliateName("AIGSTE")
        .affiliateShortName(TestsConstants.ORG_SHORTNAME)
        .allowShowPfandArticle(true)
        .alreadyUsedCredit(5.0)
        .availableCredit(20.8)
        .axInvoiceType("TAGSAMFAKT")
        .axPaymentType("NORMAL")
        .axSalesOrderPool(StringUtils.EMPTY)
        .axSendMethod(TestsConstants.SEND_METHOD_TOUR)
        .branch(buildCustomerBranch())
        .cashDiscount(StringUtils.EMPTY)
        .cashOrCreditTypeCode(TestsConstants.CREDIT)
        .category(StringUtils.EMPTY)
        .city(StringUtils.EMPTY)
        .comment(StringUtils.EMPTY)
        .companyId(1L)
        .companyName(TestsConstants.ORG_NAME)
        .currency(TestsConstants.CURRENCY_EUR)
        .currencyId(TestsConstants.CURRENCY_ID)
        .defaultBranchId(StringUtils.EMPTY)
        .disposalNumber("2003014")
        .emailContacts(Arrays.asList(buildContactInfo(ContactType.EMAIL)))
        .faxContacts(Arrays.asList(buildContactInfo(ContactType.FAX)))
        .freeDeliveryEndDate(buildDate())
        .invoiceTypeCode(TestsConstants.INVOICE_TYPECODE_DAILY)
        .language(TestsConstants.LANG_DE)
        .languageId(TestsConstants.LANG_ID)
        .lastName(StringUtils.EMPTY)
        .letterSalutation(StringUtils.EMPTY)
        .name(StringUtils.EMPTY)
        .nr(TestsConstants.CUST_NR)
        .oepUvpPrint(true)
        .organisationId(TestsConstants.DDAT_ORG_ID)
        .organisationShort(TestsConstants.ORG_SHORTNAME)
        .phoneContacts(Arrays.asList(buildContactInfo(ContactType.PHONE)))
        .salesGroup("SAG-DDAT")
        .salesRepPersonalNumber(TestsConstants.PHONE_NR)
        .sendMethod(TestsConstants.SEND_METHOD_TOUR)
        .sendMethodCode(TestsConstants.SEND_METHOD_TOUR)
        .shortName(TestsConstants.ORG_SHORTNAME)
        .statusShort(StringUtils.EMPTY)
        .termOfPayment(StringUtils.EMPTY)
        .vatNr(TestsConstants.VAT_20)
        .zipCode(TestsConstants.ZIP_CODE)
        .build(); // @formatter:on
  }

  private static ContactInfo buildContactInfo(ContactType contactType) {
    final ContactInfo contactInfo = new ContactInfo();
    contactInfo.setPrimary(true);
    if (contactType == ContactType.EMAIL) {
      contactInfo.setType(ContactType.EMAIL.name());
      contactInfo.setValue(TestsConstants.EMAIL);
      contactInfo.setDescription("Email contact");
    } else if (contactType == ContactType.FAX) {
      contactInfo.setType(ContactType.FAX.name());
      contactInfo.setValue(TestsConstants.FAX);
      contactInfo.setDescription("Fax contact");
    } else if (contactType == ContactType.PHONE) {
      contactInfo.setType(ContactType.PHONE.name());
      contactInfo.setValue(TestsConstants.PHONE_NR);
      contactInfo.setDescription("Phone contact");
    }
    return contactInfo;
  }

  private static CustomerBranch buildCustomerBranch() {
    final CustomerBranch customerBranch = new CustomerBranch();
    customerBranch.setBranchId(TestsConstants.BRANCH_ID);
    customerBranch.setBranchName(TestsConstants.BRANCH_NAME);
    customerBranch.setPhoneNr(TestsConstants.PHONE_NR);
    return customerBranch;
  }

  private static PaymentMethod buildPaymentMethod() {
    final PaymentMethod paymentMethod = new PaymentMethod();
    paymentMethod.setId(TestsConstants.PAYMENT_METHOD_ID);
    paymentMethod.setDescCode(TestsConstants.CREDIT);
    paymentMethod.setDescription(TestsConstants.PAY_METHOD_RECHNUNG);
    paymentMethod.setOrderDisplay(1);
    paymentMethod.setPayMethod(TestsConstants.PAY_METHOD_RECHNUNG);
    return paymentMethod;
  }

  private static InvoiceType buildInvoiceType() {
    final InvoiceType invoiceType = new InvoiceType();
    invoiceType.setId(TestsConstants.INVOICETYPE_ID);
    invoiceType.setInvoiceTypeCode(TestsConstants.TWO_WEEKLY_CREDIT);
    invoiceType.setInvoiceTypeDesc(TestsConstants.TWO_WEEKLY_CREDIT_DESC);
    invoiceType.setInvoiceTypeName(TestsConstants.TWO_WEEKLY_CREDIT_TYPE);
    return invoiceType;
  }

  private static CouponUseLog buildCouponUseLog() {
    final CouponUseLog couponUseLog = new CouponUseLog();
    couponUseLog.setId(TestsConstants.COUPON_LOG_ID);
    couponUseLog.setUserId(TestsConstants.USER_ID);
    couponUseLog.setCustomerNr(TestsConstants.ORG_CODE);
    couponUseLog.setUmarId(TestsConstants.ART_ID);
    couponUseLog.setDiscountArticleId(TestsConstants.ART_ID);
    couponUseLog.setArticleIdMatch("1000000014461823757");
    couponUseLog.setCouponsCode(TestsConstants.COUPON_CODE);
    couponUseLog.setDateUsed(buildDate());
    couponUseLog.setOrderID(7L);
    couponUseLog.setAffiliateMatch("DERENDINGER-AT");
    couponUseLog.setAmoutApplied(25);
    couponUseLog.setArticleCategories("82,854");
    couponUseLog.setBrandsMatch("30,2,48");
    couponUseLog.setCountryMatch("CH");
    couponUseLog.setOrderConfirmationId(5L);
    couponUseLog.setUsageCountRemainder(10);
    return couponUseLog;
  }

  private static Date buildDate() {
    final Calendar cal = Calendar.getInstance();
    cal.set(Calendar.YEAR, 2016);
    cal.set(Calendar.MONTH, 9);
    cal.set(Calendar.DAY_OF_MONTH, 21);
    cal.set(Calendar.HOUR_OF_DAY, 9);
    cal.set(Calendar.MINUTE, 18);
    cal.set(Calendar.SECOND, 27); // 21.10.2016 09:18:27
    return cal.getTime();
  }

  public EshopUser buildWholesalerUserAdmin() {
    final EshopUser user = new EshopUser();
    user.setUsername(FINAL_USER_NAME_WSS_USER_ADMIN);
    user.setGroupUsers(Arrays.asList(buildGroupUser(EshopAuthority.FINAL_USER_ADMIN)));
    return user;
  }

  private GroupUser buildGroupUser(EshopAuthority authority) {
    final GroupUser groupUser = new GroupUser();
    groupUser.setEshopGroup(buildGroup(authority));
    return groupUser;
  }

}
