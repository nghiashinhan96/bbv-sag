package com.sagag.eshop.repo.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@NamedQuery(name = "Organisation.findAll", query = "SELECT o FROM Organisation o")
@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Table(name = "ORGANISATION")
@ToString(of = { "id", "description", "name", "parentId", "shortname" })
public class Organisation implements Serializable {

  private static final long serialVersionUID = 5374541200236169781L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  private String description;

  private String name;

  @Column(name = "ORG_CODE")
  private String orgCode;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "ORDER_SETTINGS_ID", referencedColumnName = "ID")
  @JsonManagedReference
  private CustomerSettings customerSettings;

  @ManyToOne
  @JoinColumn(name = "ORGTYPE_ID", referencedColumnName = "ID")
  private OrganisationType organisationType;

  @Column(name = "PARENT_ID")
  private int parentId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "PARENT_ID", referencedColumnName = "ID", insertable = false,
      updatable = false)
  @JsonIgnore
  @Getter(value = AccessLevel.NONE)
  @Setter(value = AccessLevel.NONE)
  private Organisation parent;

  @OneToMany(mappedBy = "organisation")
  @JsonManagedReference
  private List<OrganisationAddress> organisationAddresses;

  @OneToMany(mappedBy = "organisation")
  @JsonBackReference
  private List<OrganisationGroup> organisationGroups;

  private String shortname;

  @OneToMany
  @JoinColumn(name = "CUSTOMER_NR", referencedColumnName = "ORG_CODE")
  private List<CouponUseLog> couponUseLog;

  public Organisation(String name, String orgCode, String shortname) {
    super();
    this.name = name;
    this.orgCode = orgCode;
    this.shortname = shortname;
  }

  /**
   * Constructs the Organisation from fields.
   *
   * @param id the id field
   * @param name the name field
   * @param orgCode the organisation code field
   * @param parentId the organisation parent id field
   * @param shortName the short name field
   */
  public Organisation(final int id, final String name, final String orgCode, final int parentId,
      final String shortName) {
    this.id = id;
    this.name = name;
    this.orgCode = orgCode;
    this.parentId = parentId;
    this.shortname = shortName;
  }

  public String getCustomerNrByOrg() {
    return StringUtils.defaultString(this.orgCode);
  }
}
