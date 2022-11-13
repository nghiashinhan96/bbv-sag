package com.sagag.services.tools.domain.ax;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sagag.services.tools.domain.external.CustomLink;
import com.sagag.services.tools.domain.external.Customer;
import com.sagag.services.tools.support.CustomerBlockStatus;

import lombok.Data;

/**
 * Class to receive the customer info from Dynamic AX ERP.
 *
 */
@Data
public class AxCustomer implements Serializable {

  private static final long serialVersionUID = 4575232317801386772L;

  private String nr;

  private String name;

  private String vatNr;

  private String comments;

  private String language;

  private String addressSalutation;

  private CustomerBlockStatus blockedStatus;

  private String blockedReason;

  private String currency;

  private String sendMethod;

  private String paymentType;

  private String invoiceType;

  private String invoiceTypeDesc;

  private String defaultBranchId;

  private String salesOrderPool;

  private String salesGroup;

  /* The category of the customer (under PKZ_BAUM hierarchy - KuKas_DD/ KuKas_MA) */
  private String kuKa;

  private String salesRepPersonalNumber;

  private String termOfPayment;

  private String cashDiscount;

  private String letterCode;

  private List<AxContact> contacts;

  private String disposalNumber;

  @JsonProperty("_links")
  private Map<String, CustomLink> links;

  @JsonIgnore
  public Customer toCustomerDto() {
    // (#1407): Just adapt for temporary
    // Updated 20/09/2017: In AX the customer is found always active
    return Customer.builder()
        .nr(Long.valueOf(this.nr)).currency(this.currency)
        .companyName(this.name).name(this.name)
        .active(true).vatNr(this.vatNr).comment(this.comments)
        .language(this.language).addressSalutation(this.addressSalutation)
        .axSendMethod(this.sendMethod).axPaymentType(this.paymentType)
        .axInvoiceType(this.invoiceType).axSalesOrderPool(salesOrderPool)
        .defaultBranchId(this.defaultBranchId).links(this.links)
        .salesGroup(salesGroup).category(kuKa).termOfPayment(termOfPayment)
        .cashDiscount(cashDiscount).salesRepPersonalNumber(salesRepPersonalNumber)
        .disposalNumber(this.disposalNumber).build();
  }

}
