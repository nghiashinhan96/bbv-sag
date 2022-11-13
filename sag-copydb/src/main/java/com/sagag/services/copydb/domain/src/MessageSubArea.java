package com.sagag.services.copydb.domain.src;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import lombok.Data;

/**
 * The persistent class for the MESSAGE_SUB_AREA database table.
 * 
 */
@Entity
@Table(name = "MESSAGE_SUB_AREA")
@NamedQuery(name = "MessageSubArea.findAll", query = "SELECT m FROM MessageSubArea m")
@Data
public class MessageSubArea implements Serializable {

  private static final long serialVersionUID = 7437593533361589338L;

  @Id
  @Column(name = "ID")
  private int id;

  @Column(name = "DESCRIPTION")
  private String description;

  @Column(name = "MESSAGE_AREA_ID")
  private Integer messageAreaId;

  @Column(name = "[SORT]")
  private short sort;

  @Column(name = "SUB_AREA")
  private String subArea;

}
