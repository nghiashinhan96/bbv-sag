package com.sagag.services.ax.api.impl;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.TypeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientResponseException;

import com.sagag.services.article.api.token.ErpAuthenService;
import com.sagag.services.ax.common.filter.ErpArticleIdFilter;
import com.sagag.services.ax.exception.AxExternalException;
import com.sagag.services.ax.exception.AxUnauthorizedRequestException;
import com.sagag.services.ax.exception.translator.AxExternalExceptionTranslator;
import com.sagag.services.common.config.AppProperties;
import com.sagag.services.common.contants.SagConstants;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AxProcessor {

  private static final String WARN_MESSAGE =
      "The AX token is expired, we will refresh token and re-try this request.";

  protected static final String EMPTY_ACCESS_TOKEN = StringUtils.EMPTY;

  @Autowired
  @Qualifier("defaultAxExternalExceptionTranslator")
  private AxExternalExceptionTranslator defaultAxExternalExceptionTranslator;

  @Autowired
  @Qualifier("axInternalServerExceptionTranslator")
  private AxExternalExceptionTranslator axInternalServerExceptionTranslator;

  @Autowired
  private ErpAuthenService erpAuthenService;

  @Autowired
  private ErpArticleIdFilter erpArticleIdFilter;

  @Autowired
  private AppProperties appProps;

  protected <T> T retryIfExpiredToken(Function<String, T> function) {
    try {
      return function.apply(erpAuthenService.getAxToken());
    } catch (AxUnauthorizedRequestException ex) {
      log.warn(WARN_MESSAGE);
      return function.apply(erpAuthenService.refreshAccessToken());
    } catch (HttpClientErrorException ex) {
      if (HttpStatus.UNAUTHORIZED != ex.getStatusCode()) {
        throw ex;
      }
      log.warn(WARN_MESSAGE);
      return function.apply(erpAuthenService.refreshAccessToken());
    }
  }

  protected <T> T getOrThrow(Supplier<T> getSupplier,
      Function<Exception, AxExternalException> exceptionTranslator) {
    try {
      return getSupplier.get();
    } catch (RestClientResponseException ex) {
      // Handle the request / response exception from AX SWS
      throw exceptionTranslator.apply(ex);
    } catch (Exception ex) {
      // Handle the generic exception, if the AX request got any problems
      throw axInternalServerExceptionTranslator.apply(ex);
    }
  }

  protected <T> T getOrDefaultThrow(Supplier<T> getSupplier) {
    return getOrThrow(getSupplier, defaultAxExternalExceptionTranslator);
  }

  protected UnaryOperator<List<String>> articleIdsCollector() {
    return articleIds -> {
      final int maxRequestSize = appProps.getErpConfig().getMaxRequestSize();
      if (CollectionUtils.size(articleIds) > maxRequestSize) {
        final String message =
            "The size of article ids should be less or equal than " + maxRequestSize;
        throw new IllegalArgumentException(message);
      }
      log.debug("Before filtering articleIds {}", articleIds);
      // filter the valid pimIds only
      articleIds = articleIds.stream().filter(erpArticleIdFilter).collect(Collectors.toList());
      log.debug("After filtering articleIds {}", articleIds);
      return articleIds;
    };
  }

  protected Function<List<String>, String> articleIdsRequestParamConverter() {
    return articleIds -> StringUtils.join(articleIdsCollector().apply(articleIds),
        SagConstants.COMMA_NO_SPACE);
  }

  protected <T> T getDefaultValueIfThrowException(Supplier<T> supplier,
    Supplier<T> defaultValSupplier, Class<?> clazz) {
    return getDefaultValueIfThrowException(supplier, defaultValSupplier, null, clazz);
  }

  protected <T> T getDefaultValueIfThrowException(Supplier<T> supplier,
    Supplier<T> defaultValSupplier, Supplier<String> errorMessageSupplier, Class<?> clazz) {
    try {
      return supplier.get();
    } catch (Exception ex) {
      if (TypeUtils.isInstance(ex, clazz)) {
        Optional.ofNullable(errorMessageSupplier).map(Supplier::get).ifPresent(log::error);
        return defaultValSupplier.get();
      }
      throw ex;
    }
  }
}
