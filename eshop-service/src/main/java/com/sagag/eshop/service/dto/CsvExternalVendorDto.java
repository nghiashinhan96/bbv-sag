package com.sagag.eshop.service.dto;

import com.opencsv.bean.CsvBindByName;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CsvExternalVendorDto implements Serializable {

  private static final long serialVersionUID = 7129360509774270438L;

  private Integer id;
  @CsvBindByName(column = "COUNTRY", required = true)
  private String country;

  @CsvBindByName(column = "SAG_ARTICLE_GROUP")
  private String sagArticleGroup;

  @CsvBindByName(column = "BRAND_ID")
  private Long brandId;

  @CsvBindByName(column = "VENDOR_ID")
  private Long vendorId;

  @CsvBindByName(column = "VENDOR_NAME")
  private String vendorName;

  @CsvBindByName(column = "VENDOR_PRIORITY", required = true)
  private Integer vendorPriority;

  @CsvBindByName(column = "DELIVERY_PROFILE_ID", required = true)
  private Integer deliveryProfileId;

  @CsvBindByName(column = "AVAILABILITY_TYPE_ID", required = true)
  private String availabilityTypeId;

  @CsvBindByName(column = "CREATED_BY")
  private Long createdUserId;

  @CsvBindByName(column = "MODIFIED_BY")
  private Long modifiedUserId;

}
