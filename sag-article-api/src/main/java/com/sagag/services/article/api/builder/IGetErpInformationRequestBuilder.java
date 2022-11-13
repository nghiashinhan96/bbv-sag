package com.sagag.services.article.api.builder;

import org.apache.commons.collections4.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

public interface IGetErpInformationRequestBuilder<C, T, R> {

  final int POOL_ID_TECDOC = 200;

  final int TYPE_ID_NORMAL = 1;

  /**
   * Builds XML Request from Java object.
   *
   * @param credentials the user credentials
   * @param request the body request
   * @param additionals the additionals info
   * @return the XML object request
   */
  R buildRequest(C credentials, T request, Object... additionals);

  default <O> Map<String, O> buildUUIDMap(List<O> items) {
    if (CollectionUtils.isEmpty(items)) {
      return Collections.emptyMap();
    }
    return items.stream().collect(Collectors.toMap(item -> UUID.randomUUID().toString(),
        Function.identity()));
  }
}
