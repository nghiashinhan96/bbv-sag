package com.sagag.services.thule.http;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.CookieProcessor;
import org.apache.tomcat.util.http.Rfc6265CookieProcessor;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;

@RunWith(SpringRunner.class)
public class Rfc6265CookieProcessorTest {

  private CookieProcessor cookieProcessor;

  @Mock
  private HttpServletResponse response;

  @Before
  public void setup() {
    this.cookieProcessor = new Rfc6265CookieProcessor();
  }

  @Test
  public void testCookieProcessor() {
    Cookie[] cookies = {
        new Cookie("dealer", "{7A411F84-2FD3-4293-ABF7-AC3BC9101EE7}"),
        new Cookie("order_list", "599_1_599001_|598_1_598001_|561_1_561000_"),
        new Cookie("orderlist", "599001_1|598001_1|561000_1")
    };

    for (Cookie cookie : cookies) {
      System.out.println(String.format("cookie name = %s", cookie.getName()));
      cookieProcessor.generateHeader(cookie);
    }
  }

  @Test
  public void testHandleThuleResponse() {
    MultiValueMap<String, String> mapValues = new LinkedMultiValueMap<>();
    mapValues.add("dealer", "{7A411F84-2FD3-4293-ABF7-AC3BC9101EE7}");
    mapValues.add("order_list", "599_1_599001_|598_1_598001_|561_1_561000_");
    mapValues.add("orderlist", "599001_1|598001_1|561000_1");
    Mockito.when(response.isCommitted()).thenReturn(true);
    this.handleThuleResponse(response, mapValues);
  }

  /**
   * This is implemented for testing runtime function at ForwardController.handleThuleResponse.
   *
   */
  private void handleThuleResponse(HttpServletResponse response,
      MultiValueMap<String, String> data) {
    data.toSingleValueMap().entrySet().stream()
    .map(entry -> new Cookie(entry.getKey(), StringUtils.replace(entry.getValue(), ",", "|")))
    .forEach(response::addCookie);
    response.addCookie(new Cookie("fromThule", Boolean.TRUE.toString()));
  }

}
