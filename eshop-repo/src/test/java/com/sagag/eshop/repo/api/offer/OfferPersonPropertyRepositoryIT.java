package com.sagag.eshop.repo.api.offer;

import static org.junit.Assert.assertEquals;

import com.sagag.eshop.repo.app.RepoApplication;
import com.sagag.eshop.repo.entity.offer.OfferPersonProperty;
import com.sagag.eshop.repo.entity.offer.OfferPersonPropertyId;
import com.sagag.eshop.repo.utils.RepoDataTests;
import com.sagag.services.common.annotation.EshopIntegrationTest;
import com.sagag.services.common.enums.offer.OfferPersonPropertyType;

import org.apache.commons.lang3.RandomStringUtils;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Integration test class for {@link OfferPersonPropertyRepository}.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = { RepoApplication.class })
@EshopIntegrationTest
@Transactional
public class OfferPersonPropertyRepositoryIT {

  @Autowired
  private OfferPersonPropertyRepository repository;

  @Test
  public void testSaveSuccessful() {
    final OfferPersonProperty entity =
        OfferPersonProperty.builder()
            // .person(OfferPerson.builder().id(1L).build())
            .personId(1L)
            .type(OfferPersonPropertyType.SALUTATION.getValue())
            .value(RandomStringUtils.random(10)).build();

    repository.save(entity);
  }

  @Test
  public void testUpdateValueIfSameIdWithoutError() {
    final OfferPersonProperty entity1 = OfferPersonProperty.builder().personId(1L)
        .type(OfferPersonPropertyType.SALUTATION.getValue())
        .value("Mr")
        .build();
    repository.save(entity1);
    final OfferPersonProperty verify1 = repository.findById(OfferPersonPropertyId.builder()
        .personId(1L).type(OfferPersonPropertyType.SALUTATION.getValue()).build()).orElse(null);
    assertEquals(entity1.getValue(), verify1.getValue());

    final OfferPersonProperty entity2 = OfferPersonProperty.builder().personId(1L)
        .type(OfferPersonPropertyType.SALUTATION.getValue()).value("Ms")
        .build();
    repository.save(entity2);
    final OfferPersonProperty verify2 = repository.findById(OfferPersonPropertyId.builder()
        .personId(1L).type(OfferPersonPropertyType.SALUTATION.getValue()).build()).orElse(null);
    assertEquals(entity2.getValue(), verify2.getValue());
  }

  @Test
  public void testFindSuccessful() {
    final OfferPersonPropertyId id =
        OfferPersonPropertyId.builder()
            .personId(RepoDataTests.OFFER_PERSON_ID)
            .type(OfferPersonPropertyType.FAX.getValue()).build();
    final OfferPersonProperty result = repository.findById(id).orElse(null);
    Assert.assertThat(result, Matchers.notNullValue());
  }

  @Test
  public void testFindByPersonIdAndTypeSuccessful() {
    final Optional<OfferPersonProperty> result =
        repository.findByPersonIdAndType(RepoDataTests.OFFER_PERSON_ID,
            OfferPersonPropertyType.FAX.getValue());
    Assert.assertThat(result.isPresent(), Matchers.is(true));
  }
}
