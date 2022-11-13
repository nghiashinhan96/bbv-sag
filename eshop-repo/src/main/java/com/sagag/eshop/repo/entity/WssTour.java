package com.sagag.eshop.repo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * Entity of WssTour table.
 *
 */
@Builder
@Data
@Entity
@Table(name = "WSS_TOUR")
@NoArgsConstructor
@AllArgsConstructor
public class WssTour implements Serializable {

  private static final long serialVersionUID = 189666912329216119L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "ORG_ID", nullable = false)
  private Integer orgId;

  @Column(name = "NAME", nullable = false)
  private String name;

  @OneToMany(mappedBy = "wssTour", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private List<WssTourTimes> wssTourTimes;

  @OneToMany(mappedBy = "wssTour")
  private List<WssDeliveryProfileTours> wssDeliveryProfileTours;
}
