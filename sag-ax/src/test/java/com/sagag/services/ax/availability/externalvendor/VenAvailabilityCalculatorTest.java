package com.sagag.services.ax.availability.externalvendor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class VenAvailabilityCalculatorTest {

  @Spy
  private List<VenAvailabilityCalculator> venAvailabilityCalculator = new ArrayList<>();
  
  @Spy
  private BeethovenCalculator beethovenCalculator;
  
  @Spy
  private MozartCalculator mozartCalculator;
  
  @Before
  public void init() {
    venAvailabilityCalculator.addAll(Arrays.asList(mozartCalculator, beethovenCalculator));
  }
  
  @Test
  public void testGettingBeethovenForVEN() {
    String availabilityTypeId = "VEN";
    List<VenAvailabilityCalculator> actualCalculators = venAvailabilityCalculator.stream()
        .filter(calculator -> calculator.isSupportedType(availabilityTypeId))
        .collect(Collectors.toList());
    Assert.assertNotNull(actualCalculators);
    Assert.assertEquals(1, actualCalculators.size());
    Assert.assertEquals(mozartCalculator.getClass(), actualCalculators.get(0).getClass());
  }
  
  @Test
  public void testGettingBeethovenForVWO() {
    String availabilityTypeId = "VWO";
    List<VenAvailabilityCalculator> actualCalculators = venAvailabilityCalculator.stream()
        .filter(calculator -> calculator.isSupportedType(availabilityTypeId))
        .collect(Collectors.toList());
    Assert.assertNotNull(actualCalculators);
    Assert.assertEquals(1, actualCalculators.size());
    Assert.assertEquals(beethovenCalculator.getClass(), actualCalculators.get(0).getClass());
  }
  
  @Test
  public void testGettingBeethovenForVWI() {
    String availabilityTypeId = "VWI";
    List<VenAvailabilityCalculator> actualCalculators = venAvailabilityCalculator.stream()
        .filter(calculator -> calculator.isSupportedType(availabilityTypeId))
        .collect(Collectors.toList());
    Assert.assertNotNull(actualCalculators);
    Assert.assertEquals(1, actualCalculators.size());
    Assert.assertEquals(beethovenCalculator.getClass(), actualCalculators.get(0).getClass());
  }
  
  @Test
  public void testGettingBeethovenForVWH() {
    String availabilityTypeId = "VWH";
    List<VenAvailabilityCalculator> actualCalculators = venAvailabilityCalculator.stream()
        .filter(calculator -> calculator.isSupportedType(availabilityTypeId))
        .collect(Collectors.toList());
    Assert.assertNotNull(actualCalculators);
    Assert.assertEquals(1, actualCalculators.size());
    Assert.assertEquals(beethovenCalculator.getClass(), actualCalculators.get(0).getClass());
  }
  
  @Test
  public void testGettingNOCalculatorForAvailabilityTypeIdEmpty() {
    String availabilityTypeId = "";
    List<VenAvailabilityCalculator> actualCalculators = venAvailabilityCalculator.stream()
        .filter(calculator -> calculator.isSupportedType(availabilityTypeId))
        .collect(Collectors.toList());
    Assert.assertNotNull(actualCalculators);
    Assert.assertEquals(0, actualCalculators.size());
  }
  
  @Test
  public void testGettingNOCalculatorForAvailabilityTypeIdNULL() {
    String availabilityTypeId = "";
    List<VenAvailabilityCalculator> actualCalculators = venAvailabilityCalculator.stream()
        .filter(calculator -> calculator.isSupportedType(availabilityTypeId))
        .collect(Collectors.toList());
    Assert.assertNotNull(actualCalculators);
    Assert.assertEquals(0, actualCalculators.size());
  }
  
  @Test
  public void testGettingNOCalculatorForAvailabilityTypeIdNotDefined() {
    String availabilityTypeId = "NOT_DEFINED";
    List<VenAvailabilityCalculator> actualCalculators = venAvailabilityCalculator.stream()
        .filter(calculator -> calculator.isSupportedType(availabilityTypeId))
        .collect(Collectors.toList());
    Assert.assertNotNull(actualCalculators);
    Assert.assertEquals(0, actualCalculators.size());
  }

}
