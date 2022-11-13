package com.sagag.services.common.utils;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

@Slf4j
public class XmlUtilsTest {

  @Test(expected = IllegalArgumentException.class)
  public void testMarshal() {
    String xml = XmlUtils.marshal(new Object());
    log.debug("XML returned = {}", xml);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testUnmarshal() {
    XmlUtils.unmarshal(Object.class, StringUtils.EMPTY);
  }

}
