package com.sagag.eshop.service.api.impl;

import com.sagag.eshop.repo.api.EshopUserViewRepository;
import com.sagag.eshop.repo.entity.VActiveUser;
import com.sagag.eshop.repo.specification.EshopUserSpecifications;
import com.sagag.eshop.service.api.UserManageService;
import com.sagag.eshop.service.converter.ViewUserConverter;
import com.sagag.services.domain.eshop.criteria.UserSearchCriteria;
import com.sagag.services.domain.eshop.criteria.UserSearchCriteriaRequest;
import com.sagag.services.domain.eshop.criteria.UserSearchSortableColumn;
import com.sagag.services.domain.eshop.dto.UserSearchResultItemDto;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class UserManageServiceImpl implements UserManageService {

  private static final String ORGANISATION_NAME_EXPRESSION = "orgName";
  private static final String CUSTOMER_NUMBER_EXPRESSION = "orgCode";
  private static final String USERNAME_EXPRESSION = "username";
  private static final String EMAIL_EXPRESSION = "email";
  private static final String IS_USER_ACTIVE = "isUserActive";
  private static final String ROLE_EXPRESSION = "roleName";

  @Autowired
  private EshopUserViewRepository eshopUserViewRepo;

  @Override
  public Page<UserSearchResultItemDto> searchActiveUserProfile(
      UserSearchCriteriaRequest criteriaRequest) {
    if (criteriaRequest == null) {
      criteriaRequest = new UserSearchCriteriaRequest();
    }
    UserSearchCriteria searchCriteria =
        UserSearchCriteria.builder().affiliate(defaultValue(criteriaRequest.getAffiliate()))
            .customerNumber(defaultValue(criteriaRequest.getCustomerNumber()))
            .email(defaultValue(criteriaRequest.getEmail()))
            .name(defaultValue(criteriaRequest.getName()))
            .telephone(defaultValue(criteriaRequest.getTelephone()))
            .isUserActive(criteriaRequest.getIsUserActive())
            .userName(defaultValue(criteriaRequest.getUserName())).build();
    final Pageable paging = buildPaging(criteriaRequest);
    Specification<VActiveUser> spec =
        EshopUserSpecifications.searchActiveUserProfile(searchCriteria);
    return eshopUserViewRepo.findAll(spec, paging).map(ViewUserConverter::convert);
  }

  private static String defaultValue(String value) {
    return StringUtils.defaultString(value);
  }

  private static Pageable buildPaging(UserSearchCriteriaRequest criteria) {
    return PageRequest.of(criteria.getPage(), criteria.getSize(), buildSort(criteria.getSorting()));
  }

  private static Sort buildSort(UserSearchSortableColumn sorting) {
    if (sorting == null) {
      return Sort.unsorted();
    }
    final List<Order> builder = new ArrayList<>();
    builder.add(buildOrder(sorting.getOrderByCustomerNumberDesc(), CUSTOMER_NUMBER_EXPRESSION));
    builder.add(buildOrder(sorting.getOrderByOrganisationNameDesc(), ORGANISATION_NAME_EXPRESSION));
    builder.add(buildOrder(sorting.getOrderByRoleDesc(), ROLE_EXPRESSION));
    builder.add(buildOrder(sorting.getOrderByEmailDesc(), EMAIL_EXPRESSION));
    builder.add(buildOrder(sorting.getOrderByUserNameDesc(), USERNAME_EXPRESSION));
    builder.add(buildOrder(sorting.getOrderByStatusDesc(), IS_USER_ACTIVE));
    final List<Order> orders =
        builder.stream().filter(Objects::nonNull).collect(Collectors.toList());
    if (CollectionUtils.isEmpty(orders)) {
      return Sort.unsorted();
    }
    return Sort.by(orders);
  }

  private static Order buildOrder(Boolean isDescending, String columnExpression) {
    if (isDescending == null) {
      return null;
    }
    return new Order(isDescending ? Direction.DESC : Direction.ASC, columnExpression);
  }

}
