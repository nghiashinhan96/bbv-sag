package com.sagag.services.domain.sag.erp;

import java.io.Serializable;
import java.util.Map;

import org.springframework.hateoas.Link;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExternalOrderPosition implements Serializable {

  private static final long serialVersionUID = 1L;

  private Integer sequence;

  private String articleId;

  private Integer quantity;

  private String status;

  @JsonProperty("_links")
  private Map<String, Link> links;

}
