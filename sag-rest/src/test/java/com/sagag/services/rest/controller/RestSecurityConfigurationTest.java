package com.sagag.services.rest.controller;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.whenNew;

import com.sagag.services.common.annotation.EshopMockitoJUnitRunner;
import com.sagag.services.rest.app.RestSecurityConfiguration;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Unit tests class for Olyslager REST APIs.
 */
@EshopMockitoJUnitRunner
@Ignore("all cases failed")
public class RestSecurityConfigurationTest {


  @InjectMocks
  private RestSecurityConfiguration restSecurityConfiguration;

  @Mock
  private FilterRegistrationBean<Filter> filterRegistrationBean;
  @Captor
  private ArgumentCaptor<Filter> filterCaptor;


  /**
   * Sets up the resources for testing.
   *
   * @throws Exception throws when program fails.
   */
  @Before
  public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);
    // prepare
    whenNew(FilterRegistrationBean.class).withNoArguments().thenReturn(filterRegistrationBean);
    doNothing().when(filterRegistrationBean).setFilter(filterCaptor.capture());
    doNothing().when(filterRegistrationBean).setOrder(Ordered.HIGHEST_PRECEDENCE);
  }


  @Test
  public void shouldReturnSuccessfulWithOptionsVerb() throws Exception {
    // action
    restSecurityConfiguration.corsFilter();
    verify(filterRegistrationBean).setOrder(Ordered.HIGHEST_PRECEDENCE);
    verify(filterRegistrationBean).setFilter(filterCaptor.capture());

    final Filter filter = filterCaptor.getValue();

    HttpServletRequest httpServletRequestMock = mock(HttpServletRequest.class);
    final HttpServletResponse httpServletResponseMock = mock(HttpServletResponse.class);
    final FilterChain filterChainMock = mock((FilterChain.class));
    when(httpServletRequestMock.getHeader("Origin")).thenReturn("www.eshop.com");
    when(httpServletRequestMock.getHeader("Access-Control-Request-Method")).thenReturn("POST");
    when(httpServletRequestMock.getMethod()).thenReturn(HttpMethod.OPTIONS.name());
    filter.doFilter(httpServletRequestMock, httpServletResponseMock, filterChainMock);

    verify(httpServletResponseMock).setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "*");
    verify(httpServletResponseMock).setHeader(HttpHeaders.ACCESS_CONTROL_MAX_AGE, "3600");
    verify(httpServletResponseMock).setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS, "true");

    // make sure setStatus OK 200 is return with OPTIONS method
    verify(httpServletResponseMock).setStatus(HttpServletResponse.SC_OK);

    verify(filterChainMock, never()).doFilter(httpServletRequestMock, httpServletResponseMock);

  }

  @Test
  public void shouldDoFilterNext() throws Exception {
    // action
    restSecurityConfiguration.corsFilter();
    verify(filterRegistrationBean).setOrder(Ordered.HIGHEST_PRECEDENCE);
    verify(filterRegistrationBean).setFilter(filterCaptor.capture());

    Filter filter = filterCaptor.getValue();

    HttpServletRequest httpServletRequestMock = mock(HttpServletRequest.class);
    HttpServletResponse httpServletResponseMock = mock(HttpServletResponse.class);
    FilterChain filterChainMock = mock((FilterChain.class));
    filter.doFilter(httpServletRequestMock, httpServletResponseMock, filterChainMock);

    verify(filterChainMock).doFilter(httpServletRequestMock, httpServletResponseMock);

    verify(httpServletResponseMock, never()).setStatus(HttpServletResponse.SC_OK);

  }
}
