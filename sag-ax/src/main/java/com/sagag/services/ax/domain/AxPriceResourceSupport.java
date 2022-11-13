package com.sagag.services.ax.domain;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.hateoas.ResourceSupport;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Class to receive the list of article prices from Dynamic AX ERP.
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class AxPriceResourceSupport extends ResourceSupport implements Serializable {

  private static final long serialVersionUID = 4386111847592607287L;

  private List<AxPriceWithArticle> prices;

  @JsonIgnore
  public boolean hasPrices() {
    return CollectionUtils.isNotEmpty(this.prices);
  }

}
