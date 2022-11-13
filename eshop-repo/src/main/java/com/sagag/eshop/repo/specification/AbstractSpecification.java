package com.sagag.eshop.repo.specification;

import lombok.NonNull;

import org.springframework.data.jpa.domain.Specification;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;

public abstract class AbstractSpecification<T> implements Specification<T> {

  private static final long serialVersionUID = 1724420221112298389L;

  private static final  String LIKE_STRING = "%";

  protected static Predicate andTogether(List<Predicate> predicates, CriteriaBuilder criteriaBuilder) {
    return criteriaBuilder.and(predicates.stream().toArray(Predicate[]::new));
  }

  protected static String likeBetween(@NonNull final String searchCondition) {
    return LIKE_STRING + searchCondition + LIKE_STRING;
  }

  protected static Order defaultOrder(CriteriaBuilder criteriaBuilder,
      Expression<?> expression, boolean orderDesc) {
    return orderDesc ? criteriaBuilder.desc(expression) : criteriaBuilder.asc(expression);
  }
}
