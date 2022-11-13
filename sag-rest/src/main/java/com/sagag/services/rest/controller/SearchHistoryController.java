package com.sagag.services.rest.controller;

import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.domain.eshop.dto.UserSearchHistoryDto;
import com.sagag.services.service.api.SearchHistoryService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/search/history")
@Api(tags = { "User search History APIs" })
public class SearchHistoryController {

  @Autowired
  private SearchHistoryService searchHistoryService;

  @ApiOperation(value = "Get search histories")
  @GetMapping
  public UserSearchHistoryDto getUserSearchHistories(final OAuth2Authentication authed,
      @RequestParam final String fromSource) {
    final UserInfo user = (UserInfo) authed.getPrincipal();
    return searchHistoryService.getLatestHistories(user, fromSource);
  }

}
