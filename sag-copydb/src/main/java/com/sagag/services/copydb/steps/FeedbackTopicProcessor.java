package com.sagag.services.copydb.steps;

import com.sagag.services.copydb.config.CopyDbProfile;
import com.sagag.services.copydb.domain.src.FeedbackTopic;
import com.sagag.services.copydb.domain.dest.DestFeedbackTopic;

import org.dozer.DozerBeanMapper;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@CopyDbProfile
public class FeedbackTopicProcessor implements ItemProcessor<FeedbackTopic, DestFeedbackTopic> {

  @Autowired
  @Qualifier(value = "dozerBeanMapper")
  private DozerBeanMapper dozerBeanMapper;

  @Override
  public DestFeedbackTopic process(FeedbackTopic item) throws Exception {
    return dozerBeanMapper.map(item, DestFeedbackTopic.class);
  }
}
