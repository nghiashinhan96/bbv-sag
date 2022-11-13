package com.sagag.services.gtmotive.builder.mainmoduleservice;

import static org.junit.Assert.assertThat;

import com.sagag.services.gtmotive.DataProvider;
import com.sagag.services.gtmotive.dto.response.mainmoduleservice.GtmotivePart;
import com.sagag.services.gtmotive.dto.response.mainmoduleservice.GtmotivePartsListResponse;

import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class GtmotivePartsListResponseBuilderTest {

  @Test
  public void shouldReturnPartsListResponseGivenRawXmlResponse() throws Exception {
    GtmotivePartsListResponse response = new GtmotivePartsListResponseBuilder()
        .unescapseXmlData(DataProvider.gtmotivePartsListResponse()).build();

    List<GtmotivePart> parts = response.getParts();
    GtmotivePart part1 = parts.get(0);
    assertThat(parts.size(), Matchers.is(3));
    assertThat(part1.getPartCode(), Matchers.is("61XBR"));
    assertThat(part1.getPartDescription(), Matchers.is("Blinkerleuchte vo re"));
    assertThat(part1.getFunctionalGroup(), Matchers.is("11000"));
    assertThat(part1.getFunctionalGroupDescription(), Matchers.is("KAROSSERIE VO AUSSEN"));
  }
}
