package com.sagag.services.ivds.freetext.impl;

import com.sagag.services.elasticsearch.enums.FreetextSearchOption;
import com.sagag.services.ivds.api.IvdsVehicleService;
import com.sagag.services.ivds.freetext.IFreetextSearchService;
import com.sagag.services.ivds.request.FreetextSearchRequest;
import com.sagag.services.ivds.response.FreetextResponseDto;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VehicleFreetextSearchServiceImpl implements IFreetextSearchService {

  @Autowired
  private IvdsVehicleService vehicleService;

  @Override
  public void search(FreetextSearchRequest request, FreetextResponseDto response) {
    final String freetext = request.getText();
    request.setText(StringUtils.lowerCase(freetext));
    vehicleService.searchFreetext(getSearchOption(request.getSearchOptions()), request)
    .ifPresent(response::setVehData);
  }

  @Override
  public boolean support(List<String> options) {
    return options.contains(FreetextSearchOption.VEHICLES.lowerCase())
        || options.contains(FreetextSearchOption.VEHICLES_MOTOR.lowerCase());
  }

}
