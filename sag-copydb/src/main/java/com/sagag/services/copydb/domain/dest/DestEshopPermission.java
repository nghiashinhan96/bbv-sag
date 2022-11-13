package com.sagag.services.copydb.domain.dest;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import lombok.Data;

/**
 * The persistent class for the ESHOP_PERMISSION database table.
 * 
 */
@Entity
@Table(name = "ESHOP_PERMISSION")
@NamedQuery(name = "DestEshopPermission.findAll", query = "SELECT e FROM DestEshopPermission e")
@Data
public class DestEshopPermission implements Serializable {

  private static final long serialVersionUID = -8424576890774331581L;

  @Id
  @Column(name = "ID")
  private int id;

  @Column(name = "CREATED_BY")
  private Long createdBy;

  @Column(name = "CREATED_DATE")
  private Date createdDate;

  @Column(name = "DESCRIPTION")
  private String description;

  @Column(name = "MODIFIED_BY")
  private Long modifiedBy;

  @Column(name = "MODIFIED_DATE")
  private Date modifiedDate;

  @Column(name = "PERMISSION")
  private String permission;

  @Column(name = "PERMISSION_KEY")
  private String permissionKey;

  @Column(name = "[SORT]")
  private short sort;

}
