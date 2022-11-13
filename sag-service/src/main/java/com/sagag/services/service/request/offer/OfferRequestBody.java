package com.sagag.services.service.request.offer;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.sagag.eshop.service.dto.offer.OfferDto;
import com.sagag.services.common.enums.offer.OfferStatus;

import lombok.Data;

import org.apache.commons.collections.CollectionUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OfferRequestBody implements Serializable {

  private static final long serialVersionUID = -3433367769354292478L;

  private Long offerId;

  private Long offerPersonId;

  private Long offerAddressId;

  private String offerNumber;

  private Date offerDate;

  private Date deliveryDate;

  private String remark;

  private OfferStatus status;

  private Double totalGrossPrice;

  private Double vat;

  private List<OfferPositionItemRequestBody> offerPositionRequests;

  @JsonIgnore
  public OfferDto toOffer() {
    final OfferDto offer = new OfferDto();
    offer.setId(offerId);
    offer.setOfferNr(offerNumber);
    offer.setOfferDate(offerDate);
    offer.setDeliveryDate(deliveryDate);
    offer.setRemark(remark);
    offer.setVat(vat);
    if (status != null) {
      offer.setStatus(status.name());
    }
    if (!CollectionUtils.isEmpty(offerPositionRequests)) {
      offer.setOfferPositions(offerPositionRequests.stream()
          .map(OfferPositionItemRequestBody::toOfferPositionDto)
          .collect(Collectors.toList()));
    }
    return offer;
  }

}
