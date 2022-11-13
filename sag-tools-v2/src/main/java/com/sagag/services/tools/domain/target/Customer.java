package com.sagag.services.tools.domain.target;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.sagag.services.tools.domain.external.Contact;
import com.sagag.services.tools.domain.external.CustomLink;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.data.annotation.Transient;

import java.io.Serializable;
import java.util.Collections;
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
      "showDiscount",
      "showNetPrice",
      "cashOrCreditTypeCode",
      "invoiceTypeCode",
      "sendMethodCode",
      "contacts"
    })
// @formatter:on
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
  private boolean showDiscount;
  private boolean showNetPrice;
  private String cashOrCreditTypeCode;
  private String invoiceTypeCode;
  private String sendMethodCode;

  @JsonIgnore
  private List<Contact> contacts;

  @Transient
  private String zipCode;

  @Transient
  private String city;

  @JsonProperty("_links")
  private Map<String, CustomLink> links;

  @JsonIgnore
  public List<Contact> getPhoneContacts() {
    if (CollectionUtils.isEmpty(contacts)) {
      return Collections.emptyList();
    }

    return contacts.stream().filter(c -> Contact.TYPE_PHONE.equalsIgnoreCase(c.getContactType()))
        .collect(Collectors.toList());
  }

  @JsonIgnore
  public List<Contact> getFaxContacts() {
    if (CollectionUtils.isEmpty(contacts)) {
      return Collections.emptyList();
    }

    return contacts.stream().filter(c -> Contact.TYPE_FAX.equalsIgnoreCase(c.getContactType()))
        .collect(Collectors.toList());
  }

  @JsonIgnore
  public List<Contact> getEmailContacts() {
    if (CollectionUtils.isEmpty(contacts)) {
      return Collections.emptyList();
    }

    return contacts.stream().filter(c -> Contact.TYPE_EMAIL.equalsIgnoreCase(c.getContactType()))
        .collect(Collectors.toList());
  }
}
