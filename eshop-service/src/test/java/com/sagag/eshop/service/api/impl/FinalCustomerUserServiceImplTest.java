package com.sagag.eshop.service.api.impl;

import static org.junit.Assert.assertThat;

import com.sagag.eshop.repo.api.VUserDetailRepository;
import com.sagag.eshop.repo.criteria.finaluser.FinalUserSearchCriteria;
import com.sagag.eshop.repo.entity.VUserDetail;
import com.sagag.eshop.service.dto.finalcustomer.FinalCustomerUserDto;

import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.Arrays;

@RunWith(MockitoJUnitRunner.class)
public class FinalCustomerUserServiceImplTest {

  @InjectMocks
  private FinalCustomerUserServiceImpl finalUserService;

  @Mock
  private VUserDetailRepository vUserDetailRepo;

  @Test
  public void searchFinalUsersBelongToFinalCustomer_givenFinalCustomerId() {
    final int finalCustomerId = 139;
    VUserDetail userDetail = new VUserDetail();
    FinalUserSearchCriteria criteria = FinalUserSearchCriteria.builder().build();
    Page<VUserDetail> finalListPage = new PageImpl<>(Arrays.asList(userDetail));
    Mockito.when(vUserDetailRepo.findAll(ArgumentMatchers.<Specification<VUserDetail>>any(),
        Mockito.any(Pageable.class))).thenReturn(finalListPage);
    Page<FinalCustomerUserDto> result = finalUserService
        .searchFinalUsersBelongToFinalCustomer(finalCustomerId, criteria);
    assertThat(result.getTotalElements(), Matchers.is(1L));
  }

}
