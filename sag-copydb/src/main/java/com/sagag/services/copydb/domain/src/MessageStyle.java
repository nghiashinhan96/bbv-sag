package com.sagag.services.copydb.domain.src;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import lombok.Data;

/**
 * The persistent class for the MESSAGE_STYLE database table.
 * 
 */
@Entity
@Table(name = "MESSAGE_STYLE")
@NamedQuery(name = "MessageStyle.findAll", query = "SELECT m FROM MessageStyle m")
@Data
public class MessageStyle implements Serializable {

  private static final long serialVersionUID = -7597853475269471107L;

  @Id
  @Column(name = "ID")
  private int id;

  @Column(name = "DESCRIPTION")
  private String description;

  @Column(name = "STYLE")
  private String style;

}
