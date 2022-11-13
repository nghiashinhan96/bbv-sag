package com.sagag.services.service.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sagag.services.service.enums.BasketFilterEnum;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@JsonInclude(value = Include.NON_NULL)
public class BasketHistorySearchRequest implements Serializable {

  private static final long serialVersionUID = -2958109148207708293L;

  @JsonProperty("basket_filter_mode")
  private BasketFilterEnum basketFilterMode;

  @JsonProperty("basket_name")
  private String basketName;

  @JsonProperty("customer_number")
  private String customerNumber;

  @JsonProperty("customer_name")
  private String customerName;

  @JsonProperty("last_name")
  private String lastName;

  @JsonProperty("updated_date")
  private Date updatedDate;

  @JsonProperty("customer_ref_text")
  private String customerRefText;

  @JsonProperty("order_by_desc_customer_number")
  private Boolean orderByDescCustomerNumber;

  @JsonProperty("order_by_desc_customer_name")
  private Boolean orderByDescCustomerName;

  @JsonProperty("order_by_desc_last_name")
  private Boolean orderByDescLastName;

  @JsonProperty("order_by_desc_basket_name")
  private Boolean orderByDescBasketName;

  @JsonProperty("order_by_desc_updated_date")
  private Boolean orderByDescUpdatedDate;

  @JsonProperty("order_by_desc_grand_total_exl_vat")
  private Boolean orderByDescGrandTotalExcludeVat;

  @JsonProperty("order_by_desc_customer_ref_text")
  private Boolean orderByDescCustomerRefText;

  private int offset;

  private int size;

}
