package com.sagag.services.ivds.executor.impl.topmotive;

import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.article.api.domain.article.AdditionalSearchCriteria;
import com.sagag.services.common.profiles.TopmotiveErpProfile;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.ivds.executor.impl.AbstractIvdsPerfectMatchArticleTaskExecutor;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ListUtils;
import org.assertj.core.util.Lists;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Component
@TopmotiveErpProfile
public class TmIvdsPerfectMatchArticleTaskExecutorImpl
  extends AbstractIvdsPerfectMatchArticleTaskExecutor {

  @Override
  public List<ArticleDocDto> executePerfectMatchTask(UserInfo user, Page<ArticleDocDto> articles,
      Pageable pageable, Optional<AdditionalSearchCriteria> additional) {
    if (!isValidToRequestAvailability(articles, pageable)) {
      return ListUtils.defaultIfNull(articles.getContent(), Lists.newArrayList());
    }
    final List<ArticleDocDto> limitedArticlesToGetAvail =
        splitListOfArticlesToRequestAvailabilitiy(articles);

    final List<ArticleDocDto> updatedErpArticles = ivdsArticleTaskExecutors
        .executeTaskPriceAndAvailabilityWithoutVehicle(user, limitedArticlesToGetAvail);

    return processSortedPerfectMatchArticles(articles, limitedArticlesToGetAvail,
        updatedErpArticles);
  }

  @Override
  public List<ArticleDocDto> executeGetPriceTask(UserInfo user, List<ArticleDocDto> allArticles,
      Page<ArticleDocDto> articles, Optional<Integer> finalCustomerOrgIdOpt,
      Optional<AdditionalSearchCriteria> additional) {
    final Map<String, ArticleDocDto> articlesMap = allArticles.stream()
        .collect(Collectors.toMap(ArticleDocDto::getArtid, Function.identity(),
            (oldArt, newArt) -> newArt));

    articles.forEach(article -> Optional.ofNullable(articlesMap.get(article.getIdSagsys()))
        .ifPresent(updatedArt -> updatePriceAndAvailabilities(article, updatedArt)));
    if (articles.stream().allMatch(hasPricePredicate())) {
      return articles.getContent();
    }

    return ivdsArticleTaskExecutors.executeTaskWithPriceOnlyWithoutVehicle(user,
        articles.getContent(), finalCustomerOrgIdOpt, additional);
  }

  private static void updatePriceAndAvailabilities(ArticleDocDto article,
      ArticleDocDto updatedArt) {
    article.setPrice(updatedArt.getPrice());
    article.setAvailabilities(updatedArt.getAvailabilities());
    article.setAutonetInfos(updatedArt.getAutonetInfos());
  }

  private static Predicate<ArticleDocDto> hasPricePredicate() {
    return article -> {
      // With Stakis and Autonet ERP, we just has single request for Price, Availability.
      // So we no need request again for Pricing at this case.
      if (article.isAvailRequested()) {
        return true;
      }
      return article.hasPrice()
          || (article.getAutonetInfos() != null
            && !CollectionUtils.isEmpty(article.getAutonetInfos().getPrices()));
    };
  }

}
