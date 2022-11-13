package com.sagag.eshop.service.dto.offer;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class OfferAddressDto implements Serializable {

  private static final long serialVersionUID = -3518451912866059777L;

  private Long id;

  private String city;

  private String countryiso;

  private Long personId;

  private String line1;

  private String line2;

  private String line3;

  private String state;

  private String zipcode;

  private String tecstate;

  private String erpId;

  private String poBox;

  private String type;

  @JsonIgnore
  private Long createdUserId;

  @JsonIgnore
  private Date createdDate;

  @JsonIgnore
  private Long modifiedUserId;

  @JsonIgnore
  private Date modifiedDate;

  @JsonIgnore
  private Integer version;

}
