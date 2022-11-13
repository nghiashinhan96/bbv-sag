package com.sagag.eshop.service.api.impl;

import com.sagag.eshop.repo.criteria.finalcustomer.FinalCustomerSearchCriteria;
import com.sagag.eshop.repo.criteria.finalcustomer.FinalCustomerSearchSortCriteria;
import com.sagag.eshop.repo.criteria.finalcustomer.FinalCustomerSearchTermCriteria;
import com.sagag.eshop.service.app.ServiceApplication;
import com.sagag.eshop.service.dto.finalcustomer.FinalCustomerDto;
import com.sagag.services.common.annotation.EshopIntegrationTest;
import com.sagag.services.common.enums.FinalCustomerType;
import com.sagag.services.common.utils.PageUtils;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Optional;

import javax.transaction.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { ServiceApplication.class })
@EshopIntegrationTest
@Transactional
public class FinalCustomerServiceImplIT {

  private static final int CUSTOMER_ORG_ID = 137;

  @Autowired
  private FinalCustomerServiceImpl service;

  @Test
  public void shouldReturnFirstPageOfFinalCustomers() {
    final FinalCustomerSearchTermCriteria term = new FinalCustomerSearchTermCriteria();
    term.setName("final");
    term.setFinalCustomerType(FinalCustomerType.ONLINE);
    term.setAddress("70001");
    term.setContactInfo("firstname");

    final FinalCustomerSearchSortCriteria sort = new FinalCustomerSearchSortCriteria();
    sort.setOrderDescByName(true);

    final FinalCustomerSearchCriteria criteria = new FinalCustomerSearchCriteria();
    criteria.setTerm(term);
    criteria.setSort(sort);

    final Page<FinalCustomerDto> finalCustomers =
        service.searchFinalCustomersBelongToCustomer(CUSTOMER_ORG_ID, criteria, PageUtils.DEF_PAGE);

    Assert.assertThat(finalCustomers.hasContent(), Matchers.is(true));
    Assert.assertThat(finalCustomers.getNumber(), Matchers.is(0));
    Assert.assertThat(finalCustomers.getTotalElements(), Matchers.greaterThanOrEqualTo(1l));
  }

  @Test
  public void shouldReturnFirstPageOfFinalCustomersWithEmptyCriteria() {
    final FinalCustomerSearchTermCriteria term = new FinalCustomerSearchTermCriteria();
    final FinalCustomerSearchSortCriteria sort = new FinalCustomerSearchSortCriteria();
    final FinalCustomerSearchCriteria criteria = new FinalCustomerSearchCriteria();
    criteria.setTerm(term);
    criteria.setSort(sort);

    final Page<FinalCustomerDto> finalCustomers =
        service.searchFinalCustomersBelongToCustomer(CUSTOMER_ORG_ID, criteria, PageUtils.DEF_PAGE);

    Assert.assertThat(finalCustomers.hasContent(), Matchers.is(true));
    Assert.assertThat(finalCustomers.getNumber(), Matchers.is(0));
    Assert.assertThat(finalCustomers.getTotalElements(), Matchers.greaterThanOrEqualTo(1l));
  }

  @Test
  public void shouldReturnFinalCustomerInfo() {
    final int finalCustOrgId = 139;
    final Optional<FinalCustomerDto> finalCustomerOpt =
        service.getFinalCustomerInfo(finalCustOrgId, true);

    Assert.assertThat(finalCustomerOpt.isPresent(), Matchers.is(true));
    finalCustomerOpt.ifPresent(finalCustInfo -> {
      Assert.assertThat(finalCustInfo.getOrgId(), Matchers.is(finalCustOrgId));
      Assert.assertThat(finalCustInfo.getName(), Matchers.is("final customer"));
      Assert.assertThat(finalCustInfo.getDescription(), Matchers.is("final customer"));
      Assert.assertThat(finalCustInfo.getParentOrgId(), Matchers.is(CUSTOMER_ORG_ID));
      Assert.assertThat(finalCustInfo.getFinalCustomerType(),
          Matchers.is(FinalCustomerType.ONLINE));
      Assert.assertThat(finalCustInfo.getAddressInfo(),
          Matchers.containsString("70001"));
      Assert.assertThat(finalCustInfo.getContactInfo(), Matchers.is("test surname test firstname"));
      Assert.assertThat(finalCustInfo, Matchers.notNullValue());
    });
  }

}
