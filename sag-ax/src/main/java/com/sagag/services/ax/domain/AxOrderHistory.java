package com.sagag.services.ax.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.sagag.services.domain.sag.erp.ExternalOrderDetail;
import com.sagag.services.domain.sag.erp.ExternalOrderHistory;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.hateoas.Link;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Class to receive the order history info from Dynamic AX ERP.
 *
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "links", "orders" })
public class AxOrderHistory implements Serializable {

  private static final long serialVersionUID = 6257782038843572987L;

  private List<AxOrderDetail> orders;

  @JsonProperty("_links")
  private Map<String, Link> links;

  @JsonIgnore
  public boolean hasOrders() {
    return CollectionUtils.isNotEmpty(this.orders);
  }

  public ExternalOrderHistory toExternalOrderHistoryDto() {
    List<ExternalOrderDetail> externalOrderDetails = new ArrayList<>();
    if (hasOrders()) {
      externalOrderDetails.addAll(orders.stream()
          .map(AxOrderDetail::toExternalOrderDetailDto).collect(Collectors.toList()));
    }
    return ExternalOrderHistory.builder().orders(externalOrderDetails).links(this.links).build();
  }
}
