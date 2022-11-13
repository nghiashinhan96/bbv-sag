package com.sagag.services.service.validator;

import com.sagag.services.common.validator.IDataValidator;
import com.sagag.services.domain.eshop.dto.MakeItem;
import com.sagag.services.gtmotive.api.GtmotiveService;
import com.sagag.services.gtmotive.domain.request.GtmotiveVinDecodeCriteria;
import com.sagag.services.hazelcast.api.MakeCacheService;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class GtmotiveVinDecoderValidator implements IDataValidator<GtmotiveVinDecodeCriteria> {

  @Autowired
  private GtmotiveService gtmotiveService;

  @Autowired
  private MakeCacheService makeCacheService;

  @Override
  public boolean validate(GtmotiveVinDecodeCriteria criteria) {
    // #828 validate VIN
    final String makeCode = gtmotiveService.getMakeCodeFromVinDecoder(criteria.getModifiedVin());
    if (StringUtils.isBlank(makeCode)) {
      return false;
    }
    Optional<MakeItem> makeItem = makeCacheService.findMakeItemByCode(makeCode);
    if (!makeItem.isPresent()) {
      return false;
    }
    makeItem.ifPresent(item -> criteria.setMakeCode(item.getMake()));
    criteria.setMakeCode(makeCode);
    return makeItem.get().isGtVin();
  }

}
