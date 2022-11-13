package com.sagag.services.copydb.steps;

import com.sagag.services.copydb.config.CopyDbProfile;
import com.sagag.services.copydb.domain.src.UserVehicleHistory;
import com.sagag.services.copydb.domain.dest.DestUserVehicleHistory;

import org.dozer.DozerBeanMapper;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@CopyDbProfile
public class UserVehicleHistoryProcessor implements ItemProcessor<UserVehicleHistory, DestUserVehicleHistory> {

  @Autowired
  @Qualifier(value = "dozerBeanMapper")
  private DozerBeanMapper dozerBeanMapper;

  @Override
  public DestUserVehicleHistory process(UserVehicleHistory item) throws Exception {
    return dozerBeanMapper.map(item, DestUserVehicleHistory.class);
  }
}
