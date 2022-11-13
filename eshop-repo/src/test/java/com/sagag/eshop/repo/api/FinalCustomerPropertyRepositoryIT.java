package com.sagag.eshop.repo.api;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

import com.sagag.eshop.repo.app.RepoApplication;
import com.sagag.eshop.repo.entity.FinalCustomerProperty;
import com.sagag.services.common.annotation.EshopIntegrationTest;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Integration test class for {@link FinalCustomerPropertyRepository}.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = { RepoApplication.class })
@EshopIntegrationTest
@Transactional
public class FinalCustomerPropertyRepositoryIT {


  @Autowired
  private FinalCustomerPropertyRepository finalCustomerPropertyRepo;

  @Test
  public void findByOrgId_shouldReturnResult_givenOrgId() {
    List<FinalCustomerProperty> result = finalCustomerPropertyRepo.findByOrgId(139L);
    Assert.assertThat(result, is(not(empty())));
  }
}
