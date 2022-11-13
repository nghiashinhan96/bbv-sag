package com.sagag.eshop.repo.api.client;

import com.sagag.eshop.repo.app.RepoApplication;
import com.sagag.eshop.repo.entity.client.VEshopClient;
import com.sagag.services.common.annotation.EshopIntegrationTest;
import com.sagag.services.domain.eshop.common.EshopAuthority;

import org.apache.commons.lang3.StringUtils;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { RepoApplication.class })
@EshopIntegrationTest
@Transactional
public class VEshopClientRepositoryIT {

  @Autowired
  private VEshopClientRepository repository;

  @Test
  public void shouldNotFoundEmpty() {
    final String clientName = StringUtils.EMPTY;
    Optional<VEshopClient> clientOpt = repository.findByClientName(clientName);
    Assert.assertThat(clientOpt.isPresent(), Matchers.is(false));
  }

  @Test
  public void shouldNotFoundInvalidName() {
    final String clientName = "adddd";
    Optional<VEshopClient> clientOpt = repository.findByClientName(clientName);
    Assert.assertThat(clientOpt.isPresent(), Matchers.is(false));
  }

  @Test
  public void shouldFindByClientNameEshopWeb() {
    final String clientName = "eshop-web";
    final String clientSecret = "ZXNob3Atd2ViOmVzaG9wLXdlYi15enRBaEdwRlc=";
    final String resourceId = "sag-rest";
    Optional<VEshopClient> clientOpt = repository.findByClientName(clientName);

    Assert.assertThat(clientOpt.isPresent(), Matchers.is(true));
    clientOpt.ifPresent(client -> {
      Assert.assertThat(client.getClientName(), Matchers.is(clientName));
      Assert.assertThat(client.getClientSecret(), Matchers.is(clientSecret));
      Assert.assertThat(client.getResourceId(), Matchers.is(resourceId));
      Assert.assertThat(client.getEshopAuthorities(), Matchers.containsInAnyOrder(
          EshopAuthority.USER_ADMIN, EshopAuthority.NORMAL_USER, EshopAuthority.SALES_ASSISTANT,
          EshopAuthority.MARKETING_ASSISTANT, EshopAuthority.GROUP_ADMIN));
    });
  }

  @Test
  public void shouldFindByClientNameEshopAdmin() {
    final String clientName = "eshop-admin";
    final String clientSecret = "ZXNob3AtYWRtaW46ZXNob3AtYWRtaW4tV0ZwR2hBdHp5";
    final String resourceId = "sag-admin";
    Optional<VEshopClient> clientOpt = repository.findByClientName(clientName);

    Assert.assertThat(clientOpt.isPresent(), Matchers.is(true));
    clientOpt.ifPresent(client -> {
      Assert.assertThat(client.getClientName(), Matchers.is(clientName));
      Assert.assertThat(client.getClientSecret(), Matchers.is(clientSecret));
      Assert.assertThat(client.getResourceId(), Matchers.is(resourceId));
      Assert.assertThat(client.getEshopAuthorities(), Matchers.containsInAnyOrder(
          EshopAuthority.SYSTEM_ADMIN));
    });
  }
}
