package com.sagag.services.domain.sag.financialcard;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FinancialCardDetailDto implements Serializable {

  private static final long serialVersionUID = 1L;

  private String documentNr;

  @JsonFormat(shape = JsonFormat.Shape.NUMBER)
  private Date postingDate;

  @JsonFormat(shape = JsonFormat.Shape.NUMBER)
  private Date dueDate;

  private String description;

  private String externalDocumentNr;

  private String salesperson;

  private Double remainingAmount;

  private Integer entryLinesNo;

  private List<FinancialCardEntryDto> entryLines;

  public Double getTotalAmountInclVAT() {
    if (CollectionUtils.isEmpty(entryLines)) {
      return NumberUtils.DOUBLE_ZERO;
    }
    return entryLines.stream().mapToDouble(entry -> entry.getAmountInclVAT()).sum();
  }

}
