package com.sagag.services.admin.app;

import com.sagag.services.admin.exporter.BackOfficeUserExportAttributeType;
import com.sagag.services.common.exporter.IExportAttributes;
import java.util.Collections;
import java.util.List;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.jmx.export.MBeanExporter;
import org.springframework.jmx.export.annotation.AnnotationMBeanExporter;
import org.springframework.jmx.support.RegistrationPolicy;

@Configuration
@PropertySource("classpath:version.properties")
public class AdminCoreConfiguration {

  /**
   * Message externalization/internationalization.
   */
  @Bean
  public MessageSource messageSource() {
    ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
    messageSource.setBasename("messages");
    return messageSource;
  }

  @Bean
  public MBeanExporter exporter() {
    final MBeanExporter exporter = new AnnotationMBeanExporter();
    exporter.setAutodetect(true);
    exporter.setRegistrationPolicy(RegistrationPolicy.IGNORE_EXISTING);
    exporter.setExcludedBeans("dataSource");
    return exporter;
  }

  @Bean
  @ConditionalOnMissingBean(IExportAttributes.class)
  public IExportAttributes<BackOfficeUserExportAttributeType> defaultExportAttributes() {
    return new IExportAttributes<BackOfficeUserExportAttributeType>() {

      @Override
      public List<BackOfficeUserExportAttributeType> getExportAttributes() {
        return Collections.emptyList();
      }
    };
  }

}
