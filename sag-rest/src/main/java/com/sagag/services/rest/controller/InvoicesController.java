package com.sagag.services.rest.controller;

import com.sagag.eshop.repo.hz.entity.ShopType;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.common.enums.SupportedAffiliate;
import com.sagag.services.common.exception.ResultNotFoundException;
import com.sagag.services.common.utils.RestExceptionUtils;
import com.sagag.services.domain.sag.invoice.InvoiceDto;
import com.sagag.services.hazelcast.domain.cart.ShoppingCart;
import com.sagag.services.rest.authorization.annotation.CanUsedSubShoppingCartPreAuthorization;
import com.sagag.services.rest.resource.InvoiceResource;
import com.sagag.services.rest.swagger.docs.ApiDesc;
import com.sagag.services.rest.swagger.docs.ApiDesc.Invoice;
import com.sagag.services.service.api.CartBusinessService;
import com.sagag.services.service.api.InvoiceService;
import com.sagag.services.service.cart.support.ShopTypeDefault;
import com.sagag.services.service.request.invoice.InvoiceSearchRequest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "/invoice/archives", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(tags = "Invoice Archives APIs")
@ApiResponses(value = {
    @ApiResponse(code = ApiDesc.Code.OK, message = ApiDesc.Message.SUCCESSFUL),
    @ApiResponse(code = ApiDesc.Code.NOT_FOUND, message = ApiDesc.Message.NOT_FOUND)
})
public class InvoicesController {

  @Autowired
  private InvoiceService invoiceService;

  @Autowired
  private CartBusinessService cartBusService;

  /**
   * Search invoices.
   *
   * @param auth the authenticated user
   * @return list of <code>InvoiceDetailDto</code> instance
   * @throws ResultNotFoundException
   */
  @ApiOperation(value = Invoice.GET_INVOICES_DESC, notes = Invoice.GET_INVOICES_NOTE)
  @PostMapping("/search")
  public InvoiceResource searchInvoice(final OAuth2Authentication auth,
      @RequestBody InvoiceSearchRequest request) throws ResultNotFoundException {
    final UserInfo user = (UserInfo) auth.getPrincipal();
    final List<InvoiceDto> invoices = invoiceService.searchInvoices(user, request);
    return InvoiceResource.of(RestExceptionUtils.doSafelyReturnNotEmptyRecords(invoices));
  }

  /**
   * Shows invoice article list in detail.
   *
   * @param auth the authenticated user
   * @param invoiceNr the invoice number
   */
  @ApiOperation(value = Invoice.GET_INVOICES_DESC, notes = Invoice.GET_INVOICES_NOTE)
  @GetMapping("/{invoiceNr:.+}")
  public InvoiceDto getInvoiceDetail(final OAuth2Authentication auth,
      @PathVariable("invoiceNr") final String invoiceNr,
      @RequestParam(required = false) String orderNr,
      @RequestParam(required = false) boolean oldInvoice,
      @RequestParam(defaultValue = "true") final boolean simpleMode)
          throws ResultNotFoundException {
    final UserInfo user = (UserInfo) auth.getPrincipal();

    return RestExceptionUtils.doSafelyReturnOptionalRecord(
        invoiceService.getInvoiceDetail(user, invoiceNr, orderNr, simpleMode, oldInvoice));
  }

  /**
   * Streams invoice PDF URL.
   *
   * @param authed the authenticated user
   * @param invoiceNr the invoice number
   * @throws IOException
   */
  @ApiOperation(value = Invoice.DOWLOAD_INVOICE_PDF_DESC, notes = Invoice.DOWLOAD_INVOICE_PDF_NOTE)
  @GetMapping("/{invoiceNr:.+}/pdf")
  public ResponseEntity<byte[]> streamInvoicePdf(final OAuth2Authentication authed,
      @RequestParam(required = false) boolean oldInvoice,
      @RequestParam(required = false) String orderNr,
      @PathVariable("invoiceNr") final String invoiceNr) throws IOException {
    final UserInfo user = (UserInfo) authed.getPrincipal();
    SupportedAffiliate affiliate = SupportedAffiliate.fromDesc(user.getAffiliateShortName());
    return invoiceService.streamInvoicePdf(affiliate, user.getCustNrStr(), invoiceNr, oldInvoice,
        orderNr).buildResponseEntity();
  }

  /**
   * Adds invoice to shopping cart.
   */
  @ApiOperation(value = Invoice.ADD_TO_CART_BY_INVOICE_NR_DESC)
  @PostMapping("/{invoiceNr:.+}/addToCart")
  @CanUsedSubShoppingCartPreAuthorization
  public ShoppingCart addInvoiceToCart(final HttpServletRequest request,
      final OAuth2Authentication authed,
      @PathVariable final String invoiceNr,
      @RequestParam(required = false) String orderNr,
      @RequestParam(required = false) Long orderHistoryId,
      @RequestParam(required = false) String basketItemSourceId,
      @RequestParam(required = false) String basketItemSourceDesc,
      @ShopTypeDefault ShopType shopType) {
    final UserInfo user = (UserInfo) authed.getPrincipal();
    return cartBusService.addInvoiceToCart(user, invoiceNr, orderNr, orderHistoryId,
        shopType, basketItemSourceId, basketItemSourceDesc);
  }

}
