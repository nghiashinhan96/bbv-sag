package com.sagag.services.rest.controller.analytics;

import com.sagag.services.hazelcast.api.EshopGlobalSettingCacheService;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/analytics")
@Slf4j
public class DataAnalyticsController {

  private static final String JSON_EVENT_SETTING_TYPE = "JSON_EVENT";

  @Autowired
  private EshopGlobalSettingCacheService connectGlobalSettingService;

  @Autowired
  private DataAnalyticsClient dataAnalyticsClient;

  @ResponseStatus(value = HttpStatus.OK)
  @PostMapping(value = "/receive", produces = MediaType.APPLICATION_JSON_VALUE)
  public void track(final HttpServletRequest request,
      @RequestParam(value = "event_type", required = false) final String eventType)
      throws IOException {
    if (!connectGlobalSettingService.isAllowedToUse(JSON_EVENT_SETTING_TYPE, eventType)) {
      log.debug("The JSON event type = {} is disabled by DB configuration.", eventType);
      return;
    }
    dataAnalyticsClient.forwardLog(IOUtils.toString(request.getReader()));
  }

  @GetMapping("/settings")
  public Map<String, Boolean> getJsonEventTypeSettings() {
    return connectGlobalSettingService.getSettingTypeSettings(JSON_EVENT_SETTING_TYPE);
  }
}
