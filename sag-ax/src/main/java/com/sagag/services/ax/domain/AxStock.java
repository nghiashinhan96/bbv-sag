package com.sagag.services.ax.domain;

import java.io.Serializable;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
/**
 * Class to receive the article stock result from Dynamic AX ERP.
 *
 */
@Data
public class AxStock implements Serializable {

  private static final long serialVersionUID = -1230903826388467441L;

  private AxArticleStock stock;

  private String articleId;

  @JsonIgnore
  public boolean hasStock() {
    return !Objects.isNull(this.stock) && this.stock.getStock() > 0;
  }

}
