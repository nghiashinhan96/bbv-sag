package com.sagag.services.ivds.api;

import com.sagag.services.domain.eshop.unitree.dto.UnitreeFreetextSearchDto;
import com.sagag.services.ivds.request.UnitreeFreetextSearchRequest;

import org.springframework.data.domain.Page;

public interface IvdsUnitreeService {
  Page<UnitreeFreetextSearchDto> searchFreetext(final UnitreeFreetextSearchRequest request);
}
