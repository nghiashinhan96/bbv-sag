package com.sagag.eshop.repo.api;

import com.sagag.eshop.repo.app.RepoApplication;
import com.sagag.eshop.repo.entity.VUserExport;
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

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { RepoApplication.class })
@EshopIntegrationTest
@Transactional
public class VUserExportRepositoryIT {

  @Autowired
  private VUserExportRepository repo;

  @Test
  public void findByOrgParentShortName_shouldFindVUserExports_givenOrgParentShortName()
      throws Exception {
    List<VUserExport> users = repo.findByOrgParentShortName("derendinger-at");
    Assert.assertNotNull(users);
    Assert.assertThat(users.get(0).getOrgParentShortName(), Matchers.is("derendinger-at"));
  }

}
