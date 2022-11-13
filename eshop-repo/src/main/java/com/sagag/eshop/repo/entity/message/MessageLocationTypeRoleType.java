package com.sagag.eshop.repo.entity.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Entity mapping class to table MESSAGE_LOCATION_TYPE_ROLE_TYPE.
 */
@Entity
@Table(name = "MESSAGE_LOCATION_TYPE_ROLE_TYPE")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageLocationTypeRoleType implements Serializable {

  private static final long serialVersionUID = -7889966530649864366L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @ManyToOne
  @JoinColumn(name = "MESSAGE_LOCATION_TYPE_ID", nullable = false)
  private MessageLocationType messageLocationType;

  @ManyToOne
  @JoinColumn(name = "MESSAGE_ROLE_TYPE_ID", nullable = false)
  private MessageRoleType messageRoleType;
}
