package com.sagag.services.copydb.steps;

import com.sagag.services.copydb.batch.AbstractJobConfig;
import com.sagag.services.copydb.config.CopyDbProfile;
import com.sagag.services.copydb.domain.src.Feedback;
import com.sagag.services.copydb.domain.dest.DestFeedback;
import com.sagag.services.copydb.utils.Constants;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@CopyDbProfile
public class FeedbackCopySteps extends AbstractJobConfig {

  @Autowired
  private FeedbackProcessor feedbackProcessor;

  @Autowired
  private DestFeedbackWriter feedbackWriter;

  @Bean(name = "copyFeedback")
  public Step copyFeedback() {
    return stepBuilderFactory.get("copyFeedback").<Feedback, DestFeedback>chunk(DF_CHUNK)
        .reader(feedbackReader())
        .processor(feedbackProcessor)
        .writer(feedbackWriter)
        .transactionManager(targetTransactionManager)
        .build();
  }

  @Bean
  @StepScope
  public JpaPagingItemReader<Feedback> feedbackReader() {
    final JpaPagingItemReader<Feedback> reader = new JpaPagingItemReader<>();
    reader.setTransacted(true);
    reader.setEntityManagerFactory(getEntityManagerFactory());
    reader.setPageSize(Constants.MAX_PAGE_SIZE);
    reader.setQueryString("select e from Feedback e");
    reader.setName("feedbackReader");
    return reader;
  }

}
