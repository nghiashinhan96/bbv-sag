package com.sagag.eshop.service.dto.offer;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sagag.eshop.repo.entity.offer.OfferAddress;
import com.sagag.eshop.repo.entity.offer.OfferPerson;
import com.sagag.eshop.repo.entity.offer.OfferPersonProperty;
import com.sagag.services.common.enums.offer.OfferPersonPropertyType;

import lombok.Data;
import lombok.NoArgsConstructor;

import org.apache.commons.collections.CollectionUtils;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import javax.validation.ValidationException;

/**
 *
 */
@Data
@NoArgsConstructor
public class OfferPersonDto implements Serializable {

  private static final long serialVersionUID = -5779700912015903240L;

  private Long id;

  private String salutation;

  private String companyName;

  private String firstName;

  private String lastName;

  private String road;

  private String additionalAddress1;

  private String additionalAddress2;

  private String postCode;

  @JsonIgnore
  private String poBox;

  private String place;

  private String phone;

  private String email;

  private String fax;

  @JsonIgnore
  private String type;

  @JsonIgnore
  private Long currentUserId;

  @JsonIgnore
  private Integer organisationId;

  public OfferPersonDto(final OfferPerson offerPerson) {
    final OfferAddress offerAddress = offerPerson.getOfferAddress();
    if (Objects.isNull(offerAddress)) {
      throw new ValidationException("Recipient address is empty");
    }
    this.id = offerPerson.getId();
    this.road = offerAddress.getLine1();
    this.additionalAddress1 = offerAddress.getLine2();
    this.additionalAddress2 = offerAddress.getLine3();
    this.postCode = offerAddress.getZipcode();
    this.poBox = offerAddress.getPoBox();
    this.place = offerAddress.getCity();

    this.companyName = offerPerson.getOfferCompanyName();
    this.firstName = offerPerson.getFirstName();
    this.lastName = offerPerson.getLastName();

    this.type = offerPerson.getType();
    this.email = offerPerson.getEmail();

    final Set<OfferPersonProperty> properties = offerPerson.getProperties();
    if (CollectionUtils.isEmpty(properties)) {
      return;
    }
    // Salutation
    final Optional<OfferPersonProperty> salutaion = properties.stream()
        .filter(
            property -> OfferPersonPropertyType.SALUTATION.getValue().equals(property.getType()))
        .findFirst();
    salutaion.ifPresent(obj -> this.salutation = obj.getValue());

    final Optional<OfferPersonProperty> phoneOpt = properties.stream()
        .filter(property -> OfferPersonPropertyType.PHONE.getValue().equals(property.getType()))
        .findFirst();
    phoneOpt.ifPresent(obj -> this.phone = obj.getValue());

    final Optional<OfferPersonProperty> faxOpt = properties.stream()
        .filter(property -> OfferPersonPropertyType.FAX.getValue().equals(property.getType()))
        .findFirst();
    faxOpt.ifPresent(obj -> this.fax = obj.getValue());
  }
}
