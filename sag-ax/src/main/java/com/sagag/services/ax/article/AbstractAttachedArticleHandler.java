package com.sagag.services.ax.article;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.sagag.services.article.api.attachedarticle.AttachedArticleHandler;
import com.sagag.services.article.api.attachedarticle.AttachedArticleRequestBuilder;
import com.sagag.services.article.api.executor.ArticleSearchCriteria;
import com.sagag.services.article.api.executor.AttachedArticleSearchCriteria;
import com.sagag.services.article.api.executor.AttachedArticleSearchExternalExecutor;
import com.sagag.services.article.api.request.AttachedArticleRequest;
import com.sagag.services.ax.executor.helper.AxArticleSearchHelper;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.domain.sag.erp.Article;
import com.sagag.services.domain.sag.erp.Availability;
import com.sagag.services.domain.sag.erp.ErpArticleMemo;
import com.sagag.services.domain.sag.erp.ErpArticleMemoKey;
import com.sagag.services.domain.sag.erp.ItemApprovalTypeName;
import com.sagag.services.domain.sag.external.CustomerApprovalType;

public abstract class AbstractAttachedArticleHandler implements AttachedArticleHandler {

  @Autowired(required = false)
  protected AttachedArticleRequestBuilder attachedArticleRequestBuilder;

  @Autowired
  protected AttachedArticleSearchExternalExecutor<Map<String, ArticleDocDto>> attachedArticleSearchExternalExecutor;

  @Autowired(required = false)
  protected AxArticleSearchHelper axArticleSearchHelper;

  @Override
  public void buildMemoList(List<ArticleDocDto> filteredArticles, ArticleSearchCriteria criteria) {
    if (CollectionUtils.isEmpty(filteredArticles)) {
      return;
    }

    if (isMissingErpArticleInfo(filteredArticles)) {
      thenFulFillMissingErpArticleData(filteredArticles, criteria);
    }
  }

  @Override
  public void doFilterAvailabilityFgasCases(List<ArticleDocDto> finalArticles,
      ArticleSearchCriteria criteria) {
    CollectionUtils.emptyIfNull(finalArticles).forEach(art -> {
      if (CollectionUtils.isNotEmpty(art.getAvailabilities())) {
        Article articleErp = art.getArticle();
        if (Objects.nonNull(articleErp) && existingValidItemApprovalType(articleErp)
            && !axArticleSearchHelper.isMatchingCertificates(articleErp.getItemApprovalTypeNames(),
                criteria.getCustApprovalTypes())) {
          // #6302
          resetAvailabilities(art);
        }
      }
    });
  }

  protected boolean existingValidItemApprovalType(Article articleErp) {
    return CollectionUtils.isNotEmpty(articleErp.getItemApprovalTypeNames())
        && articleErp.getItemApprovalTypeNames().stream()
            .anyMatch(approvalType -> StringUtils.isNotEmpty(approvalType.getApprovalTypename()));
  }

  protected Map<String, ArticleDocDto> getMapDepositArticle(List<ArticleDocDto> filteredArticles,
      ArticleSearchCriteria criteria) {
    List<AttachedArticleRequest> artReqs =
        attachedArticleRequestBuilder.buildAttachedArticleDepositRequestList(filteredArticles);

    if (CollectionUtils.isNotEmpty(artReqs)) {
      AttachedArticleSearchCriteria req = AttachedArticleSearchCriteria
          .createArticleRequestForFilter(criteria.getAffiliate(), criteria.getCustNr(),
              criteria.isCalculateVatPriceRequired(), criteria.getVatRate(), artReqs);
      req.setPriceTypeDisplayEnum(criteria.getPriceTypeDisplayEnum());

      return attachedArticleSearchExternalExecutor.execute(req);
    }

    return Collections.emptyMap();
  }

  protected void resetAvailabilities(ArticleDocDto art) {
    Availability resetAvailability = new Availability();
    resetAvailability.setSendMethodCode(art.getAvailabilities().stream()
        .filter(avail -> StringUtils.isNotEmpty(avail.getSendMethodCode()))
        .map(Availability::getSendMethodCode).findFirst().orElse(null));
    art.setAvailabilities(Arrays.asList(resetAvailability));
  }

  protected boolean allowedAddToShoppingCart(List<ItemApprovalTypeName> itemApprovals,
      List<CustomerApprovalType> custApprovalTypes) {
    return axArticleSearchHelper.isMatchingCertificates(itemApprovals, custApprovalTypes);
  }

  protected void activeMemo(String key, List<ErpArticleMemo> articleMemos) {
    articleMemos.stream().filter(memo -> StringUtils.equals(key, memo.getStatusKey())).findFirst()
        .ifPresent(memo -> memo.setStatusValue(ErpArticleMemoKey.StatusValue.YES.getValue()));
  }

  protected List<ErpArticleMemo> buildArtMemos() {
    List<ErpArticleMemo> articleMemos = new LinkedList<>();

    ErpArticleMemo deposit =
        ErpArticleMemo.builder().type(0).statusKey(ErpArticleMemoKey.DEPOSIT_ITEM.getKey())
            .statusValue(ErpArticleMemoKey.DEPOSIT_ITEM.getDefaultValue().getValue()).build();
    ErpArticleMemo fgas =
        ErpArticleMemo.builder().type(0).statusKey(ErpArticleMemoKey.FGAS.getKey())
            .statusValue(ErpArticleMemoKey.FGAS.getDefaultValue().getValue()).build();
    ErpArticleMemo nonReturnable =
        ErpArticleMemo.builder().type(0).statusKey(ErpArticleMemoKey.NON_RETURNABLE.getKey())
            .statusValue(ErpArticleMemoKey.NON_RETURNABLE.getDefaultValue().getValue()).build();

    articleMemos.add(nonReturnable);
    articleMemos.add(deposit);
    articleMemos.add(fgas);

    return articleMemos;
  }
  
  protected void setArticleVrgArticle(Map<String, ArticleDocDto> mapDepositArticle,
      ArticleDocDto art, Article articleErp) {
    activeMemo(ErpArticleMemoKey.DEPOSIT_ITEM.getKey(), art.getMemos());
    art.setVrgArticle(mapDepositArticle.get(articleErp.getVrgArticleId()));
  }
  
  protected void setArticleVocArticle(Map<String, ArticleDocDto> mapDepositArticle,
      ArticleDocDto art, Article articleErp) {
    activeMemo(ErpArticleMemoKey.DEPOSIT_ITEM.getKey(), art.getMemos());
    art.setVocArticle(mapDepositArticle.get(articleErp.getVocArticleId()));
  }

  private void thenFulFillMissingErpArticleData(List<ArticleDocDto> filteredArticles,
      ArticleSearchCriteria criteria) {
    final ServletRequestAttributes attributes =
        (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
    List<ArticleDocDto> artWithErpInfo = axArticleSearchHelper.filterArticles(criteria, attributes);
    if (CollectionUtils.isEmpty(artWithErpInfo)) {
      return;
    }
    CollectionUtils.emptyIfNull(filteredArticles).forEach(beUpdateArt -> {
      Optional<ArticleDocDto> erpArtOpt = artWithErpInfo.stream()
          .filter(erpArt -> StringUtils.equals(erpArt.getArtid(), beUpdateArt.getArtid())
              && Objects.nonNull(erpArt.getArticle()))
          .findFirst();
      if (erpArtOpt.isPresent()) {
        beUpdateArt.setArticle(erpArtOpt.get().getArticle());
      }
    });
  }

  private static boolean isMissingErpArticleInfo(List<ArticleDocDto> filteredArticles) {
    return CollectionUtils.emptyIfNull(filteredArticles).stream()
        .anyMatch(art -> Objects.isNull(art.getArticle()));
  }
}
