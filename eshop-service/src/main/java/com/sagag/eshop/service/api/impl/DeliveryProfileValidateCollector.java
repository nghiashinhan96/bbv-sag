package com.sagag.eshop.service.api.impl;

import com.sagag.eshop.repo.entity.DeliveryProfile;

import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class DeliveryProfileValidateCollector {

  private List<DeliveryProfile> validItems = new ArrayList<>();

  private List<DeliveryProfile> invalidItems = new ArrayList<>();

  public static Collector<DeliveryProfile, DeliveryProfileValidateCollector, List<DeliveryProfile>> toObjectDeliveryProfile() {
    return Collector.of(DeliveryProfileValidateCollector::new,
        DeliveryProfileValidateCollector::add, DeliveryProfileValidateCollector::merge,
        DeliveryProfileValidateCollector::finish);
  }

  private DeliveryProfileValidateCollector merge(DeliveryProfileValidateCollector other) {
    validItems.addAll(other.validItems);
    invalidItems.addAll(other.invalidItems);
    return this;
  }

  private void add(DeliveryProfile element) {
    List<DeliveryProfile> sameId = validItems.stream()
        .filter(item -> element.getDeliveryProfileId().equals(item.getDeliveryProfileId()))
        .collect(Collectors.toList());
    boolean inValid = CollectionUtils.isNotEmpty(sameId)
        ? sameId.stream().map(DeliveryProfile::getDeliveryProfileName)
            .anyMatch(item -> !element.getDeliveryProfileName().equalsIgnoreCase(item))
        : validItems.stream().map(DeliveryProfile::getDeliveryProfileName)
            .anyMatch(element.getDeliveryProfileName()::equalsIgnoreCase);
    if (!inValid) {
      validItems.add(element);
      return;
    }
    invalidItems.add(element);
  }

  private List<DeliveryProfile> finish() {
    return invalidItems;
  }

}
