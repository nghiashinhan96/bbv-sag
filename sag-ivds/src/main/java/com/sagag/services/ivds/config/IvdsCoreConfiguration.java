package com.sagag.services.ivds.config;

import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.common.exception.ServiceException;
import com.sagag.services.common.utils.SagBeanUtils;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.domain.article.oil.OilTypeIdsDto;
import com.sagag.services.domain.eshop.dto.VehicleDto;
import com.sagag.services.ivds.api.IvdsOilSearchService;
import com.sagag.services.ivds.converter.article.ArticleConverter;
import com.sagag.services.ivds.utils.ArticleInformationExtractor;
import com.sagag.services.ivds.utils.DefaultArticleInformationExtractorImpl;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Page;

@Configuration
public class IvdsCoreConfiguration {

  @Bean
  @ConditionalOnMissingBean(IvdsOilSearchService.class)
  public IvdsOilSearchService defaultIvdsOilSearchService() {
    return new IvdsOilSearchService() {

      @Override
      public List<OilTypeIdsDto> getOilTypesByVehicleId(Optional<VehicleDto> vehicleOpt,
          List<String> oilGenericArticleIds, List<String> categoryIds) throws ServiceException {
        return Collections.emptyList();
      }

      @Override
      public Page<ArticleDocDto> searchOilRecommendArticles(UserInfo user, List<String> oilIds,
          List<String> oilGenericArticleIds, Optional<VehicleDto> vehicleOpt,
          List<String> categoryIds) throws ServiceException {
        return Page.empty();
      }

      @Override
      public List<String> extractOilGenericArticleIds(Collection<String> gaIds) {
        return Collections.emptyList();
      }

    };
  }

  @Bean
  @ConditionalOnMissingBean(ArticleConverter.class)
  public ArticleConverter defaultArticleConverterImpl() {
    return article -> {
      ArticleDocDto articleDocDto = SagBeanUtils.map(article, ArticleDocDto.class);
      articleDocDto.setOriginIdSagsys(article.getIdSagsys());
      return articleDocDto;
    };
  }

  @Bean
  @ConditionalOnMissingBean(ArticleInformationExtractor.class)
  public ArticleInformationExtractor defaultArticleInformationExtractorImpl() {
    return new DefaultArticleInformationExtractorImpl();
  }

}
