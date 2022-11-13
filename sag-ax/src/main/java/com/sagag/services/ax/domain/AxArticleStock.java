package com.sagag.services.ax.domain;

import java.io.Serializable;
import java.util.Map;

import org.springframework.hateoas.Link;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sagag.services.domain.sag.erp.ArticleStock;

import lombok.Data;

/**
 * Class to receive the article stock from Dynamic AX ERP.
 *
 */
@Data
public class AxArticleStock implements Serializable {

  private static final long serialVersionUID = 3184135837940679155L;

  private Double stock;

  private String branchId;

  @JsonProperty("_links")
  private Map<String, Link> links;

  @JsonIgnore
  public ArticleStock toStockDto() {
    return ArticleStock.builder().stock(this.stock).branchId(this.branchId).build();
  }
}
