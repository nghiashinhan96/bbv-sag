package com.sagag.services.ivds.request;

import com.sagag.services.domain.sag.erp.ArticleStock;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Availability request item.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArticleInformationRequestItem implements Serializable {

  private static final long serialVersionUID = -8730165266822795979L;

  private String idPim;
  private int quantity;
  private ArticleStock stock;
  private Double totalAxStock;

}
