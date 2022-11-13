package com.sagag.services.elasticsearch.config;

import com.sagag.services.common.profiles.AutonetProfile;
import com.sagag.services.elasticsearch.config.indices.CustomElasticsearchConfigProperties;
import com.sagag.services.elasticsearch.domain.article.FitmentArticle;
import com.sagag.services.elasticsearch.enums.ArticleField;
import com.sagag.services.elasticsearch.enums.Index;
import com.sagag.services.elasticsearch.query.articles.article.freetext.IFreetextFields;
import java.util.function.Function;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ElasticsearchCoreConfiguration {

  @Bean
  @ConditionalOnMissingBean(IFreetextFields.class)
  public IFreetextFields defaultFreetextFields() {
    return new IFreetextFields() { };
  }

  @Bean
  @AutonetProfile
  public ArticleIdFieldMapper autonetArtIdFieldMapper() {
    return new ArticleIdFieldMapper() {

      @Override
      public ArticleField getField() {
        return ArticleField.ID_AUTONET;
      }

      @Override
      public Function<FitmentArticle, String> articleIdExtractor() {
        return FitmentArticle::getIdAutonet;
      }

      @Override
      public Index.Fitment getArtIdFitment() {
        return Index.Fitment.ID_AUTONET;
      }
    };
  }

  @Bean
  @ConditionalOnMissingBean(ArticleIdFieldMapper.class)
  public ArticleIdFieldMapper defaultArticleIdFieldMapper() {
    return new ArticleIdFieldMapper() {

      @Override
      public ArticleField getField() {
        return ArticleField.ID_SAGSYS;
      }

      @Override
      public Function<FitmentArticle, String> articleIdExtractor() {
        return FitmentArticle::getIdSagsys;
      }

      @Override
      public Index.Fitment getArtIdFitment() {
        return Index.Fitment.ARTICLE_ID;
      }
    };
  }

  @Bean
  @ConfigurationProperties(prefix = "elasticsearch.custom")
  public CustomElasticsearchConfigProperties customElasticsearchConfigProperties() {
    return new CustomElasticsearchConfigProperties();
  }

}
