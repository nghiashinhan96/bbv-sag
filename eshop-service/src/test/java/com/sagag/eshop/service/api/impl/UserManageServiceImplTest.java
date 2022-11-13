package com.sagag.eshop.service.api.impl;

import com.sagag.eshop.repo.api.EshopUserViewRepository;
import com.sagag.services.domain.eshop.criteria.UserSearchCriteriaRequest;
import com.sagag.services.domain.eshop.dto.UserSearchResultItemDto;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

/**
 * UT class to verify for {@link UserManageServiceImpl}.
 */
@RunWith(MockitoJUnitRunner.class)
public class UserManageServiceImplTest {

  @Mock
  private EshopUserViewRepository eshopUserViewRepo;

  @InjectMocks
  private UserManageServiceImpl userManageService;

  @SuppressWarnings("unchecked")
  @Test
  public void testSearchActiveUserProfile() {
    Mockito.when(eshopUserViewRepo
      .findAll(Mockito.any(Specification.class), Mockito.any(Pageable.class)))
      .thenReturn(Page.empty());
    final Page<UserSearchResultItemDto> users =
      userManageService.searchActiveUserProfile(new UserSearchCriteriaRequest());
    Assert.assertThat(users, Matchers.notNullValue());
  }
}
