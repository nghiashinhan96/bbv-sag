package com.sagag.services.domain.bo.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.sagag.services.domain.sag.external.Customer;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
@JsonPropertyOrder(
    {
      "nr",
      "companyId",
      "shortName",
      "companyName",
      "lastName",
      "name",
      "active",
      "vatNr",
      "comment",
      "languageId",
      "language",
      "organisationId",
      "organisationShort",
      "addressSalutation",
      "statusShort",
      "letterSalutation",
      "freeDeliveryEndDate",
      "currencyId",
      "currency",
      "oepUvpPrint",
      "sendMethod",
      "cashOrCreditTypeCode",
      "invoiceTypeCode",
      "sendMethodCode",
      "addressesLink",
      "defaultBranchId",
      "defaultBranchName"
    })
public class CustomerBODto {
  private long nr;
  private long companyId;
  private String shortName;
  private String companyName;
  private String lastName;
  private String name;
  private boolean active;
  private String vatNr;
  private String comment;
  private long languageId;
  private String language;
  private long organisationId;
  private String organisationShort;
  private String addressSalutation;
  private String statusShort;
  private String letterSalutation;
  private Date freeDeliveryEndDate;
  private long currencyId;
  private String currency;
  private boolean oepUvpPrint;
  private String sendMethod;
  private String cashOrCreditTypeCode;
  private String invoiceTypeCode;
  private String sendMethodCode;
  private String addressesLink;
  private String defaultBranchId;
  private String defaultBranchName;
  private String priceDisplayStrategy;
  private String priceDisplaySelection;

  public static CustomerBODto fromCustomer(Customer customer) {
    return CustomerBODto.builder()
        .nr(customer.getCustNrLong())
        .companyId(customer.getCompanyId())
        .shortName(customer.getShortName())
        .companyName(customer.getCompanyName())
        .lastName(customer.getLastName())
        .name(customer.getName())
        .active(customer.isActive())
        .vatNr(customer.getVatNr())
        .comment(customer.getComment())
        .languageId(customer.getLanguageId())
        .language(customer.getLanguage())
        .organisationId(customer.getOrganisationId())
        .organisationShort(customer.getOrganisationShort())
        .addressSalutation(customer.getAddressSalutation())
        .statusShort(customer.getStatusShort())
        .letterSalutation(customer.getLetterSalutation())
        .freeDeliveryEndDate(customer.getFreeDeliveryEndDate())
        .currencyId(customer.getCurrencyId())
        .currency(customer.getCurrency())
        .oepUvpPrint(customer.isOepUvpPrint())
        .sendMethod(customer.getSendMethod())
        .cashOrCreditTypeCode(customer.getCashOrCreditTypeCode())
        .invoiceTypeCode(customer.getInvoiceTypeCode())
        .sendMethodCode(customer.getSendMethodCode())
        .defaultBranchId(customer.getDefaultBranchId())
        .priceDisplayStrategy(customer.getPriceDisplayStrategy().getUiText())
        .priceDisplaySelection(customer.getPriceDisplaySelection().getValue())
        .build();
  }

}
