package com.sagag.services.copydb.domain.src;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import lombok.Data;

/**
 * The persistent class for the USER_ORDER_HISTORY database table.
 * 
 */
@Entity
@Table(name = "USER_ORDER_HISTORY")
@NamedQuery(name = "UserOrderHistory.findAll", query = "SELECT u FROM UserOrderHistory u")
@Data
public class UserOrderHistory implements Serializable {

  private static final long serialVersionUID = -3441418824496685297L;

  @Id
  @Column(name = "ID")
  private long id;

  @Column(name = "USER_ID")
  private Long userId;

  @Column(name = "ORDER_ID")
  private Long orderId;

}
