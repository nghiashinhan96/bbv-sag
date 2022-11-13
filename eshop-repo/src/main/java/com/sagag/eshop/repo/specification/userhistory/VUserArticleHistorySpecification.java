package com.sagag.eshop.repo.specification.userhistory;

import com.sagag.eshop.repo.criteria.user_history.UserArticleHistorySearchCriteria;
import com.sagag.eshop.repo.entity.user_history.VUserArticleHistory;
import com.sagag.eshop.repo.enums.ArticleHistorySearchType;
import com.sagag.eshop.repo.specification.AbstractSpecification;
import com.sagag.services.common.enums.ArticleSearchMode;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.util.Lists;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

@RequiredArgsConstructor
public class VUserArticleHistorySpecification extends AbstractSpecification<VUserArticleHistory> {

  private static final long serialVersionUID = 4151543998481352408L;

  private static final String SEARCH_MODE = "searchMode";

  private static final String SEARCH_TERM_WITH_ARTNR = "searchTermWithArtNr";

  @NonNull
  private UserArticleHistorySearchCriteria criteria;

  @Override
  public Predicate toPredicate(Root<VUserArticleHistory> root, CriteriaQuery<?> query,
      CriteriaBuilder criteriaBuilder) {
    final List<Predicate> predicates = new ArrayList<>();

    Optional.ofNullable(criteria.getFilterMode()).ifPresent(val -> UserHistorySpecificationHelper
        .addFilterMode(predicates, val, criteria.getFullName(), criteria.getUserId(),
            root, criteriaBuilder, AbstractSpecification::likeBetween));

    List<String> searchModes = extractArticleSearchMode(criteria.getSearchType());
    if (CollectionUtils.isNotEmpty(searchModes)) {
      predicates.add(root.get(SEARCH_MODE).in(searchModes));
    }

    Optional.ofNullable(criteria.getOrgId()).filter(Objects::nonNull)
        .ifPresent(val -> predicates.add(criteriaBuilder.equal(root.get("orgId"), val)));

    Optional.ofNullable(criteria.getSearchTerm()).filter(StringUtils::isNotBlank)
        .ifPresent(val -> predicates.add(criteriaBuilder.like(root.get(SEARCH_TERM_WITH_ARTNR),
            likeBetween(StringUtils.trim(val)))));

    Optional.ofNullable(criteria.getFromDate()).filter(Objects::nonNull).ifPresent(val -> predicates
        .add(criteriaBuilder.greaterThanOrEqualTo(root.get(
            UserHistorySpecificationHelper.SELECT_DATE_FIELD), val)));


    Optional.ofNullable(criteria.getToDate()).filter(Objects::nonNull).ifPresent(
        val -> predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get(
            UserHistorySpecificationHelper.SELECT_DATE_FIELD), val)));

    final List<Order> orders = new ArrayList<>();

    Optional.ofNullable(criteria.getOrderDescBySelectDate()).filter(Objects::nonNull)
        .ifPresent(val -> orders
            .add(defaultOrder(criteriaBuilder, root.get(
                UserHistorySpecificationHelper.SELECT_DATE_FIELD), val.booleanValue())));

    if (!CollectionUtils.isEmpty(orders)) {
      query.orderBy(orders);
    }

    return andTogether(predicates, criteriaBuilder);
  }

  private List<String> extractArticleSearchMode(ArticleHistorySearchType searchType) {
    if (searchType == null) {
      return Lists.emptyList();
    }
    if (searchType == ArticleHistorySearchType.FREE_TEXT) {
      return Lists.newArrayList(ArticleSearchMode.FREE_TEXT.name(),
          ArticleSearchMode.ARTICLE_ID.name());
    }
    if (searchType == ArticleHistorySearchType.ARTICLE_DESC) {
      return Lists.newArrayList(ArticleSearchMode.ARTICLE_DESC.name(),
          ArticleSearchMode.SHOPPING_LIST.name());
    }
    if (searchType == ArticleHistorySearchType.ARTICLE_NR) {
      return Lists.newArrayList(ArticleSearchMode.ARTICLE_NUMBER.name());
    }
    return Lists.emptyList();
  }
}
