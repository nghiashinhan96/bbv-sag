package com.sagag.services.domain.eshop.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NextWorkingDates implements Serializable{

  private static final long serialVersionUID = -7578812342821100983L;

  Date backorderDate;
  Date noBackorderDate;
}
