package com.sagag.eshop.repo.api.collection;

import com.sagag.eshop.repo.app.RepoApplication;
import com.sagag.eshop.repo.entity.collection.OrganisationCollectionsSettings;
import com.sagag.services.common.annotation.EshopIntegrationTest;

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
public class OrganisationCollectionSettingsRepositoryIT {

  @Autowired
  private OrganisationCollectionsSettingsRepository repo;

  @Test
  public void testFindAllOrganisationCollection() {
    final List<OrganisationCollectionsSettings> collections = repo.findAll();
    Assert.assertThat(collections, Matchers.not(Matchers.empty()));
  }

}
