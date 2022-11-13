package com.sagag.services.copydb.steps;

import com.sagag.services.copydb.config.CopyDbProfile;
import com.sagag.services.copydb.domain.src.FeedbackStatus;
import com.sagag.services.copydb.domain.dest.DestFeedbackStatus;

import org.dozer.DozerBeanMapper;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@CopyDbProfile
public class FeedbackStatusProcessor implements ItemProcessor<FeedbackStatus, DestFeedbackStatus> {

  @Autowired
  @Qualifier(value = "dozerBeanMapper")
  private DozerBeanMapper dozerBeanMapper;

  @Override
  public DestFeedbackStatus process(FeedbackStatus item) throws Exception {
    return dozerBeanMapper.map(item, DestFeedbackStatus.class);
  }
}
