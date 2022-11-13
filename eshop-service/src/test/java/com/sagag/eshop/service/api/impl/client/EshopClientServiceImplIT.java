package com.sagag.eshop.service.api.impl.client;

import com.sagag.eshop.service.app.ServiceApplication;
import com.sagag.eshop.service.dto.client.CreatedEshopClientDto;
import com.sagag.eshop.service.dto.client.EshopClientCriteria;
import com.sagag.services.common.annotation.EshopIntegrationTest;
import com.sagag.services.domain.eshop.common.EshopAuthority;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;

import javax.transaction.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { ServiceApplication.class })
@EshopIntegrationTest
@Transactional
public class EshopClientServiceImplIT {

  @Autowired
  private EshopClientServiceImpl service;

  @Test
  public void shouldCreateEshopClientSuccessful() {
    final String clientName = "eshop-web";
    final String resource = "sag-rest";
    final EshopClientCriteria criteria = initCriteria(clientName, resource,
        EshopAuthority.FINAL_NORMAL_USER);
    final CreatedEshopClientDto result = service.createEshopClient(criteria);

    Assert.assertThat(result.getId(), Matchers.notNullValue());
    Assert.assertThat(result.getClientName(), Matchers.is(clientName));
    Assert.assertThat(result.getClientSecret(), Matchers.notNullValue());
  }

  private EshopClientCriteria initCriteria(String name, String resource,
      EshopAuthority authorities) {
    EshopClientCriteria criteria = new EshopClientCriteria();
    criteria.setClientName(name);
    criteria.setResourceName(resource);
    criteria.setAllowedAuthorities(Arrays.asList(authorities));
    return criteria;
  }
}
