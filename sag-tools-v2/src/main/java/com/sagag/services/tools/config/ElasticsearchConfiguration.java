package com.sagag.services.tools.config;

import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

import java.net.InetSocketAddress;

@Configuration
@EnableElasticsearchRepositories(basePackages = { "com.sagag.services.tools.repository.elasticsearch" })
public class ElasticsearchConfiguration {

  @Value("${spring.data.elasticsearch.host}")
  private String host;

  @Value("${spring.data.elasticsearch.port}")
  private Integer port;

  @Value("${spring.data.elasticsearch.cluster-name}")
  private String clusterName;

  @Bean
  public ElasticsearchOperations elasticsearchTemplate() {
    return new ElasticsearchTemplate(client());
  }

  @Bean
  public Client client() {
    final Settings settings = Settings.builder()
      .put("client.transport.sniff", true)
      .put("cluster.name", clusterName)
      .build();
    final TransportClient client = new PreBuiltTransportClient(settings);
    client.addTransportAddress(new InetSocketTransportAddress(new InetSocketAddress(host, port)));
    return client;
  }

}
