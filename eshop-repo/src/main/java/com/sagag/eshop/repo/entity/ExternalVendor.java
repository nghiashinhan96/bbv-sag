package com.sagag.eshop.repo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "EXTERNAL_VENDOR")
public class ExternalVendor implements Serializable {

  private static final long serialVersionUID = 4232507515885088424L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  private String country;

  private String sagArticleGroup;
  private Long brandId;
  private Long vendorId;
  private String vendorName;
  private Integer vendorPriority;
  private Integer deliveryProfileId;
  private String availabilityTypeId;

  private Long createdUserId;
  private Date createdDate;
  private Long modifiedUserId;
  private Date modifiedDate;

  public static class ExternalVendorBuilder {
    public ExternalVendorBuilder createdDate() {
      this.createdDate = Calendar.getInstance().getTime();
      return this;
    }
  }
}
