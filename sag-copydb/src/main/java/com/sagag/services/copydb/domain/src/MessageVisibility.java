package com.sagag.services.copydb.domain.src;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import lombok.Data;

/**
 * The persistent class for the MESSAGE_VISIBILITY database table.
 * 
 */
@Entity
@Table(name = "MESSAGE_VISIBILITY")
@NamedQuery(name = "MessageVisibility.findAll", query = "SELECT m FROM MessageVisibility m")
@Data
public class MessageVisibility implements Serializable {

  private static final long serialVersionUID = 2015639714344531019L;

  @Id
  @Column(name = "ID")
  private int id;

  @Column(name = "DESCRIPTION")
  private String description;

  @Column(name = "VISIBILITY")
  private String visibility;

}
