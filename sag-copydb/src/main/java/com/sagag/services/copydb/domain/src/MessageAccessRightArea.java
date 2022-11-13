package com.sagag.services.copydb.domain.src;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import lombok.Data;

/**
 * The persistent class for the MESSAGE_ACCESS_RIGHT_AREA database table.
 * 
 */
@Entity
@Table(name = "MESSAGE_ACCESS_RIGHT_AREA")
@NamedQuery(name = "MessageAccessRightArea.findAll", query = "SELECT m FROM MessageAccessRightArea m")
@Data
public class MessageAccessRightArea implements Serializable {

  private static final long serialVersionUID = -4992049828161136183L;

  @Id
  @Column(name = "ID")
  private int id;

  @Column(name = "MESSAGE_ACCESS_RIGHT_ID")
  private Integer messageAccessRightId;

  @Column(name = "MESSAGE_AREA_ID")
  private Integer messageAreaId;

}
