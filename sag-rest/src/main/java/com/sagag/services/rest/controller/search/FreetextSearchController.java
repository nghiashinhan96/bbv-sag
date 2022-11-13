package com.sagag.services.rest.controller.search;

import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.ivds.freetext.IFreetextSearchService;
import com.sagag.services.ivds.request.FreetextSearchRequest;
import com.sagag.services.ivds.response.FreetextResponseDto;
import com.sagag.services.rest.mapper.GenericFreetextResponseDtoMapper;
import com.sagag.services.service.request.FreetextSearchRequestBody;

import io.swagger.annotations.Api;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/search/free-text", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(tags = "Freetext Search API")
public class FreetextSearchController {

  @Autowired
  private List<IFreetextSearchService> freetextSearchServices;

  @Autowired
  private GenericFreetextResponseDtoMapper genericFreetextResponseMapper;

  /**
   * Returns the vehicles articles by free text matched.
   *
   * @param authed the authenticated user
   * @param body the freetext search request body
   * @return an object of {@link FreetextResponseDto}
   */
  @PostMapping
  public FreetextResponseDto searchItemsByFreeText(final OAuth2Authentication authed,
      @RequestBody FreetextSearchRequestBody body) {
    Assert.notEmpty(body.getOptions(), "The search options must not be empty");

    final UserInfo user = (UserInfo) authed.getPrincipal();
    final String[] options = body.getOptions();
    final FreetextSearchRequest searchRequest = FreetextSearchRequest.builder()
            .text(body.getKeyword())
            .user(user)
            .fitering(Optional.empty())
            .isFullRequest(body.isFullRequest())
            .searchOptions(Arrays.asList(options))
            .pageRequest(PageRequest.of(body.getPage(), body.getSize()))
            .build();

    final FreetextResponseDto response = new FreetextResponseDto();
    final String freetext = searchRequest.getText();
    if (StringUtils.isBlank(freetext)) {
      return response;
    }

    freetextSearchServices.stream()
    .filter(service -> service.support(searchRequest.getSearchOptions()))
    .forEach(service -> service.search(searchRequest, response));

    if (!body.isGenericSearch()) {
      return response;
    }
    return genericFreetextResponseMapper.toDto(response);
  }
}
