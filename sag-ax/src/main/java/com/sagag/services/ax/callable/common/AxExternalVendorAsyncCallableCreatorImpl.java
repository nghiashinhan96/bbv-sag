package com.sagag.services.ax.callable.common;

import java.util.List;
import java.util.concurrent.Callable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sagag.services.article.api.ArticleExternalService;
import com.sagag.services.article.api.domain.vendor.VendorDto;
import com.sagag.services.article.api.executor.ArticleSearchCriteria;
import com.sagag.services.article.api.executor.callable.ExternalVendorCallableCreator;
import com.sagag.services.article.api.request.ExternalVendorRequestBody;
import com.sagag.services.common.executor.CallableCreator;
import com.sagag.services.common.profiles.AxProfile;

@Component
@AxProfile
public class AxExternalVendorAsyncCallableCreatorImpl implements ExternalVendorCallableCreator {

  @Autowired
  private ArticleExternalService articleExtService;

  @Override
  public Callable<List<VendorDto>> create(ExternalVendorRequestBody request, Object... objects) {
    return () -> {
      CallableCreator.setupThreadContext(objects);
      final List<VendorDto> vendors = articleExtService.searchVendors(request.getCompanyName(),
          request.getArticleIds());
      ArticleSearchCriteria.class.cast(objects[1]).addVendors(vendors);
      return vendors;
    };
  }

}
