package com.sagag.services.stakis.erp.executor.callable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sagag.services.article.api.SoapArticleErpExternalService;
import com.sagag.services.article.api.domain.article.AdditionalSearchCriteria;
import com.sagag.services.article.api.executor.ArticleSearchCriteria;
import com.sagag.services.article.api.executor.callable.AsyncUpdateMode;
import com.sagag.services.article.api.executor.callable.ErpCallableCreator;
import com.sagag.services.common.executor.CallableCreator;
import com.sagag.services.common.profiles.CzProfile;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.domain.eshop.dto.VehicleDto;
import com.sagag.services.domain.sag.erp.Availability;
import com.sagag.services.stakis.erp.utils.StakisConstants;

@Component
@CzProfile
public class StakisArticleAvailabilitiesDetailCallableCreatorImpl implements ErpCallableCreator {

  @Autowired
  private SoapArticleErpExternalService articleExtService;

  @Override
  public Callable<List<ArticleDocDto>> create(ArticleSearchCriteria criteria, Object... objects) {
    return () -> {
      CallableCreator.setupThreadContext(objects);
      return updateArticleAvailabilitiesDetails(criteria);
    };
  }

  private List<ArticleDocDto> updateArticleAvailabilitiesDetails(ArticleSearchCriteria criteria) {
    final String username = criteria.getExtUsername();
    final String custId = criteria.getExtCustomerId();
    final String password = criteria.getExtSecurityToken();
    final String language = criteria.getLanguage();
    final double vatRate = criteria.getVatRate();
    final List<ArticleDocDto> articles = criteria.getArticles();
    final Optional<VehicleDto> vehOpt = Optional.ofNullable(criteria.getVehicle());
    final AdditionalSearchCriteria additional = criteria.getAdditional();

  List<ArticleDocDto> clonedArticles =
      articles.stream().map(SerializationUtils::clone).collect(Collectors.toList());

    // Avail of same articles belong to different vehicles should be requested together
    updateRequestedQuantityIfNecessary(clonedArticles);

    final List<List<ArticleDocDto>> bundles = ListUtils.partition(clonedArticles,
        StakisConstants.BATCH_SIZE);
    List<ArticleDocDto> articlesWithAvailabilities = bundles.stream()
        .map(updateArticlePricesAndAvailabilities(
            username, password, custId, language, vehOpt, additional, vatRate))
        .flatMap(List::stream).collect(Collectors.toList());

    articles.forEach(art -> {
      Optional<List<Availability>> responseAvail = articlesWithAvailabilities.stream()
          .filter(artWithAvail -> StringUtils.equalsIgnoreCase(
              artWithAvail.getArtid(), art.getArtid()))
          .map(ArticleDocDto::getAvailabilities).findFirst();
      if (responseAvail.isPresent()) {
        art.setAvailabilities(responseAvail.get());
      }
    });
    return articles;
  }

  private UnaryOperator<List<ArticleDocDto>> updateArticlePricesAndAvailabilities(
      final String username, final String password, final String custId, final String language,
      final Optional<VehicleDto> vehOpt, final AdditionalSearchCriteria additional,
      final double vatRate) {
    return articles -> articleExtService.searchArticleAvailabilitiesDetails(username, custId,
        password, language, articles, vatRate, vehOpt);
  }

  private void updateRequestedQuantityIfNecessary(final List<ArticleDocDto> articles) {
    Map<String, List<ArticleDocDto>> articlesById =
        articles.stream().collect(Collectors.groupingBy(ArticleDocDto::getArtid));
    Map<String, Integer> sameArticlesById = new HashMap<>();
    for (Entry<String, List<ArticleDocDto>> articleDocDto : articlesById.entrySet()) {
      if (articleDocDto.getValue().size() > 1) {
        sameArticlesById.put(articleDocDto.getKey(), articleDocDto.getValue().stream()
            .collect(Collectors.summingInt(ArticleDocDto::getAmountNumber)));
      }
    }

    articles.forEach(t -> {
      if (sameArticlesById.containsKey(t.getArtid())) {
        t.setAmountNumber(sameArticlesById.get(t.getArtid()));
      }
    });
  }

  @Override
  public AsyncUpdateMode asyncUpdateMode() {
      return AsyncUpdateMode.AVAILABILITY;
  }

}
