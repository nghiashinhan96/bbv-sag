package com.sagag.services.rest.controller.account;

import com.sagag.services.common.exception.ServiceException;
import com.sagag.services.domain.eshop.dto.EshopUserLoginDto;
import com.sagag.services.domain.eshop.dto.SecurityCodeRequestDto;
import com.sagag.services.service.user.password.reset.SecurityCodeGeneratorHandler;
import com.sagag.services.service.user.password.reset.SelfEshopUserResetPasswordHandler;
import com.sagag.services.service.user.password.reset.SelfEshopUserValidateSecurityCodeResetPasswordHandler;

import io.swagger.annotations.Api;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller class to define service APIs for Forgot Password.
 */
@RestController
@Api(tags = "Forgot Password APIs")
@Slf4j
public class ForgotPasswordController {

  @Autowired
  private SecurityCodeGeneratorHandler securityCodeGeneratorHandler;

  @Autowired
  private SelfEshopUserResetPasswordHandler selfEshopUserResetPasswordHandler;

  @Autowired
  private SelfEshopUserValidateSecurityCodeResetPasswordHandler validateCodeResetPasswordHandler;

  /**
   * Request to get new code with user-name or email.
   *
   * @throws ServiceException
   *
   */
  @PostMapping(value = "/user/send-code",
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(value = HttpStatus.CREATED)
  public String getCode(@RequestBody final SecurityCodeRequestDto securityCodeRequestDto)
      throws ServiceException {
    log.debug("Get new code for user request");
    return securityCodeGeneratorHandler.handle(securityCodeRequestDto);
  }

  @PostMapping(value = "/user/forgot-password/code", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(value = HttpStatus.OK)
  public String validateForgotPasswordSecurityCode(
    @RequestBody final EshopUserLoginDto eshopUserLoginDto) {
    return validateCodeResetPasswordHandler.handle(eshopUserLoginDto).getCode();
  }

  @PostMapping(value = "/user/forgot-password/reset-password",
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(value = HttpStatus.OK)
  public void updateForgotPasswordByOwner(@RequestBody final EshopUserLoginDto eshopUserLoginDto)
    throws ServiceException {
    selfEshopUserResetPasswordHandler.handle(eshopUserLoginDto);
  }

}
