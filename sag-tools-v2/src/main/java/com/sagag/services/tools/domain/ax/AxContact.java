package com.sagag.services.tools.domain.ax;

import org.apache.commons.lang3.StringUtils;

import com.sagag.services.tools.domain.external.ContactInfo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Class to receive the contact info from Dynamic AX ERP.
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class AxContact extends AxBaseResponse {

  private static final long serialVersionUID = 2771621907427605730L;

  private String contactValue;

  private String contactDescription;

  /** Supported values : Phone, Email, URL, Telex, Fax. */
  private String contactType;

  private Boolean isPrimary;

  public ContactInfo toDto() {
    return ContactInfo.builder().isPrimary(isPrimary).value(contactValue)
        .description(contactDescription).type(StringUtils.upperCase(contactType)).build();
  }
}
