package com.sagag.services.rest.controller;

import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.article.api.request.FinancialCardDetailRequest;
import com.sagag.services.article.api.request.FinancialCardHistoryRequest;
import com.sagag.services.ax.api.WtFinancialCardExternalService;
import com.sagag.services.common.exception.ResultNotFoundException;
import com.sagag.services.common.profiles.SbProfile;
import com.sagag.services.common.utils.RestExceptionUtils;
import com.sagag.services.domain.sag.financialcard.FinancialCardAmountDto;
import com.sagag.services.domain.sag.financialcard.FinancialCardDetailDto;
import com.sagag.services.domain.sag.financialcard.FinancialCardDocDto;
import com.sagag.services.rest.swagger.docs.ApiDesc;
import com.sagag.services.rest.swagger.docs.ApiDesc.FinancialCard;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller class to provide RESTful APIs for Financial Card.
 */
@RestController
@RequestMapping("/financial-cards")
@Api(tags = "Financial Card APIs")
@ApiResponses(value = { @ApiResponse(code = ApiDesc.Code.OK, message = ApiDesc.Message.SUCCESSFUL),
    @ApiResponse(code = ApiDesc.Code.NOT_FOUND, message = ApiDesc.Message.NOT_FOUND) })
@SbProfile
public class FinancialCardController {

  @Autowired
  private WtFinancialCardExternalService wtFinancialCardExternalService;

  /**
   * Shows financial card history payment
   *
   * @param authed the authenticated user
   * @param request the request option {@link FinancialCardHistoryRequest}
   */
  @ApiOperation(value = ApiDesc.FinancialCard.GET_FINANCIAL_CARD_HISTORY_DESC,
      notes = ApiDesc.FinancialCard.GET_FINANCIAL_CARD_HISTORY_NOTE)
  @PostMapping(value = "/history", produces = MediaType.APPLICATION_JSON_VALUE)
  public Page<FinancialCardDocDto> getFinancialCardHistory(
      @RequestBody FinancialCardHistoryRequest request, final OAuth2Authentication authed)
      throws ResultNotFoundException {
    final UserInfo user = (UserInfo) authed.getPrincipal();
    final Page<FinancialCardDocDto> financialCards = wtFinancialCardExternalService
        .getFinancialCardHistory(user.getCompanyName(), user.getCustNr(), request);
    return RestExceptionUtils.doSafelyReturnNotEmptyRecords(financialCards,
        String.format("Not found any history payment info with customer = %s", user.getCustNr()));
  }

  /**
   * Shows financial card amount
   *
   * @param authed the authenticated user
   * @param paymentMethod the payment method
   */
  @ApiOperation(value = FinancialCard.GET_FINANCIAL_CARD_AMOUNT_DESC,
      notes = FinancialCard.GET_FINANCIAL_CARD_AMOUNT_NOTE)
  @GetMapping(value = "/amount", produces = MediaType.APPLICATION_JSON_VALUE)
  public FinancialCardAmountDto getFinancialCardAmount(
      @RequestParam(value = "paymentMethod") String paymentMethod,
      final OAuth2Authentication authed) throws ResultNotFoundException {
    final UserInfo user = (UserInfo) authed.getPrincipal();
    return RestExceptionUtils.doSafelyReturnOptionalRecord(
        wtFinancialCardExternalService.getFinancialCardAmount(user.getCompanyName(),
            user.getCustNr(), paymentMethod),
        String.format("Not found any amount info with customer = %s", user.getCustNr()));
  }

  /**
   * Shows financial card in detail.
   *
   * @param auth the authenticated user
   * @param documentNr the financial card document number
   */
  @ApiOperation(value = FinancialCard.VIEW_FINANCIAL_CARD_DETAIL_DESC,
      notes = FinancialCard.VIEW_FINANCIAL_CARD_DETAIL_NOTE)
  @PostMapping("/detail")
  public FinancialCardDetailDto getFinancialCardDetail(final OAuth2Authentication auth,
      @RequestParam("documentNr") final String documentNr,
      @RequestBody FinancialCardDetailRequest request) throws ResultNotFoundException {
    final UserInfo user = (UserInfo) auth.getPrincipal();

    return RestExceptionUtils.doSafelyReturnOptionalRecord(
    		wtFinancialCardExternalService.getFinancialCardDetail(user.getCompanyName(),
				    user.getCustNrStr(), documentNr, request));
  }
}
