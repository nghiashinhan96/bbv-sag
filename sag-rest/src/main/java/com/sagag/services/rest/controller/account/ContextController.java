package com.sagag.services.rest.controller.account;

import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.common.exception.ResultNotFoundException;
import com.sagag.services.hazelcast.api.ActiveDmsUserCacheService;
import com.sagag.services.hazelcast.api.ContextService;
import com.sagag.services.hazelcast.api.HaynesProCacheService;
import com.sagag.services.hazelcast.api.NextWorkingDateCacheService;
import com.sagag.services.hazelcast.domain.user.EshopContext;
import com.sagag.services.hazelcast.domain.user.EshopContextType;
import com.sagag.services.rest.swagger.docs.ApiDesc;
import com.sagag.services.service.api.OrderBusinessService;
import com.sagag.services.service.api.UserBusinessService;
import com.sagag.services.service.request.ContextRequestBody;
import com.sagag.services.service.virtualuser.VirtualUserHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller class to define APIs for application context.
 */
@RestController
@RequestMapping("/context")
@Api(tags = "User Context APIs")
@Slf4j
public class ContextController {

  @Autowired
  private ContextService contextService;
  @Autowired
  private UserBusinessService userBusinessService;
  @Autowired
  private NextWorkingDateCacheService nextWorkingDateCacheService;
  @Autowired
  private OrderBusinessService orderBusinessService;
  @Autowired
  private HaynesProCacheService haynesProCacheService;
  @Autowired
  private VirtualUserHelper virtualUserHelper;
  @Autowired
  private ActiveDmsUserCacheService activeDmsUserCacheService;

  /**
   * Returns the EshopContext for this user.
   *
   * @param authed the authenticated user
   */
  @ApiOperation(value = ApiDesc.SAGContext.GET_ESHOP_CONTEXT_API_DESC,
      notes = ApiDesc.SAGContext.GET_ESHOP_CONTEXT_API_NOTE)
  @GetMapping(value = "/",
      produces = MediaType.APPLICATION_JSON_VALUE)
  public EshopContext getEshopContext(final OAuth2Authentication authed) {
    final UserInfo user = (UserInfo) authed.getPrincipal();
    log.debug("context for this user {} is {}", user.getCachedUserId(), contextService.getEshopContext(user
            .key()));
    return contextService.getEshopContext(user.key());
  }

  /**
   * Updates the application context.
   *
   * @param contextType context type.
   * @param body the request body
   * @param authed the authenticated user
   * @throws ResultNotFoundException
   */
  // @formatter:off
  @ApiOperation(
      value = ApiDesc.SAGContext.UPDATE_ESHOP_CONTEXT_API_DESC,
      notes = ApiDesc.SAGContext.UPDATE_ESHOP_CONTEXT_API_NOTE
      )
  @PostMapping(
      value = "/update/{contextType}",
      produces = MediaType.APPLICATION_JSON_VALUE
      )
  @ResponseStatus(value = HttpStatus.OK)
  public void updateEshopContext(
      final OAuth2Authentication authed,
      @PathVariable final Integer contextType,
      @RequestBody ContextRequestBody body) throws ResultNotFoundException {
    // @formatter:on
    final UserInfo user = (UserInfo) authed.getPrincipal();
    EshopContextType type = EshopContextType.getEshopContextType(contextType);
    if (Objects.isNull(type)) {
      throw new ResultNotFoundException("update context has error.");
    }
    contextService.updateByContextType(user, body.getContextDto(), type);
    log.debug("EshopContext: updateByContextType: User id: {} \nContext dto Body: {} \nType {}",
            user.getCachedUserId(), body.getContextDto(), type);
  }

  /**
   * refresh Eshop context use for 1. Click HomePage button 2. Log out
   *
   * @param body the request body to refresh
   * @param authed the authenticated user
   */
  // @formatter:off
  @ApiOperation(
      value = ApiDesc.SAGContext.REFRESH_ESHOP_CONTEXT_API_DESC,
      notes = ApiDesc.SAGContext.REFRESH_ESHOP_CONTEXT_API_NOTE
      )
  @DeleteMapping(
      value = "/clear",
      produces = MediaType.APPLICATION_JSON_VALUE
      )
  @ResponseStatus(value = HttpStatus.OK)
  public void refreshContext(final OAuth2Authentication authed,
      @RequestBody ContextRequestBody body) {
    final UserInfo user = (UserInfo) authed.getPrincipal();
    contextService.clearContext(user.key(), body.getScope(), body.getContextId());
  }

  /**
   * Clears context in cache by user when user logout
   *
   * @param authed the authenticated user
   */
  @ApiOperation(value = ApiDesc.SAGContext.CLEAR_CONTEXT_IN_CACHE_API_DESC,
      notes = ApiDesc.SAGContext.CLEAR_CONTEXT_IN_CACHE_NOTE)
  @ResponseStatus(value = HttpStatus.OK)
  @DeleteMapping(value = "/cache/clear", produces = MediaType.APPLICATION_JSON_VALUE)
  public void clearCache(final OAuth2Authentication authed) {
    final UserInfo user = (UserInfo) authed.getPrincipal();
    final Long userId = user.getId();
    userBusinessService.clearCacheUser(userId);
    activeDmsUserCacheService.remove(userId);
    virtualUserHelper.releaseDataForVirtualUser(user);
    nextWorkingDateCacheService.clear(user);
    haynesProCacheService.clearLabourTimes(user.key());
  }

  /**
   * Refreshes the next working date cache by user for case user login/ login on behalf.
   *
   * @param authed the authenticated user
   */
  @ApiOperation(value = ApiDesc.SAGContext.REFRESH_NEXT_WORKING_DATE_API_DESC,
      notes = ApiDesc.SAGContext.REFRESH_NEXT_WORKING_DATE_NOTE)
  @ResponseStatus(value = HttpStatus.OK)
  @PostMapping(value = "/cache/nextWorkingDate/refresh",
      produces = MediaType.APPLICATION_JSON_VALUE)
  public void refreshNextWorkingDateCache(final OAuth2Authentication authed) {
    final UserInfo user = (UserInfo) authed.getPrincipal();
    // Clear cache and update next working date with the default branch id
    nextWorkingDateCacheService.clear(user);
    nextWorkingDateCacheService.update(user, user.getDefaultBranchId());
  }

  /**
   * Initializes order condition for user
   *
   * @param authed the authenticated user
   * @return the initial order condition, the object of {@link EshopContext}
   */
  @ApiOperation(value = ApiDesc.SAGContext.REFRESH_NEXT_WORKING_DATE_API_DESC,
      notes = ApiDesc.SAGContext.REFRESH_NEXT_WORKING_DATE_NOTE)
  @ResponseStatus(value = HttpStatus.OK)
  @PostMapping(value = "/init", produces = MediaType.APPLICATION_JSON_VALUE)
  public EshopContext initOrderCondition(final OAuth2Authentication authed) {
    final UserInfo user = (UserInfo) authed.getPrincipal();
    contextService.updateOrderConditions(user, orderBusinessService.initializeOrderConditions(user));
    return contextService.getEshopContext(user.key());
  }
}
