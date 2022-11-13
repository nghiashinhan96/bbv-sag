package com.sagag.services.tools.domain.target;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "ROLE_PERMISSION")
@NamedQuery(name = "RolePermission.findAll", query = "SELECT r FROM RolePermission r")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class RolePermission implements Serializable {

  private static final long serialVersionUID = 2816913568223795492L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @ManyToOne
  @JoinColumn(name = "PERM_ID")
  @JsonManagedReference
  private EshopPermission eshopPermission;

  @ManyToOne
  @JoinColumn(name = "ROLE_ID")
  @JsonBackReference
  private EshopRole eshopRole;

}
