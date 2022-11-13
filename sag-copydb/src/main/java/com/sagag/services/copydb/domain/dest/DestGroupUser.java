package com.sagag.services.copydb.domain.dest;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import lombok.Data;

/**
 * The persistent class for the GROUP_USER database table.
 * 
 */
@Entity
@Table(name = "GROUP_USER")
@NamedQuery(name = "DestGroupUser.findAll", query = "SELECT g FROM DestGroupUser g")
@Data
public class DestGroupUser implements Serializable {

  private static final long serialVersionUID = -4627385714858573420L;

  @Id
  @Column(name = "ID")
  private int id;

  @Column(name = "GROUP_ID")
  private Integer groupId;

  @Column(name = "USER_ID")
  private Long userId;

}
