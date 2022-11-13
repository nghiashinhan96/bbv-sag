package com.sagag.services.copydb.domain.src;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import lombok.Data;

/**
 * The persistent class for the MESSAGE_TYPE database table.
 * 
 */
@Entity
@Table(name = "MESSAGE_TYPE")
@NamedQuery(name = "MessageType.findAll", query = "SELECT m FROM MessageType m")
@Data
public class MessageType implements Serializable {

  private static final long serialVersionUID = 1665448767071803098L;

  @Id
  @Column(name = "ID")
  private int id;

  @Column(name = "DESCRIPTION")
  private String description;

  @Column(name = "[TYPE]")
  private String type;

}
