package com.sagag.services.tools.domain.target;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Table(name = "DELIVERY_TYPE")
@Entity
@NamedQuery(name = "DeliveryType.findAll", query = "SELECT a FROM DeliveryType a")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryType implements Serializable {

  private static final long serialVersionUID = -3506928523760404340L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @Column(name = "DESC_CODE", nullable = false)
  private String descCode;

  private String type;

  private String description;

}
