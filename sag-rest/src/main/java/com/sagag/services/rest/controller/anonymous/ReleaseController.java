package com.sagag.services.rest.controller.anonymous;

import com.sagag.services.domain.eshop.dto.EshopReleaseDto;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Calendar;

/**
 * Release controller class to expose public APIs for release.
 */
@RestController
@Api(tags = "Release Info API")
public class ReleaseController {

  @Value("${app.releaseBranch}")
  private String appReleaseBranch;

  @Value("${app.buildNumber}")
  private String appBuildNumber;

  @Value("${app.version}")
  private String appVersion;

  @ApiOperation(value = "The API to return release version of application")
  @GetMapping(value = "/release", produces = MediaType.APPLICATION_JSON_VALUE)
  public EshopReleaseDto getRelease() {
    return EshopReleaseDto.builder()
        .releaseBuild(StringUtils.defaultString(appBuildNumber))
        .releaseVersion(StringUtils.defaultIfBlank(appVersion, "1.0.0.0.0"))
        .releaseDate(Calendar.getInstance().getTime())
        .releaseBranch(StringUtils.defaultString(appReleaseBranch))
        .build();
  }

}
