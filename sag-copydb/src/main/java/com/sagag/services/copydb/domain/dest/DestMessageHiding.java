package com.sagag.services.copydb.domain.dest;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import lombok.Data;

/**
 * The persistent class for the MESSAGE_HIDING database table.
 * 
 */
@Entity
@Table(name = "MESSAGE_HIDING")
@NamedQuery(name = "DestMessageHiding.findAll", query = "SELECT m FROM DestMessageHiding m")
@Data
public class DestMessageHiding implements Serializable {

  private static final long serialVersionUID = 7305787197078546782L;

  @Id
  @Column(name = "ID")
  private long id;

  @Column(name = "MESSAGE_ID")
  private Long messageId;

  @Column(name = "USER_ID")
  private Long userId;

}
