package com.sagag.services.rest.controller.feedback;

import com.sagag.eshop.service.dto.EmailAttachment;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.eshop.service.exception.UserValidationException;
import com.sagag.services.common.enums.SupportedAffiliate;
import com.sagag.services.common.utils.SagJSONUtil;
import com.sagag.services.common.utils.WholesalerUtils;
import com.sagag.services.rest.swagger.docs.ApiDesc;
import com.sagag.services.service.api.FeedbackBusinessService;
import com.sagag.services.service.dto.feedback.FeedBackCustomerUserDataDto;
import com.sagag.services.service.dto.feedback.FeedBackSalesNotOnBehalfUserDataDto;
import com.sagag.services.service.dto.feedback.FeedBackSalesOnBehalfUserDataDto;
import com.sagag.services.service.request.FeedbackMessageRequest;
import com.sagag.services.service.resource.FeedbackMasterDataResource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/feedback")
@Api(tags = "Feedback APIs")
public class FeedbackController {

  @Autowired
  private FeedbackBusinessService feedbackBusinessService;

  @ApiOperation(value = ApiDesc.Feedback.CREATE_CUSTOMER_FEEDBACK_MESSAGE_API_NOTE,
      notes = ApiDesc.Feedback.CREATE_CUSTOMER_FEEDBACK_MESSAGE_API_DESC)
  @PostMapping(value = "/customer/create", produces = MediaType.APPLICATION_JSON_VALUE)
  public void createCustomerFeedback(final OAuth2Authentication authed,
      @RequestBody final FeedbackMessageRequest request) throws UserValidationException {
    final UserInfo user = (UserInfo) authed.getPrincipal();
    feedbackBusinessService.createCustomerFeedback(user, request);
  }

  @ApiOperation(value = ApiDesc.Feedback.CREATE_SALES_ONBEHALF_FEEDBACK_MESSAGE_API_NOTE,
      notes = ApiDesc.Feedback.CREATE_SALES_ONBEHALF_FEEDBACK_MESSAGE_API_DESC)
  @PostMapping(value = "/sales-onbehalf/create", produces = MediaType.APPLICATION_JSON_VALUE,
      consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public void createSaleOnBehalfFeedback(final OAuth2Authentication authed,
      @RequestParam("feedbackModel") String feedbackModel,
      @RequestParam("files") List<MultipartFile> attachments)
      throws UserValidationException, IOException {

    final UserInfo user = (UserInfo) authed.getPrincipal();
    FeedbackMessageRequest model =
        SagJSONUtil.convertJsonToObject(feedbackModel, FeedbackMessageRequest.class);
    model.setAttachments(EmailAttachment.fromMultipartFiles(attachments));
    feedbackBusinessService.createSalesOnBehalfFeedback(user, model);
  }

  @ApiOperation(value = ApiDesc.Feedback.CREATE_SALES_NOT_ONBEHALF_FEEDBACK_MESSAGE_API_NOTE,
      notes = ApiDesc.Feedback.CREATE_SALES_NOT_ONBEHALF_FEEDBACK_MESSAGE_API_DESC)
  @PostMapping(value = "/sales-not-onbehalf/{affiliate}/create", produces = MediaType.APPLICATION_JSON_VALUE,
      consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public void createSaleNotOnBehalfFeedback(final OAuth2Authentication authed,
      @PathVariable("affiliate") final String affiliate,
      @RequestParam("feedbackModel") String feedbackModel,
      @RequestParam("files") List<MultipartFile> attachments)
      throws UserValidationException, IOException {
    final UserInfo user = (UserInfo) authed.getPrincipal();
    FeedbackMessageRequest model =
        SagJSONUtil.convertJsonToObject(feedbackModel, FeedbackMessageRequest.class);
    model.setAttachments(EmailAttachment.fromMultipartFiles(attachments));
    // For wholesaler endpoint we map to affiliate wbb.
    model.setAffiliate(
        WholesalerUtils.isFinalCustomerEndpoint(affiliate) ? SupportedAffiliate.WBB.getAffiliate() : affiliate);
    feedbackBusinessService.createSalesNotOnBehalfFeedback(user, model);
  }

  @ApiOperation(value = ApiDesc.Feedback.GET_FEEDBACK_CUSTOMER_USER_DATA_API_NOTE,
      notes = ApiDesc.Feedback.GET_FEEDBACK_CUSTOMER_USER_DATA_API_DESC)
  @GetMapping(value = "/customer/user-data", produces = MediaType.APPLICATION_JSON_VALUE)
  public FeedbackMasterDataResource<FeedBackCustomerUserDataDto> getCustomerUserData(
      final OAuth2Authentication authed) {
    final UserInfo user = (UserInfo) authed.getPrincipal();
    return feedbackBusinessService.getFeedbackCustomerUserData(user);
  }

  @ApiOperation(value = ApiDesc.Feedback.GET_FEEDBACK_SALES_ONBEHALF_USER_DATA_API_NOTE,
      notes = ApiDesc.Feedback.GET_FEEDBACK_SALES_ONBEHALF_USER_DATA_API_DESC)
  @GetMapping(value = "/sales-onbehalf/user-data", produces = MediaType.APPLICATION_JSON_VALUE)
  public FeedbackMasterDataResource<FeedBackSalesOnBehalfUserDataDto> getSalesOnBehalflUserData(
      final OAuth2Authentication authed) {
    final UserInfo user = (UserInfo) authed.getPrincipal();
    return feedbackBusinessService.getFeedbackSalesOnBehalfUserData(user);
  }

  @ApiOperation(value = ApiDesc.Feedback.GET_FEEDBACK_SALES_NOT_ONBEHALF_USER_DATA_API_NOTE,
      notes = ApiDesc.Feedback.GET_FEEDBACK_SALES_NOT_ONBEHALF_USER_DATA_API_DESC)
  @GetMapping(value = "/sales-not-onbehalf/user-data", produces = MediaType.APPLICATION_JSON_VALUE)
  public FeedbackMasterDataResource<FeedBackSalesNotOnBehalfUserDataDto> getSalesNotOnBehalflUserData(
      final OAuth2Authentication authed) {
    final UserInfo user = (UserInfo) authed.getPrincipal();
    return feedbackBusinessService.getFeedbackSalesNotOnBehalfUserData(user);
  }
}
