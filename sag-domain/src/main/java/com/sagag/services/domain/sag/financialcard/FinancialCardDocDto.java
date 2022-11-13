package com.sagag.services.domain.sag.financialcard;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

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
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FinancialCardDocDto implements Serializable {

  private static final long serialVersionUID = -649695330910390463L;

  private String customerNr;

  private String paymentMethod;

  private String documentType;

  private String documentNr;

  private String webOrderNr;

  @JsonFormat(shape = JsonFormat.Shape.NUMBER)
  private Date postingDate;

  @JsonFormat(shape = JsonFormat.Shape.NUMBER)
  private Date dueDate;

  private String paymentDeadlineNotification;

  private String status;

  private Double remainingAmount;
}
