package com.sagag.services.stakis.erp.executor.callable;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

import org.apache.commons.collections4.ListUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sagag.services.article.api.SoapArticleErpExternalService;
import com.sagag.services.article.api.executor.ArticleSearchCriteria;
import com.sagag.services.article.api.executor.callable.AsyncUpdateMode;
import com.sagag.services.article.api.executor.callable.ErpCallableCreator;
import com.sagag.services.common.executor.CallableCreator;
import com.sagag.services.common.profiles.CzProfile;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.domain.eshop.dto.VehicleDto;
import com.sagag.services.stakis.erp.utils.StakisConstants;

@Component
@CzProfile
public class StakisArticlePriceAndAvailabilitiesCallableCreatorImpl implements ErpCallableCreator {

  @Autowired
  private SoapArticleErpExternalService articleExtService;

  @Override
  public Callable<List<ArticleDocDto>> create(ArticleSearchCriteria criteria, Object... objects) {
    return () -> {
      CallableCreator.setupThreadContext(objects);
      return updateArticlePriceAndAvailabilities(criteria);
    };
  }

  private List<ArticleDocDto> updateArticlePriceAndAvailabilities(ArticleSearchCriteria criteria) {
    final String username = criteria.getExtUsername();
    final String custId = criteria.getExtCustomerId();
    final String password = criteria.getExtSecurityToken();
    final String language = criteria.getLanguage();
    final double vatRate = criteria.getVatRate();
    final List<ArticleDocDto> articles = criteria.getArticles();
    final Optional<VehicleDto> vehOpt = Optional.ofNullable(criteria.getVehicle());

    final List<List<ArticleDocDto>> bundles =
        ListUtils.partition(articles, StakisConstants.BATCH_SIZE);
    return bundles.stream().map(
        updateArticlePricesAndAvailabilities(username, password, custId, language, vehOpt, vatRate))
        .flatMap(List::stream).collect(Collectors.toList());
  }

  private UnaryOperator<List<ArticleDocDto>> updateArticlePricesAndAvailabilities(
      final String username, final String password, final String custId, final String language,
      final Optional<VehicleDto> vehOpt, final double vatRate) {
    return articles -> articleExtService.searchArticlePricesAndAvailabilities(username, custId,
        password, language, articles, vatRate, vehOpt);
  }

  @Override
  public AsyncUpdateMode asyncUpdateMode() {
    return AsyncUpdateMode.PRICE;
  }

}
