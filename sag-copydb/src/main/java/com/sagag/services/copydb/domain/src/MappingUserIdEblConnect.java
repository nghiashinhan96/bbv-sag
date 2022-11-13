package com.sagag.services.copydb.domain.src;

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
@NamedQuery(name = "MappingUserIdEblConnect.findAll", query = "SELECT m FROM MappingUserIdEblConnect m")
@Data
public class MappingUserIdEblConnect implements Serializable {

  private static final long serialVersionUID = 6614785026028386195L;

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
