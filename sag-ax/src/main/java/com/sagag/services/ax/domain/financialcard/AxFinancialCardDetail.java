package com.sagag.services.ax.domain.financialcard;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.sagag.services.common.utils.SagBeanUtils;
import com.sagag.services.domain.sag.financialcard.FinancialCardDetailDto;

import lombok.Data;

/**
 * Class to receive the financial card detail info from Dynamic AX ERP.
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AxFinancialCardDetail implements Serializable {

  private static final long serialVersionUID = 1L;

  private String documentNr;

  private Date postingDate;

  private Date dueDate;

  private String description;

  private String externalDocumentNr;

  private String salesperson;

  private Double remainingAmount;

  private Integer entryLinesNo;

  private List<AxFinancialCardEntry> entryLines;

  @JsonIgnore
  public FinancialCardDetailDto toDto() {
    return SagBeanUtils.map(this, FinancialCardDetailDto.class);
  }

}
