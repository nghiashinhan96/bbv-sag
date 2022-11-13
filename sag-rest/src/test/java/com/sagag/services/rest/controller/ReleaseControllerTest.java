package com.sagag.services.rest.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.sagag.services.rest.controller.anonymous.ReleaseController;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

/**
 * Unit tests class for Release REST APIs.
 */
@RunWith(MockitoJUnitRunner.class)
public class ReleaseControllerTest {

  private static final String RELEASE_VERSION = "1.1.66.0.0-m42";

  private static final String RELEASE_BUILD_NR = "1.0";

  private static final String RELEASE_BRANCH = "derendinger-at";

  private MockMvc mockMvc;

  @InjectMocks
  private ReleaseController releaseController;

  @Before
  public void init() {
    MockitoAnnotations.initMocks(this);
    this.mockMvc = MockMvcBuilders.standaloneSetup(releaseController).build();
  }

  @Test
  public void testGetRelease() throws Exception {
    ReflectionTestUtils.setField(releaseController, "appReleaseBranch", RELEASE_BRANCH);
    ReflectionTestUtils.setField(releaseController, "appBuildNumber", RELEASE_BUILD_NR);
    ReflectionTestUtils.setField(releaseController, "appVersion", RELEASE_VERSION);
    mockMvc.perform(get("/release")).andExpect(status().isOk())
        .andExpect(jsonPath("$.releaseBuild", Matchers.is(RELEASE_BUILD_NR)))
        .andExpect(jsonPath("$.releaseVersion", Matchers.is(RELEASE_VERSION)))
        .andExpect(jsonPath("$.releaseBranch", Matchers.is(RELEASE_BRANCH)));
  }
}
