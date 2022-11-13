package com.sagag.services.article.api.utils;

import com.sagag.services.common.contants.SagConstants;
import com.sagag.services.domain.eshop.dto.VehicleDto;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;

import java.util.Optional;

/**
 * Unit test class for Default amount helper.
 */
public class DefaultAmountHelperTest {

  @Test
  public void testGetArticleSalesQuantity_WithOutOfGaid() {
    int salesQuantity = DefaultAmountHelper.getArticleSalesQuantity("1", Optional.empty());
    Assert.assertThat(salesQuantity, Matchers.is(1));
  }

  @Test
  public void testGetArticleSalesQuantity_WithGaidIs_78() {
    int salesQuantity = DefaultAmountHelper.getArticleSalesQuantity("78", Optional.empty());
    Assert.assertThat(salesQuantity, Matchers.is(1));
  }

  @Test
  public void testGetArticleSalesQuantity_WithGaidIs_82() {
    int salesQuantity = DefaultAmountHelper.getArticleSalesQuantity("82", Optional.empty());
    Assert.assertThat(salesQuantity, Matchers.is(2));
  }

  @Test
  public void testGetArticleSalesQuantity_WithGaidIs_243() {
    int salesQuantity = DefaultAmountHelper.getArticleSalesQuantity("243", Optional.empty());
    Assert.assertThat(salesQuantity, Matchers.is(1));
  }

  @Test
  public void testGetArticleSalesQuantity_WithGaidIs_686() {
    int salesQuantity = DefaultAmountHelper.getArticleSalesQuantity("686", Optional.empty());
    Assert.assertThat(salesQuantity, Matchers.is(1));
  }

  @Test
  public void testGetArticleSalesQuantity_WithGaidIs_243_AndVehicle() {
    int salesQuantity = DefaultAmountHelper.getArticleSalesQuantity("243",
        Optional.of(vehicle("VEH001", 8)));
    Assert.assertThat(salesQuantity, Matchers.is(8));
  }

  @Test
  public void testGetArticleSalesQuantity_WithGaidIs_243_AndNoVehicleKey() {
    int salesQuantity = DefaultAmountHelper.getArticleSalesQuantity("243",
        Optional.of(vehicle(SagConstants.KEY_NO_VEHICLE, 8)));
    Assert.assertThat(salesQuantity, Matchers.is(1));
  }

  @Test
  public void testGetArticleSalesQuantity_WithGaidIs_686_AndVehicle() {
    int salesQuantity = DefaultAmountHelper.getArticleSalesQuantity("686",
        Optional.of(vehicle("VEH001", 10)));
    Assert.assertThat(salesQuantity, Matchers.is(10));
  }

  @Test
  public void testGetArticleSalesQuantity_WithGaidIs_686_AndNoVehicleKey() {
    int salesQuantity = DefaultAmountHelper.getArticleSalesQuantity("686",
        Optional.of(vehicle(SagConstants.KEY_NO_VEHICLE, 10)));
    Assert.assertThat(salesQuantity, Matchers.is(1));
  }

  @Test
  public void testGetArticleSalesQuantity_WithGaidIs_11697() {
    int salesQuantity = DefaultAmountHelper.getArticleSalesQuantity("11697", Optional.empty());
    Assert.assertThat(salesQuantity, Matchers.is(4));
  }

  @Test
  public void testGetArticleSalesQuantity_WithGaidIs_1169710() {
    int salesQuantity = DefaultAmountHelper.getArticleSalesQuantity("1169710", Optional.empty());
    Assert.assertThat(salesQuantity, Matchers.is(4));
  }

  @Test
  public void testGetArticleSalesQuantity_WithGaidIs_116971() {
    int salesQuantity = DefaultAmountHelper.getArticleSalesQuantity("116971", Optional.empty());
    Assert.assertThat(salesQuantity, Matchers.is(4));
  }

  @Test
  public void testGetArticleSalesQuantity_WithGaidIs_11698() {
    int salesQuantity = DefaultAmountHelper.getArticleSalesQuantity("11698", Optional.empty());
    Assert.assertThat(salesQuantity, Matchers.is(4));
  }

  @Test
  public void testGetArticleSalesQuantity_WithGaidIs_1169810() {
    int salesQuantity = DefaultAmountHelper.getArticleSalesQuantity("1169810", Optional.empty());
    Assert.assertThat(salesQuantity, Matchers.is(4));
  }

  @Test
  public void testGetArticleSalesQuantity_WithGaidIs_116981() {
    int salesQuantity = DefaultAmountHelper.getArticleSalesQuantity("116981", Optional.empty());
    Assert.assertThat(salesQuantity, Matchers.is(4));
  }

  @Test
  public void testGetArticleSalesQuantity_WithGaidIs_11699() {
    int salesQuantity = DefaultAmountHelper.getArticleSalesQuantity("11699", Optional.empty());
    Assert.assertThat(salesQuantity, Matchers.is(4));
  }

  @Test
  public void testGetArticleSalesQuantity_WithGaidIs_1169910() {
    int salesQuantity = DefaultAmountHelper.getArticleSalesQuantity("1169910", Optional.empty());
    Assert.assertThat(salesQuantity, Matchers.is(4));
  }

  @Test
  public void testGetArticleSalesQuantity_WithGaidIs_116991() {
    int salesQuantity = DefaultAmountHelper.getArticleSalesQuantity("116991", Optional.empty());
    Assert.assertThat(salesQuantity, Matchers.is(4));
  }

  private static final VehicleDto vehicle(String vehId, Integer vehZylinder) {
    VehicleDto veh = new VehicleDto();
    veh.setVehId(vehId);
    veh.setVehicleZylinder(vehZylinder);
    return veh;
  }

}
