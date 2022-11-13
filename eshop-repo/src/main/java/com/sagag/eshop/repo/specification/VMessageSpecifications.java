package com.sagag.eshop.repo.specification;

import com.sagag.eshop.repo.entity.message.VMessage;
import com.sagag.eshop.repo.utils.SpecificationUtils;
import com.sagag.services.domain.eshop.criteria.VMessageSearchCriteria;

import lombok.experimental.UtilityClass;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * Specifications for VMessage
 */
@UtilityClass
public class VMessageSpecifications {

  private static final String TITLE = "title";
  private static final String TYPE = "type";
  private static final String AREA = "area";
  private static final String SUB_AREA = "subArea";
  private static final String LOCATION_VALUE = "locationValue";
  private static final String ACTIVE = "active";
  private static final String CREATED_DATE = "createdDate";
  private static final String DATE_VALID_FROM = "dateValidFrom";
  private static final String DATE_VALID_TO = "dateValidTo";

  public static Specification<VMessage> searchMessages(VMessageSearchCriteria criteria) {
    return (root, query, criteriaBuilder) -> {
      final List<Predicate> predicates =
          buildSearchMessageConditions(criteria, root, criteriaBuilder);

      query.orderBy(buildSearchMessagesOrders(criteria, root, criteriaBuilder));

      return criteriaBuilder.and(predicates.stream().toArray(Predicate[]::new));
    };
  }

  private static List<Predicate> buildSearchMessageConditions(VMessageSearchCriteria criteria,
      Root<VMessage> root, CriteriaBuilder criteriaBuilder) {

    final List<Predicate> predicates = new ArrayList<>();

    if (StringUtils.isNotBlank(criteria.getTitle())) {
      predicates.add(criteriaBuilder.like(root.get(TITLE),
          SpecificationUtils.appendLikeText(criteria.getTitle())));
    }

    if (StringUtils.isNotBlank(criteria.getType())) {
      predicates.add(criteriaBuilder.equal(root.get(TYPE), criteria.getType()));
    }

    if (StringUtils.isNotBlank(criteria.getArea())) {
      predicates.add(criteriaBuilder.equal(root.get(AREA), criteria.getArea()));
    }

    if (StringUtils.isNotBlank(criteria.getSubArea())) {
      predicates.add(criteriaBuilder.equal(root.get(SUB_AREA), criteria.getSubArea()));
    }

    if (StringUtils.isNotBlank(criteria.getLocationValue())) {
      predicates.add(criteriaBuilder.like(root.get(LOCATION_VALUE),
          SpecificationUtils.appendLikeText(criteria.getLocationValue())));
    }

    if (!Objects.isNull(criteria.getActive())) {
      predicates.add(criteriaBuilder.equal(root.get(ACTIVE), criteria.getActive()));
    }
    return predicates;
  }


  private static List<Order> buildSearchMessagesOrders(VMessageSearchCriteria criteria,
      Root<VMessage> root, CriteriaBuilder criteriaBuilder) {
    final List<Order> orders = new ArrayList<>();

    if (!Objects.isNull(criteria.getOrderDescByTitle())) {
      orders.add(SpecificationUtils.defaultOrder(criteriaBuilder, root.get(TITLE),
          criteria.getOrderDescByTitle()));
    }

    if (!Objects.isNull(criteria.getOrderDescByType())) {
      orders.add(SpecificationUtils.defaultOrder(criteriaBuilder, root.get(TYPE),
          criteria.getOrderDescByType()));
    }

    if (!Objects.isNull(criteria.getOrderDescByArea())) {
      orders.add(SpecificationUtils.defaultOrder(criteriaBuilder, root.get(AREA),
          criteria.getOrderDescByArea()));
    }

    if (!Objects.isNull(criteria.getOrderDescBySubArea())) {
      orders.add(SpecificationUtils.defaultOrder(criteriaBuilder, root.get(SUB_AREA),
          criteria.getOrderDescBySubArea()));
    }

    if (!Objects.isNull(criteria.getActive())) {
      orders.add(SpecificationUtils.defaultOrder(criteriaBuilder, root.get(ACTIVE),
          criteria.getActive()));
    }

    if (!Objects.isNull(criteria.getOrderDescByDateValidFrom())) {
      orders.add(SpecificationUtils.defaultOrder(criteriaBuilder, root.get(DATE_VALID_FROM),
          criteria.getOrderDescByDateValidFrom()));
    }

    if (!Objects.isNull(criteria.getOrderDescByDateValidTo())) {
      orders.add(SpecificationUtils.defaultOrder(criteriaBuilder, root.get(DATE_VALID_TO),
          criteria.getOrderDescByDateValidTo()));
    }

    orders.add(SpecificationUtils.defaultOrder(criteriaBuilder, root.get(CREATED_DATE), true));
    return orders;
  }

}
