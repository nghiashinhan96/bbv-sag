package com.sagag.services.ivds.executor.impl.topmotive;

import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.article.api.executor.ArticleSearchCriteria.ArticleSearchCriteriaBuilder;
import com.sagag.services.common.profiles.TopmotiveErpProfile;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.domain.eshop.dto.VehicleDto;
import com.sagag.services.ivds.domain.ArticleExternalRequestOption;
import com.sagag.services.ivds.executor.impl.AbstractIvdsArticleTaskExecutor;

import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
@TopmotiveErpProfile
public class TmIvdsArticleTaskExecutorImpl extends AbstractIvdsArticleTaskExecutor {

  @Override
  public ArticleSearchCriteriaBuilder initArticleSearchCriteriaBuilder(UserInfo user,
      List<ArticleDocDto> updatedArticles, VehicleDto vehicleDto,
      ArticleExternalRequestOption option) {
    ArticleSearchCriteriaBuilder builder = super.initArticleSearchCriteriaBuilder(user,
        updatedArticles, vehicleDto, option);
    Optional.ofNullable(user.getExternalUserSession())
    .ifPresent(session -> {
      builder.extLanguage(session.getLId());
      builder.extCustomerId(session.getCustomerId());
      builder.extUsername(session.getUser());
      builder.extSecurityToken(session.getUid());
    });
    return builder;
  }

  @Override
  protected void loadAvailabilitiesInfo(ArticleSearchCriteriaBuilder builder, UserInfo user,
      String pickupBranchId) {
    builder.nextWorkingDate(null);
    builder.externalVendors(Collections.emptyList());
    builder.deliveryProfiles(Collections.emptyList());
  }

}
