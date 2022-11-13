package com.sagag.eshop.repo.entity;

import com.sagag.services.common.contants.SagConstants;
import com.sagag.services.domain.eshop.common.EshopAuthority;

import lombok.Data;
import lombok.NoArgsConstructor;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * User detail view class which maps to view V_USER_DETAIL.
 */
@Data
@Entity
@Table(name = "V_USER_DETAIL")
@NoArgsConstructor
public class VUserDetail implements Serializable {

  private static final long serialVersionUID = -2374711264532759853L;

  @Id
  private Long userId;

  private String userName;

  private String userEmail;

  private String firstName;

  private String lastName;

  private String telephone;

  private Integer userSettingId;

  private Boolean saleOnBehalfOf;

  private Integer roleId;

  private String roleName;

  private Integer groupId;

  private String groupName;

  private Integer orgId;

  private Integer orgParentId;

  private String orgCode;

  private String orgShortName;

  private String orgName;

  private Integer orgSettingsId;

  private String salutCode;

  private String userType;

  private Integer salutId;

  /**
   * Constructs the user detail with <code>id</code>, <code>username</code> and flag
   * <code>saleOnBehalfOf</code>.
   *
   * @param
   * @param username the username
   * @param saleOnBehalfOf the flag indicating whether this user is allowed to login onbehalf by
   *        sales.
   */
  public VUserDetail(Long userId, final String username, final Boolean saleOnBehalfOf) {
    this.userId = userId;
    this.userName = username;
    this.saleOnBehalfOf = saleOnBehalfOf;
  }

  /**
   * Returns the fullname of this user is the concatenation of firstname and lastname.
   *
   * @return the fullname of this user
   */
  public String getFullName() {
    return String.join(SagConstants.SPACE, firstName, lastName);
  }

  public boolean isUserAdminRole() {
    return equalsRoleName(EshopAuthority.USER_ADMIN, roleName);
  }

  private static boolean equalsRoleName(EshopAuthority authority, String roleName) {
    if (StringUtils.isBlank(roleName) || authority == null) {
      return false;
    }
    return StringUtils.equals(authority.name(), roleName);
  }
}
