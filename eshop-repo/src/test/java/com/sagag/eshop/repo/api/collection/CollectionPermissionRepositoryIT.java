package com.sagag.eshop.repo.api.collection;

import com.sagag.eshop.repo.app.RepoApplication;
import com.sagag.eshop.repo.entity.collection.CollectionPermission;
import com.sagag.services.common.annotation.EshopIntegrationTest;
import com.sagag.services.common.utils.PageUtils;

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
 * Integration test class for role {@link CollectionPermissionRepository}.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = { RepoApplication.class })
@EshopIntegrationTest
@Transactional
public class CollectionPermissionRepositoryIT {

  @Autowired
  private CollectionPermissionRepository repo;

  @Test
  public void testFindAllOrganisationCollectionPermission() {
    final Page<CollectionPermission> collections = repo.findAll(PageUtils.defaultPageable(10));
    Assert.assertThat(collections, Matchers.notNullValue());
    Assert.assertThat(collections.getContent(), Matchers.hasSize(Matchers.greaterThanOrEqualTo(1)));
  }

}
