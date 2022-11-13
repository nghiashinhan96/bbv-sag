package com.sagag.eshop.repo.api.feedback;

import com.sagag.eshop.repo.api.BaseRepositoryIT;
import com.sagag.eshop.repo.app.RepoApplication;
import com.sagag.eshop.repo.entity.feedback.Feedback;
import com.sagag.services.common.annotation.EshopIntegrationTest;

import org.apache.commons.lang3.RandomStringUtils;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;

import javax.validation.ConstraintViolationException;

/**
 * IT for {@link FeedbackRepository}.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = { RepoApplication.class })
@EshopIntegrationTest
@Transactional
public class FeedbackRepositoryIT extends BaseRepositoryIT {

  @Autowired
  private FeedbackRepository repository;

  @Test
  public void testFindAll_WithPagination() {
    final Page<Feedback> entities = repository.findAll(PAGEABLE);
    Assert.assertThat(entities.hasContent(), Matchers.is(true));
  }

  @Test
  public void testFindOne() {
    final long id = 1L;
    final Feedback entity = repository.getOne(id);
    Assert.assertThat(entity, Matchers.notNullValue());
    Assert.assertThat(entity.getId(), Matchers.equalTo(id));
  }

  @Test
  public void testSaveSucceed() {
    final Feedback entity = new Feedback();
    entity.setUserId(1L);
    entity.setSalesId(null);
    entity.setStatusId(1);
    entity.setTopicId(1);
    entity.setOrgId(1);
    entity.setUserInformation("add some user info");
    entity.setFeedbackMessage("add some feedback message");
    entity.setTechnicalInformation("add some techinical info");
    entity.setCreatedDate(Calendar.getInstance().getTime());
    entity.setCreatedUserId(1L);

    repository.save(entity);
  }

  @Test(expected = ConstraintViolationException.class)
  public void testSaveFailed_WithOverMaxSize() {
    final Feedback entity = new Feedback();
    entity.setUserId(1L);
    entity.setSalesId(null);
    entity.setStatusId(1);
    entity.setTopicId(1);
    entity.setOrgId(1);
    entity.setUserInformation("add some user info");
    entity.setFeedbackMessage(RandomStringUtils.random(3001));
    entity.setTechnicalInformation("add some techinical info");
    entity.setCreatedDate(Calendar.getInstance().getTime());
    entity.setCreatedUserId(1L);

    repository.save(entity);
  }

  @Test(expected = ConstraintViolationException.class)
  public void testSaveFailed_WithNullCreatedDate() {
    final Feedback entity = new Feedback();
    entity.setUserId(1L);
    entity.setSalesId(null);
    entity.setStatusId(1);
    entity.setTopicId(1);
    entity.setOrgId(1);
    entity.setUserInformation("add some user info");
    entity.setFeedbackMessage("add some feedback message");
    entity.setTechnicalInformation("add some techinical info");
    entity.setCreatedDate(null);
    entity.setCreatedUserId(1L);

    repository.save(entity);
  }

  @Test(expected = ConstraintViolationException.class)
  public void testSaveFailed_WithNullCreatedUserId() {
    final Feedback entity = new Feedback();
    entity.setUserId(1L);
    entity.setSalesId(null);
    entity.setStatusId(1);
    entity.setTopicId(1);
    entity.setOrgId(1);
    entity.setUserInformation("add some user info");
    entity.setFeedbackMessage("add some feedback message");
    entity.setTechnicalInformation("add some techinical info");
    entity.setCreatedDate(Calendar.getInstance().getTime());
    entity.setCreatedUserId(null);

    repository.save(entity);
  }
}
