package com.sagag.eshop.repo.api.offer;

import static org.junit.Assert.assertNotNull;

import com.sagag.eshop.repo.app.RepoApplication;
import com.sagag.eshop.repo.entity.Organisation;
import com.sagag.eshop.repo.entity.offer.Offer;
import com.sagag.eshop.repo.entity.offer.OfferAddress;
import com.sagag.eshop.repo.entity.offer.OfferPerson;
import com.sagag.services.common.annotation.EshopIntegrationTest;
import com.sagag.services.common.enums.offer.OfferStatus;
import com.sagag.services.common.enums.offer.OfferType;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;

/**
 * Integration test class for {@link OfferRepository}.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = { RepoApplication.class })
@EshopIntegrationTest
@Transactional
public class OfferRepositoryIT {

  @Autowired
  private OfferRepository repository;

  @Test
  public void testSaveSuccessfully() {
    final Offer entity =
        Offer.builder()
          .offerNumber("AS21125")
          .organisation(Organisation.builder().id(1).build())
          .ownerId(1L)
          .status(OfferStatus.OPEN.name())
          .vat(0.5)
          .altOfferPriceUsed(false)
          .recipientAddress(OfferAddress.builder().id(1L).build())
          .tecstate("ACTIVE")
          .createdUserId(1L)
          .offerDate(new Date())
          .recipient(OfferPerson.builder().id(1L).build())
          .type(OfferType.OFFER.name())
          .version(1).build();
    repository.save(entity);
    Assert.assertThat(entity, Matchers.notNullValue());
    Assert.assertThat(entity.getId(), Matchers.notNullValue());

    // verify the value from DB
    final Optional<Offer> createdOffer =
        repository.findByActiveOfferId(entity.getId(), entity.getOrganisation().getId());
    Assert.assertThat(createdOffer.isPresent(), Matchers.is(true));
    Assert.assertThat(createdOffer.get().getId(), Matchers.is(entity.getId()));

    // release created offer test
    repository.deleteById(entity.getId());
  }

  @Test
  public void testFindWithOfferPosition() {
    final long offerId = 25L;
    final Offer offer = repository.findById(offerId).orElse(null);
    assertNotNull(offer);
  }

  @Test
  public void testFindFirstByActiveOfferId() {
    final long offerId = 25L; // Offer Nr: 123
    final int orgId = 41;
    final Optional<Offer> offerOpt = repository.findByActiveOfferId(offerId, orgId);
    Assert.assertThat(offerOpt.isPresent(), Matchers.is(true));
  }

}
