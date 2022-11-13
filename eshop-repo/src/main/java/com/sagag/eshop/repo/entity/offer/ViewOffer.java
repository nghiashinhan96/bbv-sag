package com.sagag.eshop.repo.entity.offer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "VIEW_OFFER")
public class ViewOffer implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  private Long id;

  private Long ownerId;

  private Long organisationId;

  private String offerNumber;

  private String customerName;

  private String ownerUsername;

  private String status;

  private Date offerDate;

  private Double totalGrossPrice;

  private String remark;

  private String vehicleDescriptions;
}
