package com.sagag.services.rest.controller.message;

import com.sagag.eshop.service.api.MessageService;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.domain.eshop.message.dto.MessageDto;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/messages")
@Api(tags = "Messages APIs")
public class MessageController {

  @Autowired
  private MessageService messageService;

  @ApiOperation(value = "To get authorized messages",
      notes = "Get eshop messages for authorized user")
  @GetMapping(value = "/own/{affiliate}",
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseBody
  public List<MessageDto> getAuthorizedMessages(
      final OAuth2Authentication authed,
      @PathVariable("affiliate") final String affiliate) {
    final UserInfo user = (UserInfo) authed.getPrincipal();
    return messageService.getAuthorizedMessages(user, affiliate);
  }

  @ApiOperation(value = "To get unauthorized messages",
      notes = "Get eshop messages for unauthorized user")
  @GetMapping(value = "/common/{affiliate}/{lang}",
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseBody
  public List<MessageDto> getUnAuthorizedMessages(
      @PathVariable("affiliate") final String affiliate,
      @PathVariable("lang") final String langIso) {

    return messageService.getUnauthorizedMessages(affiliate, langIso);
  }

  @ApiOperation(value = "To update message visibility",
      notes = "Update message visibility for authorized user")
  @PostMapping(value = "/hide/{messageIds}",
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(value = HttpStatus.OK)
  public void hideMessages(final OAuth2Authentication authed,
      @PathVariable("messageIds") final String messageIds) {
    final UserInfo user = (UserInfo) authed.getPrincipal();
    messageService.hideMessages(user.getId(), messageIds);
  }

}
