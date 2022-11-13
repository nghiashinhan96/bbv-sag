package com.sagag.eshop.repo.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.sagag.services.domain.eshop.common.EshopUserCreateAuthority;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Table(name = "ESHOP_GROUP")
@Entity
@NamedQuery(name = "EshopGroup.findAll", query = "SELECT e FROM EshopGroup e")
@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@ToString(of = { "id", "description", "name" })
public class EshopGroup implements Serializable {

  private static final long serialVersionUID = -4293605382650853514L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  private String description;

  private String name;

  @OneToMany(mappedBy = "eshopGroup")
  @JsonManagedReference
  private List<GroupPermission> groupPermissions;

  @OneToMany(mappedBy = "eshopGroup")
  @JsonManagedReference
  private List<GroupRole> groupRoles;

  @OneToMany(mappedBy = "eshopGroup")
  @JsonBackReference
  private List<GroupUser> groupUsers;

  @OneToMany(mappedBy = "eshopGroup")
  @JsonManagedReference
  private List<OrganisationGroup> organisationGroups;

  public static EshopGroup buildGroupBy(Organisation customer, EshopUserCreateAuthority authority) {
    Assert.notNull(customer, "The given customer must not be null");
    final EshopGroup eshopGroupAdmin = new EshopGroup();
    eshopGroupAdmin.setName(authority.getGroupName(customer.getOrgCode()));
    eshopGroupAdmin.setDescription(authority.getGroupDesc(customer.getDescription()));
    return eshopGroupAdmin;
  }

}
