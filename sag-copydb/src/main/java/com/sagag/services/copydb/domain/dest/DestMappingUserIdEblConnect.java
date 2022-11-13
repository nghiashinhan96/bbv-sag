package com.sagag.services.copydb.domain.dest;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import lombok.Data;

/**
 * The persistent class for the MAPPING_USER_ID_EBL_CONNECT database table.
 * 
 */
@Entity
@Table(name = "MAPPING_USER_ID_EBL_CONNECT")
@NamedQuery(name = "DestMappingUserIdEblConnect.findAll", query = "SELECT m FROM DestMappingUserIdEblConnect m")
@Data
public class DestMappingUserIdEblConnect implements Serializable {

  private static final long serialVersionUID = -480733350758635813L;

  @Id
  @Column(name = "ID")
  private long id;

  @Column(name = "CONNECT_ORG_ID")
  private Integer connectOrgId;

  @Column(name = "CONNECT_USER_ID")
  private Long connectUserId;

  @Column(name = "EBL_ORG_ID")
  private Long eblOrgId;

  @Column(name = "EBL_USER_ID")
  private Long eblUserId;

}
