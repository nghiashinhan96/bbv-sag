package com.sagag.eshop.repo.api;

import com.sagag.eshop.repo.app.RepoApplication;
import com.sagag.eshop.repo.entity.EshopRelease;
import com.sagag.services.common.annotation.EshopIntegrationTest;

import org.hamcrest.core.Is;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * Integration test class for {@link EshopReleaseRepository}.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = { RepoApplication.class })
@EshopIntegrationTest
@Transactional
@Ignore("We don't need this schema in the future")
public class EshopReleaseRepositoryIT {

  @Autowired
  private EshopReleaseRepository eshopReleaseRepo;

  @Test
  public void testFindReleaseById() {
    final Integer id = Integer.valueOf(1);
    final EshopRelease release = eshopReleaseRepo.findById(id).orElse(null);
    Assert.assertThat(true, Is.is(!Objects.isNull(release)));
    Assert.assertThat(id, Is.is(release.getId()));
  }

  @Test
  public void testFindAll() {
    final List<EshopRelease> releases = eshopReleaseRepo.findAll();
    Assert.assertThat(true, Is.is(!releases.isEmpty()));
  }

}
