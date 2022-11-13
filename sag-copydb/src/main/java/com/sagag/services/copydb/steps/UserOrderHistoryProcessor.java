package com.sagag.services.copydb.steps;

import com.sagag.services.copydb.config.CopyDbProfile;
import com.sagag.services.copydb.domain.src.UserOrderHistory;
import com.sagag.services.copydb.domain.dest.DestUserOrderHistory;

import org.dozer.DozerBeanMapper;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@CopyDbProfile
public class UserOrderHistoryProcessor implements ItemProcessor<UserOrderHistory, DestUserOrderHistory> {

  @Autowired
  @Qualifier(value = "dozerBeanMapper")
  private DozerBeanMapper dozerBeanMapper;

  @Override
  public DestUserOrderHistory process(UserOrderHistory item) throws Exception {
    return dozerBeanMapper.map(item, DestUserOrderHistory.class);
  }
}
