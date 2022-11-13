package com.sagag.eshop.repo.api.collection;

import com.sagag.eshop.repo.app.RepoApplication;
import com.sagag.eshop.repo.entity.collection.CollectionRelation;
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

import java.util.List;

/**
 * Integration test class for role {@link CollectionRelation}.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = { RepoApplication.class })
@EshopIntegrationTest
@Transactional
public class CollectionRelationRepositoryIT {

  private static final int WHOLESALER_ID = 137;

  @Autowired
  private CollectionRelationRepository repo;

  @Test
  public void testFindAllCollectionRelation() {
    final Page<CollectionRelation> collections =repo.findAll(PageUtils.defaultPageable(10));
    Assert.assertThat(collections, Matchers.notNullValue());
    Assert.assertThat(collections.getContent(), Matchers.hasSize(Matchers.greaterThanOrEqualTo(1)));
  }

  @Test
  public void testFindAllOrganisationCollectionPermission() {
    final List<CollectionRelation> collections =repo.findAllByWholesalerOrgId(WHOLESALER_ID);
    Assert.assertThat(collections, Matchers.notNullValue());
    Assert.assertThat(collections, Matchers.hasSize(Matchers.greaterThanOrEqualTo(1)));
  }

  @Test
  public void testCountByCollectionId() {
    final Long result = repo.countByCollectionId(2);
    Assert.assertThat(result, Matchers.notNullValue());
    Assert.assertThat(result, Matchers.greaterThan(1L));
  }
}
