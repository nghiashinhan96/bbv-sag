package com.sagag.services.ax.availability.externalvendor;

import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import com.sagag.services.domain.eshop.dto.externalvendor.ExternalVendorDto;

@RunWith(MockitoJUnitRunner.class)
public class ExternalVendorUtilsTest {

  @Test
  public void compareByLayerRule_shouldReturnBrandIdAndSagArticleGroupFirst() {

    List<ExternalVendorDto> externalVendorDtos = mockDataForBrandAndSagArticleGroup();

    List<ExternalVendorDto> sortedVendors = externalVendorDtos.stream()
        .sorted(ExternalVendorUtils.compareByLayerRule()).collect(Collectors.toList());
    Optional<ExternalVendorDto> x = sortedVendors.stream().findFirst();

    assertThat(x.isPresent(), Matchers.is(true));
    assertThat(x.get().getId(), Matchers.is(1));

  }

  @Test
  public void compareByLayerRule_shouldReturnVendorIdFirst() {

    List<ExternalVendorDto> externalVendorDtos = mockDataForBrandAndSagArticleGroup();
    externalVendorDtos.addAll(mockDataForVendorId());
    List<ExternalVendorDto> sortedVendors = externalVendorDtos.stream()
        .sorted(ExternalVendorUtils.compareByLayerRule()).collect(Collectors.toList());
    Optional<ExternalVendorDto> x = sortedVendors.stream().findFirst();

    assertThat(x.isPresent(), Matchers.is(true));
    assertThat(x.get().getId(), Matchers.is(2));

  }

  private List<ExternalVendorDto> mockDataForBrandAndSagArticleGroup() {
    ExternalVendorDto a = new ExternalVendorDto();
    a.setId(1);
    a.setBrandId(1L);
    a.setSagArticleGroup("LE");

    ExternalVendorDto b = new ExternalVendorDto();
    b.setBrandId(1L);

    ExternalVendorDto c = new ExternalVendorDto();
    c.setSagArticleGroup("xx");

    ExternalVendorDto d = new ExternalVendorDto();

    List<ExternalVendorDto> externalVendorDtos = new ArrayList<>();
    externalVendorDtos.add(b);
    externalVendorDtos.add(c);
    externalVendorDtos.add(d);
    externalVendorDtos.add(a);
    return externalVendorDtos;
  }

  private List<ExternalVendorDto> mockDataForVendorId() {
    ExternalVendorDto a = new ExternalVendorDto();
    a.setId(2);
    a.setVendorId("2");

    ExternalVendorDto b = new ExternalVendorDto();
    b.setBrandId(1L);

    List<ExternalVendorDto> externalVendorDtos = new ArrayList<>();
    externalVendorDtos.add(a);
    externalVendorDtos.add(b);
    return externalVendorDtos;
  }


}
