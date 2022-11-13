package com.sagag.services.copydb.domain.src;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "GROUP_ROLE")
@NamedQuery(name = "GroupRole.findAll", query = "SELECT g FROM GroupRole g")
@Data
public class GroupRole implements Serializable {

  private static final long serialVersionUID = -5121058735241806922L;

  @Id
  @Column(name = "ID")
  private int id;

  @Column(name = "GROUP_ID")
  private Integer groupId;

  @Column(name = "ROLE_ID")
  private Integer roleId;

}
