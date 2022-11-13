package com.sagag.eshop.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sagag.eshop.service.dto.client.EshopClientDto;
import com.sagag.services.common.contants.SagConstants;
import com.sagag.services.common.enums.ErpAddressType;
import com.sagag.services.common.enums.PermissionEnum;
import com.sagag.services.common.enums.SupportedAffiliate;
import com.sagag.services.common.enums.UserType;
import com.sagag.services.domain.eshop.common.EshopAuthority;
import com.sagag.services.domain.eshop.dto.OrderLocation;
import com.sagag.services.domain.eshop.dto.OrganisationDto;
import com.sagag.services.domain.sag.erp.Address;
import com.sagag.services.domain.sag.external.Customer;
import com.sagag.services.domain.sag.external.ExternalUserSession;

import lombok.Data;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Session cache user info for the current logging user<br/>
 * to avoid the request at server side to get user info again and thus, to reduce performance.
 */
@Data
@JsonInclude(Include.NON_NULL)
public class UserInfo implements Serializable {

  private static final long serialVersionUID = 6104002983995084264L;

  // User Login or User on-behalf Information
  private Long id;

  private String username;

  private String firstName;

  private String lastName;

  private String language;

  private String companyName;

  private Long originalUserId; // Original user id if using virtual user

  private Long orgUsrId; // Original user id if using virtual user

  private String affiliateShortName;

  private String defaultBranchName;

  private String email;

  private Locale userLocale;

  @JsonIgnore
  private String type;

  private Date signInDate;

  private Date firstLoginDate;

  private Date lastOfOnBehalfDate;

  // User Roles and Permissions
  @JsonIgnore
  private List<String> roles;

  private List<PermissionDto> permissions;

  // User Order Information if existing
  private List<OrderLocation> orderLocations;

  // Sales information if existing
  private Long salesId; // sale on behalf id if existing

  @JsonIgnore
  private String salesUsername; // sale on behalf username if existing

  private String salesEmployeeNumber;

  // User Settings
  private OwnSettings settings;

  // Customer Information and Settings
  @JsonIgnore
  private Integer organisationId;

  private Customer customer;

  private List<Address> addresses;

  private boolean vatConfirm;

  private Double hourlyRate;

  // Collection Information
  private Integer collectionId;

  private String collectionShortname;

  private String collectionName;

  // Wholesaler and Final Customer Information
  private OrganisationDto finalCustomer;

  // External user credentials for Stakis ERP if existing
  private ExternalUserSession externalUserSession;

  // Authorization Client Information
  private EshopClientDto eshopClient;


  private boolean legalAccepted = true;

  /**
   * Default constructor.
   */
  public UserInfo() {
    settings = new OwnSettings();
  }

  /**
   * Checks if the user who belongs to the specific customer.
   *
   * @return <code>true</code> if the user belongs to a specific customer. Otherwise, return
   *         <code>false</code>.
   */
  @JsonIgnore
  public boolean hasCust() {
    return !Objects.isNull(getCustomer());
  }

  /**
   * Returns the customer availability url for this user.
   *
   * @return the availability url
   */
  @JsonIgnore
  public String getAvailabilityUrl() {
    if (!hasCust()) {
      return StringUtils.EMPTY;
    }
    return getCustomer().getAvailabilityRelativeUrl();
  }

  /**
   * Returns the first address id of the customer.
   *
   * @return the first address id
   */
  public String getFirstAddressId() {
    if (!hasAddress()) {
      return StringUtils.EMPTY;
    }
    return this.addresses.get(0).getId();
  }

  /**
   * Checks if the customer has the addresses.
   *
   * @return <code>true</code> if the customer has the addresses. Otherwise, return
   *         <code>false</code>.
   */
  @JsonIgnore
  public boolean hasAddress() {
    return !CollectionUtils.isEmpty(this.addresses);
  }

  /**
   * Returns the customer number that this user belongs.
   *
   * @return the customer number
   */
  public String getCustNr() {
    return getCustNrStr();
  }

  public String getCustNrStr() {
    if (!hasCust()) {
      return StringUtils.EMPTY;
    }
    return this.customer.getNr();
  }

  @JsonIgnore
  public boolean isValidCustNr() {
    if (!hasCust()) {
      return false;
    }
    return NumberUtils.compare(this.customer.getCustNrLong(), NumberUtils.LONG_ZERO) > 0
        || !StringUtils.isBlank(this.getCustNrStr());
  }

  /**
   * Checks if the user is role of {@link EshopAuthority.SYSTEM_ADMIN}.
   *
   * @return <code>true</code> if that user has role of {@link EshopAuthority.SYSTEM_ADMIN},
   *         otherwise return <code>false</code>.
   */
  @JsonIgnore
  public boolean isAdmin() {
    return this.getRoles().contains(EshopAuthority.SYSTEM_ADMIN.name());
  }

  /**
   * Checks if the user is role of {@link EshopAuthority.GROUP_ADMIN}.
   *
   * @return <code>true</code> if that user has role of {@link EshopAuthority.GROUP_ADMIN},
   *         otherwise return <code>false</code>.
   */
  @JsonIgnore
  public boolean isGroupAdminRole() {
    return this.getRoles().contains(EshopAuthority.GROUP_ADMIN.name());
  }

  /**
   * Checks if the user is role of {@link EshopAuthority.USER_ADMIN}.
   *
   * @return <code>true</code> if that user has role of {@link EshopAuthority.USER_ADMIN}, otherwise
   *         return <code>false</code>.
   */
  public boolean isUserAdminRole() {
    return this.getRoles().contains(EshopAuthority.USER_ADMIN.name());
  }

  /**
   * Checks if the user is role of {@link EshopAuthority.NORMAL_USER}.
   *
   * @return <code>true</code> if that user has role of {@link EshopAuthority.NORMAL_USER},
   *         otherwise return <code>false</code>.
   */
  @JsonIgnore
  public boolean isNormalUserRole() {
    return this.getRoles().contains(EshopAuthority.NORMAL_USER.name());
  }

  /**
   * Checks if the user is role of {@link EshopAuthority.SALES_ASSISTANT}.
   *
   * @return <code>true</code> if that user has role of {@link EshopAuthority.SALES_ASSISTANT},
   *         otherwise return <code>false</code>.
   */
  @JsonIgnore
  public boolean isSalesAssistantRole() {
    return this.getRoles().contains(EshopAuthority.SALES_ASSISTANT.name());
  }

  public boolean isFinalUserAdminRole() {
    return this.getRoles().contains(EshopAuthority.FINAL_USER_ADMIN.name());
  }

  @JsonIgnore
  public boolean isFinalNormalUserRole() {
    return this.getRoles().contains(EshopAuthority.FINAL_NORMAL_USER.name());
  }

  @JsonIgnore
  public boolean isAutonetUser() {
    return this.getRoles().contains(EshopAuthority.AUTONET_USER_ADMIN.name());
  }

  @JsonProperty("isFinalUserRole")
  public boolean isFinalUserRole() {
    return this.isFinalNormalUserRole() || this.isFinalUserAdminRole();
  }

  @JsonIgnore
  public boolean isCustomerRole() {
    return this.isUserAdminRole() || this.isNormalUserRole();
  }

  /**
   * Checks if the user is from the garage.
   *
   * @return <code>true</code> if that user is from the garage, otherwise return <code>false</code>.
   */
  @JsonIgnore
  public boolean isCustChecked() {
    return this.isCustomerRole() || this.isFinalUserRole();
  }

  /**
   * Checks if the user is from the specific affiliate.
   *
   * @return <code>true</code> if that user is from the specific affiliate, otherwise return
   *         <code>false</code>.
   */
  @JsonIgnore
  public boolean isAffChecked() {
    return this.isCustChecked() || this.isGroupAdminRole();
  }

  public String getRoleName() {
    return this.getRoles().get(0);
  }

  public boolean isSalesUser() {
    return this.isSalesAssistantRole();
  }

  public boolean isNormalUser() {
    return this.isCustChecked();
  }

  /**
   * Returns the customer default branch id.
   *
   * @return the default branch id
   */
  public String getDefaultBranchId() {
    if (!hasCust()) {
      return StringUtils.EMPTY;
    }
    return StringUtils.defaultString(getCustomer().getDefaultBranchId());
  }

  /**
   * Checks if the current user session is sales on behalf or not.
   *
   * @return <code>true</code> if the session is "on behalf", return <code>false</code> otherwise.
   */
  @JsonIgnore
  public boolean isSaleOnBehalf() {
    return !Objects.isNull(this.salesId);
  }


  /**
   * Checks if the current user session is virtual user or not.
   *
   * @return <code>true</code> if the session is "is DMS", return <code>false</code> otherwise.
   */
  @JsonIgnore
  public boolean isVirtualDMSUser() {
    return StringUtils.equals(StringUtils.trimToEmpty(this.type), UserType.VIRTUAL_DMS.name())
        && !Objects.isNull(this.originalUserId);
  }

  /**
   * Returns the correct cache userid for the current login and even sales on behalf login.
   * <p>
   * in case the sales on behalf login, the userid to get shopping basket cache is the userid of
   * sale in stead of customer admin's
   *
   * @return the alias userid used in cached shopping basket key.
   */
  public String getCachedUserId() {
    if (this.isSaleOnBehalf()) {
      return String.valueOf(this.salesId);
    }

    return String.valueOf(id);
  }


  /**
   * #5693
   * Returns the correct real userId account for the current login and even virtual user login (DMS session).
   * <p>
   * #5693:
   * in case the virtual user login, the userId of original user will be return.
   * </p>
   * @return the alias use
   */
  @JsonIgnore
  public long getOriginalUserId() {
    if (this.isVirtualDMSUser()) {
      return this.originalUserId;
    }
    return this.id;
  }

  /**
   * Returns the correct cache username for the current login and even sales on behalf login.
   * <p>
   * in case the sales on behalf login, the username to get shopping basket cache is the username of
   * sale in stead of customer admin's
   *
   * @return the alias username used in cached shopping basket key.
   */
  @JsonIgnore
  public String getCachedUsername() {
    if (this.isSaleOnBehalf()) {
      return this.getSalesUsername();
    }
    return getUsername();
  }

  /**
   * Returns the cached context unique key for this user.
   *
   * @return the unique cache context key for this user
   */
  @JsonIgnore
  public String key() {
    final String cachedUserId = this.getCachedUserId();
    final String custNr = this.getCustNrStr();
    if (Objects.isNull(custNr)) {
      return StringUtils.join(Arrays.asList(cachedUserId, companyName), SagConstants.UNDERSCORE);
    }
    return userKey(cachedUserId, custNr);
  }

  /**
   * Returns the unique user key for a specific customer.
   *
   * @param userId the user id
   * @param custNr the customer number
   * @return the unique key across customers
   */
  public static String userKey(String userId, String custNr) {
    return StringUtils.join(Arrays.asList(userId, custNr), SagConstants.UNDERSCORE);
  }


  /**
   * Returns the delivery address id.
   *
   * @return the delivery address id from DB, otherwise return the AX address id
   */
  public String getDeliveryAddressId() {
    return StringUtils.defaultIfBlank(settings.getDeliveryAddressId(), this.getFirstAddressId());
  }

  /**
   * Returns the billing address id.
   *
   * @return the billing address id from DB, otherwise return the AX address id
   */
  public String getBillingAddressId() {
    return StringUtils.defaultIfBlank(settings.getBillingAddressId(), this.getFirstAddressId());
  }

  /**
   * Checks if the user has the setting.
   *
   * @return <code>true</code> if the user has the own settings. Otherwise, return
   *         <code>false</code>.
   */
  @JsonIgnore
  public boolean hasUserSetting() {
    return !(Objects.isNull(this.settings) || Objects.isNull(this.settings.getUserSettings()));
  }

  /**
   * Returns the delivery id of user.
   *
   * @return the delivery id of user
   */
  @JsonIgnore
  public int getDeliveryId() {
    if (!hasUserSetting() && Objects.isNull(this.settings.getUserSettings().getDeliveryId())) {
      // Set default to TOUR
      return NumberUtils.INTEGER_ONE;
    }
    return this.settings.getUserSettings().getDeliveryId();
  }

  @JsonIgnore
  public boolean hasUrlPermission(String url) {
    if (CollectionUtils.isEmpty(this.permissions)) {
      return false;
    }
    return this.permissions.stream().flatMap(p -> p.getFunctions().stream())
        .anyMatch(f -> StringUtils.equalsIgnoreCase(f.getRelativeUrl(), url));
  }

  @JsonIgnore
  public boolean hasPermissionByName(PermissionEnum permission) {
    if (permission == null) {
      return false;
    }
    return this.getPermissions().stream()
        .anyMatch(p -> permission.name().equals(p.getPermission()));
  }

  @JsonProperty("hasAvailabilityPermission")
  public boolean hasAvailabilityPermission() {
    return (!isFinalUserRole() && hasCust()) || isAutonetUser() || existsWssDeliveryProfile();
  }

  private boolean existsWssDeliveryProfile() {
    return isFinalUserRole() && getSettings().hasWssDeliveryProfile();
  }

  /**
   * The util is to support building DVSE url which is added ESID = saleID The empty string is
   * default value which is normal user not sale on behalf.
   *
   * @return salesid string
   */
  public String getSalesIdString() {
    return Objects.nonNull(getSalesId()) ? String.valueOf(getSalesId()) : StringUtils.EMPTY;
  }

  public String getIdString() {
    return Objects.nonNull(getId()) ? String.valueOf(getId()) : StringUtils.EMPTY;
  }

  public boolean isShowHappyPoints() {
    return settings.isShowHappyPoints();
  }

  public boolean isAcceptHappyPointTerm() {
    return settings.isAcceptHappyPointTerm();
  }

  @JsonIgnore
  public List<FunctionDto> getAllFunctions() {
    if (CollectionUtils.isEmpty(permissions)) {
      return Collections.emptyList();
    }
    return permissions.stream().flatMap(p -> p.getFunctions().stream())
        .collect(Collectors.toList());
  }

  public boolean isSalesNotOnBehalf() {
    return isSalesUser() && !isSaleOnBehalf();
  }

  @JsonIgnore
  public String getFullName() {
    return StringUtils.defaultString(firstName) + StringUtils.SPACE
        + StringUtils.defaultString(lastName);
  }

  @JsonIgnore
  public String getCity() {
    return CollectionUtils.emptyIfNull(addresses).stream()
        .filter(item -> ErpAddressType.DEFAULT.name().equals(item.getAddressType())).findFirst()
        .map(Address::getCity).orElse(StringUtils.EMPTY);
  }


  @JsonIgnore
  public String getISO3Language() {
    return getUserLocale().getISO3Language();
  }

  public Optional<Address> getInvoiceAddress() {
    if (CollectionUtils.isEmpty(addresses)) {
      return Optional.empty();
    }
    // #3206: Take the DEFAULT address for invoice
    return addresses.stream()
        .filter(add -> ErpAddressType.DEFAULT.name().equals(add.getAddressTypeCode())).findFirst();
  }

  @JsonIgnore
  public String getDefaultBranch() {
    return getDefaultBranchId() + SagConstants.HYPHEN_HAS_SPACE_DELIMITER + getDefaultBranchName();
  }

  @JsonIgnore
  public SupportedAffiliate getSupportedAffiliate() {
    if (isSalesAssistantRole() || isAdmin()) {
      return null;
    }
    return SupportedAffiliate.fromDesc(getAffiliateShortName());
  }

  @JsonIgnore
  public String getUserCurrency() {
    if (!hasCust()) {
      return StringUtils.EMPTY;
    }
    return getCustomer().getCurrency();
  }

  @JsonIgnore
  public boolean isOciVirtualUser() {
    return StringUtils.equalsIgnoreCase(UserType.VIRTUAL_OCI.name(), this.getType());
  }

  @JsonProperty("hasWholesalerPermission")
  public boolean hasWholesalerPermission() {
    return CollectionUtils.emptyIfNull(permissions).stream()
        .anyMatch(item -> PermissionEnum.WHOLESALER.name().equals(item.getPermission()));
  }

  @JsonProperty("hasDmsPermission")
  public boolean hasDmsPermission() {
    return CollectionUtils.emptyIfNull(permissions).stream()
        .anyMatch(item -> PermissionEnum.DMS.name().equals(item.getPermission()));
  }

  @JsonIgnore
  public Integer getFinalCustomerOrgId() {
    return Optional.ofNullable(finalCustomer).map(OrganisationDto::getId).orElse(null);
  }

  /**
   * Is normauto display.
   *
   * @return boolean
   */
  @JsonIgnore
  public boolean isNormautoDisplay() {
    return getSettings().isNormautoDisplay();
  }

  @JsonIgnore
  public boolean isWbbCustomerUserAdmin() {
    if (this.getSupportedAffiliate() == null) {
      return false;
    }
    return SupportedAffiliate.WBB == this.getSupportedAffiliate() && this.isUserAdminRole();
  }

  @JsonIgnore
  public boolean isOnbehalfAdminUserType() {
    return UserType.ON_BEHALF_ADMIN.name()
        .equalsIgnoreCase(StringUtils.defaultString(this.getType()));
  }

  @JsonIgnore
  public boolean isAutonetUserType() {
    return UserType.AUTONET.name().equalsIgnoreCase(StringUtils.defaultString(this.getType()));
  }

  public boolean isShowNetPriceEnabled() {
    return settings.isWholeSalerHasNetPrice() && settings.isNetPriceView();
  }

  public boolean isFinalCustomerHasNetPrice() {
    return settings.isFinalCustomerHasNetPrice();
  }

  public boolean isWholeSalerHasNetPrice() {
    return settings.isWholeSalerHasNetPrice();
  }

  public boolean isFinalUserHasNetPrice() {
    return this.isFinalUserRole() && this.isWholeSalerHasNetPrice()
        && this.getSettings().isNetPriceConfirm() && this.isFinalCustomerHasNetPrice();
  }

  @JsonIgnore
  public Integer getUserOrganisationId() {
    return this.isFinalUserRole() ? this.getFinalCustomerOrgId() : this.getOrganisationId();
  }

  public boolean isUsedExternalPart() {
    if (Objects.isNull(this.getSettings().getExternalPartSettings())) {
      return false;
    }
    return this.getSettings().getExternalPartSettings().isUseExternalParts();
  }

  public boolean isShownInReferenceGroup() {
    if (Objects.isNull(this.getSettings().getExternalPartSettings())) {
      return false;
    }
    return this.getSettings().getExternalPartSettings().isUseExternalParts()
        && this.getSettings().getExternalPartSettings().isShowInReferenceGroup();
  }
}
