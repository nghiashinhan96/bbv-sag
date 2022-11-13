package com.sagag.services.rest.controller.legal_term;

import com.sagag.eshop.service.api.LegalTermService;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.domain.sag.legal_term.LegalTermDto;
import com.sagag.services.rest.resource.LegalTermResource;
import com.sagag.services.rest.swagger.docs.ApiDesc;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controller class to provide RESTful APIs for Legal Term.
 */
@RestController
@RequestMapping("/legal-terms")
@Api(tags = "Customers APIs")
public class LegalTermsController {

  @Autowired
  private LegalTermService legalTermService;

  /**
   * @param authed the user who requests
   * @return the list legal terms.
   */
  @ApiOperation(value = ApiDesc.LegalTerm.RETRIEVE_LEGAL_TERMS_API_DESC,
      notes = ApiDesc.LegalTerm.RETRIEVE_LEGAL_TERMS_API_NOTE)
  @ApiResponses(value = {
      @ApiResponse(code = ApiDesc.Code.OK, message = ApiDesc.Message.SUCCESSFUL),
      @ApiResponse(code = ApiDesc.Code.INTERNAL_SERVER_ERROR, message = ApiDesc.Message.INTERNAL_SERVER_ERROR)
  })
  @GetMapping
  public LegalTermResource getLegalTerms(final OAuth2Authentication authed) {
    final UserInfo user = (UserInfo) authed.getPrincipal();
    final List<LegalTermDto> legalTerms = legalTermService
        .getLegalTerms(user.getOrganisationId(), user.getLanguage(), user.getFirstLoginDate());
    return LegalTermResource.builder().legalTerms(legalTerms).build();
  }

  /**
   * @param authed the user who requests
   * @return nothing
   */
  @ApiOperation(value = ApiDesc.LegalTerm.ACCEPT_LEGAL_TERM_API_DESC,
      notes = ApiDesc.LegalTerm.ACCEPT_LEGAL_TERM_API_NOTE)
  @ApiResponses(value = {
      @ApiResponse(code = ApiDesc.Code.NO_CONTENT, message = ApiDesc.Message.NO_CONTENT),
      @ApiResponse(code = ApiDesc.Code.BAD_REQUEST, message = ApiDesc.Message.BAD_REQUEST)
  })
  @PutMapping(value = "/{termId}")
  @ResponseStatus(value = HttpStatus.NO_CONTENT)
  public void acceptLegalTerm(final OAuth2Authentication authed, @PathVariable final Long termId) {
    final UserInfo user = (UserInfo) authed.getPrincipal();
    legalTermService.acceptLegalTerm(termId, user.getOrganisationId());
  }

  /**
   * @param authed the user who requests
   * @return boolean value
   */
  @ApiOperation(value = ApiDesc.LegalTerm.HAS_EXPIRED_TERMS_API_DESC,
      notes = ApiDesc.LegalTerm.HAS_EXPIRED_TERMS_API_NOTE)
  @ApiResponses(value = {
      @ApiResponse(code = ApiDesc.Code.OK, message = ApiDesc.Message.SUCCESSFUL),
      @ApiResponse(code = ApiDesc.Code.INTERNAL_SERVER_ERROR,
      message = ApiDesc.Message.INTERNAL_SERVER_ERROR)
  })
  @PostMapping(value = "/has-expired-terms")
  public LegalTermResource hasExpiredTerms(final OAuth2Authentication authed) {
    final UserInfo user = (UserInfo) authed.getPrincipal();
    return LegalTermResource.builder()
        .hasExpiredTerms(legalTermService.hasExpiredTerms(
            user.getOrganisationId(), user.getLanguage(), user.getFirstLoginDate())).build();
  }

  /**
   * @param authed the user who requests
   * @return boolean value
   */
  @ApiOperation(value = ApiDesc.LegalTerm.RETRIEVE_UNACCEPTED_LEGAL_TERMS_API_DESC,
      notes = ApiDesc.LegalTerm.RETRIEVE_UNACCEPTED_LEGAL_TERMS_API_NOTE)
  @ApiResponses(value = {
      @ApiResponse(code = ApiDesc.Code.OK, message = ApiDesc.Message.SUCCESSFUL),
      @ApiResponse(code = ApiDesc.Code.INTERNAL_SERVER_ERROR,
      message = ApiDesc.Message.INTERNAL_SERVER_ERROR)
  })
  @PostMapping(value = "/unaccepted-terms")
  public LegalTermResource getUnAcceptedLegalTerms(final OAuth2Authentication authed) {
    final UserInfo user = (UserInfo) authed.getPrincipal();
    return LegalTermResource.builder().legalTerms(legalTermService
        .getUnacceptedTerms(user.getOrganisationId(), user.getLanguage(), user.getFirstLoginDate()))
        .build();
  }

}
