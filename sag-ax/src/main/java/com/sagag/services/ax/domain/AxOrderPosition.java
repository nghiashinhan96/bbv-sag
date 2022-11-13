package com.sagag.services.ax.domain;

import java.io.Serializable;
import java.util.Map;

import org.springframework.hateoas.Link;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sagag.services.domain.sag.erp.ExternalOrderPosition;

import lombok.Data;

/**
 * Class to receive the order position from Dynamic AX ERP.
 *
 */
@Data
public class AxOrderPosition implements Serializable {

  private static final long serialVersionUID = 4734965370402979301L;

  private Integer sequence;

  private String articleId;

  private Integer quantity;

  private String status;

  @JsonProperty("_links")
  private Map<String, Link> links;

  public ExternalOrderPosition toExternalOrderPositionDto() {

    return ExternalOrderPosition.builder().sequence(this.sequence).articleId(this.articleId).quantity(this.quantity)
        .status(this.status).links(this.links).build();
  }

}
