package com.sagag.eshop.repo.api.collection;

import com.sagag.eshop.repo.app.RepoApplication;
import com.sagag.eshop.repo.entity.collection.OrganisationCollection;
import com.sagag.services.common.annotation.EshopIntegrationTest;

import org.apache.commons.collections4.CollectionUtils;
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
 * Integration test class for role {@link OrganisationCollectionRepository}.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = { RepoApplication.class })
@EshopIntegrationTest
@Transactional
public class OrganisationCollectionRepositoryIT {

  @Autowired
  private OrganisationCollectionRepository repo;

  @Test
  public void testFindAllOrganisationCollection() {
    final List<OrganisationCollection> collections = repo.findAll();
    Assert.assertThat(collections, Matchers.not(Matchers.empty()));
  }

  @Test
  public void testFindAllOrganisationCollectionByAffiliateId() {
    final List<OrganisationCollection> collections = repo.findByAffiliateId(2);
    Assert.assertThat(collections, Matchers.not(Matchers.empty()));
  }

  @Test
  public void shouldFindCustomersByCollectionShortName() {
    final String shortName = "derendinger-ch";
    final List<String> customers = repo.findCustomersByCollectionShortName(shortName);
    Assert.assertTrue(CollectionUtils.isNotEmpty(customers));
  }
}
