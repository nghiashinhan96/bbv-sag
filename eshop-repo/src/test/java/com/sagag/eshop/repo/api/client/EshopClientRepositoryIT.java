package com.sagag.eshop.repo.api.client;

import com.sagag.eshop.repo.app.RepoApplication;
import com.sagag.eshop.repo.entity.client.EshopClient;
import com.sagag.services.common.annotation.EshopIntegrationTest;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { RepoApplication.class })
@EshopIntegrationTest
@Transactional
public class EshopClientRepositoryIT {

  @Autowired
  private EshopClientRepository repository;

  @Test
  public void testSave() {

    EshopClient eshopClient = new EshopClient();
    eshopClient.setClientName("test");
    eshopClient.setClientSecret("test_endcoded");
    eshopClient.setResourceId(1);
    eshopClient.setActive(true);

    eshopClient = repository.save(eshopClient);

    Assert.assertThat(eshopClient.getId(), Matchers.notNullValue());
  }
}
