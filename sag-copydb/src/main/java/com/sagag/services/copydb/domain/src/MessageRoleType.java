package com.sagag.services.copydb.domain.src;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import lombok.Data;

/**
 * The persistent class for the MESSAGE_ROLE_TYPE database table.
 * 
 */
@Entity
@Table(name = "MESSAGE_ROLE_TYPE")
@NamedQuery(name = "MessageRoleType.findAll", query = "SELECT m FROM MessageRoleType m")
@Data
public class MessageRoleType implements Serializable {

  private static final long serialVersionUID = -4746814104213356736L;

  @Id
  @Column(name = "ID")
  private int id;

  @Column(name = "DESCRIPTION")
  private String description;

  @Column(name = "ROLE_TYPE")
  private String roleType;

}
