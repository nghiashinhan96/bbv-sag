package com.sagag.eshop.service.tests.utils;

import static org.mockito.Mockito.when;

import com.sagag.eshop.service.dto.FunctionDto;
import com.sagag.eshop.service.dto.PermissionDto;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.eshop.service.utils.PermissionUtils;
import com.sagag.services.common.enums.PermissionEnum;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
public class PermissionUtilsTest {

  @Mock
  private UserInfo user;

  private final List<String> FUNCTIONS_ACCESS_SHOPS =
      Arrays.asList("OIL_URL_ACCESS", "BULB_URL_ACCESS", "BATTERY_URL_ACCESS", "TYRE_URL_ACCESS");

  private static final String TYPE_LINK_1 = "shop";

  private static final String TYPE_LINK_2 = "thule";

  private static final String TYPE_LINK_ATTR_1 = "oil";

  private static final String TYPE_LINK_ATTR_2 = "tyre";
  
  private static final String OIL_URL_ACCESS = "OIL_URL_ACCESS";

  private static final String BATTERY_URL_ACCESS = "BATTERY_URL_ACCESS";

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);

    List<PermissionDto> perms = Arrays
        .asList(
            PermissionDto.builder()
                .functions(
                    Arrays.asList(FunctionDto.builder().functionName(OIL_URL_ACCESS).build()))
                .build(),
            PermissionDto.builder()
                .functions(
                    Arrays.asList(FunctionDto.builder().functionName(BATTERY_URL_ACCESS).build()))
                .build());
    when(user.getPermissions()).thenReturn(perms);
  }

  @Test
  public void testGetFunctionGrantedList() {
    List<FunctionDto> functionDtos =
        PermissionUtils.getFunctionGrantedList(user.getPermissions(), FUNCTIONS_ACCESS_SHOPS);

    Assert.assertEquals(functionDtos.size(), 2);
    long total =
        functionDtos.stream().filter(func -> func.getFunctionName().equals(OIL_URL_ACCESS)
            || func.getFunctionName().equals(BATTERY_URL_ACCESS)).count();
    Assert.assertEquals(total, 2);
  }


  @Test
  public void testGetPermissionShopUniparts() {
    Map<String, Boolean> shopUniparts =
        PermissionUtils.getPermissionShopUniparts(user.getPermissions());

    Assert.assertEquals(shopUniparts.size(), 4);
    Assert.assertEquals(shopUniparts.get(PermissionEnum.OIL.name()), true);
    Assert.assertEquals(shopUniparts.get(PermissionEnum.BATTERY.name()), true);
    Assert.assertEquals(shopUniparts.get(PermissionEnum.TYRE.name()), false);
  }

  @Test
  public void testIsAccess() {
    Map<String, Boolean> shopUniparts =
        PermissionUtils.getPermissionShopUniparts(user.getPermissions());

    boolean isAccessOil = PermissionUtils.isAccess(shopUniparts, PermissionEnum.OIL.name());
    boolean isAccessTyre = PermissionUtils.isAccess(shopUniparts, PermissionEnum.TYRE.name());
    Assert.assertEquals(isAccessOil, true);
    Assert.assertEquals(isAccessTyre, false);
  }

  @Test
  public void testIsActiveLink() {
    Map<String, Boolean> shopUniparts =
        PermissionUtils.getPermissionShopUniparts(user.getPermissions());

    boolean result1 = PermissionUtils.isActiveLink(TYPE_LINK_1, TYPE_LINK_ATTR_1, shopUniparts);
    Assert.assertEquals(result1, true);

    boolean result2 = PermissionUtils.isActiveLink(TYPE_LINK_1, TYPE_LINK_ATTR_2, shopUniparts);
    Assert.assertEquals(result2, false);

    boolean result3 = PermissionUtils.isActiveLink(TYPE_LINK_2, TYPE_LINK_ATTR_1, shopUniparts);
    Assert.assertEquals(result3, true);

    boolean result4 = PermissionUtils.isActiveLink(TYPE_LINK_2, TYPE_LINK_ATTR_2, shopUniparts);
    Assert.assertEquals(result4, true);
  }
}
