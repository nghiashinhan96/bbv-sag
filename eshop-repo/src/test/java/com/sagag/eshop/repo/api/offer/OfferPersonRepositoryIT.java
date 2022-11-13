package com.sagag.eshop.repo.api.offer;

import static org.junit.Assert.assertNotNull;

import com.sagag.eshop.repo.app.RepoApplication;
import com.sagag.eshop.repo.entity.offer.OfferPerson;
import com.sagag.services.common.annotation.EshopIntegrationTest;
import com.sagag.services.common.enums.offer.OfferPersonType;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration test class for {@link OfferPersonRepository}.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = { RepoApplication.class })
@EshopIntegrationTest
@Transactional
public class OfferPersonRepositoryIT {

  @Autowired
  private OfferPersonRepository repository;

  @Test
  public void testSaveSuccessful() {
    final OfferPerson entity =
        OfferPerson.builder().email("thuan.nguyen@bbv.vn").firstName("Nguyen").lastName("Thuan")
            .status("ACTIVE").type(OfferPersonType.ADDRESSEE.name())
            .languageId(1).createdUserId(1L)
            .organisationId(7)
            .tecstate("ACTIVE").version(1).build();
    repository.save(entity);
  }

  @Test
  public void testFindOne() {
    final long offerPersonId = 24L; // belong to org_id = 41
    final OfferPerson result = repository.findById(offerPersonId).orElse(null);
    assertNotNull(result);
  }
}
