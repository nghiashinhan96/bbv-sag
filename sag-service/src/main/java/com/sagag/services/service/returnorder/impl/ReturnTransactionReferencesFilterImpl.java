package com.sagag.services.service.returnorder.impl;

import com.sagag.services.article.api.ArticleExternalService;
import com.sagag.services.article.api.ReturnExternalOrderService;
import com.sagag.services.article.api.executor.callable.ChunkedRequestProcessor;
import com.sagag.services.common.config.AppProperties;
import com.sagag.services.common.enums.SupportedAffiliate;
import com.sagag.services.common.enums.UserType;
import com.sagag.services.domain.sag.erp.Article;
import com.sagag.services.domain.sag.erp.BulkArticleResult;
import com.sagag.services.domain.sag.returnorder.TransactionReferenceDto;
import com.sagag.services.service.returnorder.ReturnTransactionReferencesFilter;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Component
@Slf4j
public class ReturnTransactionReferencesFilterImpl implements ReturnTransactionReferencesFilter,
  ChunkedRequestProcessor<TransactionReferenceDto> {

  @Autowired
  private ArticleExternalService articleExtService;

  @Autowired
  private ReturnExternalOrderService axReturnOrderService;

  @Autowired
  private AppProperties appProps;

  @Override
  public List<TransactionReferenceDto> filterTransactionReferences(SupportedAffiliate affiliate,
      String reference, String userType) {
    final List<TransactionReferenceDto> references =
        filterTransactionReferencesFromAx(affiliate, reference);
    if (CollectionUtils.isEmpty(references)) {
      return Collections.emptyList();
    }

    // Request ERP article information from transaction references.
    // #4977: [CH-AX] New article extensions VOC, VRG (similar to depot)
    // can be used by C4S & customer: Return function
    // From request: https://app.assembla.com/spaces/sag-eshop/tickets/4977/details?comment=1671542423
    final List<String> articleIds = references.stream().map(TransactionReferenceDto::getArticleId)
        .distinct().collect(Collectors.toList());
    final Map<String, BulkArticleResult> erpArticles = searchErpArticles(affiliate, articleIds);

    // #7161: [All builds][C4S][Return Order] exclude non-returnable articles in search result
    if (UserType.ON_BEHALF_ADMIN.name().equals(userType)) {
      erpArticles.entrySet().removeIf(art -> Objects.nonNull(art.getValue())
          && Objects.nonNull(art.getValue().getArticle())
          && art.getValue().getArticle().isNonReturnable());
    }

    final List<TransactionReferenceDto> mainTransactionReferences =
        findMainTransactionReferences(references, erpArticles);
    final List<TransactionReferenceDto> attachedTransactionReferences =
        findAttachedTransactionReferences(references, erpArticles);

    // Combined attached articles into result list.
    final List<TransactionReferenceDto> updatedTransactionReferences = new ArrayList<>();
    String articleId;
    BulkArticleResult bulkArticleResult;
    Article erpArticle;
    List<TransactionReferenceDto> attachedTransReferences;
    for (TransactionReferenceDto transactionRef : mainTransactionReferences) {
      articleId = transactionRef.getArticleId();
      bulkArticleResult = erpArticles.get(articleId);
      if (bulkArticleResult == null || bulkArticleResult.getArticle() == null) {
        continue;
      }
      erpArticle = bulkArticleResult.getArticle();
      attachedTransReferences = new ArrayList<>();

      // Check article contain depot or recycle
      if (erpArticle.hasDepotArticleId()) {
        findAttachedTransactionReference(attachedTransactionReferences,
            erpArticle.getDepotArticleId())
        .ifPresent(updateAttachedTransactionReference(attachedTransReferences, true));
      }

      if (erpArticle.hasRecycleArticleId()) {
        findAttachedTransactionReference(attachedTransactionReferences,
            erpArticle.getRecycleArticleId())
        .ifPresent(updateAttachedTransactionReference(attachedTransReferences, true));
      }

      // Check article contain VOC or VRG
      if (erpArticle.hasVocArticleId()) {
        findAttachedTransactionReference(
          attachedTransactionReferences, erpArticle.getVocArticleId())
        .ifPresent(updateAttachedTransactionReference(attachedTransReferences, false));
      }

      if (erpArticle.hasVrgArticleId()) {
        findAttachedTransactionReference(
          attachedTransactionReferences, erpArticle.getVrgArticleId())
        .ifPresent(updateAttachedTransactionReference(attachedTransReferences, false));
      }

      if (!CollectionUtils.isEmpty(attachedTransactionReferences)) {
        transactionRef.setAttachedTransactionReferences(attachedTransReferences);
      }
      updatedTransactionReferences.add(transactionRef);
    }
    return updatedTransactionReferences;
  }

  private static Consumer<TransactionReferenceDto> updateAttachedTransactionReference(
      List<TransactionReferenceDto> attachedTransactionReferences, boolean editable) {
    return attachedTransReference -> {
      attachedTransReference.setEditable(editable);
      attachedTransactionReferences.add(attachedTransReference);
    };
  }

  private static List<TransactionReferenceDto> findMainTransactionReferences(
      final List<TransactionReferenceDto> references,
      final Map<String, BulkArticleResult> erpArticles) {
    return findTransactionReferencesByType(references, erpArticles, false);
  }

  private static List<TransactionReferenceDto> findAttachedTransactionReferences(
      final List<TransactionReferenceDto> references,
      final Map<String, BulkArticleResult> erpArticles) {
    return findTransactionReferencesByType(references, erpArticles, true);
  }

  private static List<TransactionReferenceDto> findTransactionReferencesByType(
      final List<TransactionReferenceDto> references,
      final Map<String, BulkArticleResult> erpArticles, boolean isAttachedType) {
    List<String> attachedErpArticleIds = new ArrayList<>();
    erpArticles.forEach((articleId, bulkArticle) -> {
      Article erpArticle = bulkArticle.getArticle();
      if (erpArticle.hasDepotArticleId()) {
        attachedErpArticleIds.add(String.valueOf(erpArticle.getDepotArticleId()));
      }

      if (erpArticle.hasRecycleArticleId()) {
        attachedErpArticleIds.add(String.valueOf(erpArticle.getRecycleArticleId()));
      }

      if (erpArticle.hasVocArticleId()) {
        attachedErpArticleIds.add(String.valueOf(erpArticle.getVocArticleId()));
      }

      if (erpArticle.hasVrgArticleId()) {
        attachedErpArticleIds.add(String.valueOf(erpArticle.getVrgArticleId()));
      }
    });

    final Predicate<TransactionReferenceDto> transactionReferencesPredicate;
    if (isAttachedType) {
      transactionReferencesPredicate =
          transReference -> attachedErpArticleIds.contains(transReference.getArticleId());
    } else {
      transactionReferencesPredicate =
          transReference -> !attachedErpArticleIds.contains(transReference.getArticleId());
    }
    return references.stream().filter(transactionReferencesPredicate).collect(Collectors.toList());
  }

  private static Optional<TransactionReferenceDto> findAttachedTransactionReference(
      List<TransactionReferenceDto> attachedTransactionReferences, String attachedArticleId) {
    return attachedTransactionReferences.stream()
        .filter(reference -> StringUtils.equalsIgnoreCase(reference.getArticleId(),
            attachedArticleId)).findFirst();
  }

  private Map<String, BulkArticleResult> searchErpArticles(SupportedAffiliate affiliate,
    List<String> articleIds) {
    if (CollectionUtils.isEmpty(articleIds)) {
      return Collections.emptyMap();
    }
    final List<List<String>> articleIdPartitions = partition(articleIds);
    final Map<String, BulkArticleResult> articlesRes = new HashMap<>();
    for (final List<String> articleIdPartition : articleIdPartitions) {
      try {
        articlesRes.putAll(articleExtService.searchByArticleIds(
          affiliate.getCompanyName(), articleIdPartition));
      } catch (final RestClientException e) {
        log.error("Getting articles from return reference has error", e);
      }
    }

    return articlesRes;
  }

  private List<TransactionReferenceDto> filterTransactionReferencesFromAx(
      SupportedAffiliate affiliate, String reference) {
    log.debug("Searching transaction references by affiliate = {} and reference = {}", affiliate,
        reference);
    if (affiliate == null || StringUtils.isBlank(reference)) {
      return Collections.emptyList();
    }
    return axReturnOrderService.searchTransactionReferences(affiliate.getCompanyName(), reference);
  }

  @Override
  public int maxRequestSize() {
    return appProps.getErpConfig().getMaxRequestSize();
  }
}
