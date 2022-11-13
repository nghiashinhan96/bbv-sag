package com.sagag.services.common.cors;

import com.sagag.services.common.contants.SagConstants;
import com.sagag.services.common.locale.LocaleContextHelper;
import com.sagag.services.common.utils.RequestContextUtils;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.cors.CorsUtils;

@Slf4j
public class SagCorsFilter implements Filter {

  private static final String HEADER_CONTENT_LANGUAGE = "Accept-Language";

  private static final String HEADER_XSS_PROTECTION = "X-XSS-Protection";

  private static final String HEADER_CONTENT_TYPE_OPTIONS = "X-Content-Type-Options";

  private static final String HEADER_CONTENT_SECURITY_POLICY = "X-Content-Security-Policy";

  private static final String HEADER_X_FRAME_OPTIONS = "X-Frame-Options";

  private static final String[] TRUSTED_URL_LIST = {
    "preconnect.sag.services", "preconnect2.sag.services", "preconnect-int.sag.services",
    "connect.sag.services", "ax.sib-services.ch", "haynespro-services.com",
    "shop.derendinger.at", "shop-derendinger.at", "shop.matik.at", "shop-matik.at" };

  // @formatter:off
  private static final String[] RESPONSE_ALLOWED_HEADER = new String[] {
      HttpHeaders.CONTENT_TYPE,
      HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS,
      HttpHeaders.ACCESS_CONTROL_REQUEST_HEADERS,
      HttpHeaders.AUTHORIZATION,
      HttpHeaders.ACCESS_CONTROL_MAX_AGE,
      "x-auth-token",
      "x-requested-with",
      "X-XSRF-TOKEN",
      SagConstants.HEADER_X_SAG_REQUEST_ID,
      SagConstants.HEADER_X_VERSION };

  private static final String[] CORS_SUPPORT_METHODS = new String[] {
      HttpMethod.GET.name(),
      HttpMethod.PUT.name(),
      HttpMethod.POST.name(),
      HttpMethod.DELETE.name(),
      HttpMethod.OPTIONS.name() }; // @formatter:on

  @Autowired
  private LocaleContextHelper localeContextHelper;

  @Override
  public void init(FilterConfig filterConfig) { /* do nothing */}

  @Override
  public void destroy() { /* do nothing */}

  @Override
  public void doFilter(final ServletRequest req, final ServletResponse res,
      final FilterChain chain) throws IOException, ServletException {

    final HttpServletResponse response = (HttpServletResponse) res;
    final HttpServletRequest request = (HttpServletRequest) req;
    storeRequestIdToLog4JContext(request);
    resolveRequestLocale(request);
    updateResponseHeaders(response);

    if (CorsUtils.isPreFlightRequest(request)) {
      response.setStatus(HttpServletResponse.SC_OK);
    } else {
      chain.doFilter(request, res);
    }
  }

  /**
   * This method should be called before any log entry.
   *
   * @param request
   */
  private void storeRequestIdToLog4JContext(HttpServletRequest request) {
    final ServletRequestAttributes requestAttributes = new ServletRequestAttributes(request);
    RequestContextUtils.setupThreadContext(requestAttributes);
  }

  private void resolveRequestLocale(final HttpServletRequest request) {
    final String requestLang = request.getHeader(HEADER_CONTENT_LANGUAGE);
    final Locale requestLocale = localeContextHelper.toSupportedLocale(requestLang);
    log.debug("Changing language = {} to locale = {} ", requestLang, requestLocale);
    LocaleContextHolder.setLocale(requestLocale);
  }

  public void updateResponseHeaders(HttpServletResponse response) {
    response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "*");
    response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS,
        StringUtils.join(CORS_SUPPORT_METHODS, ","));
    response.setHeader(HttpHeaders.ACCESS_CONTROL_MAX_AGE, "3600");
    response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS,
        StringUtils.join(RESPONSE_ALLOWED_HEADER, ","));
    response.setHeader(HEADER_XSS_PROTECTION, "1; mode=block");
    response.setHeader(HEADER_CONTENT_TYPE_OPTIONS, "nosniff");
    response.setHeader(HEADER_CONTENT_SECURITY_POLICY, buildContentSecurityPolicy().stream()
        .collect(Collectors.joining(SagConstants.SEMICOLON)));
    response.setHeader(HEADER_X_FRAME_OPTIONS, "DENY");
  }

  private static List<String> buildContentSecurityPolicy() {
    final String trustedUrls = Lists.newArrayList(TRUSTED_URL_LIST).stream()
      .collect(Collectors.joining(SagConstants.SPACE));
    return Lists.newArrayList(
        "allow self",
        "img-src *",
        StringUtils.join("object-src ", trustedUrls),
        StringUtils.join("script-src ", trustedUrls));
  }

}
