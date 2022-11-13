package com.sagag.services.domain.sag.erp;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.hateoas.Link;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Builder
@AllArgsConstructor
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "links", "orders" })
@NoArgsConstructor
public class ExternalOrderHistory implements Serializable {

  private static final long serialVersionUID = 6257782038843572987L;

  @JsonProperty("_links")
  private Map<String, Link> links;

  private List<ExternalOrderDetail> orders;

  public boolean hasNext() {
    return !Objects.isNull(links) && links.containsKey(Link.REL_NEXT);
  }
  public String next() {
    return links.get(Link.REL_NEXT).getHref();
  }
}
