package com.sagag.services.copydb.domain.dest;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import lombok.Data;

/**
 * The persistent class for the COLLECTION_PERMISSION database table.
 * 
 */
@Entity
@Table(name = "COLLECTION_PERMISSION")
@NamedQuery(name = "DestCollectionPermission.findAll", query = "SELECT c FROM DestCollectionPermission c")
@Data
public class DestCollectionPermission implements Serializable {

  private static final long serialVersionUID = -4861518619475973729L;

  @Id
  @Column(name = "ID")
  private int id;

  @Column(name = "ESHOP_PERMISSION_ID", nullable = false)
  private Integer eshopPermissionId;

  @Column(name = "COLLECTION_ID", nullable = false)
  private Integer collectionId;

}
