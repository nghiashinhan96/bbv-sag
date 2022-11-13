package com.sagag.services.copydb.domain.dest;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import lombok.Data;

/**
 * The persistent class for the ESHOP_FUNCTION database table.
 * 
 */
@Entity
@Table(name = "ESHOP_FUNCTION")
@NamedQuery(name = "DestEshopFunction.findAll", query = "SELECT e FROM DestEshopFunction e")
@Data
public class DestEshopFunction implements Serializable {

  private static final long serialVersionUID = -3127228119748340067L;

  @Id
  @Column(name = "ID")
  private int id;

  @Column(name = "DESCRIPTION")
  private String description;

  @Column(name = "FUNCTION_NAME")
  private String functionName;

  @Column(name = "RELATIVE_URL")
  private String relativeUrl;

}
