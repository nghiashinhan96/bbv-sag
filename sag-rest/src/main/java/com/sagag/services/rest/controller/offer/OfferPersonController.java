package com.sagag.services.rest.controller.offer;

import com.sagag.eshop.repo.criteria.offer.OfferPersonSearchCriteria;
import com.sagag.eshop.service.api.OfferPersonService;
import com.sagag.eshop.service.api.SalutationService;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.eshop.service.dto.offer.OfferPersonDto;
import com.sagag.eshop.service.dto.offer.ViewOfferPersonDto;
import com.sagag.services.common.exception.ResultNotFoundException;
import com.sagag.services.common.utils.RestExceptionUtils;
import com.sagag.services.common.utils.SagBeanUtils;
import com.sagag.services.rest.resource.offer.OfferPersonPageResource;
import com.sagag.services.rest.resource.offer.OfferPersonResource;
import com.sagag.services.rest.swagger.docs.ApiDesc;
import com.sagag.services.service.request.offer.OfferPersonRequestBody;
import com.sagag.services.service.request.offer.OfferPersonSearchRequestBody;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

/**
 * Offer Personal controllers.
 */
@RestController
@RequestMapping("/offer/person")
@Api(tags = "Offer Person APIs")
public class OfferPersonController {

  private static final String NOT_FOUND_ANY_OFFER_PERSON_MSG =
      "Not found any shop article with id = %s";

  @Autowired
  private OfferPersonService offerPersonService;

  @Autowired
  private SalutationService salutationService;

  /**
   * Returns the list of offer personals info.
   *
   * @param auth
   * @param body
   * @return the result of {@link OfferPersonPageResource}
   * @throws ResultNotFoundException
   */
  @ApiOperation(value = ApiDesc.OfferPerson.SEARCH_OFFER_PERSON_API_DESC,
      notes = ApiDesc.OfferPerson.SEARCH_OFFER_PERSON_NOTE)
  @PostMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
  public OfferPersonPageResource searchOfferPersons(final OAuth2Authentication auth,
      HttpServletRequest request, @RequestBody final OfferPersonSearchRequestBody body,
      @PageableDefault final Pageable pageable) throws ResultNotFoundException {
    final UserInfo user = (UserInfo) auth.getPrincipal();

    final OfferPersonSearchCriteria criteria =
        SagBeanUtils.map(body, OfferPersonSearchCriteria.class);
    criteria.setOrganisationId(user.getOrganisationId());

    final Page<ViewOfferPersonDto> offerPersons =
        offerPersonService.searchOfferPersons(criteria, pageable);
    return OfferPersonPageResource.of(
        RestExceptionUtils.doSafelyReturnNotEmptyRecords(offerPersons, "Not found any people"));
  }

  /**
   * Creates the new offer person.
   *
   * @param auth
   * @return the result of {@link OfferPersonResource}
   */
  @ApiOperation(value = ApiDesc.OfferPerson.CREATE_OFFER_PERSON_API_DESC,
      notes = ApiDesc.OfferPerson.CREATE_OFFER_PERSON_NOTE)
  @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
  public OfferPersonResource createOfferPerson(final OAuth2Authentication auth,
      HttpServletRequest request, @RequestBody final OfferPersonRequestBody requestBody) {
    final UserInfo user = (UserInfo) auth.getPrincipal();
    final OfferPersonDto offerPerson = SagBeanUtils.map(requestBody, OfferPersonDto.class);
    offerPerson.setCurrentUserId(user.getId());
    offerPerson.setOrganisationId(user.getOrganisationId());
    return OfferPersonResource.of(offerPersonService.createOfferPerson(offerPerson));
  }

  /**
   * Updates the offer person.
   *
   * @param auth
   * @return the result of {@link OfferPersonResource}
   */
  @ApiOperation(value = ApiDesc.OfferPerson.UPDATE_OFFER_PERSON_API_DESC,
      notes = ApiDesc.OfferPerson.UPDATE_OFFER_PERSON_NOTE)
  @PutMapping(value = "/edit/{offerPersonId}", produces = MediaType.APPLICATION_JSON_VALUE)
  public OfferPersonResource editOfferPerson(final OAuth2Authentication auth,
      HttpServletRequest request, @RequestBody final OfferPersonRequestBody requestBody,
      @PathVariable("offerPersonId") final Long offerPersonId) {
    final UserInfo user = (UserInfo) auth.getPrincipal();
    final OfferPersonDto offerPerson = SagBeanUtils.map(requestBody, OfferPersonDto.class);
    offerPerson.setId(offerPersonId);
    offerPerson.setCurrentUserId(user.getId());
    offerPerson.setOrganisationId(user.getOrganisationId());
    return OfferPersonResource.of(offerPersonService.editOfferPerson(offerPerson));
  }

  /**
   * Returns the offer person details by id.
   *
   * @param auth
   * @param offerPersonId
   * @return the resource object of {@link OfferPersonResource}
   * @throws ResultNotFoundException
   */
  @ApiOperation(value = ApiDesc.OfferPerson.GET_OFFER_PERSON_DETAILS_API_DESC,
      notes = ApiDesc.OfferPerson.GET_OFFER_PERSON_DETAILS_NOTE,
      response = OfferPersonResource.class)
  @ApiResponses(
      value = { @ApiResponse(code = ApiDesc.Code.OK, message = ApiDesc.Message.SUCCESSFUL),
          @ApiResponse(code = ApiDesc.Code.BAD_REQUEST, message = ApiDesc.Message.BAD_REQUEST),
          @ApiResponse(code = ApiDesc.Code.NOT_FOUND, message = ApiDesc.Message.NOT_FOUND) })
  @GetMapping(value = "/{offerPersonId}", produces = MediaType.APPLICATION_JSON_VALUE)
  public OfferPersonResource getOfferPersonDetails(final OAuth2Authentication auth,
      HttpServletRequest request, @PathVariable("offerPersonId") final Long offerPersonId)
      throws ResultNotFoundException {
    final Optional<OfferPersonDto> offerPerson =
        offerPersonService.getOfferPersonDetails(offerPersonId);

    return OfferPersonResource.of(RestExceptionUtils.doSafelyReturnOptionalRecord(
        offerPerson, String.format(NOT_FOUND_ANY_OFFER_PERSON_MSG, offerPersonId)));
  }

  /**
   * Removes existing offer person.
   *
   * @param auth
   * @param offerPersonId
   * @return the resource object of {@link OfferPersonResource}
   */
  @ApiOperation(value = ApiDesc.OfferPerson.REMOVE_OFFER_PERSON_API_DESC,
      notes = ApiDesc.OfferPerson.REMOVE_OFFER_PERSON_API_NOTE,
      response = OfferPersonResource.class)
  @ApiResponses(
      value = { @ApiResponse(code = ApiDesc.Code.OK, message = ApiDesc.Message.SUCCESSFUL),
          @ApiResponse(code = ApiDesc.Code.BAD_REQUEST, message = ApiDesc.Message.BAD_REQUEST) })
  @DeleteMapping(value = "/remove/{offerPersonId}", produces = MediaType.APPLICATION_JSON_VALUE)
  public OfferPersonResource removeOfferPerson(final OAuth2Authentication auth,
      HttpServletRequest request, @PathVariable final Long offerPersonId) {
    final UserInfo user = (UserInfo) auth.getPrincipal();
    return OfferPersonResource
        .of(offerPersonService.removeOfferPerson(user.getId(), offerPersonId));
  }

  /**
   * Removes existing offer person.
   *
   * @param auth
   * @param offerPersonId
   * @return the resource object of {@link OfferPersonResource}
   */
  @ApiOperation(value = ApiDesc.OfferPerson.GET_SALUTATION_LIST_API_DESC,
      notes = ApiDesc.OfferPerson.GET_SALUTATION_LIST_API_NOTE)
  @GetMapping(value = "/salutations", produces = MediaType.APPLICATION_JSON_VALUE)
  public List<String> getSalutations(HttpServletRequest request) {
    return salutationService.getOfferSalutations();
  }
}
