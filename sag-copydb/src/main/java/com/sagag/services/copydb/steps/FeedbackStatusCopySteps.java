package com.sagag.services.copydb.steps;

import com.sagag.services.copydb.batch.AbstractJobConfig;
import com.sagag.services.copydb.config.CopyDbProfile;
import com.sagag.services.copydb.domain.src.FeedbackStatus;
import com.sagag.services.copydb.domain.dest.DestFeedbackStatus;
import com.sagag.services.copydb.utils.Constants;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@CopyDbProfile
public class FeedbackStatusCopySteps extends AbstractJobConfig {

  @Autowired
  private FeedbackStatusProcessor feedbackStatusProcessor;

  @Autowired
  private DestFeedbackStatusWriter feedbackStatusWriter;

  @Bean(name = "copyFeedbackStatus")
  public Step copyFeedbackStatus() {
    return stepBuilderFactory.get("copyFeedbackStatus").<FeedbackStatus, DestFeedbackStatus>chunk(DF_CHUNK)
        .reader(feedbackStatusReader())
        .processor(feedbackStatusProcessor)
        .writer(feedbackStatusWriter)
        .transactionManager(targetTransactionManager)
        .build();
  }

  @Bean
  @StepScope
  public JpaPagingItemReader<FeedbackStatus> feedbackStatusReader() {
    final JpaPagingItemReader<FeedbackStatus> reader = new JpaPagingItemReader<>();
    reader.setTransacted(true);
    reader.setEntityManagerFactory(getEntityManagerFactory());
    reader.setPageSize(Constants.MAX_PAGE_SIZE);
    reader.setQueryString("select e from FeedbackStatus e");
    reader.setName("feedbackStatusReader");
    return reader;
  }

}
