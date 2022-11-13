package com.sagag.eshop.repo.specification;

import com.sagag.eshop.repo.entity.VDeliveryProfile;
import com.sagag.eshop.repo.utils.SpecificationUtils;
import com.sagag.services.common.utils.DateUtils;
import com.sagag.services.domain.eshop.criteria.DeliveryProfileSearchCriteria;

import lombok.experimental.UtilityClass;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

@UtilityClass
public class DeliveryProfileSpecifications {

  private static final String COUNTRY = "country";
  private static final String DELIVERY_PROFILE_NAME = "deliveryProfileName";
  private static final String DISTRIBUTION = "distributionBranchCode";
  private static final String DELIVERY_BRANCH = "deliveryBranchCode";
  private static final String VENDOR_CUT_OFF_TIME = "vendorCutOffTime";
  private static final String LASTEST_TIME = "latestTime";
  private static final String LAST_DELIVERY = "lastDelivery";
  private static final String DELIVERY_DURATION = "deliveryDuration";

  public static Specification<VDeliveryProfile> searchDeliveryProfile(
      DeliveryProfileSearchCriteria criteria) {
    return (root, query, criteriaBuilder) -> {
      final List<Predicate> predicates =
          buildSearchDeliveryProfileConditions(criteria, root, criteriaBuilder);

      query.orderBy(buildSearchDeliveryProfileOrders(criteria, root, criteriaBuilder));

      return criteriaBuilder.and(predicates.stream().toArray(Predicate[]::new));
    };
  }

  private static List<Predicate> buildSearchDeliveryProfileConditions(
      DeliveryProfileSearchCriteria criteria, Root<VDeliveryProfile> root,
      CriteriaBuilder criteriaBuilder) {
    final List<Predicate> predicates = new ArrayList<>();

    if (StringUtils.isNotBlank(criteria.getCountry())) {
      predicates.add(criteriaBuilder.equal(root.get(COUNTRY), criteria.getCountry()));
    }

    if (StringUtils.isNotBlank(criteria.getDeliveryProfileName())) {
      predicates.add(criteriaBuilder.like(root.get(DELIVERY_PROFILE_NAME),
          SpecificationUtils.appendLikeText(criteria.getDeliveryProfileName().trim())));
    }

    if (StringUtils.isNotBlank(criteria.getDistributionBranchCode())) {
      predicates.add(criteriaBuilder.like(root.get(DISTRIBUTION),
          SpecificationUtils.appendLikeText(criteria.getDistributionBranchCode())));
    }

    if (StringUtils.isNotBlank(criteria.getDeliveryBranchCode())) {
      predicates.add(criteriaBuilder.like(root.get(DELIVERY_BRANCH),
          SpecificationUtils.appendLikeText(criteria.getDeliveryBranchCode())));
    }

    if (StringUtils.isNotBlank(criteria.getVendorCutOffTime())) {
      predicates.add(criteriaBuilder.equal(buildStringExpr(root, VENDOR_CUT_OFF_TIME),
          DateUtils.formatTimeStr(criteria.getVendorCutOffTime(), DateUtils.TIME_PATTERN)));
    }

    if (StringUtils.isNotBlank(criteria.getLastestTime())) {
      predicates.add(criteriaBuilder.equal(buildStringExpr(root, LASTEST_TIME),
          DateUtils.formatTimeStr(criteria.getLastestTime(), DateUtils.TIME_PATTERN)));
    }

    if (StringUtils.isNotBlank(criteria.getLastDelivery())) {
      predicates.add(criteriaBuilder.equal(buildStringExpr(root, LAST_DELIVERY),
          DateUtils.formatTimeStr(criteria.getLastDelivery(), DateUtils.TIME_PATTERN)));
    }

    if (!Objects.isNull(criteria.getDeliveryDuration())) {
      predicates
          .add(criteriaBuilder.equal(root.get(DELIVERY_DURATION), criteria.getDeliveryDuration()));
    }

    return predicates;
  }

  private static List<Order> buildSearchDeliveryProfileOrders(
      DeliveryProfileSearchCriteria criteria, Root<VDeliveryProfile> root,
      CriteriaBuilder criteriaBuilder) {
    final List<Order> orders = new ArrayList<>();
    if (Objects.isNull(criteria.getSort())) {
      return Collections.emptyList();
    }
    if (!Objects.isNull(criteria.getSort().getOrderByCountryDesc())) {
      orders.add(SpecificationUtils.defaultOrder(criteriaBuilder, root.get(COUNTRY),
          criteria.getSort().getOrderByCountryDesc()));
    }

    if (!Objects.isNull(criteria.getSort().getOrderByDeleviryProfileNameDesc())) {
      orders.add(SpecificationUtils.defaultOrder(criteriaBuilder, root.get(DELIVERY_PROFILE_NAME),
          criteria.getSort().getOrderByDeleviryProfileNameDesc()));
    }

    if (!Objects.isNull(criteria.getSort().getOrderByDistributionBranchCodeDesc())) {
      orders.add(SpecificationUtils.defaultOrder(criteriaBuilder, root.get(DISTRIBUTION),
          criteria.getSort().getOrderByDistributionBranchCodeDesc()));
    }

    if (!Objects.isNull(criteria.getSort().getOrderByDeliveryBranchCodeDesc())) {
      orders.add(SpecificationUtils.defaultOrder(criteriaBuilder, root.get(DELIVERY_BRANCH),
          criteria.getSort().getOrderByDeliveryBranchCodeDesc()));
    }

    if (!Objects.isNull(criteria.getSort().getOrderByVendorCutOffTimeDesc())) {
      orders.add(SpecificationUtils.defaultOrder(criteriaBuilder, root.get(VENDOR_CUT_OFF_TIME),
          criteria.getSort().getOrderByVendorCutOffTimeDesc()));
    }

    if (!Objects.isNull(criteria.getSort().getOrderByLastestTimeDesc())) {
      orders.add(SpecificationUtils.defaultOrder(criteriaBuilder, root.get(LASTEST_TIME),
          criteria.getSort().getOrderByLastestTimeDesc()));
    }

    if (!Objects.isNull(criteria.getSort().getOrderByLastDeliveryDesc())) {
      orders.add(SpecificationUtils.defaultOrder(criteriaBuilder, root.get(LAST_DELIVERY),
          criteria.getSort().getOrderByLastDeliveryDesc()));
    }

    if (!Objects.isNull(criteria.getSort().getOrderByDeliveryDuration())) {
      orders.add(SpecificationUtils.defaultOrder(criteriaBuilder, root.get(DELIVERY_DURATION),
          criteria.getSort().getOrderByDeliveryDuration()));
    }
    return orders;
  }

  private static Expression<String> buildStringExpr(final Root<VDeliveryProfile> root,
      final String field) {
    return root.get(field).as(String.class);
  }
}
