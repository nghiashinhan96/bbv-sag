package com.sagag.services.logging.utils;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.UUID;

@RunWith(SpringRunner.class)
public class LogUtilsTest {

  @Test
  public void testPutUser() {
    LogUtils.putUser(UUID.randomUUID().toString());
  }

  @Test
  public void testHandleRequestId() {
    final String requestId = UUID.randomUUID().toString();

    LogUtils.putRequestId(requestId);
    String actualRequestId = LogUtils.getRequestId();
    Assert.assertThat(actualRequestId, Matchers.equalTo(requestId));

    LogUtils.removeRequestId();
    actualRequestId = LogUtils.getRequestId();
    Assert.assertThat(actualRequestId, Matchers.nullValue());
  }

}
