package com.sagag.services.hazelcast.app;

import com.hazelcast.config.Config;
import com.hazelcast.config.EvictionConfig;
import com.hazelcast.config.EvictionPolicy;
import com.hazelcast.config.ExecutorConfig;
import com.hazelcast.config.InMemoryFormat;
import com.hazelcast.config.JoinConfig;
import com.hazelcast.config.ManagementCenterConfig;
import com.hazelcast.config.MapConfig;
import com.hazelcast.config.MapStoreConfig;
import com.hazelcast.config.MapStoreConfig.InitialLoadMode;
import com.hazelcast.config.MaxSizeConfig;
import com.hazelcast.config.NearCacheConfig;
import com.hazelcast.config.PartitioningStrategyConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.partition.strategy.StringAndPartitionAwarePartitioningStrategy;
import com.hazelcast.spring.cache.HazelcastCacheManager;
import com.sagag.eshop.repo.cache.RepoCacheMaps;
import com.sagag.services.common.cache.CacheConstants;
import com.sagag.services.common.config.AppProperties;
import com.sagag.services.common.config.AppProperties.Cache;
import com.sagag.services.common.config.AppProperties.Cache.ManagementCenter;
import com.sagag.services.common.country.CountryConfiguration;
import com.sagag.services.common.locale.LocaleContextHelper;
import com.sagag.services.hazelcast.api.CacheService;
import com.sagag.services.hazelcast.mapstore.ConnectShoppingCartMapStore;
import java.util.Locale;
import java.util.function.BiFunction;
import java.util.stream.Stream;
import javax.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

@Configuration
@EnableCaching
@Slf4j
public class HazelcastCoreConfig {

  private static final boolean DF_NEAR_CACHE_MODE_REPO_CACHE = false;

  private static final int FREE_HEAP_PERCENTAGE_VALUE = 10;

  @Autowired
  @Lazy
  private ConnectShoppingCartMapStore shoppingCartMapStore;

  @Autowired
  private CountryConfiguration countryConfig;

  @Autowired
  public LocaleContextHelper localeContextHelper;

  @PreDestroy
  public void destroy() {
    log.info("Closing Cache Manager");
    Hazelcast.shutdownAll();
  }

  @Bean
  public CacheManager cacheManager(final HazelcastInstance hazelcastInstance) {
    log.info("Starting HazelcastCacheManager");
    return new HazelcastCacheManager(hazelcastInstance);
  }

  @Bean
  public HazelcastInstance hazelcastInstance(final AppProperties appProperties) {
    log.debug("Configuring Hazelcast");
    final Cache cache = appProperties.getCache();
    final String instance = appProperties.getName();
    final HazelcastInstance hazelCastInstance = Hazelcast.getHazelcastInstanceByName(instance);
    if (hazelCastInstance != null) {
      log.debug("Hazelcast already initialized");
      return hazelCastInstance;
    }
    final Config config = new Config();
    config.setInstanceName(instance);
    config.getNetworkConfig().setPort(5701); // default port of hazelcast
    config.getNetworkConfig().setPortAutoIncrement(true);
    config.addExecutorConfig(initExecutorConfig(appProperties));

    // Set log4j2
    config.setProperty("hazelcast.logging.type", "log4j2");

    // Enabled back pressure
    config.setProperty("hazelcast.backpressure.enabled", "true");
    config.setProperty("hazelcast.backpressure.max.concurrent.invocations.per.partition", "300");
    config.setProperty("hazelcast.backpressure.backoff.timeout.millis", "1000");

    // I/O threading
    config.setProperty("hazelcast.io.thread.count", "10");
    config.setProperty("hazelcast.io.balancer.interval.seconds", "5");

    // Operation threading - partition aware operations
    config.setProperty("hazelcast.operation.thread.count", "10");
    config.setProperty("hazelcast.event.thread.count", "10");

    final JoinConfig joinConfig = config.getNetworkConfig().getJoin();
    joinConfig.getAwsConfig().setEnabled(false);
    joinConfig.getMulticastConfig().setEnabled(false); // only enable TCP clustering
    joinConfig.getTcpIpConfig().setEnabled(cache.getTcpConfig().isEnabled());
    joinConfig.getTcpIpConfig().setMembers(cache.getTcpConfig().getMembers());
    config.getMapConfigs().put("default", initDefaultMapConfig(appProperties));
    config.setManagementCenterConfig(initDefaultManagementCenterConfig(appProperties));
    initAllHazelcastMaps(config, appProperties);
    initAllRepoCacheMaps(config);

    return Hazelcast.newHazelcastInstance(config);
  }

  private void initAllHazelcastMaps(final Config config, final AppProperties appProperties) {
    final String[] languages = countryConfig.languages();

    final BiFunction<HazelcastMaps, Locale, MapConfig> mapConfigBuilder =
        (map, locale) -> initMapConfig(map, locale, appProperties);

    for (final HazelcastMaps map : HazelcastMaps.values()) {
      if (!map.isMultilingual()) {
        config.addMapConfig(mapConfigBuilder.apply(map, Locale.GERMAN));
        continue;
      }
      log.debug("Creating map config for multilingual map size = {}", languages.length);
      Stream.of(languages).map(localeContextHelper::toLocale)
      .map(lang -> mapConfigBuilder.apply(map, lang))
      .forEach(config::addMapConfig);
    }
  }

  private MapConfig initMapConfig(final HazelcastMaps map, final Locale locale,
      final AppProperties appProperties) {
    final String mapName = CacheService.buildCacheName(map.name(), locale);
    final MapConfig mapConfig = createMapConfig(mapName, map.getTtl(), map.isOnNearCacheMode());
    if (map.isShoppingCartMap()) {
      mapConfig.setMapStoreConfig(initShoppingBasketMapStore(appProperties));
      mapConfig.setPartitioningStrategyConfig(
          new PartitioningStrategyConfig(StringAndPartitionAwarePartitioningStrategy.INSTANCE));
    }
    return mapConfig;
  }

  private static void initAllRepoCacheMaps(final Config config) {
    for (final String mapName : RepoCacheMaps.getAllMaps()) {
      config.addMapConfig(createMapConfig(mapName, CacheConstants.TTL_4_HOURS,
          DF_NEAR_CACHE_MODE_REPO_CACHE));
    }
  }

  private MapStoreConfig initShoppingBasketMapStore(final AppProperties appProperties) {
    final MapStoreConfig mapStoreConfig = new MapStoreConfig();
    mapStoreConfig.setEnabled(true);
    mapStoreConfig.setWriteDelaySeconds(appProperties.getCache().getCart().getWriteDelaySeconds());
    mapStoreConfig.setWriteBatchSize(appProperties.getCache().getCart().getWriteBatchSize());
    mapStoreConfig.setInitialLoadMode(InitialLoadMode.LAZY);
    mapStoreConfig.setImplementation(shoppingCartMapStore);
    return mapStoreConfig;
  }

  private static ManagementCenterConfig initDefaultManagementCenterConfig(
      final AppProperties appProperties) {
    final ManagementCenterConfig managementCenterConfig = new ManagementCenterConfig();
    final ManagementCenter managementCenter = appProperties.getCache().getManagementCenter();
    managementCenterConfig.setEnabled(managementCenter.isEnabled());
    managementCenterConfig.setUrl(managementCenter.getUrl());
    managementCenterConfig.setUpdateInterval(managementCenter.getUpdateInterval());
    return managementCenterConfig;
  }

  private static MapConfig initDefaultMapConfig(final AppProperties appProperties) {
    final MapConfig mapConfig = new MapConfig();
    mapConfig.setBackupCount(appProperties.getCache().getBackupCount());
    mapConfig.setEvictionPolicy(EvictionPolicy.LRU);
    mapConfig.setMaxSizeConfig(new MaxSizeConfig(FREE_HEAP_PERCENTAGE_VALUE,
        MaxSizeConfig.MaxSizePolicy.FREE_HEAP_PERCENTAGE));
    return mapConfig;
  }

  private static ExecutorConfig initExecutorConfig(final AppProperties appProperties) {
    final ExecutorConfig executorConfig = new ExecutorConfig();
    executorConfig.setName("hz:query");
    executorConfig.setPoolSize(appProperties.getCache().getQuery().getPoolSize());
    executorConfig.setQueueCapacity(appProperties.getCache().getQuery().getQueueCapacity());
    return executorConfig;
  }

  private static MapConfig createMapConfig(final String cacheName, final int ttl,
      final boolean nearCacheMode) {
    final MapConfig mapConfig = new MapConfig();
    mapConfig.setName(cacheName);
    mapConfig.setTimeToLiveSeconds(ttl);
    mapConfig.setReadBackupData(true);
    mapConfig.setEvictionPolicy(EvictionPolicy.LRU);

    if (nearCacheMode) {
      mapConfig.setNearCacheConfig(initNearCacheConfig(cacheName));
    }
    return mapConfig;
  }

  private static NearCacheConfig initNearCacheConfig(String cacheName) {
    log.info("Initialize near cache configuration for cache map name = {}", cacheName);
    final NearCacheConfig config = new NearCacheConfig(cacheName + "-near-cache")
        .setInMemoryFormat(InMemoryFormat.BINARY)
        .setSerializeKeys(true)
        .setInvalidateOnChange(false)
        .setMaxIdleSeconds(5)
        .setCacheLocalEntries(true)
        .setLocalUpdatePolicy(NearCacheConfig.LocalUpdatePolicy.INVALIDATE);

    config.getEvictionConfig().setMaximumSizePolicy(EvictionConfig.MaxSizePolicy.ENTRY_COUNT);
    config.getEvictionConfig().setEvictionPolicy(EvictionPolicy.LRU);
    config.getEvictionConfig().setSize(1);

    log.info("The near cache configuration info = {}", config.toString());
    return config;
  }
}
