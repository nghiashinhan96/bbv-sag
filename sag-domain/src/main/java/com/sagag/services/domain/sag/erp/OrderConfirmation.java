package com.sagag.services.domain.sag.erp;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sagag.services.domain.eshop.dto.GrantedBranchDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.hateoas.Link;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderConfirmation implements Serializable {

  private static final long serialVersionUID = 798486424438671125L;

  // Number of created order in case of successful order placing.
  private String orderNr;

  // The basket number of underlying ERP system, which belongs to the order.
  private Long frontEndBasketNr;

  private String axOrderURL;

  private List<String> workIds;

  private Long orderHistoryId;

  @JsonProperty("_links")
  private Map<String, Link> links;

  private List<String> cartKeys;

  private OrderErrorMessage errorMsg;

  private String warningMsgCode;

  private String orderType;

  private double subTotalWithNet;

  private double vatTotalWithNet;

  private String orderExecutionType;

  private GrantedBranchDto location;

  private List<OrderAvailabilityResponse> orderAvailabilities;

}
