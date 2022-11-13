package com.sagag.services.domain.sag.financialcard;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FinancialCardEntryDto implements Serializable {

  private static final long serialVersionUID = 1L;

  private Double sequence;

  private String type;

  private String nr;

  private String description;

  private String uoM;

  private Integer quantity;

  private Double unitPrice;

  private Double amountInclVAT;
}
