package com.sagag.services.hazelcast.app;

import com.hazelcast.core.HazelcastInstance;
import net.javacrumbs.shedlock.provider.hazelcast.HazelcastLockProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;

@Configuration
public class HazelcastScheduledTaskConfiguration {

  @Bean
  @Description("Initializing Hazelcast Lock Provider for Spring Task Scheduler")
  public HazelcastLockProvider lockProvider(HazelcastInstance hazelcastInstance) {
    return new HazelcastLockProvider(hazelcastInstance,
      HazelcastMaps.SPRING_SCHEDULER_LOCK_MAP.name());
  }
}
