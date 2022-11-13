package com.sagag.services.dvse.config;

import com.sagag.services.common.enums.ErpSendMethodEnum;
import com.sagag.services.common.enums.SupportedAffiliate;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.domain.eshop.dto.NextWorkingDates;
import com.sagag.services.dvse.builder.IAvailableStateBuilder;
import com.sagag.services.dvse.wsdl.dvse.AvailableState;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jmx.export.MBeanExporter;
import org.springframework.jmx.export.annotation.AnnotationMBeanExporter;
import org.springframework.jmx.support.RegistrationPolicy;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

@Configuration
public class DvseConfiguration {

  @Bean
  public MBeanExporter exporter() {
    final MBeanExporter exporter = new AnnotationMBeanExporter();
    exporter.setAutodetect(true);
    exporter.setRegistrationPolicy(RegistrationPolicy.IGNORE_EXISTING);
    exporter.setExcludedBeans("dataSource");
    return exporter;
  }

  @Bean(name = "stompClient")
  public WebSocketStompClient stompClient() {
    Transport webSocketTransport = new WebSocketTransport(new StandardWebSocketClient());
    List<Transport> transports = Collections.singletonList(webSocketTransport);
    final WebSocketStompClient stompClient = new WebSocketStompClient(new SockJsClient(transports));
    stompClient.setMessageConverter(new MappingJackson2MessageConverter());
    return stompClient;
  }

  @Bean
  @ConditionalOnMissingBean(IAvailableStateBuilder.class)
  public IAvailableStateBuilder defaultAvailableStateBuilder() {
    return new IAvailableStateBuilder() {

      @Override
      public AvailableState buildAvailableState(Optional<ArticleDocDto> articleOpt,
          SupportedAffiliate affiliate, ErpSendMethodEnum sendMethodEnum,
          NextWorkingDates nextWorkingDate) {
        throw new UnsupportedOperationException("No support this method");
      }

    };
  }

}
