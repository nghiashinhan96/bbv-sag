package com.sagag.services.copydb.steps;

import com.sagag.services.copydb.batch.AbstractJobConfig;
import com.sagag.services.copydb.config.CopyDbProfile;
import com.sagag.services.copydb.domain.src.FeedbackTopic;
import com.sagag.services.copydb.domain.dest.DestFeedbackTopic;
import com.sagag.services.copydb.utils.Constants;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@CopyDbProfile
public class FeedbackTopicCopySteps extends AbstractJobConfig {

  @Autowired
  private FeedbackTopicProcessor feedbackTopicProcessor;

  @Autowired
  private DestFeedbackTopicWriter feedbackTopicWriter;

  @Bean(name = "copyFeedbackTopic")
  public Step copyFeedbackTopic() {
    return stepBuilderFactory.get("copyFeedbackTopic").<FeedbackTopic, DestFeedbackTopic>chunk(DF_CHUNK)
        .reader(feedbackTopicReader())
        .processor(feedbackTopicProcessor)
        .writer(feedbackTopicWriter)
        .transactionManager(targetTransactionManager)
        .build();
  }

  @Bean
  @StepScope
  public JpaPagingItemReader<FeedbackTopic> feedbackTopicReader() {
    final JpaPagingItemReader<FeedbackTopic> reader = new JpaPagingItemReader<>();
    reader.setTransacted(true);
    reader.setEntityManagerFactory(getEntityManagerFactory());
    reader.setPageSize(Constants.MAX_PAGE_SIZE);
    reader.setQueryString("select e from FeedbackTopic e");
    reader.setName("feedbackTopicReader");
    return reader;
  }

}
