package com.sagag.services.ax.executor;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.google.common.collect.Lists;
import com.sagag.services.article.api.domain.vendor.VendorDto;
import com.sagag.services.article.api.executor.ArticleErpExternalExecutorBuilders;
import com.sagag.services.article.api.executor.ArticleSearchCriteria;
import com.sagag.services.article.api.executor.ExternalVendorSearchExecutor;
import com.sagag.services.common.aspect.LogExecutionTime;
import com.sagag.services.common.config.AppProperties;
import com.sagag.services.common.profiles.AxProfile;

@Component
@AxProfile
public class AxExternalVendorSearchExecutor implements ExternalVendorSearchExecutor {

  @Autowired
  private ArticleErpExternalExecutorBuilders artErpExternalExecutorBuilder;

  @Autowired
  private AppProperties appProps;

  @Override
  @LogExecutionTime
  public List<VendorDto> execute(String companyName, List<String> articleIds) {
    if (CollectionUtils.isEmpty(articleIds) || StringUtils.isBlank(companyName)) {
      return Collections.emptyList();
    }

    final ServletRequestAttributes attributes =
        (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

    final ArticleSearchCriteria criteria = ArticleSearchCriteria.builder()
        .companyName(companyName).vendors(Lists.newArrayList()).updateVendors(true)
        .build();

    final int defaultRequestSize = appProps.getErpConfig().getMaxRequestSize();
    Optional.ofNullable(artErpExternalExecutorBuilder.buildAsyncCallableCreator(criteria,
        articleIds, defaultRequestSize, attributes)).ifPresent(CompletableFuture::join);

    return criteria.getVendors();
  }

}
