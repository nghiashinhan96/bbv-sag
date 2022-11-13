package com.sagag.services.sso.tasks;

import com.microsoft.aad.adal4j.AuthenticationResult;

import java.util.concurrent.ExecutorService;

public class TaskBase {
  protected static final String AUTHORITY = "https://login.windows.net/sag-ag.ch";
  protected static AuthenticationResult result = null;
  protected static ExecutorService service = null;
}
