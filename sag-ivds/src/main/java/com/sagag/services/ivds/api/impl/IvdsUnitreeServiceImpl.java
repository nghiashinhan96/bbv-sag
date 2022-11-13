package com.sagag.services.ivds.api.impl;

import com.sagag.eshop.service.utils.PermissionUtils;
import com.sagag.services.domain.eshop.unitree.dto.UnitreeFreetextSearchDto;
import com.sagag.services.elasticsearch.api.UnitreeSearchService;
import com.sagag.services.elasticsearch.criteria.unitree.KeywordUnitreeSearchCriteria;
import com.sagag.services.elasticsearch.domain.unitree.UnitreeDoc;
import com.sagag.services.ivds.api.IvdsUnitreeService;
import com.sagag.services.ivds.converter.unitree.UnitreeConverter;
import com.sagag.services.ivds.request.UnitreeFreetextSearchRequest;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class IvdsUnitreeServiceImpl implements IvdsUnitreeService {

  @Autowired
  private UnitreeSearchService unitreeSearchService;

  @Override
  public Page<UnitreeFreetextSearchDto> searchFreetext(UnitreeFreetextSearchRequest request) {
    log.debug("Searching unitree by free text");
    final String text = request.getText();
    final Pageable pageable = request.getPageRequest();
    final Map<String, Boolean> shopAccessPermission =
        PermissionUtils.getPermissionShopUniparts(request.getUser().getPermissions());

    final KeywordUnitreeSearchCriteria criteria =
        KeywordUnitreeSearchCriteria.builder().text(text).build();
    Page<UnitreeDoc> unitreeDocs = unitreeSearchService.search(criteria, pageable);
    List<UnitreeFreetextSearchDto> unitreeContent = new ArrayList<>();
    unitreeDocs.getContent().stream().forEach(
        doc -> unitreeContent.addAll(UnitreeConverter.convert(doc, text, shopAccessPermission)));
    return new PageImpl<>(unitreeContent);
  }
}
