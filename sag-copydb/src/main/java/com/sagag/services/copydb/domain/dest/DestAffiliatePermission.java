package com.sagag.services.copydb.domain.dest;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import lombok.Data;

/**
 * The persistent class for the AFFILIATE_PERMISSION database table.
 * 
 */
@Entity
@Table(name = "AFFILIATE_PERMISSION")
@NamedQuery(name = "DestAffiliatePermission.findAll", query = "SELECT a FROM DestAffiliatePermission a")
@Data
public class DestAffiliatePermission implements Serializable {

  private static final long serialVersionUID = 6271368354330791610L;

  @Id
  @Column(name = "ID")
  private int id;

  @Column(name = "SUPPORTED_AFFILIATE_ID")
  private int supportedAffiliateId;

  @Column(name = "ESHOP_PERMISSION_ID")
  private int eshopPermissionId;

}
