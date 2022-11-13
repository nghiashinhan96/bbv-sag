package com.sagag.eshop.repo.api;

import com.sagag.eshop.repo.app.RepoApplication;
import com.sagag.eshop.repo.entity.ReturnOrderReason;
import com.sagag.services.common.annotation.EshopIntegrationTest;

import org.apache.commons.lang3.BooleanUtils;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Integration test class for {@link ReturnOrderReasonRepository}.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = { RepoApplication.class })
@EshopIntegrationTest
@Transactional
public class ReturnOrderReasonRepositoryIT {

  private static final int MIN_REASON_SIZE = 9;

  private static final String[] SUPPORTED_REASONS = new String[] {
      "DEFECT",
      "INCORRECT_FEED_LINE",
      "FALSE_ARTICLE",
      "WRONG_AMOUNT",
      "SELECTION",
      "NOT_USED",
      "SUPPLIER_CALLBACK",
      "ALTERNATIVE",
      "AIS_RETOUR"
      };

  @Autowired
  private ReturnOrderReasonRepository repository;

  @Test
  public void shouldFindAll() {
    final List<ReturnOrderReason> actual = repository.findAll();
    Assert.assertThat(actual.size(), Matchers.greaterThanOrEqualTo(MIN_REASON_SIZE));
    assertList(actual);
  }

  private static void assertList(List<ReturnOrderReason> entities) {
    Assert.assertThat(entities.size(), Matchers.greaterThanOrEqualTo(MIN_REASON_SIZE));
    long sizeOfDefault = entities.stream().map(ReturnOrderReason::isDefault)
        .filter(BooleanUtils::isTrue).count();
    Assert.assertThat(sizeOfDefault, Matchers.is(1L));
    for (ReturnOrderReason entity : entities) {
      assertOne(entity);
    }
  }

  private static void assertOne(ReturnOrderReason entity) {
    Assert.assertThat(entity, Matchers.notNullValue());
    Assert.assertThat(entity.getId(), Matchers.notNullValue());
    Assert.assertThat(entity.getCode(), Matchers.isOneOf(SUPPORTED_REASONS));
    Assert.assertThat(entity.getAxCode(), Matchers.notNullValue());
    Assert.assertThat(entity.isDefault(), Matchers.isOneOf(Boolean.TRUE, Boolean.FALSE));
  }

}
