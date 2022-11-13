package com.sagag.services.ax.domain;

import java.io.Serializable;
import java.util.Map;

import org.springframework.hateoas.Link;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
 * Common class to receive common response from Dynamic AX ERP.
 *
 */
@Data
public class AxBaseResponse implements Serializable {

  private static final long serialVersionUID = -5524054756140698832L;

  @JsonProperty("_links")
  private Map<String, Link> links;

}
