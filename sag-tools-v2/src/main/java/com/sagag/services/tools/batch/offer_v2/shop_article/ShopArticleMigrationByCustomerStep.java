package com.sagag.services.tools.batch.offer_v2.shop_article;

import com.sagag.services.tools.batch.offer_v2.AbstractOfferMigrationSimpleStep;
import com.sagag.services.tools.batch.offer_v2.OfferJpaQueryProvider;
import com.sagag.services.tools.batch.offer_v2.OfferUtils;
import com.sagag.services.tools.config.OracleProfile;
import com.sagag.services.tools.domain.source.SourceShopArticle;
import com.sagag.services.tools.domain.target.TargetShopArticle;
import com.sagag.services.tools.repository.target.CurrencyRepository;
import com.sagag.services.tools.repository.target.MappingUserIdEblConnectRepository;
import com.sagag.services.tools.repository.target.TargetShopArticleRepository;
import com.sagag.services.tools.service.MappingUserIdEblConnectService;
import com.sagag.services.tools.support.OfferTables;
import com.sagag.services.tools.utils.DefaultUtils;
import com.sagag.services.tools.utils.EntityManagerUtils;
import com.sagag.services.tools.utils.EnvUtils;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.internal.SessionImpl;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.step.builder.SimpleStepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.persistence.Query;

@Configuration
@OracleProfile
@Order(OfferUtils.ORDERED_SHOP_ARTICLE)
@Slf4j
public class ShopArticleMigrationByCustomerStep
    extends AbstractOfferMigrationSimpleStep<SourceShopArticle, TargetShopArticle> {

  private static final String TABLE = OfferTables.SHOP_ARTICLE.getTableName();

  @Value("${spring.profiles.active}")
  private String activeProfile;

  @Value("${default.userId:}")
  private Long defaultUserId;

  @Value("${default.organisationId:}")
  private Integer defaultOrgId;

  @Autowired
  private MappingUserIdEblConnectRepository mappingRepo;

  @Autowired
  private CurrencyRepository currencyRepo;

  @Autowired
  private MappingUserIdEblConnectService mappingService;

  @Autowired
  private TargetShopArticleRepository targetShopArticleRepo;

  @Override
  public SimpleStepBuilder<SourceShopArticle, TargetShopArticle> stepBuilder() {
    return stepBuilder("shopArticleMigrationByCustomerStep");
  }

  @Bean("shopArticleMigrationByCustomerStepItemReader")
  @StepScope
  @Override
  public ItemStreamReader<SourceShopArticle> itemReader() throws Exception {
    final List<Long> vendorIds =
        mappingUserIdEblConnectService.searchVendorIds(sysVars.getCustNrs());
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

  @Bean("shopArticleMigrationByCustomerStepItemProcessor")
  @StepScope
  @Override
  public ItemProcessor<SourceShopArticle, TargetShopArticle> itemProcessor() {
    return source -> {
      log.debug("Shop Article ID = {}", source.getId());
      // Just map real id with prod env
      Integer orgId = defaultOrgId;
      Long createdUserId = defaultUserId;
      Long modifiedUserId = null;

      Integer currencyId = currencyRepo.findIdByIso(source.getCurrencyISO());
      if (EnvUtils.isProductionEnv(activeProfile)) {
        orgId = mappingRepo.findOrgIdByEbl(source.getOrganisationId());
        createdUserId = mappingService.searchUserIdByEbl(source.getUserCreateId());
        modifiedUserId = mappingService.searchUserIdByEbl(source.getUserModifyId());
      }
      if (orgId == null || createdUserId == null) {
        return null;
      }
      return mapSourceToTarget(source, orgId, createdUserId, modifiedUserId, currencyId);
    };
  }

  private static TargetShopArticle mapSourceToTarget(final SourceShopArticle source, final Integer orgId,
      final Long createdUserId, final Long modifiedUserId,
      final Integer currencyId) {
    Assert.notNull(source, "The given source must not be null");
    TargetShopArticle target = new TargetShopArticle();
    target.setId(source.getId());
    target.setType(source.getType());
    target.setArticleNumber(StringUtils.defaultString(source.getArticleNumber()));
    target.setName(DefaultUtils.toUtf8Value(source.getName()));
    target.setDescription(DefaultUtils.toUtf8Value(source.getDescription()));
    target.setAmount(DefaultUtils.defaultDouble(source.getAmount()));
    target.setPrice(source.getPrice());
    target.setCreatedDate(source.getDateCreate());
    target.setModifiedDate(source.getDateModify());
    target.setTecstate(source.getTecState());
    target.setVersion(DefaultUtils.defaultInt(source.getVersion()));
    target.setOrganisationId(orgId);
    target.setCreatedUserId(createdUserId);
    target.setModifiedUserId(modifiedUserId);
    target.setCurrencyId(currencyId);
    return target;
  }

  @Bean("shopArticleMigrationByCustomerStepItemWriter")
  @StepScope
  @Override
  public ItemWriter<TargetShopArticle> itemWriter() {
    return items -> {
      SessionImpl session = (SessionImpl) targetEntityManager.getDelegate();
      EntityManagerUtils.turnOnIdentityInsert(session, TABLE);

      final List<TargetShopArticle> articles = items.stream()
          .filter(Objects::nonNull)
          .collect(Collectors.toList());
      targetShopArticleRepo.saveAll(articles);
    };
  }

}
