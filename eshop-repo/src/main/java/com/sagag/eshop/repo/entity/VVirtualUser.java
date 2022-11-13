package com.sagag.eshop.repo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Entity mapping to V_VIRTUAL_USER view.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "V_VIRTUAL_USER")
@Entity
public class VVirtualUser implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  private Long id;

  private Long originalUserId;

  private Integer setting;

  private String type;

  @Temporal(TemporalType.TIMESTAMP)
  private Date firstLoginDate;
}