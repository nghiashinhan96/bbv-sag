package com.sagag.services.copydb.steps;

import com.sagag.services.copydb.config.CopyDbProfile;
import com.sagag.services.copydb.domain.src.VehicleHistory;
import com.sagag.services.copydb.domain.dest.DestVehicleHistory;

import org.dozer.DozerBeanMapper;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@CopyDbProfile
public class VehicleHistoryProcessor implements ItemProcessor<VehicleHistory, DestVehicleHistory> {

  @Autowired
  @Qualifier(value = "dozerBeanMapper")
  private DozerBeanMapper dozerBeanMapper;

  @Override
  public DestVehicleHistory process(VehicleHistory item) throws Exception {
    return dozerBeanMapper.map(item, DestVehicleHistory.class);
  }
}
