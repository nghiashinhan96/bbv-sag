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
 * The persistent class for the [MESSAGE] database table.
 * 
 */
@Entity
@Table(name = "[MESSAGE]")
@NamedQuery(name = "DestMessage.findAll", query = "SELECT m FROM DestMessage m")
@Data
public class DestMessage implements Serializable {

  private static final long serialVersionUID = 3960167060466474044L;

  @Id
  @Column(name = "ID")
  private long id;

  @Column(name = "ACTIVE")
  private Boolean active;

  @Column(name = "CREATED_DATE")
  private Date createdDate;

  @Column(name = "CREATED_USER_ID")
  private Long createdUserId;

  @Column(name = "DATE_VALID_FROM")
  private Date dateValidFrom;

  @Column(name = "DATE_VALID_TO")
  private Date dateValidTo;

  @Column(name = "MESSAGE_ACCESS_RIGHT_ID")
  private Integer messageAccessRightId;

  @Column(name = "MESSAGE_LOCATION_ID")
  private Integer messageLocationId;

  @Column(name = "MESSAGE_STYLE_ID")
  private Integer messageStyleId;

  @Column(name = "MESSAGE_SUB_AREA_ID")
  private Integer messageSubAreaId;

  @Column(name = "MESSAGE_TYPE_ID")
  private Integer messageTypeId;

  @Column(name = "MESSAGE_VISIBILITY_ID")
  private Integer messageVisibilityId;

  @Column(name = "MODIFIED_DATE")
  private Date modifiedDate;

  @Column(name = "MODIFIED_USER_ID")
  private Long modifiedUserId;

  @Column(name = "TITLE")
  private String title;

}
