package com.sagag.eshop.service.utils;

import com.sagag.services.domain.eshop.dto.collection.OrganisationCollectionDto;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class OrganisationCollectionUtilsTest {

  private String affShortName = "derendinger-at";

  @Test
  public void testBuildCollectionShortName() {
    List<String> names =
        IntStream.range(0, 100).mapToObj(i -> OrganisationCollectionUtils.buildCollectionShortName(affShortName))
            .collect(Collectors.toList());
    long uniqueElements = names.stream().distinct().count();
    Assert.assertEquals(uniqueElements, names.size());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testValidateCollectionRequest() {
    OrganisationCollectionDto request = null;
    OrganisationCollectionUtils.validateCollectionRequest(request);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testValidateCollectionRequest2() {
    OrganisationCollectionDto request = OrganisationCollectionDto.builder()
        .build();
    OrganisationCollectionUtils.validateCollectionRequest(request);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testValidateCollectionRequest3() {
    OrganisationCollectionDto request = OrganisationCollectionDto.builder()
        .affiliateShortName(affShortName)
        .build();
    OrganisationCollectionUtils.validateCollectionRequest(request);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testValidateCollectionRequest4() {
    OrganisationCollectionDto request = OrganisationCollectionDto.builder()
        .affiliateShortName(affShortName)
        .name("NAME")
        .build();
    OrganisationCollectionUtils.validateCollectionRequest(request);
  }

  @Test
  public void testValidateCollectionRequest5() {
    OrganisationCollectionDto request = OrganisationCollectionDto.builder()
        .affiliateShortName(affShortName)
        .name("NAME")
        .permissions(new ArrayList<>())
        .build();
    OrganisationCollectionUtils.validateCollectionRequest(request);
  }
}
