package com.sagag.eshop.repo.specification.userhistory;

import com.sagag.services.common.enums.UserHistoryFromSource;

import lombok.experimental.UtilityClass;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

@UtilityClass
public class UserHistorySpecificationHelper {

  protected static final String FROM_SOURCE_FIELD = "fromSource";

  protected static final String SELECT_DATE_FIELD = "selectDate";

  protected static final String SEARCH_TERM_WITH_ARTNR = "searchTermWithArtNr";

  protected static final String FULL_NAME_FIELD = "fullName";

  protected void addFilterMode(List<Predicate> predicates,
      String filterMode, String fullName, Long userId,
      Root<?> root, CriteriaBuilder criteriaBuilder,
      Function<String, String> likeBetweenProcessor) {
    final List<UserHistoryFromSource> fromSources = new ArrayList<>();
    final Optional<Predicate> fullNamPredicateOpt =
        buildFullNamePredicate(fullName, root, criteriaBuilder, likeBetweenProcessor);
    if ("ALL".equalsIgnoreCase(filterMode)) {
      fromSources.addAll(Stream.of(UserHistoryFromSource.values()).collect(Collectors.toList()));
      predicates.add(root.get(FROM_SOURCE_FIELD).in(fromSources));
      fullNamPredicateOpt.ifPresent(predicates::add);
    } else {
      final UserHistoryFromSource fromSource =
          UserHistoryFromSource.valueOf(StringUtils.upperCase(filterMode));
      addCustomFilterModePredicates(predicates, fromSource, userId,
          fullNamPredicateOpt, root, criteriaBuilder);
    }
  }

  private static Optional<Predicate> buildFullNamePredicate(String fullName,
      Root<?> root, CriteriaBuilder criteriaBuilder,
      Function<String, String> likeBetweenProcessor) {
    return Optional.ofNullable(fullName).filter(StringUtils::isNotBlank)
    .map(val -> criteriaBuilder.like(root.get(FULL_NAME_FIELD),
        likeBetweenProcessor.apply(StringUtils.trim(val))));
  }

  private static void addCustomFilterModePredicates(List<Predicate> predicates,
      UserHistoryFromSource fromSource, long userId, Optional<Predicate> fullNamPredicateOpt,
      Root<?> root, CriteriaBuilder criteriaBuilder) {
    final Predicate fromSourcePredicate =
        criteriaBuilder.equal(root.get(FROM_SOURCE_FIELD), fromSource);
    if (UserHistoryFromSource.C4S == fromSource && fullNamPredicateOpt.isPresent()) {
      predicates.add(criteriaBuilder.or(fromSourcePredicate, fullNamPredicateOpt.get()));
      return;
    }

    predicates.add(fromSourcePredicate);
    if (UserHistoryFromSource.C4C == fromSource) {
      predicates.add(criteriaBuilder.equal(root.get("userId"), userId));
    }

    fullNamPredicateOpt.ifPresent(predicates::add);
  }
}
