package com.sagag.services.copydb.domain.dest;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import lombok.Data;

/**
 * The persistent class for the RETURN_ORDER_REASON database table.
 * 
 */
@Entity
@Table(name = "RETURN_ORDER_REASON")
@NamedQuery(name = "DestReturnOrderReason.findAll", query = "SELECT r FROM DestReturnOrderReason r")
@Data
public class DestReturnOrderReason implements Serializable {

  private static final long serialVersionUID = 4723382020114171851L;

  @Id
  @Column(name = "ID")
  private int id;

  @Column(name = "AX_CODE")
  private String axCode;

  @Column(name = "CODE")
  private String code;

  @Column(name = "IS_DEFAULT")
  private Boolean isDefault;

  @Column(name = "NAME")
  private String name;

}
