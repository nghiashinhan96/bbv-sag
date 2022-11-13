package com.sagag.services.ax.domain;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.hateoas.ResourceSupport;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Class to receive the bulk article stocks result from Dynamic AX ERP.
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class AxBulkArticleStockResourceSupport extends ResourceSupport implements Serializable {

  private static final long serialVersionUID = 2500278942595678738L;

  // Successful response provides a list of article stocks representations.
  @JsonProperty("articleStock")
  private List<AxStock> stocks;

  @JsonIgnore
  public boolean hasStocks() {
    return CollectionUtils.isNotEmpty(this.stocks);
  }

}
