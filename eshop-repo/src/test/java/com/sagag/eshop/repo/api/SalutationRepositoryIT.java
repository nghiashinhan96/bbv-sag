package com.sagag.eshop.repo.api;

import com.sagag.eshop.repo.app.RepoApplication;
import com.sagag.eshop.repo.entity.Salutation;
import com.sagag.services.common.annotation.EshopIntegrationTest;

import org.hamcrest.core.Is;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Integration test class for {@link SalutationRepository}.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = { RepoApplication.class })
@EshopIntegrationTest
@Transactional
public class SalutationRepositoryIT {

  @Autowired
  private SalutationRepository salutationRepo;

  @Test
  public void testFindAll() {
    final List<Salutation> salutations = salutationRepo.findAll();
    Assert.assertThat(salutations.isEmpty(), Is.is(false));
  }

  @Test
  public void testFindByCode() {
    final Optional<Salutation> salutations = salutationRepo.findOneByCode("SALUTATION_OTHER");
    Assert.assertNotNull(salutations);
  }
}
