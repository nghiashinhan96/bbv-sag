package com.sagag.services.admin.controller.logging;

import lombok.extern.slf4j.Slf4j;

import org.apache.http.util.Asserts;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.LoggerContext;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.util.StringUtils;

import java.util.Locale;

/**
 * Support for users whom - don't want to use jconsole - alternative way to change log level just in
 * case jconsole not worked
 */
@RestController
@Slf4j
@RequestMapping("/admin")
public class LoggingLevelChangeController {

  @PostMapping(value = "/loglevel/{loglevel}")
  @ResponseStatus(value = HttpStatus.OK)
  public void loglevel(@PathVariable("loglevel") String logLevel,
      @RequestParam(value = "package") String packageName) {
    log.info("Log level: " + logLevel);
    log.info("Package name: " + packageName);
    setLogLevel(logLevel, packageName);
  }

  public void setLogLevel(String logLevel, String packageName) {
    Asserts.notNull(logLevel, "log level must not be null");
    Asserts.notNull(packageName, "package name must not be null");
    LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
    switch (StringUtils.toUpperCase(logLevel, Locale.ENGLISH)) {
      case "WARN":
        loggerContext.getLogger(packageName).setLevel(Level.WARN);
        break;
      case "INFO":
        loggerContext.getLogger(packageName).setLevel(Level.INFO);
        break;
      case "DEBUG":
        loggerContext.getLogger(packageName).setLevel(Level.DEBUG);
        break;
      case "TRACE":
        loggerContext.getLogger(packageName).setLevel(Level.TRACE);
        break;
      default:
        throw new IllegalArgumentException("Not a known loglevel: " + logLevel);
    }
  }
}
