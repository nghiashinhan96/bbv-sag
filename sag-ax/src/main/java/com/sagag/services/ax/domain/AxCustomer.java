package com.sagag.services.ax.domain;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sagag.services.ax.domain.wint.WtGrantedBranch;
import com.sagag.services.ax.enums.CustomerBlockStatus;
import com.sagag.services.ax.utils.AxAddressUtils;
import com.sagag.services.common.enums.PriceDisplaySelection;
import com.sagag.services.common.enums.PriceDisplayStrategy;
import com.sagag.services.domain.sag.external.ContactInfo;
import com.sagag.services.domain.sag.external.CustomLink;
import com.sagag.services.domain.sag.external.Customer;
import com.sagag.services.domain.sag.external.CustomerApprovalType;
import com.sagag.services.domain.sag.external.GrantedBranch;

import lombok.Data;


/**
 * Class to receive the customer info from Dynamic AX ERP.
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

  private List<AxCustomerApprovalType> custApprovalTypes;

  @JsonProperty("customerGrantedBranchesForOrdering")
  private List<WtGrantedBranch> grantedBranches;

  private String disposalNumber;

  @JsonProperty("sagPriceTypeSelectionEnabled")
  private String priceDisplaySelection;

  @JsonProperty("sagGwsPriceDiscDetailsForNetPricingCalcPricePresentation")
  private String priceDisplayStrategy;

  @JsonProperty("_links")
  private Map<String, CustomLink> links;

  @JsonIgnore
  public Customer toCustomerDto() {
    final PriceDisplayStrategy priceDisplay =
        PriceDisplayStrategy.fromText(this.getPriceDisplayStrategy());

    // (#1407): Just adapt for temporary
    // Updated 20/09/2017: In AX the customer is found always active
    return Customer.builder()
        .nr(Long.valueOf(this.nr))
        .currency(this.currency)
        .companyName(this.name)
        .name(this.name)
        .vatNr(this.vatNr)
        .comment(this.comments)
        .language(this.language)
        .addressSalutation(this.addressSalutation)
        .axSendMethod(this.sendMethod)
        .axPaymentType(this.paymentType)
        .axInvoiceType(this.invoiceType)
        .axSalesOrderPool(salesOrderPool)
        .defaultBranchId(this.defaultBranchId)
        .links(this.links)
        .grantedBranches(grantedBranchesConverter().apply(this.grantedBranches))
        .salesGroup(salesGroup).category(kuKa)
        .termOfPayment(termOfPayment)
        .cashDiscount(cashDiscount)
        .salesRepPersonalNumber(salesRepPersonalNumber)
        .disposalNumber(this.disposalNumber)
        .phoneContacts(contactsConverter().apply(contacts, AxAddressUtils.PHONE_CONTACT_TYPE))
        .emailContacts(contactsConverter().apply(contacts, AxAddressUtils.EMAIL_CONTACT_TYPE))
        .faxContacts(contactsConverter().apply(contacts, AxAddressUtils.FAX_CONTACT_TYPE))
        .priceDisplayStrategy(priceDisplay)
        .priceDisplaySelection(PriceDisplaySelection.fromText(this.priceDisplaySelection))
        .custApprovalTypes(CollectionUtils.emptyIfNull(this.custApprovalTypes).stream()
            .map(p -> new CustomerApprovalType(p.getApprovalTypeName(), p.getCertificate()))
            .collect(Collectors.toList()))
        .build();
  }

  /**
   * Filters and update contact from AX ERP
   *
   */
  private static BiFunction<List<AxContact>, String, List<ContactInfo>> contactsConverter() {
    return (contacts, contactType) -> AxAddressUtils.findContactsByType(contacts, contactType)
        .stream().map(AxContact::toDto).collect(Collectors.toList());
  }

  private static Function<List<WtGrantedBranch>, List<GrantedBranch>> grantedBranchesConverter() {
    return wtGrantedBranches -> CollectionUtils.emptyIfNull(wtGrantedBranches).stream()
        .map(wtGrantedBranch -> GrantedBranch.builder().branchId(wtGrantedBranch.getBranchId())
            .orderingPriority(wtGrantedBranch.getOrderingPriority())
            .paymentMethodAllowed(wtGrantedBranch.getPaymentMethodAllowed()).build())
        .collect(Collectors.toList());
  }
}
