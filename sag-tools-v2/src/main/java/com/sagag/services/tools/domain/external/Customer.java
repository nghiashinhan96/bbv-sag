package com.sagag.services.tools.domain.external;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.annotation.Transient;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
//@formatter:off
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
      "links",
      "cashOrCreditTypeCode",
      "invoiceTypeCode",
      "sendMethodCode",
      "zipCode",
      "city",
      "disposalNumber"
    })
public class Customer implements Serializable {

  private static final long serialVersionUID = 4575232317801386772L;

  public static final String ADDRESS_KEY = "collection/addresses";
  public static final String PRICE_KEY = "collection/prices";
  public static final String AVAILABILITY_KEY = "collection/availabilities";
  public static final String ORDER_KEY = "collection/orders";

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
  private String defaultBranchId;

  private Double alreadyUsedCredit;
  private Double availableCredit;
  
  //#2705: axPaymentType always get from ax service, not save to connect's DB
  private String axPaymentType;

  private String axSendMethod;
  private String axInvoiceType;
  private String axSalesOrderPool;
  private String affiliateShortName;
  private String affiliateName;

  private String salesGroup;
  private String category;
  private String termOfPayment;
  private String cashDiscount;
  private String salesRepPersonalNumber;

  private CustomerBranch branch;
  private List<ContactInfo> phoneContacts;
  private List<ContactInfo> emailContacts;
  private List<ContactInfo> faxContacts;

  @Transient
  private String zipCode;

  @Transient
  private String city;

  private String disposalNumber;

  @JsonIgnore
  private boolean allowShowPfandArticle;

  @JsonProperty("_links")
  private Map<String, CustomLink> links;

  @JsonIgnore
  public String getAddressRelativeUrl() {
    return links.get(ADDRESS_KEY).getHref();
  }

  @JsonIgnore
  public String getPriceRelativeUrl() {
    return links.get(PRICE_KEY).getHref();
  }

  @JsonIgnore
  public String getAvailabilityRelativeUrl() {
    return links.get(AVAILABILITY_KEY).getHref();
  }

  @JsonIgnore
  public String getCreateOrderRelativeUrl() {
    return links.get(ORDER_KEY).getHref();
  }

  @JsonIgnore
  public boolean hasDisposalNumber() {
    return StringUtils.isNotBlank(disposalNumber);
  }

  @JsonIgnore
  public boolean allowShowPfandArticle() {
    return isAllowShowPfandArticle() && !hasDisposalNumber();
  }

  @JsonIgnore
  public List<String> getEmails() {
    return org.apache.commons.collections4.CollectionUtils.emptyIfNull(getEmailContacts()).stream()
        .map(ContactInfo::getValue).collect(Collectors.toList());
  }

  @JsonIgnore
  public List<String> getPhones() {
    return org.apache.commons.collections4.CollectionUtils.emptyIfNull(getPhoneContacts()).stream()
        .map(ContactInfo::getValue).collect(Collectors.toList());
  }
}
