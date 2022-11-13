package com.sagag.services.common.config;

import lombok.Data;

import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import java.util.Arrays;
import java.util.List;

/**
 * Properties specific to Connect application.
 *
 * <p>
 * Properties are configured in the application.yml file.
 * </p>
 */
@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "app", ignoreUnknownFields = false)
@Data
@Order(Ordered.HIGHEST_PRECEDENCE)
public class AppProperties {

  private String name = AppDefaults.NAME;

  private String version = AppDefaults.VERSION;

  private String buildNumber = AppDefaults.BUILD_NUMBER;

  private String releaseBranch = AppDefaults.RELEASE_BRANCH;

  private final Cache cache = new Cache();

  private final DatabaseInfo db = new DatabaseInfo();

  private final ElasticsearchInfo elasticsearch = new ElasticsearchInfo();

  private final HaynesProCallback hpback = new HaynesProCallback();

  private final InvoiceConfiguration invoice = new InvoiceConfiguration();

  private final ErpRequestConfiguration erpConfig = new ErpRequestConfiguration();

  @Data
  public static final class Cache {

    private int timeToLiveSeconds = AppDefaults.Cache.TIME_TO_LIVE_SECONDS;

    private int backupCount = AppDefaults.Cache.BACKUP_COUNT;

    private final ManagementCenter managementCenter = new ManagementCenter();

    private TcpIpConfig tcpConfig = new TcpIpConfig();

    private final Query query = new Query();

    private final Cart cart = new Cart();

    @Data
    public static final class ManagementCenter {

      private boolean enabled = AppDefaults.Cache.ManagementCenter.ENABLED;

      private int updateInterval = AppDefaults.Cache.ManagementCenter.UPDATE_INTERVAL;

      private String url = AppDefaults.Cache.ManagementCenter.URL;

    }

    @Data
    public static final class TcpIpConfig {

      private boolean enabled = AppDefaults.Cache.TcpIpConfig.ENABLED;

      private List<String> members = Arrays.asList("127.0.0.1");

    }

    @Data
    public static class Query {

      private int poolSize = AppDefaults.Cache.Query.POOL_SIZE;

      private int queueCapacity = AppDefaults.Cache.Query.QUEUE_CAPACITY;

    }

    @Data
    public static class Cart {

      private int writeDelaySeconds = AppDefaults.Cache.Cart.WRITE_DELAY_SECONDS;

      private int writeBatchSize = AppDefaults.Cache.Cart.WRITE_BATCH_SIZE;

    }

  }

  @Data
  public static final class DatabaseInfo {

    private String jdbcUrl;

    private String dbName;

    private String username;

    private String password;

    private long idleTimeout;

    private int minimumIdle;

    private int maximumPoolSize;

    private int hzMaximumPoolSize;

    private long connectionTimeout;

    private long maxLifetime;

    private long leakDetectionThreshold;

    private boolean showSql;
  }

  @Data
  public static final class ElasticsearchInfo {

    private String host;

    private int port;

    private String clusterName;
  }

  @Data
  public static final class HaynesProCallback {

    private String uri;

    private String companyIdentificaton;

    private String distributorPassword;

    private String callbackUrl;

    private String derendingerChEmail = StringUtils.EMPTY;

    private String technomagEmail = StringUtils.EMPTY;

    private String defaultEmail = StringUtils.EMPTY;

    private String atEmail = StringUtils.EMPTY;
  }

  @Data
  public static final class InvoiceConfiguration {

    private String archiveUrl;
  }
  @Data
  public static final class ErpRequestConfiguration {
    private int maxRequestSize = AppDefaults.ErpConfig.DEFAULT_REQUEST_SIZE;
  }

}
