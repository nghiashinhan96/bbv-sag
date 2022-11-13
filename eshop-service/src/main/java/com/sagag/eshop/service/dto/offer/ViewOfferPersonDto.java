package com.sagag.eshop.service.dto.offer;

import com.sagag.eshop.repo.entity.offer.ViewOfferPerson;

import lombok.Data;
import lombok.NoArgsConstructor;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

/**
 *
 */
@Data
@NoArgsConstructor
public class ViewOfferPersonDto implements Serializable {

  private static final long serialVersionUID = -5779700912015903240L;

  private Long id;

  private String displayName;

  private String companyName;

  private String firstName;

  private String lastName;

  private String road;

  private String postCode;

  private String place;

  private String phone;

  private String email;

  private String fax;

  private String salutation;

  public ViewOfferPersonDto(final ViewOfferPerson offerPerson) {
    this.id = offerPerson.getId();

    this.displayName = StringUtils.defaultString(offerPerson.getDisplayName());
    this.companyName = StringUtils.defaultString(offerPerson.getCompanyName());
    this.firstName = StringUtils.defaultString(offerPerson.getFirstName());
    this.lastName = StringUtils.defaultString(offerPerson.getLastName());

    this.road = StringUtils.defaultString(offerPerson.getLine1());
    this.postCode = StringUtils.defaultString(offerPerson.getZipcode());
    this.place = StringUtils.defaultString(offerPerson.getCity());

    this.phone = StringUtils.defaultString(offerPerson.getPhone());
    this.fax = StringUtils.defaultString(offerPerson.getFax());
    this.email = StringUtils.defaultString(offerPerson.getEmail());
    this.salutation = StringUtils.defaultString(offerPerson.getSalutation());
  }
}
