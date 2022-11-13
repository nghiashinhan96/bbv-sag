package com.sagag.services.tools.domain.target;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * Permission class.
 */
@Table(name = "ESHOP_PERMISSION")
@Entity
@NamedQuery(name = "EshopPermission.findAll", query = "SELECT e FROM EshopPermission e")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EshopPermission implements Serializable {

  private static final long serialVersionUID = 3232677817638761574L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  private String description;

  private String permission;

  @Column(name = "CREATED_BY")
  private Long createdBy; // the person id who created this permission

  @Column(name = "MODIFIED_BY")
  private Long modifiedBy; // the person id who modified this permission

  @OneToMany(mappedBy = "eshopPermission")
  @JsonManagedReference
  private List<PermFunction> permFunctions;

  @OneToMany(mappedBy = "eshopPermission")
  @JsonBackReference
  private List<RolePermission> rolePermissions;

  @OneToMany(mappedBy = "eshopPermission")
  @JsonBackReference
  private List<GroupPermission> groupPermissions;

  @Column(name = "PERMISSION_KEY")
  private String permissisonKey;
}
