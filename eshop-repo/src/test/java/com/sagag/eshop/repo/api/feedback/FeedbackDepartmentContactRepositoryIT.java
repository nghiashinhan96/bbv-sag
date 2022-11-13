package com.sagag.eshop.repo.api.feedback;

import com.sagag.eshop.repo.api.BaseRepositoryIT;
import com.sagag.eshop.repo.app.RepoApplication;
import com.sagag.eshop.repo.entity.feedback.FeedbackDepartmentContact;
import com.sagag.eshop.repo.enums.FeedbackDepartmentContactType;
import com.sagag.services.common.annotation.EshopIntegrationTest;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * IT for {@link FeedbackDepartmentContactRepository}.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = { RepoApplication.class })
@EshopIntegrationTest
@Transactional
public class FeedbackDepartmentContactRepositoryIT extends BaseRepositoryIT {

  private static final int LICENSING_TOPIC = 4;

  private static final String EMAIL_TEST = "account.test.ax@bbv.ch";

  @Autowired
  private FeedbackDepartmentContactRepository repository;

  @Test
  public void testFindAll_WithPagination() {
    final Page<FeedbackDepartmentContact> entities = repository.findAll(PAGEABLE);
    Assert.assertThat(entities.hasContent(), Matchers.is(true));
  }

  @Test
  public void testFindOne() {
    final long id = 1L;
    final FeedbackDepartmentContact entity = repository.getOne(id);
    Assert.assertThat(entity, Matchers.notNullValue());
    Assert.assertThat(entity.getId(), Matchers.equalTo(id));
  }

  @Test
  public void testfindContactByTopicIdAndAffiliateShortName_givenTopicAndAffiliate() {
    final String email = repository.findContactByTopicIdAndAffiliateShortName(LICENSING_TOPIC,
        "derendinger-ch", FeedbackDepartmentContactType.EMAIL);
    Assert.assertThat(email, Matchers.is(EMAIL_TEST));
  }

  @Test
  public void testfindContactCrossAffiliateByTopicId_givenTopic() {
    final String email = repository.findContactCrossAffiliateByTopicId(LICENSING_TOPIC,
        FeedbackDepartmentContactType.EMAIL);
    Assert.assertThat(email, Matchers.is(EMAIL_TEST));
  }
}
