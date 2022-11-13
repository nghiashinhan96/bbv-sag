package com.sagag.services.sso;

import java.util.Optional;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.sagag.services.common.annotation.EshopIntegrationTest;
import com.sagag.services.sso.tasks.AuthorizationFailure;
import com.sagag.services.sso.tasks.RequestBearerTokenTask;
import com.sagag.services.sso.tasks.ValidateTokenTask;

import lombok.extern.slf4j.Slf4j;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { SingleSignOnApplication.class })
@EshopIntegrationTest
@Slf4j
public class RequestBearerTokenTaskIT {

  /**
   * The info of AX Connection.
   *
   */
  private static final String AX_APP_URL = "https://prebusiness.sag.services/tstapps/training-webshop-service-app";

  private static final String AX_CLIENT_ID = "bcbff563-d9c8-419e-ab33-20c2bcf27665";

  private static final String AX_USERNAME = "sys-ws-connect-test@sag-ag.ch";

  private static final String AX_PASSWORD = "agenda01";

  /**
   * The info of ERP Connection.
   *
   */
  private static final String ERP_APP_URL = "https://prebusiness.sag.services/business/webshop";

  private static final String ERP_CLIENT_ID = "d6365451-aa5e-4983-bb4e-4e1372b1fbd2";

  private static final String ERP_USERNAME = "sys-ws-connect-accept@sag-ag.ch";

  private static final String ERP_PASSWORD = "agenda01";


  @Test
  public void testGetAccessTokenFromUserCredentialsWithAxDevConnection() throws Exception {
	  String axAppUrl = "https://dev-webshop-service-app.tstapps.sag.services";
	  String axClientId = "dad18054-7838-4236-937a-ebf9b0cc309e";
	  String axUsername = "sys-ws-connect-dev@sag-ag.ch";
	  String axPassword = "agenda01";
	  String token = RequestBearerTokenTask
			  .getAccessTokenFromUserCredentials(axAppUrl, axClientId, axUsername, axPassword);
	  Assert.assertThat(token, Matchers.notNullValue());
	  log.info("Token result = {}", token);
  }

  @Test
  public void testGetAccessTokenFromUserCredentialsWithAxConnection() throws Exception {

    String token = RequestBearerTokenTask
        .getAccessTokenFromUserCredentials(AX_APP_URL, AX_CLIENT_ID, AX_USERNAME, AX_PASSWORD);
    Assert.assertThat(token, Matchers.notNullValue());
    log.info("Token result = {}", token);
  }

  @Test
  public void testGetAccessTokenFromUserCredentialsWithErpConnection() throws Exception {

    String token = RequestBearerTokenTask
        .getAccessTokenFromUserCredentials(ERP_APP_URL, ERP_CLIENT_ID, ERP_USERNAME, ERP_PASSWORD);
    Assert.assertThat(token, Matchers.notNullValue());
    log.debug("Token result = {}", token);
  }

  @Test
  public void testValidateTokenTask() throws Exception {

    // This is the expired token to verify
    String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsIng1dCI6IkhIQnlLVS0wRHFBcU1aaDZaRlBkMlZXY"
        + "U90ZyIsImtpZCI6IkhIQnlLVS0wRHFBcU1aaDZaRlBkMlZXYU90ZyJ9"
        + ".eyJhdWQiOiJodHRwczovL2Rldi13ZWJzaG9wLXNlcnZpY2UtYXBwLnRzdGFwcHMuc2FnLnNlcnZpY2VzIiwiaXNz"
        + "IjoiaHR0cHM6Ly9zdHMud2luZG93cy5uZXQvODdiYTdiNTctY2Q2NC00MmU4LWEzYzQtNjMwMGU0YmRjYTI1LyIsImlhd"
        + "CI6MTUwNTM3MDg0OSwibmJmIjoxNTA1MzcwODQ5LCJleHAiOjE1MDUzNzQ3NDksImFjciI6IjEiLCJhaW8iOiJBU1FBMi84R"
        + "kFBQUE1TUwwUFdKQWdZNjJydklJR250LzR0eXVvNkRRTnVYQ3Zra09HejFvNGJrPSIsImFtciI6WyJwd2Q"
        + "iXSwiYXBwaWQiOiJkYWQxODA1NC03ODM4LTQyMzYtOTM3YS1lYmY5YjBjYzMwOWUiLCJhcHBpZGFjciI6IjAiLCJlX2V4c"
        + "CI6MjYyODAwLCJmYW1pbHlfbmFtZSI6IkJ1c2luZXNzIERldmVsb3BtZW50IiwiZ2l2ZW5fbmFtZSI6IkNvbm5lY3QiLCJpcG"
        + "FkZHIiOiIxMTMuMTYxLjk0LjE1MiIsIm5hbWUiOiJDb25uZWN0IEJ1c2luZXNzIERldmVsb3BtZW50Iiwib2lkIjoiY2Q0Y2Zj"
        + "ZDktYTVhOS00YWRkLTk4NmEtMzNlN2VkMTEwNGI5Iiwib25wcmVtX3NpZCI6IlMtMS01LTIxLTcyMjkxNzMyLTcyNjI5MzQ3My0x"
        + "NDgxNTEwODc4LTE5NzU0Iiwic2NwIjoidXNlcl9pbXBlcnNvbmF0aW9uIiwic3ViIjoiMWdrdF94cGtabXhHaUU3ZWZpcGZ5aHJRN"
        + "lNzbzQ5dXFtME45Yzhac25RbyIsInRpZCI6Ijg3YmE3YjU3LWNkNjQtNDJlOC1hM2M0LTYzMDBlNGJkY2EyNSIsInVuaXF1ZV9uYW1l"
        + "Ijoic3lzLXdzLWNvbm5lY3QtZGV2QHNhZy1hZy5jaCIsInVwbiI6InN5cy13cy1jb25uZWN0LWRldkBzYWctYWcuY2giLCJ2ZXIiOiIx"
        + "LjAifQ.ZOL1AlZ9_fawZ9etghWyZWxQpx_WgsW0wWwz9P9o2YYOXx-wS_420jl-q4EOvGuy_A1URKmAElzNY8RdGV_JJg5un8J"
        + "5gRDjq0p1GDgUNte8qE5vSUcc2joZHzNhimL5ia3p4HtZrX82elCv1Nk8wDKCpP5o7G_cfyGLI3rDXzLTtM-63QlmKE1bXRRVC"
        + "jKUYxi_wFS5xkMd6wA5b7Jewdud6GMSVEfn1UUQj9KSsMSgWf8WYLei7-5OpI5wMvKvz2ezrMwjop2z8oU_hnz1FBoP_LLhmE2x"
        + "LB2ho-FdO7cWDAqExstIOqTuFhjjocxaW1jNHDlFaO-Ygs0JakBS0w";
    Optional<AuthorizationFailure> authFailureOpt =
        ValidateTokenTask.validate(token, AX_APP_URL);

    Assert.assertThat(authFailureOpt.isPresent(), Matchers.is(true));
  }
}
