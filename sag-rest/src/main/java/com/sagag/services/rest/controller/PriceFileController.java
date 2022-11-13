package com.sagag.services.rest.controller;

import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.common.exception.ResultNotFoundException;
import com.sagag.services.common.external.FtpFileInfoDto;
import com.sagag.services.common.external.FtpPriceFileListProcessor;
import com.sagag.services.common.utils.RestExceptionUtils;
import com.sagag.services.rest.authorization.annotation.IsWbbCustomerUserAdminPreAuthorization;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(value = "/price-file", consumes = MediaType.APPLICATION_JSON_VALUE)
@Api("Price File APIs")
@IsWbbCustomerUserAdminPreAuthorization
public class PriceFileController {

  @Autowired
  private FtpPriceFileListProcessor ftpPriceFileListProcessor;

  @GetMapping("/list")
  @ApiOperation(value = "Returns the list of price file of customer by number.")
  public List<FtpFileInfoDto> getPriceFilesByCustomerNr(final OAuth2Authentication authed)
      throws IOException, ResultNotFoundException {
    final UserInfo user = (UserInfo) authed.getPrincipal();
    final List<FtpFileInfoDto> ftpFiles =
        ftpPriceFileListProcessor.findPriceFilesByCustomerNr(user.getCustNrStr());
    return RestExceptionUtils.doSafelyReturnNotEmptyRecords(ftpFiles);
  }

}
