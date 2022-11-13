package com.sagag.services.ivds.executor;

import com.sagag.services.common.annotation.EshopIntegrationTest;
import com.sagag.services.domain.eshop.dto.VehicleUsageDto;
import com.sagag.services.ivds.app.IvdsApplication;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.Locale;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { IvdsApplication.class })
@EshopIntegrationTest
public class IvdsArticleTaskExecutorsIT {

  @Autowired
  private IvdsArticleTaskExecutors executors;

  @Test
  public void shouldReturnVehicleUsages() {
    LocaleContextHolder.setLocale(Locale.GERMAN);
    final String artId = "1000930017";
    List<VehicleUsageDto> result = executors.executeTaskWithVehicleUsages(artId);
    Assert.assertThat(result.isEmpty(), Matchers.is(false));
  }
}
