package com.sagag.eshop.repo.api.offer;

import static org.junit.Assert.assertNotNull;

import com.sagag.eshop.repo.app.RepoApplication;
import com.sagag.eshop.repo.entity.offer.OfferAddress;
import com.sagag.eshop.repo.utils.RepoDataTests;
import com.sagag.services.common.annotation.EshopIntegrationTest;
import com.sagag.services.common.enums.offer.OfferAddressType;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;

/**
 * Integration test class for {@link OfferAddressRepository}.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = { RepoApplication.class })
@EshopIntegrationTest
@Transactional
public class OfferAddressRepositoryIT {

  @Autowired
  private OfferAddressRepository repository;

  @Test
  public void testSaveSuccessful() {
    final OfferAddress entity = OfferAddress.builder().city("HCM").zipcode("7000000")
        .countryiso("VN").createdUserId(1L).createdDate(Calendar.getInstance().getTime())
        .personId(RepoDataTests.OFFER_PERSON_ID)
        .tecstate("ACTIVE").type(OfferAddressType.DEFAULT.name()).version(1).build();
    repository.save(entity);
  }

  @Test
  public void testFindOne() {
    final long offerAddressId = 24L; // Belong to personId = 24
    final OfferAddress address = repository.findById(offerAddressId).orElse(null);
    assertNotNull(address);
  }
}
