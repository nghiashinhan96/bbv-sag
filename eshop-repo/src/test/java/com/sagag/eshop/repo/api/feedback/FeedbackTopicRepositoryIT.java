package com.sagag.eshop.repo.api.feedback;

import static org.junit.Assert.assertThat;

import com.sagag.eshop.repo.api.BaseRepositoryIT;
import com.sagag.eshop.repo.app.RepoApplication;
import com.sagag.eshop.repo.entity.feedback.FeedbackTopic;
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

import java.util.List;

/**
 * IT for {@link FeedbackTopicRepository}.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = { RepoApplication.class })
@EshopIntegrationTest
@Transactional
public class FeedbackTopicRepositoryIT extends BaseRepositoryIT {

  @Autowired
  private FeedbackTopicRepository repository;

  @Test
  public void testFindAll_WithPagination() {
    final Page<FeedbackTopic> entities = repository.findAll(PAGEABLE);
    Assert.assertThat(entities.hasContent(), Matchers.is(true));
  }

  @Test
  public void testFindOne() {
    final int id = 1;
    final FeedbackTopic entity = repository.getOne(id);
    Assert.assertThat(entity, Matchers.notNullValue());
    Assert.assertThat(entity.getId(), Matchers.equalTo(id));
  }

  @Test
  public void findSortedTopicCodes_shouldReturnSortedTopics() throws Exception {
    List<String> topics = repository.findSortedTopicCodes();
    assertThat(topics.get(0), Matchers.is("PRICING_WHEELS"));
    assertThat(topics.get(1), Matchers.is("PRICING_CARPARTS"));
    assertThat(topics.get(2), Matchers.is("PRICING_MATIK"));
    assertThat(topics.get(3), Matchers.is("PRICING_WSP"));
    assertThat(topics.get(4), Matchers.is("PRICING_IS"));
  }
}
