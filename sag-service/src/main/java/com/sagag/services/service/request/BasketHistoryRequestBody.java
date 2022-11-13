package com.sagag.services.service.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

import java.io.Serializable;

/**
 * Request body class for create basket history.
 */
@Data
@JsonInclude(value = Include.NON_NULL)
public class BasketHistoryRequestBody implements Serializable {

  private static final long serialVersionUID = -8167956708251474573L;

  private String basketName;

  private Boolean netPriceViewInContext;

  private String customerRefText;

}
