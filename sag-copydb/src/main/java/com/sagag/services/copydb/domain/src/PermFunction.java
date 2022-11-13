package com.sagag.services.copydb.domain.src;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import lombok.Data;

/**
 * The persistent class for the PERM_FUNCTION database table.
 * 
 */
@Entity
@Table(name = "PERM_FUNCTION")
@NamedQuery(name = "PermFunction.findAll", query = "SELECT p FROM PermFunction p")
@Data
public class PermFunction implements Serializable {

  private static final long serialVersionUID = 1981764161929529802L;

  @Id
  @Column(name = "ID")
  private int id;

  @Column(name = "PERM_ID")
  private Integer permId;

  @Column(name = "FUNCTION_ID")
  private Integer functionId;

}
