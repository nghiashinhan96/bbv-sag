package com.sagag.services.oauth2.helper;

import com.sagag.eshop.repo.api.SupportedAffiliateRepository;
import com.sagag.services.common.enums.LoginMode;
import com.sagag.services.oauth2.exception.UnsupportedCompanyException;
import com.sagag.services.oauth2.model.VisitRegistration;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;
import java.util.Optional;

@Component
public class EshopAuthHelper {

  public static final String AFFILIATE = "affiliate";
  private static final String ON_BEHALF = "onbehalf";
  public static final String LOGIN_MODE = "login_mode";
  public static final String LANGUAGE = "language";
  public static final String ADDITIONAL_INFO = "auth_body";

  private static final String LOCATED_AFFILIATE = "located_affiliate";

  private HttpServletRequest request;

  private SupportedAffiliateRepository supportedAffiliateRepository;

  @Autowired
  public EshopAuthHelper(final HttpServletRequest request,
    final SupportedAffiliateRepository supportedAffiliateRepo) {
    this.request = request;
    this.supportedAffiliateRepository = supportedAffiliateRepo;
  }

  public LoginMode getLoginMode() {
    String loginModeValue =
        StringUtils.isNotEmpty(request.getParameter(LOGIN_MODE)) ? request.getParameter(LOGIN_MODE)
            : Optional.ofNullable(request.getAttribute(LOGIN_MODE)).map(String::valueOf)
                .orElse(StringUtils.EMPTY);
    String defaultLoginMode = StringUtils.defaultIfBlank(loginModeValue, LoginMode.NORMAL.name());
    return LoginMode.valueOf(defaultLoginMode);
  }

  public String getLoginAffiliate() {
    if (StringUtils.isNotBlank(getVisitRegistrationBody().getCompanyID()) && !isCloudDmsLogin()) {
      return getVisitRegistrationBody().getCompanyID();
    }
    return StringUtils.isNotEmpty(request.getParameter(AFFILIATE)) ? request.getParameter(AFFILIATE)
        : Optional.ofNullable(request.getAttribute(AFFILIATE)).map(String::valueOf)
            .orElse(StringUtils.EMPTY);
  }

  public String getLocatedAffiliate() {
    return StringUtils.defaultString(request.getParameter(LOCATED_AFFILIATE));
  }

  public String getLoginSalesToken() {
    return request.getParameter(ON_BEHALF);
  }

  public String getLoginLanguage() {
    return request.getParameter(LANGUAGE);
  }

  public boolean isDmsLogin() {
    return getLoginMode() == LoginMode.DMS;
  }

  public boolean isCloudDmsLogin() {
    return getLoginMode() == LoginMode.CLOUD_DMS;
  }

  public boolean isOciLogin() {
    return getLoginMode() == LoginMode.OCI;
  }

  public boolean isSsoLogin() {
    return getLoginMode() == LoginMode.SSO;
  }

  public void setAuthBody(VisitRegistration value) {
    request.setAttribute(ADDITIONAL_INFO, value);
  }

  public void setCloudDmsBody(VisitRegistration value) {
    final String company = value.getCompanyID();
    final String affiliate = supportedAffiliateRepository.findShortNameByCompanyName(company)
        .orElseThrow(() -> new UnsupportedCompanyException(company));
    request.setAttribute(AFFILIATE, affiliate);
    request.setAttribute(ADDITIONAL_INFO, value);
    request.setAttribute(LOGIN_MODE, LoginMode.CLOUD_DMS.name());
  }

  public VisitRegistration getVisitRegistrationBody() {
    if (Objects.nonNull(request.getAttribute(ADDITIONAL_INFO))) {
      return (VisitRegistration) request.getAttribute(ADDITIONAL_INFO);
    }
    return new VisitRegistration();
  }

}
