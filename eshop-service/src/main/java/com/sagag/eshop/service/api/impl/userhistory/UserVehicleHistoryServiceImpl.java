package com.sagag.eshop.service.api.impl.userhistory;

import com.sagag.eshop.repo.api.UserVehicleHistoryRepository;
import com.sagag.eshop.repo.api.VehicleHistoryRepository;
import com.sagag.eshop.repo.api.userhistory.VUserVehicleHistoryRepository;
import com.sagag.eshop.repo.criteria.user_history.UserVehicleHistorySearchCriteria;
import com.sagag.eshop.repo.entity.UserVehicleHistory;
import com.sagag.eshop.repo.entity.VehicleHistory;
import com.sagag.eshop.repo.specification.userhistory.VUserVehicleHistorySpecification;
import com.sagag.eshop.service.api.UserVehicleHistoryService;
import com.sagag.services.common.enums.UserHistoryFromSource;
import com.sagag.services.common.utils.PageUtils;
import com.sagag.services.common.utils.VehicleUtils;
import com.sagag.services.domain.eshop.dto.VehicleDto;
import com.sagag.services.domain.eshop.dto.VehicleHistoryDto;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserVehicleHistoryServiceImpl implements UserVehicleHistoryService {

  @Autowired
  private UserVehicleHistoryRepository userVehHistoryRepo;

  @Autowired
  private VehicleHistoryRepository vehicleHistoryRepo;

  @Autowired
  private VUserVehicleHistoryRepository vUserVehHistoryRepo;

  @Override
  public Page<VehicleHistoryDto> searchVehicleHistories(UserVehicleHistorySearchCriteria criteria,
      Pageable pageable) {
    final VUserVehicleHistorySpecification spec = new VUserVehicleHistorySpecification(criteria);
    return vUserVehHistoryRepo.findAll(spec, pageable)
        .map(entity -> new VehicleHistoryDto(entity.getVehicleId(), entity.getVehicleName(),
            entity.getVehicleClass(), entity.getSelectDate(), entity.getSearchTerm(),
            entity.getSearchMode(), entity.getFullName(), entity.getFromSource()));
  }

  @Override
  public Page<VehicleHistoryDto> getLastestVehicleHistory(long userId) {
    return vUserVehHistoryRepo.findTopVehicleHistories(userId, PageUtils.DEF_PAGE);
  }

  @Override
  public Page<VehicleHistoryDto> filterLastestVehicleHistoryByVehicleClass(long userId,
      String vehicleClass) {
    Page<VehicleHistoryDto> topVehicleHistories =
        vUserVehHistoryRepo.findTopVehicleHistories(userId, PageUtils.DEF_PAGE);
    if (StringUtils.isEmpty(vehicleClass) || !topVehicleHistories.hasContent()) {
      return topVehicleHistories;
    }
    return new PageImpl<VehicleHistoryDto>(topVehicleHistories.getContent().stream()
        .filter(vh -> StringUtils.equalsIgnoreCase(vehicleClass, vh.getVehicleClass()))
        .collect(Collectors.toList()));
  }

  @Override
  @Transactional
  public void addVehicleHistory(long userId, VehicleDto vehicle, String searchTerm,
      String searchMode, UserHistoryFromSource fromSource, boolean fromOffer) {
    if (fromOffer) {
      return;
    }
    if (vehicle == null) {
      return;
    }
    // Check exists vehicle id
    final Optional<VehicleHistory> vehHistOpt = vehicleHistoryRepo.findByVehId(vehicle.getVehId());
    if (vehHistOpt.isPresent()) {
      createUserVehHistory(userId, fromSource, vehHistOpt.get(), searchTerm, searchMode, false);
      return;
    }

    final VehicleHistory createdVehHistory = createVehicleHistory(vehicle);
    createUserVehHistory(userId, fromSource, createdVehHistory, searchTerm, searchMode, true);
  }

  private VehicleHistory createVehicleHistory(VehicleDto vehicle) {
    // Insert new vehicle history
    final String vehicleType =
        VehicleUtils.buildVehicleTypeDesc(vehicle.getVehicleName(), vehicle.getVehiclePowerKw(),
            vehicle.getVehicleEngineCode());
    final String vehicleName =
        VehicleUtils.buildVehDisplay(vehicle.getVehicleBrand(), vehicle.getVehicleModel(), vehicleType);
    final String vehicleInfo = vehicleName;
    final VehicleHistory vehHistory = VehicleHistory.builder()
        .vehId(vehicle.getVehId())
        .vehMake(vehicle.getVehicleBrand())
        .vehModel(vehicle.getVehicleModel())
        .vehType(vehicleType)
        .vehName(vehicleName)
        .vehClass(vehicle.getVehicleClass())
        .vehInfo(vehicleInfo).build();
    return vehicleHistoryRepo.save(vehHistory);
  }

  private void createUserVehHistory(long userId, UserHistoryFromSource fromSource,
      VehicleHistory vehHistory, String searchTerm, String searchMode, boolean isNew) {
    final String updatedSearchTerm = StringUtils.defaultIfBlank(searchTerm, null);
    final String updatedSearchMode = defaultVehicleSearchMode(updatedSearchTerm,
        StringUtils.defaultIfBlank(searchMode, null));

    final Optional<UserVehicleHistory> userVehicleHistOpt = userVehHistoryRepo
        .findExistingUserVehicleHistory(userId, vehHistory.getId(), updatedSearchTerm, fromSource)
        .stream()
        .filter(uvh -> StringUtils.equalsIgnoreCase(updatedSearchTerm, uvh.getSearchTerm())
            && StringUtils.equalsIgnoreCase(updatedSearchMode, uvh.getSearchMode()))
        .findFirst();

    final UserVehicleHistory userVehHistory;
    if (!isNew && userVehicleHistOpt.isPresent()) {
      // Update vehicle id to user vehicle history
      userVehHistory = userVehicleHistOpt.get();
    } else {
      userVehHistory =
          UserVehicleHistory.builder().userId(userId).vehicleHistory(vehHistory)
          .searchTerm(updatedSearchTerm)
          .searchMode(updatedSearchMode)
          .fromSource(fromSource).build();
    }
    userVehHistory.setSelectDate(Calendar.getInstance().getTime());
    userVehHistoryRepo.save(userVehHistory);
  }

  private static String defaultVehicleSearchMode(String searchTerm, String searchMode) {
    if (searchTerm != null || !"VEHICLE_DESC".equalsIgnoreCase(searchMode)) {
      return searchMode;
    }
    return null;
  }

}
