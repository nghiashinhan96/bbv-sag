package com.sagag.eshop.repo.utils;

import com.sagag.services.common.contants.SagConstants;

import lombok.experimental.UtilityClass;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;

@UtilityClass
public final class SpecificationUtils {

  public Predicate andTogether(List<Predicate> predicates, CriteriaBuilder criteriaBuilder) {
    return criteriaBuilder.and(predicates.stream().toArray(Predicate[]::new));
  }

  public static String appendLikeText(String text) {
    return SagConstants.LIKE_CHAR + text + SagConstants.LIKE_CHAR;
  }

  public static Order defaultOrder(CriteriaBuilder criteriaBuilder,
      Expression<?> expression, boolean orderDesc) {
    return orderDesc ? criteriaBuilder.desc(expression) : criteriaBuilder.asc(expression);
  }

}
