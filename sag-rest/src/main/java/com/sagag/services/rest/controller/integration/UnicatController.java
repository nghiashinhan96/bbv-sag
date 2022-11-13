package com.sagag.services.rest.controller.integration;

import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.common.profiles.UnicatProfile;
import com.sagag.services.rest.swagger.docs.ApiDesc;
import com.sagag.services.service.api.UnicatService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/unicat", produces = MediaType.APPLICATION_JSON_VALUE)
@UnicatProfile
@Api(tags = "Unicat Integration APIs")
public class UnicatController {

  private static final String UNICAT_CATALOG_URI_KEY = "unicatCatalogUri";

  @Autowired
  private UnicatService unicatService;

  @ApiOperation(value = ApiDesc.Unicat.GET_UNICAT_OPEN_CATALOG_DESC,
      notes = ApiDesc.Unicat.GET_UNICAT_OPEN_CATALOG_NOTE)
  @GetMapping(value = "/open-cataloge-uri")
  public Map<String, String> getUnicatOpenCatalogUri(final OAuth2Authentication authed) {
    final UserInfo user = (UserInfo) authed.getPrincipal();
    Map<String, String> result = new HashMap<>();
    result.put(UNICAT_CATALOG_URI_KEY, unicatService.getUnicatCatalogUri(user));
    return result;
  }

}
