package com.sagag.services.domain.eshop.dto.organisation;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sagag.services.common.utils.SagBeanUtils;
import com.sagag.services.domain.sag.erp.Address;
import com.sagag.services.domain.sag.external.Customer;

import lombok.Data;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.Date;
import java.util.Optional;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EshopCustomerDto implements Serializable {

  private static final long serialVersionUID = -7077592180709819363L;

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
  private boolean showNetPrice;
  private String cashOrCreditTypeCode;
  private String invoiceTypeCode;
  private String sendMethodCode;
  private String postCode;
  private String city;
  private boolean isExisted;

  public static EshopCustomerDto copyFrom(Customer customer,
      Optional<Address> defAddress) {
    Assert.notNull(customer, "The given customer must not be null");
    EshopCustomerDto eshopCustomer = SagBeanUtils.map(customer, EshopCustomerDto.class);
    defAddress.ifPresent(address -> {
      eshopCustomer.setPostCode(address.getPostCode());
      eshopCustomer.setCity(address.getCity());
    });
    return eshopCustomer;
  }

  public String getCustomerInfo() {
    final StringBuilder sb = new StringBuilder();
    sb.append(nr);
    if (!StringUtils.isBlank(companyName)) {
      sb.append(" - ").append(companyName);
    }
    if (!StringUtils.isBlank(postCode)) {
      sb.append(", ").append(postCode);
    }
    if (!StringUtils.isBlank(city)) {
      sb.append(StringUtils.SPACE).append(city);
    }
    return sb.toString();
  }
}
