package com.sagag.eshop.service.api.impl.user_history;

import com.sagag.eshop.service.api.impl.userhistory.UserVehicleHistoryServiceImpl;
import com.sagag.eshop.service.app.ServiceApplication;
import com.sagag.services.common.annotation.EshopIntegrationTest;
import com.sagag.services.common.enums.UserHistoryFromSource;
import com.sagag.services.domain.eshop.dto.VehicleDto;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.transaction.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { ServiceApplication.class })
@EshopIntegrationTest
@Transactional
public class UserVehicleHistoryServiceImplIT {

  @Autowired
  private UserVehicleHistoryServiceImpl service;

  @Test
  public void testAddVehHistory() {
    final VehicleDto vehicle = new VehicleDto();
    vehicle.setVehId("V31350M26497");
    vehicle.setVehicleBrand(StringUtils.EMPTY);
    vehicle.setVehicleModel(StringUtils.EMPTY);
    final String searchTerm = "";
    final String searchMode = "VEHICLE_DESC";
    final boolean fromOffer = false;
    final UserHistoryFromSource fromSource = UserHistoryFromSource.C4C;
    service.addVehicleHistory(26l, vehicle, searchTerm, searchMode, fromSource, fromOffer);
  }

}
