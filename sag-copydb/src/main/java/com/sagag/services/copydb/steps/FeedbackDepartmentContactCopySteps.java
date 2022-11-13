package com.sagag.services.copydb.steps;

import com.sagag.services.copydb.batch.AbstractJobConfig;
import com.sagag.services.copydb.config.CopyDbProfile;
import com.sagag.services.copydb.domain.src.FeedbackDepartmentContact;
import com.sagag.services.copydb.domain.dest.DestFeedbackDepartmentContact;
import com.sagag.services.copydb.utils.Constants;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@CopyDbProfile
public class FeedbackDepartmentContactCopySteps extends AbstractJobConfig {

  @Autowired
  private FeedbackDepartmentContactProcessor feedbackDepartmentContactProcessor;

  @Autowired
  private DestFeedbackDepartmentContactWriter feedbackDepartmentContactWriter;

  @Bean(name = "copyFeedbackDepartmentContact")
  public Step copyFeedbackDepartmentContact() {
    return stepBuilderFactory.get("copyFeedbackDepartmentContact").<FeedbackDepartmentContact, DestFeedbackDepartmentContact>chunk(DF_CHUNK)
        .reader(feedbackDepartmentContactReader())
        .processor(feedbackDepartmentContactProcessor)
        .writer(feedbackDepartmentContactWriter)
        .transactionManager(targetTransactionManager)
        .build();
  }

  @Bean
  @StepScope
  public JpaPagingItemReader<FeedbackDepartmentContact> feedbackDepartmentContactReader() {
    final JpaPagingItemReader<FeedbackDepartmentContact> reader = new JpaPagingItemReader<>();
    reader.setTransacted(true);
    reader.setEntityManagerFactory(getEntityManagerFactory());
    reader.setPageSize(Constants.MAX_PAGE_SIZE);
    reader.setQueryString("select e from FeedbackDepartmentContact e");
    reader.setName("feedbackDepartmentContactReader");
    return reader;
  }

}
