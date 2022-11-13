package com.sagag.services.ax.domain.financialcard;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.sagag.services.common.utils.SagBeanUtils;
import com.sagag.services.domain.sag.financialcard.FinancialCardDocDto;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Class to receive the financial card doc info from Dynamic AX ERP.
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AxFinancialCarDoc implements Serializable {

  private static final long serialVersionUID = 8763574118813294511L;

  private String customerNr;

  private String paymentMethod;

  private String documentType;

  private String documentNr;

  private String webOrderNr;

  private Date postingDate;

  private Date dueDate;

  private String paymentDeadlineNotification;

  private String status;
  
  private Integer documentCount;

  private Double remainingAmount;

  @JsonIgnore
  public FinancialCardDocDto toDto() {
    return SagBeanUtils.map(this, FinancialCardDocDto.class);
  }
}
