package com.sagag.services.ivds.freetext.impl;

import com.sagag.services.domain.unitree.FreetextUnitreeResponse;
import com.sagag.services.ivds.api.IvdsUnitreeService;
import com.sagag.services.ivds.freetext.IFreetextSearchService;
import com.sagag.services.ivds.freetext.SearchOptions;
import com.sagag.services.ivds.request.FreetextSearchRequest;
import com.sagag.services.ivds.request.UnitreeFreetextSearchRequest;
import com.sagag.services.ivds.response.FreetextResponseDto;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UnitreeFreetextSearchServiceImpl implements IFreetextSearchService {

  @Autowired
  private IvdsUnitreeService unitreeService;

  @Override
  public void search(FreetextSearchRequest request, FreetextResponseDto response) {
    UnitreeFreetextSearchRequest clonedRequest = UnitreeFreetextSearchRequest.builder()
        .text(StringUtils.lowerCase(request.getText()))
        .user(request.getUser())
        .isFullRequest(request.isFullRequest())
        .searchOptions(request.getSearchOptions())
        .pageRequest(request.getPageRequest())
        .build();
    
    FreetextUnitreeResponse unitreeResponse = new FreetextUnitreeResponse();
    unitreeResponse.setUnitrees(unitreeService.searchFreetext(clonedRequest));
    response.setUnitreeData(unitreeResponse);
  }

  @Override
  public boolean support(List<String> options) {
    return options.contains(SearchOptions.PRODUCT_CATEGORY.lowerCase());
  }

}
