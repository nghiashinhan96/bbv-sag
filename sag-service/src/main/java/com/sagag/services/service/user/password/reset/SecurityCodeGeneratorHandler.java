package com.sagag.services.service.user.password.reset;

import com.sagag.eshop.repo.entity.EshopUser;
import com.sagag.eshop.repo.entity.forgotpassword.PasswordResetToken;
import com.sagag.eshop.service.exception.UserValidationException;
import com.sagag.eshop.service.user.UserSearchFactory;
import com.sagag.services.common.exception.ServiceException;
import com.sagag.services.common.utils.WholesalerUtils;
import com.sagag.services.domain.eshop.dto.SecurityCodeRequestDto;
import com.sagag.services.service.mail.ChangePasswordCriteria;
import com.sagag.services.service.mail.ChangePasswordMailSender;
import com.sagag.services.service.user.password.ChangePasswordPermissionEvaluator;
import com.sagag.services.service.user.password.SecurityCodeGenerator;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.Optional;

@Component
@Slf4j
public class SecurityCodeGeneratorHandler extends AbstractResetPasswordHandler
  implements ResetPasswordHandler<SecurityCodeRequestDto, String> {

  @Autowired
  private SecurityCodeGenerator generator;

  @Autowired
  private ChangePasswordPermissionEvaluator changePasswordPermissionEvaluator;

  @Autowired
  private UserSearchFactory userSearchFactory;

  @Autowired
  protected ChangePasswordMailSender changePasswordMailSender;

  @Override
  @Transactional
  public String handle(SecurityCodeRequestDto securityCodeRequestDto) throws ServiceException {
    log.debug("Get new code for user request");
    // Initial variables
    final String username = securityCodeRequestDto.getUsername();
    final String shortName = securityCodeRequestDto.getAffiliateId();
    Optional<EshopUser> userOpt = Optional.empty();
    try {
      userOpt = userSearchFactory.searchEshopUserByInput(username, shortName);
    } catch (UserValidationException e) {
      log.debug("Invalid username or e-mail: " + username);
    }

    if (userOpt.isPresent()) {
      EshopUser user = userOpt.get();
      // Get user and organization info
      final boolean isFinalUser = WholesalerUtils.isFinalCustomerEndpoint(shortName);
      if (!isFinalUser) {
        changePasswordPermissionEvaluator.check(user, shortName);
      } else if (securityCodeRequestDto.isDigiInvoiceRequest()) {
        // EPIC #5912 : DIGI INVOICE NOT APPLY FOR FINAL CUSTOMER
        return StringUtils.EMPTY;
      }

      final String affiliateEmail = findDefaultAffiliateEmail(shortName, isFinalUser);

      if (securityCodeRequestDto.isDigiInvoiceRequest()) {
        Assert.hasText(securityCodeRequestDto.getInvoiceRecipientEmail(),
            "The given invoiceRecipientEmail must not be empty");
      }

      // Send mail create new password
      PasswordResetToken passwordToken = generator.generateCode(user);
      ChangePasswordCriteria criteria =
          ChangePasswordCriteria.buildSecurityCodeEmail(user, affiliateEmail,
              securityCodeRequestDto.isDigiInvoiceRequest()
                  ? securityCodeRequestDto.getInvoiceRecipientEmail()
                  : user.getEmail(),
              securityCodeRequestDto.isDigiInvoiceRequest() ? StringUtils.EMPTY
                  : securityCodeRequestDto.buildRedirectUrl(passwordToken.getToken(),
                      passwordToken.getHashUsernameCode()),
              passwordToken.getToken(), localeContextHelper.toLocale(user.getNonNullLangIso()),
              securityCodeRequestDto.isDigiInvoiceRequest());
      criteria.setFinalUser(isFinalUser);

      changePasswordMailSender.send(criteria);
      return passwordToken.getHashUsernameCode();
    }
    log.debug("Invalid username or e-mail: " + username);
    return StringUtils.EMPTY;
  }
}
