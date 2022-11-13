package com.sagag.eshop.repo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sagag.services.common.enums.UserType;
import com.sagag.services.domain.eshop.common.EshopAuthority;
import com.sagag.services.domain.eshop.dto.UserProfileDto;
import com.sagag.services.domain.sag.external.Customer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "ESHOP_USER")
@NamedQueries(
    value = {@NamedQuery(name = "EshopUser.findAll", query = "select u from EshopUser u"),
        @NamedQuery(name = "EshopUser.findByEmail",
            query = "select u from EshopUser u where u.email= :email"),
        @NamedQuery(name = "EshopUser.findAllSortByEmail",
            query = "select u from EshopUser u order by u.email"),
        @NamedQuery(name = "EshopUser.findById",
            query = "select u from EshopUser u where u.id= :id"),
        @NamedQuery(name = "EshopUser.findUsersByCustomerNumber",
            query = "select u from EshopUser u inner join u.groupUsers gu "
                + "inner join gu.eshopGroup eg " + "inner join eg.organisationGroups og "
                + "inner join og.organisation o "
                + "where o.orgCode= ? and u.login.isUserActive = 1")}
)
@NamedQuery(name = "EshopUser.countAll", query = "SELECT COUNT(u) FROM EshopUser u")
@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@ToString(exclude = { "groupUsers", "login", "userSetting", "customer", "affiliate"})
public class EshopUser implements Serializable {

  private static final long serialVersionUID = -6432600255055612624L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  private String email;

  @Column(name = "EMAIL_CONFIRMATION")
  private boolean emailConfirmation;

  @Column(name = "FIRST_NAME")
  private String firstName;

  @Column(name = "HOURLY_RATE")
  private Double hourlyRate;

  @ManyToOne
  @JoinColumn(name = "LANGUAGE", referencedColumnName = "ID")
  private Language language;

  @Column(name = "LAST_NAME")
  private String lastName;

  private boolean newsletter;

  private String phone;

  private String fax;

  @Column(name = "SIGN_IN_DATE")
  private Date signInDate;

  @ManyToOne
  @JoinColumn(name = "SALUTATION", referencedColumnName = "ID")
  private Salutation salutation;

  private int setting;

  private String type;

  private String username;

  @Column(name = "VAT_CONFIRM")
  private boolean vatConfirm;

  @OneToMany(mappedBy = "eshopUser")
  private List<GroupUser> groupUsers;

  @OneToOne(mappedBy = "eshopUser")
  private Login login;

  @ManyToOne
  @JoinColumn(name = "SETTING", referencedColumnName = "ID", updatable = false, insertable = false)
  private UserSettings userSetting;

  @Transient
  private Customer customer;

  @Transient
  private Organisation affiliate;

  @Transient
  private String langiso;

  private Long originalUserId;

  @Transient
  private Date firstLoginDate;

  @Transient
  private Date lastOfOnBehalfDate;

  /**
   * Constructs the eshop user from neccessary information.
   *
   * @param id the user id
   * @param username the username
   * @param firstName the first name
   * @param lastName the last name
   * @param langiso the lanuage iso
   * @param email the email
   * @param hourlyRate the hourly rate
   * @param vatConfirm the vat confirm option
   * @param type the user type
   */
  public EshopUser(final Long id, String username, String firstName, String lastName,
      String langiso, String email, Double hourlyRate, boolean vatConfirm, String type) {
    this.id = id;
    this.username = username;
    this.firstName = firstName;
    this.lastName = lastName;
    this.langiso = langiso;
    this.email = email;
    this.hourlyRate = hourlyRate;
    this.vatConfirm = vatConfirm;
    this.type = type;
  }

  public EshopUser(Long id, String username, String firstName, String lastName, String langiso,
      String email, Double hourlyRate, boolean vatConfirm, String type, Date signInDate,
      Date firstLoginDate, Date lastOfOnBehalfDate, Long originalUserId) {
    this.id = id;
    this.username = username;
    this.firstName = firstName;
    this.lastName = lastName;
    this.langiso = langiso;
    this.email = email;
    this.hourlyRate = hourlyRate;
    this.vatConfirm = vatConfirm;
    this.type = type;
    this.signInDate = signInDate;
    this.firstLoginDate = firstLoginDate;
    this.lastOfOnBehalfDate = lastOfOnBehalfDate;
    this.originalUserId = originalUserId;
  }

  /**
   * Constructs the eshop user from user id.
   *
   * @param id the user id
   */
  public EshopUser(final Long id) {
    this.id = id;
  }

  /**
   * Returns all roles of the user.
   *
   * @return a list of roles.
   */
  @JsonIgnore
  public List<String> getRoles() {
    // @formatter:off
    return getGroupUsers().stream()
        .map(GroupUser::getEshopGroup)
        .flatMap(group -> group.getGroupRoles().stream())
        .map(groupRole -> groupRole.getEshopRole().getName())
        .collect(Collectors.toList());
  }

  @JsonIgnore
  public Optional<RoleType> getRolesType() {
    // @formatter:off
    return getGroupUsers().stream()
        .map(GroupUser::getEshopGroup)
        .flatMap(group -> group.getGroupRoles().stream())
        .map(groupRole -> groupRole.getEshopRole().getRoleType()).findFirst();
  }

  @JsonIgnore
  public Optional<EshopGroup> getFirstEshopGroup() {
    return getGroupUsers().stream().map(GroupUser::getEshopGroup).findFirst();
  }

  /**
   * Checks if the user is role of {@link EshopAuthority.GROUP_ADMIN}
   *
   * @return <code>true</code> if that user has role of {@link EshopAuthority.GROUP_ADMIN},
   *     otherwise return <code>false</code>.
   */
  @JsonIgnore
  public boolean isGroupAdminRole() {
    return this.getRoles().contains(EshopAuthority.GROUP_ADMIN.name());
  }

  /**
   * Checks if the user is role of {@link EshopAuthority.USER_ADMIN}
   *
   * @return <code>true</code> if that user has role of {@link EshopAuthority.USER_ADMIN}, otherwise
   *         return <code>false</code>.
   */
  @JsonIgnore
  public boolean isUserAdminRole() {
    return this.getRoles().contains(EshopAuthority.USER_ADMIN.name());
  }

  /**
   * Checks if the user is role of {@link EshopAuthority.FINAL_USER_ADMIN}
   *
   * @return <code>true</code> if that user has role of {@link EshopAuthority.FINAL_USER_ADMIN}, otherwise
   * return <code>false</code>.
   */
  @JsonIgnore
  public boolean isFinalUserAdminRole() {
    return this.getRoles().contains(EshopAuthority.FINAL_USER_ADMIN.name());
  }

  /**
   * Checks if the user is role of {@link EshopAuthority.FINAL_NORMAL_USER}
   *
   * @return <code>true</code> if that user has role of {@link EshopAuthority.FINAL_NORMAL_USER}, otherwise
   * return <code>false</code>.
   */
  @JsonIgnore
  public boolean isFinalNormalUserRole() {
    return this.getRoles().contains(EshopAuthority.FINAL_NORMAL_USER.name());
  }

  /**
   * Checks if the user is role of {@link EshopAuthority.NORMAL_USER}
   *
   * @return <code>true</code> if that user has role of {@link EshopAuthority.NORMAL_USER},
   *         otherwise return <code>false</code>.
   */
  @JsonIgnore
  public boolean isNormalUserRole() {
    return this.getRoles().contains(EshopAuthority.NORMAL_USER.name());
  }

  /**
   * Checks if the user is role of {@link EshopAuthority.SALES_ASSISTANT}
   *
   * @return <code>true</code> if that user has role of {@link EshopAuthority.SALES_ASSISTANT},
   *         otherwise return <code>false</code>.
   */
  @JsonIgnore
  public boolean isSalesAssistantRole() {
    return this.getRoles().contains(EshopAuthority.SALES_ASSISTANT.name());
  }

  /**
   * Returns the first {@link Organisation} list of this user.
   *
   * <p>Actually, one user just belongs to one organisation at the same time. Thus, there are no
   * following cases:
   * <ul>
   * <li>a user belongs to multiple groups which again belong to multiple different organisations.
   * <li>a group belongs to multiple different organisations.
   * </ul>
   *
   * @return the <code>Organisation</code> of this user.
   */
  @JsonIgnore
  public Optional<Organisation> firstOrganisation() {
    // @formatter:off
    return this.getGroupUsers().stream()
        .map(GroupUser::getEshopGroup)
        .flatMap(group -> group.getOrganisationGroups().stream())
        .map(OrganisationGroup::getOrganisation)
        .findFirst();
    // @formatter:on
  }

  /**
   * Returns a lisf of coupon use log by customer number.
   *
   * @return the list of {@link CouponUseLog}
   */
  @JsonIgnore
  public List<CouponUseLog> getCouponByCustomerNr() {
    return this.getGroupUsers().stream()
        .map(GroupUser::getEshopGroup)
        .map(EshopGroup::getOrganisationGroups)
        .flatMap(Collection::stream)
        .map(OrganisationGroup::getOrganisation)
        .findFirst()
        .map(Organisation::getCouponUseLog)
        .orElseGet(Collections::emptyList);
  }

  /**
   * Returns the affiliate in URL of this user.
   *
   * @param parentOrg the parent organisation of this user organisation.
   * @return the users affiliate
   */
  @JsonIgnore
  public String affiliateInUrl(final Organisation parentOrg) {
    if (parentOrg == null) {
      return StringUtils.EMPTY;
    }
    return parentOrg.getShortname();
  }

  /**
   * Returns the company name of this user. Only applicable for USER_ADMIN and NORMAL_USER roles.
   *
   * @param parentOrg the parent organisation of this users organisation.
   * @return the users affiliate
   */
  @JsonIgnore
  public String companyName(final Organisation parentOrg) {
    if (parentOrg == null) {
      return StringUtils.EMPTY;
    }
    if (isUserAdminRole() || isNormalUserRole()) {
      return parentOrg.getName();
    }
    return StringUtils.EMPTY;
  }

  /**
   * Checks if the user needed to check its customer.
   *
   * @return <code>true</code> if the user is the user admin or normal user.
   */
  public boolean hasToCheckTheCustomer() {
    return isUserAdminRole() || isNormalUserRole();
  }

  /**
   * Checks if the user needed to check its affiliate.
   *
   * @return <code>true</code> if the user is the group admin or user admin or normal user.
   */
  public boolean hasToCheckTheAffiliate() {
    return isUserAdminRole() || isNormalUserRole() || isGroupAdminRole() || isFinalNormalUserRole()
        || isFinalUserAdminRole();
  }

  /**
   * Returns all roles of this user.
   *
   * @return all roles
   */
  @JsonIgnore
  public List<EshopRole> getAllRoles() {
    return getGroupUsers().stream()
        .map(GroupUser::getEshopGroup)
        .flatMap(group -> group.getGroupRoles().stream())
        .map(GroupRole::getEshopRole)
        .collect(Collectors.toList());
  }

  /**
   * Checks if the user is role of {@link EshopAuthority.SYSTEM_ADMIN}
   *
   * @return <code>true</code> if that user has role of {@link EshopAuthority.SYSTEM_ADMIN},
   *         otherwise return <code>false</code>.
   */
  @JsonIgnore
  public boolean isAdmin() {
    return this.getRoles().contains(EshopAuthority.SYSTEM_ADMIN.name());
  }

  @JsonIgnore
  public String getNonNullLangIso() {
    if (language == null) {
      return StringUtils.defaultIfBlank(langiso, StringUtils.EMPTY);
    }
    return StringUtils.defaultIfBlank(language.getLangiso(), StringUtils.EMPTY);
  }

  public static EshopUser buildEshopUser(UserProfileDto userProfile, Integer userSettingId,
      Language language, Optional<Salutation> salutation, Optional<UserType> userType) {
    final EshopUser eshopUser = new EshopUser();
    eshopUser.setSetting(userSettingId);
    eshopUser.setUsername(userProfile.getUserName());
    eshopUser.setEmail(userProfile.getEmail());
    eshopUser.setFirstName(userProfile.getFirstName());
    eshopUser.setLastName(userProfile.getSurName());
    eshopUser.setLastName(userProfile.getSurName());
    eshopUser.setPhone(userProfile.getPhoneNumber());
    eshopUser.setVatConfirm(true); // #1745 Default value on user creation for VAT TRUE
    userType.ifPresent(type -> eshopUser.setType(type.name()));
    eshopUser.setLanguage(language);
    salutation.ifPresent(eshopUser::setSalutation);
    eshopUser.setHourlyRate(userProfile.getHourlyRate());
    return eshopUser;
  }

  @JsonIgnore
  public String getFirstRole() {
    return CollectionUtils.isEmpty(getRoles()) ? StringUtils.EMPTY : getRoles().get(0);
  }
}
