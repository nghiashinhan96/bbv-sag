package com.sagag.services.tools.batch.offer_v2.shop_article;

import com.sagag.services.tools.batch.offer_v2.AbstractMissingOfferSimpleStep;
import com.sagag.services.tools.batch.offer_v2.OfferJpaQueryProvider;
import com.sagag.services.tools.batch.offer_v2.OfferUtils;
import com.sagag.services.tools.config.OracleProfile;
import com.sagag.services.tools.domain.source.SourceShopArticle;
import com.sagag.services.tools.domain.target.TargetShopArticle;

import lombok.extern.slf4j.Slf4j;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.step.builder.SimpleStepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.persistence.Query;

@Configuration
@OracleProfile
@Order(OfferUtils.ORDERED_SHOP_ARTICLE)
@Slf4j
public class MissingShopArticleMigrationByCustomerStep
    extends AbstractMissingOfferSimpleStep<SourceShopArticle, TargetShopArticle> {

  @Autowired
  private ShopArticleMigrationByCustomerStep step;

  @Override
  public SimpleStepBuilder<SourceShopArticle, TargetShopArticle> stepBuilder() {
    return stepBuilder("MissingShopArticleMigrationByCustomerStep");
  }

  @Bean("MissingShopArticleMigrationByCustomerStepItemReader")
  @StepScope
  @Override
  public ItemStreamReader<SourceShopArticle> itemReader() throws Exception {
    final String customerNr = sysVars.getDiffCustomerNr();
    final Optional<Long> eblOrgIdOpt = mappingUserIdEblConnectService.searchEblOrgId(customerNr);
    final List<Long> vendorIds = eblOrgIdOpt.map(Arrays::asList)
        .orElseGet(() -> Collections.emptyList());
    final OfferJpaQueryProvider queryProvider = new OfferJpaQueryProvider() {

      @Override
      public Query createQuery() {
        log.debug("Creating query for the vendorIds = {}", vendorIds);
        final String query = "select a from SourceShopArticle a where a.organisationId in (:vendorIds)";
        return this.getEntityManager().createQuery(query, SourceShopArticle.class)
          .setParameter("vendorIds", vendorIds);
      }
    };
    return jpaPagingItemReader(sourceEntityManagerFactory, queryProvider);
  }

  @Bean("MissingShopArticleMigrationByCustomerStepItemProcessor")
  @StepScope
  @Override
  public ItemProcessor<SourceShopArticle, TargetShopArticle> itemProcessor() {
    return step.itemProcessor();
  }

  @Bean("MissingShopArticleMigrationByCustomerStepItemWriter")
  @StepScope
  @Override
  public ItemWriter<TargetShopArticle> itemWriter() {
    return step.itemWriter();
  }

}
