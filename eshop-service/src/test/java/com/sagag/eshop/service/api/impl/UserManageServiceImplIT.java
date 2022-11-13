package com.sagag.eshop.service.api.impl;

import static org.junit.Assert.assertThat;

import com.sagag.eshop.service.app.ServiceApplication;
import com.sagag.services.common.annotation.EshopIntegrationTest;
import com.sagag.services.domain.eshop.criteria.UserSearchCriteriaRequest;
import com.sagag.services.domain.eshop.dto.UserSearchResultItemDto;

import org.hamcrest.CoreMatchers;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.transaction.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { ServiceApplication.class })
@EshopIntegrationTest
@Transactional
public class UserManageServiceImplIT {

  @Autowired
  private UserManageServiceImpl userMngService;

  @Test
  public void searchActiveUserProfile_GivenAffiliate() throws Exception {
    final UserSearchCriteriaRequest criteria =
        UserSearchCriteriaRequest.builder().affiliate("matik-at").build();
    Page<UserSearchResultItemDto> result = userMngService.searchActiveUserProfile(criteria);
    assertThat(result.getContent().get(0).getAffiliate(), CoreMatchers.is(criteria.getAffiliate()));
  }

  @Test
  public void searchActiveUserProfile_GivenCustomerNumber() throws Exception {
    final UserSearchCriteriaRequest criteria =
        UserSearchCriteriaRequest.builder().customerNumber("1100005").build();
    Page<UserSearchResultItemDto> result = userMngService.searchActiveUserProfile(criteria);
    Assert.assertThat(result.getContent().get(0).getOrganisationCode(),
        CoreMatchers.is(criteria.getCustomerNumber()));
  }

  @Test
  public void searchActiveUserProfile_GivenUsername() throws Exception {
    final UserSearchCriteriaRequest criteria =
        UserSearchCriteriaRequest.builder().userName("tuan1.ax").build();
    Page<UserSearchResultItemDto> result = userMngService.searchActiveUserProfile(criteria);
    assertThat(result.hasContent(), Matchers.is(true));
    Assert.assertThat(result.getContent().get(0).getUserName(),
        CoreMatchers.is(criteria.getUserName()));
  }

  @Test
  public void searchActiveUserProfile_GivenEmail() throws Exception {
    final UserSearchCriteriaRequest criteria =
        UserSearchCriteriaRequest.builder().email("phong.nguyen@bbv.vn").build();
    Page<UserSearchResultItemDto> result = userMngService.searchActiveUserProfile(criteria);
    Assert.assertThat(result.getContent().get(0).getEmail(), CoreMatchers.is(criteria.getEmail()));
  }

  @Test
  public void searchActiveUserProfile_GivenPhone() throws Exception {
    final UserSearchCriteriaRequest criteria =
        UserSearchCriteriaRequest.builder().telephone("123456789").isUserActive(true).build();
    Page<UserSearchResultItemDto> result = userMngService.searchActiveUserProfile(criteria);
    Assert.assertThat(result.getContent().get(0).getTelephone(),
        CoreMatchers.is(criteria.getTelephone()));
    Assert.assertThat(result.getContent().get(0).getIsUserActive(),
        CoreMatchers.is(criteria.getIsUserActive()));
  }

  @Test
  public void searchActiveUserProfile_GivenFullInfo() throws Exception {
    final UserSearchCriteriaRequest criteria = UserSearchCriteriaRequest.builder()
        .affiliate("derendinger-at").customerNumber("1130438").userName("tuan1.ax")
        .email("duong.dang@bbv.vn")
        .telephone(null).isUserActive(true).build();
    Page<UserSearchResultItemDto> result = userMngService.searchActiveUserProfile(criteria);
    assertThat(result.hasContent(), Matchers.is(true));
    UserSearchResultItemDto user = result.getContent().get(0);
    assertThat(user.getAffiliate(), CoreMatchers.is(criteria.getAffiliate()));
    assertThat(user.getOrganisationCode(), CoreMatchers.is(criteria.getCustomerNumber()));
    assertThat(user.getUserName(), CoreMatchers.is(criteria.getUserName()));
    assertThat(user.getEmail(), CoreMatchers.is(criteria.getEmail()));
    assertThat(user.getTelephone(), CoreMatchers.is(criteria.getTelephone()));
    assertThat(user.getIsUserActive(), CoreMatchers.is(criteria.getIsUserActive()));
  }
}
