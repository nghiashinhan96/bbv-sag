package com.sagag.services.tools.domain.target;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@NamedQuery(name = "RoleType.findAll", query = "SELECT r FROM RoleType r")
@Data
@Table(name = "ROLE_TYPE")
@ToString(of = { "id", "description", "name" })
public class RoleType implements Serializable {

  private static final long serialVersionUID = 5617182048313091031L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  private String description;

  private String name;

  @OneToMany(mappedBy = "roleType")
  @JsonBackReference
  private List<EshopRole> eshopRoles;

}
