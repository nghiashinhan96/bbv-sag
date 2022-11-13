package com.sagag.eshop.repo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "V_EXTERNAL_VENDOR")
public class VExternalVendor {

  @Id
  private Integer id;
  private String country;
  private Long vendorId;
  private String vendorName;
  private Integer vendorPriority;
  private String deliveryProfileName;
  private String availabilityTypeId;

}
