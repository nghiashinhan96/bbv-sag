package com.sagag.eshop.repo.specification;

import com.sagag.eshop.repo.entity.VExternalVendor;
import com.sagag.eshop.repo.utils.SpecificationUtils;
import com.sagag.services.domain.eshop.criteria.ExternalVendorSearchCriteria;

import lombok.experimental.UtilityClass;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

@UtilityClass
public class ExternalVendorSpecifications {

  private static final String COUNTRY = "country";
  private static final String VENDOR_ID = "vendorId";
  private static final String VENDOR_NAME = "vendorName";
  private static final String VENDOR_PRIORITY = "vendorPriority";
  private static final String DELIVERY_PROFILE_NAME = "deliveryProfileName";
  private static final String AVAILABILITY_TYPE = "availabilityTypeId";

  public static Specification<VExternalVendor> searchExternalVendor(
      ExternalVendorSearchCriteria criteria) {
    return (root, query, criteriaBuilder) -> {
      final List<Predicate> predicates = buildSearchUserConditions(criteria, root, criteriaBuilder);

      query.orderBy(buildSearchExternalVendorOrders(criteria, root, criteriaBuilder));

      return criteriaBuilder.and(predicates.stream().toArray(Predicate[]::new));
    };
  }

  private static List<Predicate> buildSearchUserConditions(ExternalVendorSearchCriteria criteria,
      Root<VExternalVendor> root, CriteriaBuilder criteriaBuilder) {
    final List<Predicate> predicates = new ArrayList<>();

    if (StringUtils.isNotBlank(criteria.getCountry())) {
      predicates.add(criteriaBuilder.equal(root.get(COUNTRY), criteria.getCountry()));
    }

    if (!Objects.isNull(criteria.getVendorId())) {
      predicates.add(criteriaBuilder.equal(root.get(VENDOR_ID), criteria.getVendorId()));
    }

    if (StringUtils.isNotBlank(criteria.getVendorName())) {
      predicates.add(criteriaBuilder.like(root.get(VENDOR_NAME),
          SpecificationUtils.appendLikeText(criteria.getVendorName().trim())));
    }

    if (!Objects.isNull(criteria.getVendorPriority())) {
      predicates
          .add(criteriaBuilder.equal(root.get(VENDOR_PRIORITY), criteria.getVendorPriority()));
    }

    if (StringUtils.isNotBlank(criteria.getDeliveryProfileName())) {
      predicates.add(criteriaBuilder.like(root.get(DELIVERY_PROFILE_NAME),
          SpecificationUtils.appendLikeText(criteria.getDeliveryProfileName())));
    }

    if (StringUtils.isNotBlank(criteria.getAvailabilityTypeId())) {
      predicates.add(
          criteriaBuilder.equal(root.get(AVAILABILITY_TYPE), criteria.getAvailabilityTypeId()));
    }

    return predicates;
  }

  private static List<Order> buildSearchExternalVendorOrders(ExternalVendorSearchCriteria criteria,
      Root<VExternalVendor> root, CriteriaBuilder criteriaBuilder) {
    final List<Order> orders = new ArrayList<>();
    if (Objects.isNull(criteria.getSort())) {
      return Collections.emptyList();
    }
    if (!Objects.isNull(criteria.getSort().getOrderByCountryDesc())) {
      orders.add(SpecificationUtils.defaultOrder(criteriaBuilder, root.get(COUNTRY),
          criteria.getSort().getOrderByCountryDesc()));
    }

    if (!Objects.isNull(criteria.getSort().getOrderByVendorIdDesc())) {
      orders.add(SpecificationUtils.defaultOrder(criteriaBuilder, root.get(VENDOR_ID),
          criteria.getSort().getOrderByVendorIdDesc()));
    }

    if (!Objects.isNull(criteria.getSort().getOrderByVendorNameDesc())) {
      orders.add(SpecificationUtils.defaultOrder(criteriaBuilder, root.get(VENDOR_NAME),
          criteria.getSort().getOrderByVendorNameDesc()));
    }

    if (!Objects.isNull(criteria.getSort().getOrderByVendorPriorityDesc())) {
      orders.add(SpecificationUtils.defaultOrder(criteriaBuilder, root.get(VENDOR_PRIORITY),
          criteria.getSort().getOrderByVendorPriorityDesc()));
    }

    if (!Objects.isNull(criteria.getSort().getOrderByDeleviryProfileNameDesc())) {
      orders.add(SpecificationUtils.defaultOrder(criteriaBuilder, root.get(DELIVERY_PROFILE_NAME),
          criteria.getSort().getOrderByDeleviryProfileNameDesc()));
    }

    if (!Objects.isNull(criteria.getSort().getOrderByAvailabilityTypeDesc())) {
      orders.add(SpecificationUtils.defaultOrder(criteriaBuilder, root.get(AVAILABILITY_TYPE),
          criteria.getSort().getOrderByAvailabilityTypeDesc()));
    }
    return orders;
  }
}
