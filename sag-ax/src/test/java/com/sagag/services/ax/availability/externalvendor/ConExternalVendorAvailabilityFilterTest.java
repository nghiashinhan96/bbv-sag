package com.sagag.services.ax.availability.externalvendor;

import static org.junit.Assert.assertNotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.sagag.services.article.api.ArticleExternalService;
import com.sagag.services.domain.article.ArticleDocDto;

@RunWith(MockitoJUnitRunner.class)
@Ignore("not yet ready , Please ignore.")
public class ConExternalVendorAvailabilityFilterTest
    extends AbstractExternalVendorAvailabilityFilterTest {

  @InjectMocks
  private ConExternalVendorAvailabilityFilter conExternalVendorAvailabilityFilter;

  @Mock
  private ArticleExternalService articleExtService;

  @Test
  public void shouldFilterAvailabilityVenCase_givenDeliveryTypePickup() throws Exception {
    Map<String, Integer> artAndQuantity = new HashMap<>();
    artAndQuantity.put("1000494876", 1);
    List<ArticleDocDto> results = conExternalVendorAvailabilityFilter.filter(buildBasicArticleDocDtos(artAndQuantity),
        buildDefaultArticleSearchCriteria(false, "PICKUP").build(), buildAdditionalArticleAvailabilityCriteria(),
        buildAxVendors(), StringUtils.EMPTY);

    assertNotNull(results.get(0).getAvailabilities());
  }

  @Test
  public void shouldFilterAvailabilityVenCase_givenDeliveryTypeTour() throws Exception {
    Map<String, Integer> artAndQuantity = new HashMap<>();
    artAndQuantity.put("1000494876", 1);
    List<ArticleDocDto> results = conExternalVendorAvailabilityFilter.filter(buildBasicArticleDocDtos(artAndQuantity),
        buildDefaultArticleSearchCriteria(false, "TOUR").build(), buildAdditionalArticleAvailabilityCriteria(),
        buildAxVendors(), StringUtils.EMPTY);

    assertNotNull(results.get(0).getAvailabilities());
  }

  @Test
  public void shouldFilterAvailabilityVenCase_givenDeliveryTypeTourAndCardModeTrue()
      throws Exception {

    Map<String, Integer> artAndQuantity = new HashMap<>();
    artAndQuantity.put("1000494876", 1);
    List<ArticleDocDto> results = conExternalVendorAvailabilityFilter.filter(buildBasicArticleDocDtos(artAndQuantity),
        buildDefaultArticleSearchCriteria(true, "TOUR").build(), buildAdditionalArticleAvailabilityCriteria(),
        buildAxVendors(), StringUtils.EMPTY);

    assertNotNull(results.get(0).getAvailabilities());
  }
}
