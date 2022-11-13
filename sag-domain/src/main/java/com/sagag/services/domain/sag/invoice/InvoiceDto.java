package com.sagag.services.domain.sag.invoice;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sagag.services.domain.sag.erp.Address;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.io.Serializable;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class InvoiceDto implements Serializable {

  private static final long serialVersionUID = -6998389434446948018L;

  private String invoiceNr;

  @JsonFormat(shape = JsonFormat.Shape.NUMBER)
  private Date invoiceDate;

  private String name;

  private String customerNr;

  private String zipcode;

  private String city;

  private String country;

  private String termOfPayment;

  private Double amount;

  private String paymentType;

  private Address address;

  private List<InvoicePositionDto> positions;

  private String invoiceType;

  @JsonIgnore
  private List<String> vehicles;

  private String deliveryNoteNr;

  private String orderNr;

  private Long orderHistoryId;

  @JsonIgnore
  private Double price;

  @JsonProperty("deliveryNoteNrs")
  public List<String> getDeliveryNoteNrs() {
    if (!hasPositions()) {
      return Collections.emptyList();
    }
    return positions.stream().map(InvoicePositionDto::getDeliveryNoteNr)
      .distinct().collect(Collectors.toList());
  }

  @JsonProperty("orderNrs")
  public List<String> getOrderNrs() {
    if (!hasPositions()) {
      return Collections.emptyList();
    }
    return positions.stream().map(InvoicePositionDto::getOrderNr)
      .distinct().collect(Collectors.toList());
  }

  @JsonIgnore
  public List<String> getPimIds() {
    return ListUtils.emptyIfNull(positions).stream().filter(p -> Objects.nonNull(p.getArticleId()))
        .map(InvoicePositionDto::getArticleId).collect(Collectors.toList());
  }

  @JsonIgnore
  public boolean hasPositions() {
    return !CollectionUtils.isEmpty(positions);
  }

  public double getAmount() {
    if (!Objects.isNull(price)) {
      return price.doubleValue();
    }
    if (!Objects.isNull(amount)) {
      return amount.doubleValue();
    }
    if (!hasPositions()) {
      return NumberUtils.DOUBLE_ZERO;
    }
    return positions.stream().mapToDouble(InvoicePositionDto::getAmount).sum();
  }
}
