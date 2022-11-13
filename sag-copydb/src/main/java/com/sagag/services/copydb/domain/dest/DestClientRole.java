package com.sagag.services.copydb.domain.dest;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import lombok.Data;

/**
 * The persistent class for the CLIENT_ROLE database table.
 * 
 */
@Entity
@Table(name = "CLIENT_ROLE")
@NamedQuery(name = "DestClientRole.findAll", query = "SELECT c FROM DestClientRole c")
@Data
public class DestClientRole implements Serializable {

  private static final long serialVersionUID = 1355656509612834623L;

  @Id
  @Column(name = "ID")
  private long id;

  @Column(name = "CLIENT_ID")
  private Integer clientId;

  @Column(name = "ROLE_ID")
  private Integer roleId;

}
