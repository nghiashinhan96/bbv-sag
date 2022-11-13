package com.sagag.services.hazelcast.domain.cart;

import com.sagag.eshop.repo.hz.entity.ShopType;
import com.sagag.services.common.contants.SagConstants;

import lombok.experimental.UtilityClass;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@UtilityClass
public class CartKeyGenerators {

  /**
   * Constructs the cart item key whenever this user adds an item to the shopping basket.
   *
   * @param userKey
   * @param vehId the vehicle id in context, if any
   * @param artId the article id
   * @return the unique cart item key saved in cache
   */
  public String cartKey(String userKey, String vehId, String artId, ShopType shopType) {
    final String nonNullVehicleId = StringUtils.defaultIfBlank(vehId, SagConstants.KEY_NO_VEHICLE);
    final String shopTypeStr = defShopTypeCartKey(shopType);
    final List<String> items = Stream.of(userKey, nonNullVehicleId, artId)
        .collect(Collectors.toList());
    if (StringUtils.isBlank(shopTypeStr)) {
      return StringUtils.join(items, SagConstants.UNDERSCORE);
    }
    items.add(shopTypeStr);
    return StringUtils.join(items, SagConstants.UNDERSCORE);
  }

  /**
   * Constructs the DVSE cart item key whenever this user adds an item to the shopping basket.
   *
   * @param userKey
   * @param vehId the vehicle id in context, if any
   * @param artId the article id
   * @param suppId the supplier id
   * @param suppArtNr the supplier number
   * @return the unique cart item key saved in cache
   */
  public String cartKey(String userKey, String vehId,  String artId, int suppId, String suppArtNr,
      ShopType shopType) {
    final String key = cartKey(userKey, vehId, artId, shopType);
    if (!Objects.isNull(artId)) {
      return key;
    }
    return StringUtils.join(Arrays.asList(key, suppId, suppArtNr), SagConstants.UNDERSCORE);
  }

  /**
   * Creates uuid key for attached article.
   *
   * @param vehicleId the vehicle id in context, if any
   * @param articleId the article id
   * @param attachedArticleId the attached article id
   * @param shopType the shopType
   * @return the unique key for attached article
   */
  public static String createUuidArticleKey(String vehicleId, String articleId,
      String attachedArticleId, ShopType shopType) {
    return StringUtils
        .join(
            Arrays.asList(StringUtils.defaultIfBlank(vehicleId, SagConstants.KEY_NO_VEHICLE),
                articleId, attachedArticleId, defShopTypeCartKey(shopType)),
            SagConstants.UNDERSCORE);
  }

  private static String defShopTypeCartKey(ShopType shopType) {
    return ShopType.defaultShopTypeCartKey(shopType);
  }

}
