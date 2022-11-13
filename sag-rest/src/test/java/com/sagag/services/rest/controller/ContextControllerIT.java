package com.sagag.services.rest.controller;

import com.sagag.services.common.annotation.EshopIntegrationTest;
import com.sagag.services.rest.app.RestApplication;
import org.hamcrest.Matchers;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Integration tests class for Context REST APIs.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { RestApplication.class })
@EshopIntegrationTest @Ignore

public class ContextControllerIT extends AbstractControllerIT {

  @Test
  public void testGetEshopContext() throws Exception {
    performGet("/context/").andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
        .andExpect(jsonPath("$", Matchers.notNullValue()));
  }

}
