package com.sagag.eshop.service.api.impl;

import com.sagag.eshop.repo.entity.DeliveryProfile;

import org.apache.commons.collections4.CollectionUtils;
import org.assertj.core.util.Lists;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.Random;

public class DeliveryProfileValidateCollectorTest {

  @Test
  public void shouldValidDeliveryProfile() {
    int deliveryProfileId1 = new Random().nextInt();
    int deliveryProfileId2 = new Random().nextInt();
    DeliveryProfile deliveryProfile1 = DeliveryProfile.builder().deliveryProfileId(deliveryProfileId1)
        .deliveryProfileName("DP_NAME_1").build();
    DeliveryProfile deliveryProfile2 = DeliveryProfile.builder().deliveryProfileId(deliveryProfileId2)
        .deliveryProfileName("DP_NAME_2").build();
    List<DeliveryProfile> deliveryProfiles = Lists.newArrayList(deliveryProfile1, deliveryProfile2);

    List<DeliveryProfile> invalidItems = deliveryProfiles.stream()
        .collect(DeliveryProfileValidateCollector.toObjectDeliveryProfile());

    Assert.assertTrue(CollectionUtils.isEmpty(invalidItems));
  }

  @Test
  public void shouldDuplicateDeliveryProfile() {
    int deliveryProfileId = new Random().nextInt();
    DeliveryProfile deliveryProfile1 = DeliveryProfile.builder().deliveryProfileId(deliveryProfileId)
        .deliveryProfileName("DP_NAME_1").build();
    DeliveryProfile deliveryProfile2 = DeliveryProfile.builder().deliveryProfileId(deliveryProfileId)
        .deliveryProfileName("DP_NAME_2").build();
    List<DeliveryProfile> deliveryProfiles = Lists.newArrayList(deliveryProfile1, deliveryProfile2);

    List<DeliveryProfile> invalidItems = deliveryProfiles.stream()
        .collect(DeliveryProfileValidateCollector.toObjectDeliveryProfile());

    Assert.assertFalse(CollectionUtils.isEmpty(invalidItems));
  }
}
