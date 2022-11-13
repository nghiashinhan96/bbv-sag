package com.sagag.services.hazelcast.user_context.impl;

import com.sagag.eshop.service.api.UserVehicleHistoryService;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.common.enums.UserHistoryFromSource;
import com.sagag.services.domain.eshop.dto.ContextDto;
import com.sagag.services.domain.eshop.dto.VehicleDto;
import com.sagag.services.hazelcast.domain.user.EshopContext;
import com.sagag.services.hazelcast.domain.user.EshopContextType;
import com.sagag.services.hazelcast.user_context.IContextMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class VehicleContextMapperImpl implements IContextMapper {

  @Autowired
  private UserVehicleHistoryService userVehHistoryService;

  @Override
  public void map(UserInfo user, EshopContext context, ContextDto contextDto) {
    context.updateSelectedVehicleDocs(contextDto.getVehicleDoc());
    final long userId = user.getOriginalUserId();
    final VehicleDto vehicle = contextDto.getVehicleDoc();
    final String vehSearchTerm = contextDto.getVehSearchTerm();
    final String vehSearchMode = contextDto.getVehSearchMode();
    final boolean fromOffer = contextDto.isFromOffer();
    final UserHistoryFromSource fromSource =
        UserHistoryFromSource.findFromSource(user.isSaleOnBehalf());
    userVehHistoryService.addVehicleHistory(userId, vehicle, vehSearchTerm, vehSearchMode,
        fromSource, fromOffer);
  }

  @Override
  public EshopContextType type() {
    return EshopContextType.VEHICLE_CONTEXT;
  }

}
