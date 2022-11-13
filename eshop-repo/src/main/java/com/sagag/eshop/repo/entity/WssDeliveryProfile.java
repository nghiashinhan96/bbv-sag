package com.sagag.eshop.repo.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "WSS_DELIVERY_PROFILE")
public class WssDeliveryProfile implements Serializable {

  private static final long serialVersionUID = -2864988238200675312L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  private Integer orgId;

  private String name;

  private String description;

  @OneToMany(mappedBy = "wssDeliveryProfile", cascade = CascadeType.ALL)
  private List<WssDeliveryProfileTours> wssDeliveryProfileTours;

  @ManyToOne
  @JoinColumn(name = "WSS_BRANCH_ID")
  @JsonBackReference
  @NotNull
  private WssBranch wssBranch;

}
