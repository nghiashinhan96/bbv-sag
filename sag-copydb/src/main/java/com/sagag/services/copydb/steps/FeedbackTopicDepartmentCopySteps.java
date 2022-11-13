package com.sagag.services.copydb.steps;

import com.sagag.services.copydb.batch.AbstractJobConfig;
import com.sagag.services.copydb.config.CopyDbProfile;
import com.sagag.services.copydb.domain.src.FeedbackTopicDepartment;
import com.sagag.services.copydb.domain.dest.DestFeedbackTopicDepartment;
import com.sagag.services.copydb.utils.Constants;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@CopyDbProfile
public class FeedbackTopicDepartmentCopySteps extends AbstractJobConfig {

  @Autowired
  private FeedbackTopicDepartmentProcessor feedbackTopicDepartmentProcessor;

  @Autowired
  private DestFeedbackTopicDepartmentWriter feedbackTopicDepartmentWriter;

  @Bean(name = "copyFeedbackTopicDepartment")
  public Step copyFeedbackTopicDepartment() {
    return stepBuilderFactory.get("copyFeedbackTopicDepartment").<FeedbackTopicDepartment, DestFeedbackTopicDepartment>chunk(DF_CHUNK)
        .reader(feedbackTopicDepartmentReader())
        .processor(feedbackTopicDepartmentProcessor)
        .writer(feedbackTopicDepartmentWriter)
        .transactionManager(targetTransactionManager)
        .build();
  }

  @Bean
  @StepScope
  public JpaPagingItemReader<FeedbackTopicDepartment> feedbackTopicDepartmentReader() {
    final JpaPagingItemReader<FeedbackTopicDepartment> reader = new JpaPagingItemReader<>();
    reader.setTransacted(true);
    reader.setEntityManagerFactory(getEntityManagerFactory());
    reader.setPageSize(Constants.MAX_PAGE_SIZE);
    reader.setQueryString("select e from FeedbackTopicDepartment e");
    reader.setName("feedbackTopicDepartmentReader");
    return reader;
  }

}
