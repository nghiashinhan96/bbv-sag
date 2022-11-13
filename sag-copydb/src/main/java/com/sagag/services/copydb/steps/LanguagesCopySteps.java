package com.sagag.services.copydb.steps;

import com.sagag.services.copydb.batch.AbstractJobConfig;
import com.sagag.services.copydb.config.CopyDbProfile;
import com.sagag.services.copydb.domain.src.Languages;
import com.sagag.services.copydb.domain.dest.DestLanguages;
import com.sagag.services.copydb.utils.Constants;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@CopyDbProfile
public class LanguagesCopySteps extends AbstractJobConfig {

  @Autowired
  private LanguagesProcessor languagesProcessor;

  @Autowired
  private DestLanguagesWriter languagesWriter;

  @Bean(name = "copyLanguages")
  public Step copyLanguages() {
    return stepBuilderFactory.get("copyLanguages").<Languages, DestLanguages>chunk(DF_CHUNK)
        .reader(languagesReader())
        .processor(languagesProcessor)
        .writer(languagesWriter)
        .transactionManager(targetTransactionManager)
        .build();
  }

  @Bean
  @StepScope
  public JpaPagingItemReader<Languages> languagesReader() {
    final JpaPagingItemReader<Languages> reader = new JpaPagingItemReader<>();
    reader.setTransacted(true);
    reader.setEntityManagerFactory(getEntityManagerFactory());
    reader.setPageSize(Constants.MAX_PAGE_SIZE);
    reader.setQueryString("select e from Languages e");
    reader.setName("languagesReader");
    return reader;
  }

}
