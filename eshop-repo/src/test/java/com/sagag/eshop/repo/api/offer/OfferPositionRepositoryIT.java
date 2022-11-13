package com.sagag.eshop.repo.api.offer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.sagag.eshop.repo.app.RepoApplication;
import com.sagag.eshop.repo.entity.offer.Offer;
import com.sagag.eshop.repo.entity.offer.OfferPosition;
import com.sagag.services.common.annotation.EshopIntegrationTest;
import com.sagag.services.common.enums.offer.OfferActionType;
import com.sagag.services.common.enums.offer.OfferContextType;
import com.sagag.services.common.enums.offer.OfferPositionType;
import com.sagag.services.common.enums.offer.UomIsoType;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;

/**
 * Integration test class for {@link OfferPositionRepository}.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = { RepoApplication.class })
@EshopIntegrationTest
@Transactional
public class OfferPositionRepositoryIT {

  @Autowired
  private OfferPositionRepository repository;

  @Test
  public void testSaveSuccessful() {
    Date now = Calendar.getInstance().getTime();
    final OfferPosition entity = OfferPosition.builder()
        .createdDate(now).sequence(1)
        .actionType(OfferActionType.NONE.name())
        .shopArticleId(1L).context(OfferContextType.BASKET.name())
        .offer(Offer.builder().id(25L).build()).currencyId(1)
        .type(OfferPositionType.VEHICLE_INFO_PROVIDER_WORK.getValue()).calculated(new Date())
        .deliveryTypeId(1).actionType(OfferActionType.NONE.name())
        .deliveryDate(new Date()).uomIso(UomIsoType.PCE.name())
        .tecstate("ACTIVE").createdUserId(1L).build();
    repository.save(entity);
  }

  @Test
  public void testFindSuccessful() {
    final long offerPositionId = 22L; // belong to OfferId = 25
    final OfferPosition result = repository.findById(offerPositionId).orElse(null);
    assertNotNull(result);
    assertEquals(OfferPositionType.CLIENT_WORK.getValue(), result.getType());
  }
}
