package com.sagag.services.rest.controller.search;

import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.common.exception.ResultNotFoundException;
import com.sagag.services.common.utils.RestExceptionUtils;
import com.sagag.services.domain.eshop.unitree.dto.UnitreeCompactDto;
import com.sagag.services.domain.eshop.unitree.dto.UnitreeNodeDto;
import com.sagag.services.rest.swagger.docs.ApiDesc;
import com.sagag.services.service.api.UnitreeBusinessService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Category controller class.
 */
@RestController
@RequestMapping("/unitrees")
@Api(tags = "Unitree APIs")
public class UnitreeController {

  @Autowired
  private UnitreeBusinessService unitreeBusinessService;

  @ApiOperation(value = ApiDesc.Unitree.ALL_COMPACT_UNITREE_API_DESC,
      notes = ApiDesc.Unitree.ALL_COMPACT_UNITREE_API_NOTE)
  @GetMapping(value = "/compact-unitrees", produces = MediaType.APPLICATION_JSON_VALUE)
  public List<UnitreeCompactDto> getAllUnitree(final OAuth2Authentication authed)
      throws ResultNotFoundException {
    final UserInfo user = (UserInfo) authed.getPrincipal();
    return RestExceptionUtils.doSafelyReturnNotEmptyRecords(
        unitreeBusinessService.getAllUnitreeCompact(user.getSupportedAffiliate()), null);
  }

  @ApiOperation(value = ApiDesc.Unitree.GET_UNITREE_DETAIL_API_DESC,
      notes = ApiDesc.Unitree.GET_UNITREE_DETAIL_API_NOTE)
  @GetMapping(value = "/{unitreeId}", produces = MediaType.APPLICATION_JSON_VALUE)
  public UnitreeNodeDto getUnitreeDetail(@PathVariable("unitreeId") final String unitreeId,
      final OAuth2Authentication authed) throws ResultNotFoundException {
    final UserInfo user = (UserInfo) authed.getPrincipal();
    return RestExceptionUtils.doSafelyReturnOptionalRecord(
        unitreeBusinessService.getUnitreeNodesByUnitreeId(unitreeId, user),
        String.format("Not found the Root Node by UnitreeId %s", unitreeId));
  }

  @ApiOperation(value = ApiDesc.Unitree.GET_UNITREE_BY_LEAF_ID_DESC,
      notes = ApiDesc.Unitree.GET_UNITREE_BY_LEAF_ID_NOTE)
  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public UnitreeCompactDto getUnitreeByLeafId(
      @RequestParam(name = "leafId", required = true) final String leafId,
      final OAuth2Authentication authed) throws ResultNotFoundException {
    return RestExceptionUtils.doSafelyReturnOptionalRecord(
        unitreeBusinessService.getUnitreeByLeafId(leafId),
        String.format("Not found the tree by leafId %s", leafId));
  }

}
