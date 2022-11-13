package com.sagag.services.rest.controller.digi_invoice;

import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.common.exception.ServiceException;
import com.sagag.services.domain.eshop.dto.DigiInvoiceChangeMailRequestDto;
import com.sagag.services.domain.eshop.dto.EshopUserLoginDto;
import com.sagag.services.domain.eshop.dto.SecurityCodeRequestDto;
import com.sagag.services.service.digi_invoice.DigiInvoiceService;
import com.sagag.services.service.user.password.reset.SecurityCodeGeneratorHandler;
import com.sagag.services.service.user.password.reset.SelfEshopUserValidateSecurityCodeResetPasswordHandler;

import io.swagger.annotations.Api;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller class to define service APIs for Digi Invoice.
 */
@RestController
@Api(tags = "Digi Invoice APIs")
@Slf4j
@RequestMapping("/digi-invoice")
public class DigiInvoiceController {

  @Autowired
  private SecurityCodeGeneratorHandler securityCodeGeneratorHandler;

  @Autowired
  private DigiInvoiceService digiInvoiceService;

  @Autowired
  private SelfEshopUserValidateSecurityCodeResetPasswordHandler validateCodeResetPasswordHandler;

  /**
   * Request to get new code with user-name or email.
   *
   * @throws ServiceException
   *
   */
  @PostMapping(value = "/security-code/send", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(value = HttpStatus.CREATED)
  public String getCode(@RequestBody final SecurityCodeRequestDto securityCodeRequestDto, final OAuth2Authentication authed)
      throws ServiceException {
    log.debug("Get new code for user request");
    final UserInfo user = (UserInfo) authed.getPrincipal();
    securityCodeRequestDto.setAffiliateId(user.getAffiliateShortName());
    securityCodeRequestDto.setLangCode(user.getLanguage());
    securityCodeRequestDto.setUsername(user.getUsername());
    securityCodeRequestDto.setDigiInvoiceRequest(true);
    return securityCodeGeneratorHandler.handle(securityCodeRequestDto);
  }

  @PostMapping(value = "/security-code/check", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(value = HttpStatus.OK)
  public String validateSecurityCode(@RequestBody final EshopUserLoginDto eshopUserLoginDto) {
    return validateCodeResetPasswordHandler.handle(eshopUserLoginDto).getCode();
  }

  @PostMapping(value = "/submission/change-mail", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(value = HttpStatus.OK)
  public void sendMailInform(@RequestBody final DigiInvoiceChangeMailRequestDto requestDto, final OAuth2Authentication authed) {
    final UserInfo user = (UserInfo) authed.getPrincipal();
    digiInvoiceService.sendMailConfirmChangeElectronicInvoice(requestDto, user);
  }

}
