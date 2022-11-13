package com.sagag.services.common.config;

import lombok.experimental.UtilityClass;

import org.apache.commons.lang3.StringUtils;

@UtilityClass
public final class AppDefaults {

  public static final String NAME = "default-app";

  public static final String VERSION = "1.0.0.0.0";

  public static final String BUILD_NUMBER = "1";

  public static final String RELEASE_BRANCH = "release/ax";

  @UtilityClass
  public static final class Cache {

    public static final int TIME_TO_LIVE_SECONDS = 3600; // 1 hour
    public static final int BACKUP_COUNT = 1;

    @UtilityClass
    public static final class ManagementCenter {

      public static final boolean ENABLED = false;
      public static final int UPDATE_INTERVAL = 3;
      public static final String URL = StringUtils.EMPTY;
    }

    @UtilityClass
    public static final class TcpIpConfig {

      public static final boolean ENABLED = false;

    }

    @UtilityClass
    public static class Cart {

      public static final int WRITE_DELAY_SECONDS = 3;
      public static final int WRITE_BATCH_SIZE = 200;

    }

    @UtilityClass
    public static class Query {

      public static final int POOL_SIZE = 10;
      public static final int QUEUE_CAPACITY = 100;
    }

  }

  public static final class ErpConfig {

    public static final int DEFAULT_REQUEST_SIZE = 20;
  }

}
