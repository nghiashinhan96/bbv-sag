package com.sagag.services.copydb.domain.src;

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
@NamedQuery(name = "MessageHiding.findAll", query = "SELECT m FROM MessageHiding m")
@Data
public class MessageHiding implements Serializable {

  private static final long serialVersionUID = -5456104473900755894L;

  @Id
  @Column(name = "ID")
  private long id;

  @Column(name = "MESSAGE_ID")
  private Long messageId;

  @Column(name = "USER_ID")
  private Long userId;

}
