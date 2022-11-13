package com.sagag.services.domain.eshop.message.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SupportedAffiliateDto implements Serializable{

  private static final long serialVersionUID = -3466297554376707153L;

  private int id;

  private String companyName;

  private String shortName;
}
