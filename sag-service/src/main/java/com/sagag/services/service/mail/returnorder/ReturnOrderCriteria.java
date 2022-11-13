package com.sagag.services.service.mail.returnorder;

import com.sagag.services.service.enums.ReturnOrderErrorType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReturnOrderCriteria {

  private String toEmail;

  private String username;

  private String affiliateEmail;

  private boolean isError;

  private List<String> orderNumbers;

  private String batchJobId;

  private ReturnOrderErrorType errorType;

  private String langiso;

}
