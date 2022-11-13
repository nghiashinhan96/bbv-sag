package com.sagag.services.admin.controller;


import com.sagag.services.domain.eshop.dto.EshopReleaseDto;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Calendar;

@RestController
public class ReleaseController {

  @Value("${app.releaseBranch}")
  private String appReleaseBranch;

  @Value("${app.buildNumber}")
  private String appBuildNumber;

  @Value("${app.version}")
  private String appVersion;

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
