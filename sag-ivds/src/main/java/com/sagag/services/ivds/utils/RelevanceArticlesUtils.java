package com.sagag.services.ivds.utils;

import com.sagag.services.ivds.request.filter.ArticleFilterRequest;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.SerializationUtils;

import java.util.Collections;

@UtilityClass
public class RelevanceArticlesUtils {

  public static ArticleFilterRequest prepareDirectMatchRequest(ArticleFilterRequest originalRequest) {
    if (!originalRequest.isKeepDirectAndOriginalMatch()) {
      return originalRequest;
    }
    ArticleFilterRequest directMatchRequest = SerializationUtils.clone(originalRequest);
    directMatchRequest.setGaIds(Collections.emptyList());
    directMatchRequest.setSuppliers(Collections.emptyList());
    directMatchRequest.setGaIdsMultiLevels(Collections.emptyList());
    return  directMatchRequest;
  }

}
