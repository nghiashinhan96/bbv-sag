package com.sagag.services.ax.domain;

import com.sagag.services.article.api.domain.customer.CreditLimitInfo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * POJO class for credit limit info.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class AxCreditLimitInfo extends CreditLimitInfo {

  private static final long serialVersionUID = 1464926580896526734L;
}
