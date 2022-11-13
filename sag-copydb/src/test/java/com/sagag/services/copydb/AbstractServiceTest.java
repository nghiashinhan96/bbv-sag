package com.sagag.services.copydb;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.context.junit4.SpringRunner;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@RunWith(SpringRunner.class)
@SpringBootTest
@Data
@Slf4j
public abstract class AbstractServiceTest {

  protected static final String NEW_LINE = "\n";
  protected static final String NEW_TAB = "\t";

  protected static final String RETURNED_LINE = "\n=====================================================";

  @Autowired
  private Environment env;

  @Autowired
  private AppProperties properties;

  enum Profiles {
    DEV, PRE, PROD
  }

  protected String getOneActiveProfile() {
    final List<String> activeProfiles = Arrays.asList(env.getActiveProfiles());
    if (activeProfiles.stream().anyMatch(Profiles.DEV.name()::equalsIgnoreCase)) {
      return Profiles.DEV.name();
    }
    if (activeProfiles.stream().anyMatch(Profiles.PRE.name()::equalsIgnoreCase)) {
      return Profiles.PRE.name();
    }
    if (activeProfiles.stream().anyMatch(Profiles.PROD.name()::equalsIgnoreCase)) {
      return Profiles.PROD.name();
    }
    throw new IllegalArgumentException("No supported environment.");
  }

  protected void writeFile(String fileName, String contents) throws IOException {
    final File outFile = new File(fileName);
    log.info("File contents {}", contents);
    FileUtils.writeStringToFile(outFile, contents, StandardCharsets.UTF_8.name());
  }

  protected void writeLinesToFile(String fileName, List<String> contents) throws IOException {
    final File outFile = new File(fileName);
    log.info("File contents {}", contents);
    FileUtils.writeLines(outFile, contents, NEW_LINE);
  }
}
