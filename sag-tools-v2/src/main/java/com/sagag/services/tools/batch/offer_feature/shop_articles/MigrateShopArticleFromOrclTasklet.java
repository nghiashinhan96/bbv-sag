package com.sagag.services.tools.batch.offer_feature.shop_articles;

import com.sagag.services.tools.batch.AbstractTasklet;
import com.sagag.services.tools.config.OracleProfile;
import com.sagag.services.tools.domain.source.SourceShopArticle;
import com.sagag.services.tools.domain.target.TargetShopArticle;
import com.sagag.services.tools.exception.BatchJobException;
import com.sagag.services.tools.repository.source.SourceShopArticleRepository;
import com.sagag.services.tools.repository.target.TargetShopArticleRepository;
import com.sagag.services.tools.support.CommonInitialResource;
import com.sagag.services.tools.support.OfferTables;
import com.sagag.services.tools.utils.CsvUtils;
import com.sagag.services.tools.utils.DefaultUtils;
import com.sagag.services.tools.utils.EntityManagerUtils;
import com.sagag.services.tools.utils.EnvUtils;
import com.sagag.services.tools.utils.ToolConstants;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;
import org.hibernate.internal.SessionImpl;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

@Component
@OracleProfile
@Slf4j
public class MigrateShopArticleFromOrclTasklet extends AbstractTasklet {

  private static final String TABLE = OfferTables.SHOP_ARTICLE.getTableName();

  @Autowired(required = false)
  private SourceShopArticleRepository sourceShopArticleRepo;

  @Autowired
  private TargetShopArticleRepository targetShopArticleRepo;

  @Autowired
  protected CommonInitialResource commonInitialResource;

  @Override
  @Transactional
  public RepeatStatus execute(StepContribution contribution,
    ChunkContext chunkContext) throws BatchJobException {

    logActiveProfile();
    // Update custom config to set exactly id
    SessionImpl session = (SessionImpl) targetEntityManager.getDelegate();
    EntityManagerUtils.turnOnIdentityInsert(session, TABLE);

    long totalRecords = sourceShopArticleRepo.count();
    int maxSize = ToolConstants.MAX_SIZE;
    int totalPage = (int) (totalRecords / maxSize) + 1;

    List<SourceShopArticle> canNotMigratedArticles = new ArrayList<>();
    List<TargetShopArticle> targetShopArticleList;
    Page<SourceShopArticle> sourceShopArticlePage;
    for (int curPage = 0; curPage < totalPage; curPage++) {
      sourceShopArticlePage = sourceShopArticleRepo.findAll(PageRequest.of(curPage, maxSize));
      if (!sourceShopArticlePage.hasContent()) {
        continue;
      }

      targetShopArticleList = sourceShopArticlePage.getContent().stream()
        .peek(source -> log.debug("Source object = {}", source))
        .map(source -> {
          // Just map real id with prod env
          Integer orgId = defaultOrgId;
          final Long createdUserId = defaultUserId;
          final Long modifiedUserId = defaultUserId;
          Integer currencyId = commonInitialResource.getCurrencyId(source.getCurrencyISO());
          if (EnvUtils.isProductionEnv(activeProfile)) {
            orgId = commonInitialResource.getOrgId(source.getOrganisationId());
          }
          if (orgId == null) {
            canNotMigratedArticles.add(source);
            return null;
          }
          return mapSourceToTarget(source, orgId, createdUserId, modifiedUserId, currencyId);
        })
        .filter(Objects::nonNull)
        .peek(target -> log.debug("Target object = {}", target))
        .collect(Collectors.toList());
      targetShopArticleRepo.saveAll(targetShopArticleList);
    }

    CsvUtils.write(SystemUtils.getUserDir() + "/csv/shop_articles_not_migrated_data.csv", canNotMigratedArticles);

    return finish(contribution);
  }

  private static TargetShopArticle mapSourceToTarget(final SourceShopArticle source, final Integer orgId,
    final Long createdUserId, final Long modifiedUserId, final Integer currencyId) {
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

}
