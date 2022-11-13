package com.sagag.services.rest.controller.anonymous;

import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.logging.utils.LogUtils;
import com.sagag.services.service.request.JSLogRequestBody;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller class to define APIs for Javascript error log services.
 */
@RestController
@RequestMapping("/log")
@Api(tags = "JS Error Logging API")
@Slf4j
public class JSErrorLogController {

  /**
   * The service will log unexpected Javascripts error to track on server side
   *
   * @param logError
   */
  @ApiOperation(value = "Gets log js error", notes = "The service will log js error")
  @PostMapping(value = "/jserror", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  public void logJSError(OAuth2Authentication authed, @RequestBody JSLogRequestBody logError) {
    final UserInfo userInfo = (UserInfo) authed.getPrincipal();
    LogUtils.putUser(userInfo.getUsername()); // query by user requested
    log.error("URL: {} \nJS Error: {} \nUser Info: {}", logError.getUrl(), logError.getMessage(),
        userInfo);
  }

}
