package com.sagag.services.common.enums.offer;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.stream.Stream;

@AllArgsConstructor
@Getter
public enum OfferPositionType {

  VENDOR_ARTICLE("VENDORARTICLE"),
  CLIENT_WORK("CLIENTWORK"),
  REMARK("REMARK"),
  VEHICLE_INFO_PROVIDER_WORK("VEHICLEINFOPROVIDERWORK"),
  CLIENT_ARTICLE("CLIENTARTICLE"),
  VENDOR_ARTICLE_WITHOUT_VEHICLE("VENDORARTICLEWITHOUTVEHICLE"),
  HAYNESPRO_PROVIDER_WORK("HAYNESPROPROVIDERWORK");

  private String value;

  public static OfferPositionType getByValue(String valueStr) {
    return Stream.of(values())
        .filter(value -> value.getValue().equals(valueStr))
        .findFirst()
        .orElseThrow(() -> new IllegalArgumentException(
            String.format("Offer position type %s is invalid", valueStr)));
  }

  public boolean isRemark() {
    return REMARK == this;
  }

  public boolean isVendorArticle() {
    return VENDOR_ARTICLE == this;
  }

  public boolean isVendorArticleWithoutVehicle() {
    return VENDOR_ARTICLE_WITHOUT_VEHICLE == this;
  }

  public boolean isHaynesProProviderWork() {
    return HAYNESPRO_PROVIDER_WORK == this;
  }

  public boolean isClientArticle() {
    return CLIENT_ARTICLE == this;
  }

  public boolean isClientWork() {
    return CLIENT_WORK == this;
  }

  public boolean isVehicleProviderWork() {
    return VEHICLE_INFO_PROVIDER_WORK == this;
  }

  public boolean isArticle() {
    return isVendorArticle() || isVendorArticleWithoutVehicle() || isClientArticle();
  }

  public boolean isWork() {
    return isClientWork() || isVehicleProviderWork() || isHaynesProProviderWork();
  }

  public boolean isHaynesProWork() {
    return HAYNESPRO_PROVIDER_WORK == this;
  }
}
