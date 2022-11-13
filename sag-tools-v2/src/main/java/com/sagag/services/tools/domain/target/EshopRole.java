package com.sagag.services.tools.domain.target;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "ESHOP_ROLE")
@NamedQuery(name = "EshopRole.findAll", query = "SELECT r FROM EshopRole r")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(of = { "id", "description", "name" })
public class EshopRole implements Serializable {

  private static final long serialVersionUID = 4695323422893429401L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;


  private String description;

  private String name;

  @ManyToOne
  @JsonManagedReference
  @JoinColumn(name = "ROLE_TYPE_ID")
  private RoleType roleType;

  @OneToMany(mappedBy = "eshopRole")
  @JsonBackReference
  private List<GroupRole> groupRoles;

  @OneToMany(mappedBy = "eshopRole")
  @JsonManagedReference
  private List<RolePermission> rolePermissions;


}
