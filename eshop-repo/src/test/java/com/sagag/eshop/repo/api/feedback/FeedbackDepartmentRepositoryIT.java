package com.sagag.eshop.repo.api.feedback;

import com.sagag.eshop.repo.api.BaseRepositoryIT;
import com.sagag.eshop.repo.app.RepoApplication;
import com.sagag.eshop.repo.entity.feedback.FeedbackDepartment;
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
 * IT for {@link FeedbackDepartmentRepository}.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = { RepoApplication.class })
@EshopIntegrationTest
@Transactional
public class FeedbackDepartmentRepositoryIT extends BaseRepositoryIT {

  @Autowired
  private FeedbackDepartmentRepository repository;

  @Test
  public void testFindAll_WithPagination() {
    final Page<FeedbackDepartment> entities = repository.findAll(PAGEABLE);
    Assert.assertThat(entities.hasContent(), Matchers.is(true));
  }

  @Test
  public void testFindOne() {
    final int id = 1;
    final FeedbackDepartment entity = repository.getOne(id);
    Assert.assertThat(entity, Matchers.notNullValue());
    Assert.assertThat(entity.getId(), Matchers.equalTo(id));
  }
}
