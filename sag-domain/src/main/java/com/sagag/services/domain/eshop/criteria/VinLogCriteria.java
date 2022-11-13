package com.sagag.services.domain.eshop.criteria;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VinLogCriteria implements Serializable {

  private static final long serialVersionUID = -4785983783178520973L;

  private Integer id;

  private Long customerId;

  private Long userId;

  private String vin;

  private String vehicleId;

  private Long estimateID;

  private Integer errorCode;

  private Integer status;

}
