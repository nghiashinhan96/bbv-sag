package com.sagag.services.hazelcast.request;

import com.sagag.services.domain.sag.erp.ArticleStock;

import lombok.Data;

import java.io.Serializable;

/**
 * Request body class for updating price for article.
 */
@Data
public class UpdateAmountRequestBody implements Serializable {

  private static final long serialVersionUID = -2431660521999650142L;

  private String pimId;
  private Integer amount;
  private String vehicleId;
  private ArticleStock stock;
  private Integer finalCustomerId;
  private Double totalAxStock;
}
