package com.sagag.services.ax.availability.externalvendor;

import static org.junit.Assert.assertNotNull;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import com.sagag.eshop.repo.api.OpeningDaysCalendarRepository;
import com.sagag.services.article.api.ArticleExternalService;
import com.sagag.services.article.api.domain.vendor.VendorStockDto;
import com.sagag.services.ax.availability.tourtime.NextWorkingDateHelper;
import com.sagag.services.ax.holidays.impl.AxDefaultHolidaysCheckerImpl;
import com.sagag.services.domain.article.ArticleDocDto;

@RunWith(MockitoJUnitRunner.class)
public class VenExternalVendorAvailabilityFilterTest
    extends AbstractExternalVendorAvailabilityFilterTest {

  @InjectMocks
  private VenExternalVendorAvailabilityFilter venExternalVendorAvailabilityFilter;

  @Mock
  private ArticleExternalService articleExtService;

  @Mock
  private AxDefaultHolidaysCheckerImpl axAustriaHolidaysChecker;

  @Spy
  private List<VenAvailabilityCalculator> venAvailabilityCalculator = new ArrayList<>();

  @Spy
  private BeethovenCalculator beethovenCalculator;
  
  @Spy
  private MozartCalculator mozartCalculator;
  
  @Spy
  private NextWorkingDateHelper nextWorkingDateHelper;
  
  @Spy
  private OpeningDaysCalendarRepository openingDaysCalendarRepository;

  @Before
  public void init() {
    MockitoAnnotations.initMocks(this);
    venAvailabilityCalculator.addAll(Arrays.asList(beethovenCalculator, mozartCalculator));
  }
  
  @Test
  public void shouldFilterAvailabilityVenCase_givenVendorDeliveryTimeBeforeLastDeliveryWithDeliveryTypePickup()
      throws Exception {
    Map<Long, String> artAndExtArt = new HashMap<>();
    artAndExtArt.put(1000494876l, "ZFR6T-11G");
    VendorStockDto vendorStockDto = buildVendorStockDto(artAndExtArt);
    LocalDate now = LocalDate.now();
    String nowStr = now.getYear() + "-" + now.getMonthValue() + "-" + now.getDayOfMonth();

    vendorStockDto.setDeliveryDate(nowStr + "T14:00:00Z");
    vendorStockDto.setCutoffTime(nowStr + "T23:00:00Z");

    Mockito.when(articleExtService.searchVendorStock(Mockito.any(), Mockito.any(), Mockito.any(),
        Mockito.any())).thenReturn(Optional.of(vendorStockDto));
    mockNextWorkingDateHelper(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").parse(vendorStockDto.getDeliveryDate()));
    Map<String, Integer> artAndQuantity = new HashMap<>();
    artAndQuantity.put("1000494876", 1);
    List<ArticleDocDto> results =
        venExternalVendorAvailabilityFilter.filter(buildBasicArticleDocDtos(artAndQuantity),
            buildDefaultArticleSearchCriteria(false, "PICKUP").build(),
            buildAdditionalArticleAvailabilityCriteria(), buildAxVendors(), StringUtils.EMPTY);

    assertNotNull(results.get(0).getAvailabilities());
  }

  @Test
  public void shouldFilterAvailabilityVenCase_givenVendorDeliveryTimeBeforeLastDeliveryWithDeliveryTypeTour()
      throws Exception {

    Map<Long, String> artAndExtArt = new HashMap<>();
    artAndExtArt.put(1000494876l, "ZFR6T-11G");
    VendorStockDto vendorStockDto = buildVendorStockDto(artAndExtArt);
    LocalDate now = LocalDate.now();
    String nowStr = now.getYear() + "-" + now.getMonthValue() + "-" + now.getDayOfMonth();

    vendorStockDto.setDeliveryDate(nowStr + "T14:00:00Z");
    vendorStockDto.setCutoffTime(nowStr + "T23:00:00Z");

    Mockito.when(articleExtService.searchVendorStock(Mockito.any(), Mockito.any(), Mockito.any(),
        Mockito.any())).thenReturn(Optional.of(vendorStockDto));

    mockNextWorkingDateHelper(new Date());
    Map<String, Integer> artAndQuantity = new HashMap<>();
    artAndQuantity.put("1000494876", 1);
    List<ArticleDocDto> results =
        venExternalVendorAvailabilityFilter.filter(buildBasicArticleDocDtos(artAndQuantity),
            buildDefaultArticleSearchCriteria(false, "TOUR").build(),
            buildAdditionalArticleAvailabilityCriteria(), buildAxVendors(), StringUtils.EMPTY);

    assertNotNull(results.get(0).getAvailabilities());
  }

  @Test
  public void shouldFilterAvailabilityVenCase_givenVendorDeliveryTimeBeforeLastDeliveryWithDeliveryTypeTourAndCardModeTrue()
      throws Exception {
    Map<Long, String> artAndExtArt = new HashMap<>();
    artAndExtArt.put(1000494876l, "ZFR6T-11G");
    VendorStockDto vendorStockDto = buildVendorStockDto(artAndExtArt);
    LocalDate now = LocalDate.now();
    String nowStr = now.getYear() + "-" + now.getMonthValue() + "-" + now.getDayOfMonth();

    vendorStockDto.setDeliveryDate(nowStr + "T14:00:00Z");
    vendorStockDto.setCutoffTime(nowStr + "T23:00:00Z");

    Mockito.when(articleExtService.searchVendorStock(Mockito.any(), Mockito.any(), Mockito.any(),
        Mockito.any())).thenReturn(Optional.of(vendorStockDto));

    Map<String, Integer> artAndQuantity = new HashMap<>();
    artAndQuantity.put("1000494876", 1);
    List<ArticleDocDto> results =
        venExternalVendorAvailabilityFilter.filter(buildBasicArticleDocDtos(artAndQuantity),
            buildDefaultArticleSearchCriteria(true, "TOUR").build(),
            buildAdditionalArticleAvailabilityCriteria(), buildAxVendors(), StringUtils.EMPTY);

    assertNotNull(results.get(0).getAvailabilities());
  }

  @Test
  public void shouldFilterAvailabilityVenCase_givenVendorDeliveryTimeAfterLastDeliveryWithDeliveryTypePickup()
      throws Exception {

    Map<Long, String> artAndExtArt = new HashMap<>();
    artAndExtArt.put(1000494876l, "ZFR6T-11G");
    VendorStockDto vendorStockDto = buildVendorStockDto(artAndExtArt);
    LocalDate now = LocalDate.now();
    String nowStr = now.getYear() + "-" + now.getMonthValue() + "-" + now.getDayOfMonth();

    vendorStockDto.setDeliveryDate(nowStr + "T20:00:00Z");
    vendorStockDto.setCutoffTime(nowStr + "T23:00:00Z");

    Mockito.when(articleExtService.searchVendorStock(Mockito.any(), Mockito.any(), Mockito.any(),
        Mockito.any())).thenReturn(Optional.of(vendorStockDto));

    Map<String, Integer> artAndQuantity = new HashMap<>();
    artAndQuantity.put("1000494876", 1);
    List<ArticleDocDto> results =
        venExternalVendorAvailabilityFilter.filter(buildBasicArticleDocDtos(artAndQuantity),
            buildDefaultArticleSearchCriteria(false, "PICKUP").build(),
            buildAdditionalArticleAvailabilityCriteria(), buildAxVendors(), StringUtils.EMPTY);

    assertNotNull(results.get(0).getAvailabilities());
  }
  
  //Assume that the next working date is always tomorrow of the input
  private void mockNextWorkingDateHelper(Date date) {
    if (date == null) {
      return;
    }
    Calendar c = Calendar.getInstance();
    c.setTime(date);
    c.add(Calendar.DATE, 1);
    Date nextDate = c.getTime();
    Mockito.when(nextWorkingDateHelper.getNextWorkingDayByDays(Mockito.any(), Mockito.any(), Mockito.any(),
        Mockito.any(), Mockito.anyInt())).thenReturn(nextDate);
    Mockito.when(nextWorkingDateHelper.getNextWorkingDate(Mockito.any(), Mockito.any(), Mockito.any()))
        .thenReturn(nextDate);
  }

}
