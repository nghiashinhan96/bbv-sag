package com.sagag.services.oates.api.impl;

import com.google.common.collect.Lists;
import com.sagag.services.common.aspect.LogExecutionTime;
import com.sagag.services.common.utils.SagJSONUtil;
import com.sagag.services.oates.api.OatesAdditionalRecommendationsService;
import com.sagag.services.oates.dto.AdditionalRecommendationDto;
import com.sagag.services.oates.dto.RecommendProductDto;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class OatesAdditionalRecommendationsServiceImpl
  implements OatesAdditionalRecommendationsService {

  @Value("${external.webservice.additionalRecommendation.endpoint:}")
  private String additionalRecommendationUrl;

  @Override
  @LogExecutionTime
  public List<RecommendProductDto> getAllRecommendProducts() {
    String url = additionalRecommendationUrl;
    if (StringUtils.isBlank(url)) {
      return Lists.newArrayList();
    }
    AdditionalRecommendationDto additionalRecommendation = null;
    try {
      additionalRecommendation =
          SagJSONUtil.readJsonFromUrl(url, AdditionalRecommendationDto.class);
    } catch (IOException | JSONException e) {
      log.error("Load Additional Recommendation error: ", e);
    }

    return Optional.ofNullable(additionalRecommendation)
      .map(AdditionalRecommendationDto::getProducts).orElseGet(() -> Lists.newArrayList());
  }
}
