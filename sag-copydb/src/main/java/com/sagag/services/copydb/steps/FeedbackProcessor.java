package com.sagag.services.copydb.steps;

import com.sagag.services.copydb.config.CopyDbProfile;
import com.sagag.services.copydb.domain.src.Feedback;
import com.sagag.services.copydb.domain.dest.DestFeedback;

import org.dozer.DozerBeanMapper;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@CopyDbProfile
public class FeedbackProcessor implements ItemProcessor<Feedback, DestFeedback> {

  @Autowired
  @Qualifier(value = "dozerBeanMapper")
  private DozerBeanMapper dozerBeanMapper;

  @Override
  public DestFeedback process(Feedback item) throws Exception {
    return dozerBeanMapper.map(item, DestFeedback.class);
  }
}
