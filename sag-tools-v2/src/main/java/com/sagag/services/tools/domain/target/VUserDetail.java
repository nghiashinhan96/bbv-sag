package com.sagag.services.tools.domain.target;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import java.io.Serializable;

@Entity
@Table(name = "V_USER_DETAIL")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VUserDetail implements Serializable {

  private static final long serialVersionUID = 7041622445549174398L;

  @Id
  private Long id;

  @Column(name = "USER_ID")
  private Long userId;

  @Column(name = "USER_NAME")
  private String userName;

  @Column(name = "USER_EMAIL")
  private String userEmail;

  @Column(name = "FIRST_NAME")
  private String firstName;

  @Column(name = "LAST_NAME")
  private String lastName;

  @Column(name = "TELEPHONE")
  private String telephone;

  @Column(name = "USER_SETTING_ID")
  private Integer userSettingId;

  @Column(name = "SALE_ON_BEHALF_OF")
  private Boolean saleOnBehalfOf;

  @Column(name = "ROLE_ID")
  private Integer roleId;
  
  @Column(name = "ROLE_NAME")
  private String roleName;

  @Column(name = "GROUP_ID")
  private Integer groupId;

  @Column(name = "GROUP_NAME")
  private String groupName;

  @Column(name = "ORG_ID")
  private Integer orgId;

  @Column(name = "ORG_PARENT_ID")
  private Integer orgParentId;

  @Column(name = "ORG_CODE")
  private String orgCode;

  @Column(name = "ORG_SHORT_NAME")
  private String orgShortName;

  @Column(name = "ORG_NAME")
  private String orgName;

  @Column(name = "ORG_SETTINGS_ID")
  private Integer orgSettingsId;

  @Column(name = "SALUT_CODE")
  private String salutCode;

  @Column(name = "USER_TYPE")
  private String userType;

  /**
   * Constructs the VUserDetail from basic information.
   * 
   * @param id the detail generated id
   * @param userId the user id
   * @param userName the user name
   * @param orgCode the customer number
   * @param saleOnBehalfOf the flag indicating that this user is configured as the account for
   *        "login on behalf".
   */
  public VUserDetail(final long id, final long userId, final String userName, final String orgCode,
      final boolean saleOnBehalfOf) {
    this.id = id;
    this.userId = userId;
    this.userName = userName;
    this.orgCode = orgCode;
    this.saleOnBehalfOf = saleOnBehalfOf;
  }

}
