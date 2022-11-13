package com.sagag.services.ivds.converter.genericarticle;

import com.sagag.services.common.contants.SagConstants;
import com.sagag.services.ivds.api.IvdsOilSearchService;
import com.sagag.services.ivds.converter.genericarticle.GenericArticleIdInVehicleContextConverter.GenericArticleIdTypeEnum;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class GenericArticleIdInVehicleContextConverter
  implements Function<List<String>, Map<GenericArticleIdTypeEnum, List<String>>> {

  private static final String DELIMITER = SagConstants.COMMA_NO_SPACE;

  @Autowired
  private IvdsOilSearchService ivdsOilSearchService;

  @Override
  public Map<GenericArticleIdTypeEnum, List<String>> apply(
      List<String> originalgenericArticleIds) {

    final List<String> allGenericArticleIds = originalgenericArticleIds.stream()
        .flatMap(genArtIdStr -> Stream.of(StringUtils.split(genArtIdStr, DELIMITER)))
        .map(StringUtils::trim).collect(Collectors.toList());

    final Map<GenericArticleIdTypeEnum, List<String>> gaIdMapByType =
        new EnumMap<>(GenericArticleIdTypeEnum.class);
    final List<String> oilGenericArticleIds =
        ivdsOilSearchService.extractOilGenericArticleIds(allGenericArticleIds);
    gaIdMapByType.put(GenericArticleIdTypeEnum.OIL_GENERIC_ARTICLE_ID, oilGenericArticleIds);

    final List<String> otherGenericArticleIds = allGenericArticleIds.stream()
        .filter(gaId -> !oilGenericArticleIds.contains(gaId)).collect(Collectors.toList());
    gaIdMapByType.put(GenericArticleIdTypeEnum.OTHER_GENERIC_ARTICLE_ID, otherGenericArticleIds);
    return gaIdMapByType;
  }

  public enum GenericArticleIdTypeEnum {
    OIL_GENERIC_ARTICLE_ID, OTHER_GENERIC_ARTICLE_ID;
  }

}
