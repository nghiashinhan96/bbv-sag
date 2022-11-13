package com.sagag.services.rest.controller;

import com.sagag.eshop.repo.hz.entity.ShopType;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.rest.authorization.annotation.IsAccessibleUrlPreAuthorization;
import com.sagag.services.rest.swagger.docs.ApiDesc;
import com.sagag.services.service.api.OciService;
import com.sagag.services.service.cart.support.ShopTypeDefault;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/oci")
@Api(tags = "OCI APIs")
public class OciController {

  @Autowired
  private OciService ociService;

  /**
   * Prepares Oci data form.
   *
   * @param authed user credential
   * @return HttpStatus.OK when preparing file successfully
   */
  @ApiOperation(value = ApiDesc.Oci.EXPORT_OCI_API_DESC, notes = ApiDesc.Oci.EXPORT_OCI_API_NOTE)
  @GetMapping(value = "/export/order",
      produces = MediaType.TEXT_HTML_VALUE)
  @IsAccessibleUrlPreAuthorization
  @PreAuthorize("hasPermission(#request, 'canUsedSubShoppingCart')")
  public String exportOrder(
      final OAuth2Authentication authed,
      @RequestParam(value = "hook_url") String hookUrl,
      @ShopTypeDefault ShopType shopType,
      HttpServletRequest request) {
    final UserInfo user = (UserInfo) authed.getPrincipal();
    return ociService.exportOrder(user, hookUrl, shopType);
  }
}
