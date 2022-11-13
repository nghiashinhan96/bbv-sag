package com.sagag.services.logging.config;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class Log4j2ConfigurationTest {

  @InjectMocks
  private Log4j2Configuration log4j2Configuration;

  @Test
  public void test() {
    Assert.assertThat(log4j2Configuration, Matchers.notNullValue());
  }

}
