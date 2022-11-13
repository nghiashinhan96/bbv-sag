package com.sagag.services.hazelcast.domain.order;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Singular;

import java.io.Serializable;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({ "id", "nr", "customerNr", "customerName", "saleName", "typeCode", "type",
    "typeDesc", "sendMethodCode", "sendMethod", "sendMethodDesc", "statusCode", "status",
    "statusDesc", "partialDelivery", "partialInvoice", "date", "links", "username", "vehicleInfos",
    "orderItems", "deliveryInformation", "totalPrice" })
public class OrderDetailDto implements Serializable {

  private static final long serialVersionUID = 6930224653184584389L;

  public static final String AGGREGRATION_ALL = "ALL";

  private Long id;

  private String nr;

  private Long customerNr;

  private String customerName;

  private String saleName;

  private String typeCode;

  private String type;

  private String typeDesc;

  private String sendMethodCode;

  private String sendMethod;

  private String sendMethodDesc;

  private String statusCode;

  private String status;

  private String statusDesc;

  private Boolean partialDelivery;

  private Boolean partialInvoice;

  private String date;

  private String username;

  private String invoiceTypeCode;

  private String reference;

  private String branchRemark;

  private Double totalPrice;

  private String goodsReceiverName;

  private boolean showPriceType;

  @Singular
  private List<String> vehicleInfos;

  @Singular
  private List<OrderItemDetailDto> orderItems;
}
