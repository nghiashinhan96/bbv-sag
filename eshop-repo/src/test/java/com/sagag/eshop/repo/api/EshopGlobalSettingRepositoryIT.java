package com.sagag.eshop.repo.api;

import com.sagag.eshop.repo.app.RepoApplication;
import com.sagag.eshop.repo.entity.EshopGlobalSetting;
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
public class EshopGlobalSettingRepositoryIT {

  @Autowired
  private EshopGlobalSettingRepository repository;

  @Test
  public void testFindAll() {
    final List<EshopGlobalSetting> settings = repository.findAll();

    Assert.assertThat(settings, Matchers.notNullValue());
    Assert.assertThat(settings.isEmpty(), Matchers.is(false));
    settings.forEach(setting -> {
      Assert.assertThat(setting.getId(), Matchers.notNullValue());
      Assert.assertThat(setting.getCode(), Matchers.notNullValue());
      Assert.assertThat(setting.getDescription(), Matchers.notNullValue());
      Assert.assertThat(setting.getSettingType(), Matchers.notNullValue());
      Assert.assertThat(setting.isEnabled(), Matchers.notNullValue());
    });
  }

}
