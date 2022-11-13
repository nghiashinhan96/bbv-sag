package com.sagag.eshop.repo.api;

import com.sagag.eshop.repo.app.RepoApplication;
import com.sagag.eshop.repo.entity.VFinalCustomer;
import com.sagag.services.common.annotation.EshopIntegrationTest;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { RepoApplication.class })
@EshopIntegrationTest
@Transactional
public class VFinalCustomerRepositoryIT {

  private static final int FINAL_CUSTOMER_ORG_ID = 139;

  @Autowired
  private VFinalCustomerRepository repository;

  @Test
  public void testFindByOrgIdAndParentOrgIdSuccessful() {
    final int finalCustOrgId = FINAL_CUSTOMER_ORG_ID;
    final int customerOrgId = 137;
    Optional<VFinalCustomer> finalCustomerOpt = repository.findByOrgId(finalCustOrgId);
    Assert.assertThat(finalCustomerOpt.isPresent(), Matchers.is(true));

    finalCustomerOpt.ifPresent(finalCustInfo -> {
      Assert.assertThat(finalCustInfo.getOrgId(), Matchers.is(finalCustOrgId));
      Assert.assertThat(finalCustInfo.getName(), Matchers.is("final customer"));
      Assert.assertThat(finalCustInfo.getDescription(), Matchers.is("final customer"));
      Assert.assertThat(finalCustInfo.getParentOrgId(), Matchers.is(customerOrgId));
      Assert.assertThat(finalCustInfo.getFinalCustomerType(), Matchers.is("ONLINE"));
      Assert.assertThat(finalCustInfo.getAddressInfo(), Matchers.containsString("70001"));
      Assert.assertThat(finalCustInfo.getContactInfo(), Matchers.is("test surname test firstname"));
    });
  }

  @Test
  public void testFindByOrgIdAndParentOrgIdFailed() {
    final int finalCustOrgId = 137;
    Optional<VFinalCustomer> finalCustomerOpt = repository.findByOrgId(finalCustOrgId);
    Assert.assertThat(finalCustomerOpt.isPresent(), Matchers.is(false));
  }

}
