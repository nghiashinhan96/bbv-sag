package com.sagag.eshop.service.api.impl;

import com.sagag.eshop.repo.criteria.finaluser.FinalUserSearchCriteria;
import com.sagag.eshop.service.api.FinalCustomerUserService;
import com.sagag.eshop.service.app.ServiceApplication;
import com.sagag.eshop.service.dto.finalcustomer.FinalCustomerUserDto;
import com.sagag.services.common.annotation.EshopIntegrationTest;

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
public class FinalCustomerUserServiceImplIT {

  @Autowired
  private FinalCustomerUserService finalUserService;

  @Test
  public void searchFinalUsersBelongToFinalCustomer_shouldReturnFinalUserList_givenFinalCustomerId() {
    final int finalCustomerId = 139;
    final FinalUserSearchCriteria criteria = FinalUserSearchCriteria.builder().build();
    final Page<FinalCustomerUserDto> resultDto = finalUserService
        .searchFinalUsersBelongToFinalCustomer(finalCustomerId, criteria);
    Assert.assertThat(resultDto.hasContent(), Matchers.is(true));
  }

  @Test
  public void searchFinalUsersBelongToFinalCustomer_shouldReturnFinalUserList_givenUserName() {
    final int finalCustomerId = 139;
    final FinalUserSearchCriteria criteria =
        FinalUserSearchCriteria.builder().userName("user.test.final").build();
    final Page<FinalCustomerUserDto> resultDto = finalUserService
        .searchFinalUsersBelongToFinalCustomer(finalCustomerId, criteria);
    Assert.assertThat(resultDto.hasContent(), Matchers.is(true));
  }

  @Test
  public void searchFinalUsersBelongToFinalCustomer_shouldReturnFinalUserList_givenFullName() {
    final int finalCustomerId = 139;
    final FinalUserSearchCriteria criteria =
        FinalUserSearchCriteria.builder().fullName("user final customer").build();
    final Page<FinalCustomerUserDto> resultDto = finalUserService
        .searchFinalUsersBelongToFinalCustomer(finalCustomerId, criteria);
    Assert.assertThat(resultDto.hasContent(), Matchers.is(true));
  }

  @Test
  public void searchFinalUsersBelongToFinalCustomer_shouldReturnFinalUserList_givenEmail() {
    final int finalCustomerId = 139;
    final FinalUserSearchCriteria criteria =
        FinalUserSearchCriteria.builder().userEmail("final@gmail.com").build();
    final Page<FinalCustomerUserDto> resultDto = finalUserService
        .searchFinalUsersBelongToFinalCustomer(finalCustomerId, criteria);
    Assert.assertThat(resultDto.hasContent(), Matchers.is(true));
  }

  @Test
  public void searchFinalUsersBelongToFinalCustomer_shouldReturnFinalUserList_givenUserNameEmail() {
    final int finalCustomerId = 139;
    final FinalUserSearchCriteria criteria = FinalUserSearchCriteria.builder()
        .userEmail("user.test.final").userEmail("final@gmail.com").build();
    final Page<FinalCustomerUserDto> resultDto = finalUserService
        .searchFinalUsersBelongToFinalCustomer(finalCustomerId, criteria);
    Assert.assertThat(resultDto.hasContent(), Matchers.is(true));
  }

  @Test
  public void searchFinalUsersBelongToFinalCustomer_shouldReturnFinalUserList_givenFirstName() {
    final int finalCustomerId = 139;
    final FinalUserSearchCriteria criteria =
        FinalUserSearchCriteria.builder().firstName("user").build();
    final Page<FinalCustomerUserDto> resultDto =
        finalUserService.searchFinalUsersBelongToFinalCustomer(finalCustomerId, criteria);
    Assert.assertThat(resultDto.hasContent(), Matchers.is(true));
  }

  @Test
  public void searchFinalUsersBelongToFinalCustomer_shouldReturnFinalUserList_givenLastName() {
    final int finalCustomerId = 139;
    final FinalUserSearchCriteria criteria =
        FinalUserSearchCriteria.builder().lastName("final customer").build();
    final Page<FinalCustomerUserDto> resultDto =
        finalUserService.searchFinalUsersBelongToFinalCustomer(finalCustomerId, criteria);
    Assert.assertThat(resultDto.hasContent(), Matchers.is(true));
  }

  @Test
  public void searchFinalUsersBelongToFinalCustomer_shouldReturnFinalUserList_givenSalutation() {
    final int finalCustomerId = 139;
    final FinalUserSearchCriteria criteria =
        FinalUserSearchCriteria.builder().salutation(1).build();
    final Page<FinalCustomerUserDto> resultDto =
        finalUserService.searchFinalUsersBelongToFinalCustomer(finalCustomerId, criteria);
    Assert.assertThat(resultDto.hasContent(), Matchers.is(true));
  }

}
