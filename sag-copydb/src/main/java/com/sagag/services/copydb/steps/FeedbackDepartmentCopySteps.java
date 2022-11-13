package com.sagag.services.copydb.steps;

import com.sagag.services.copydb.batch.AbstractJobConfig;
import com.sagag.services.copydb.config.CopyDbProfile;
import com.sagag.services.copydb.domain.src.FeedbackDepartment;
import com.sagag.services.copydb.domain.dest.DestFeedbackDepartment;
import com.sagag.services.copydb.utils.Constants;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@CopyDbProfile
public class FeedbackDepartmentCopySteps extends AbstractJobConfig {

  @Autowired
  private FeedbackDepartmentProcessor feedbackDepartmentProcessor;

  @Autowired
  private DestFeedbackDepartmentWriter feedbackDepartmentWriter;

  @Bean(name = "copyFeedbackDepartment")
  public Step copyFeedbackDepartment() {
    return stepBuilderFactory.get("copyFeedbackDepartment").<FeedbackDepartment, DestFeedbackDepartment>chunk(DF_CHUNK)
        .reader(feedbackDepartmentReader())
        .processor(feedbackDepartmentProcessor)
        .writer(feedbackDepartmentWriter)
        .transactionManager(targetTransactionManager)
        .build();
  }

  @Bean
  @StepScope
  public JpaPagingItemReader<FeedbackDepartment> feedbackDepartmentReader() {
    final JpaPagingItemReader<FeedbackDepartment> reader = new JpaPagingItemReader<>();
    reader.setTransacted(true);
    reader.setEntityManagerFactory(getEntityManagerFactory());
    reader.setPageSize(Constants.MAX_PAGE_SIZE);
    reader.setQueryString("select e from FeedbackDepartment e");
    reader.setName("feedbackDepartmentReader");
    return reader;
  }

}
