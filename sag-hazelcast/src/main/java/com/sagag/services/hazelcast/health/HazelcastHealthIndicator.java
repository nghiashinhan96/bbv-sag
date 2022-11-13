package com.sagag.services.hazelcast.health;

import com.hazelcast.core.Cluster;
import com.hazelcast.core.HazelcastInstance;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health.Builder;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
@Slf4j
public class HazelcastHealthIndicator extends AbstractHealthIndicator {

  @Autowired
  private HazelcastInstance hazelcastInstance;
  @Autowired
  private CacheManager cacheManager;

  @Override
  protected void doHealthCheck(Builder builder) throws Exception {
    verifyCacheNames(builder);
    verifyCluster(builder);
  }

  private void verifyCluster(final Builder builder) {
    final Cluster cluster = hazelcastInstance.getCluster();
    builder.withDetail("clusterState", cluster.getClusterState());
    cluster.getMembers().stream()
    .forEach(member -> builder.withDetail(member.getAddress().toString(), member.getAttributes()));
  }

  private void verifyCacheNames(final Builder builder) {
    final Collection<String> cacheNames = cacheManager.getCacheNames();
    for (String cacheName : cacheNames) {
      try {
        builder.up().withDetail(cacheName, hazelcastInstance.getMap(cacheName).size());
      } catch (Exception ex) {
        log.error("Verify the cache of {} has error,", cacheName, ex);
        builder.down(ex);
      }
    }
  }

}
