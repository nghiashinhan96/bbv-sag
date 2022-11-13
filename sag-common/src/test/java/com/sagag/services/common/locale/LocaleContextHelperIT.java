package com.sagag.services.common.locale;

import com.sagag.services.common.CommonApplication;
import com.sagag.services.common.annotation.EshopIntegrationTest;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Locale;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { CommonApplication.class })
@EshopIntegrationTest
public class LocaleContextHelperIT {

  @Autowired
  private LocaleContextHelper helper;

  @Test
  public void shouldReturnDEIfSendENForAT() {
    final Locale actual = helper.toSupportedLocale("EN");
    Assert.assertThat(actual.getLanguage(), Matchers.equalTo("de"));
  }

}
