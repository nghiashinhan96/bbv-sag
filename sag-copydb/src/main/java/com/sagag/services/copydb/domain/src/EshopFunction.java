package com.sagag.services.copydb.domain.src;

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
@NamedQuery(name = "EshopFunction.findAll", query = "SELECT e FROM EshopFunction e")
@Data
public class EshopFunction implements Serializable {

  private static final long serialVersionUID = 9046505723131833955L;

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
