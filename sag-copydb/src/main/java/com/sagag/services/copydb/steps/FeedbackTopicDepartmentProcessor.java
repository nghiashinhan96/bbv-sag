package com.sagag.services.copydb.steps;

import com.sagag.services.copydb.config.CopyDbProfile;
import com.sagag.services.copydb.domain.src.FeedbackTopicDepartment;
import com.sagag.services.copydb.domain.dest.DestFeedbackTopicDepartment;

import org.dozer.DozerBeanMapper;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@CopyDbProfile
public class FeedbackTopicDepartmentProcessor implements ItemProcessor<FeedbackTopicDepartment, DestFeedbackTopicDepartment> {

  @Autowired
  @Qualifier(value = "dozerBeanMapper")
  private DozerBeanMapper dozerBeanMapper;

  @Override
  public DestFeedbackTopicDepartment process(FeedbackTopicDepartment item) throws Exception {
    return dozerBeanMapper.map(item, DestFeedbackTopicDepartment.class);
  }
}
