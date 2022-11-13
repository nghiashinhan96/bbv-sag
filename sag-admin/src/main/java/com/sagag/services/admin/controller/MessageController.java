package com.sagag.services.admin.controller;

import com.sagag.eshop.service.api.MessageService;
import com.sagag.eshop.service.exception.CustomerNotFoundException;
import com.sagag.eshop.service.exception.MessageTimeOverlapException;
import com.sagag.services.admin.swagger.docs.ApiDesc;
import com.sagag.services.domain.eshop.criteria.VMessageSearchRequestCriteria;
import com.sagag.services.domain.eshop.message.dto.MessageFilterOptionsMasterData;
import com.sagag.services.domain.eshop.message.dto.MessageMasterDataDto;
import com.sagag.services.domain.eshop.message.dto.MessageResultDto;
import com.sagag.services.domain.eshop.message.dto.MessageSavingRequestDto;
import com.sagag.services.domain.eshop.message.dto.MessageSearchResultDto;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * The controller exposes api for message.
 *
 */
@RestController
@RequestMapping("/admin/message")
@Api(tags = "admin")
public class MessageController {

  @Autowired
  private MessageService messageService;

  @ApiOperation(value = ApiDesc.Message.CREATES_NEW_MESSAGE_NOTE,
      notes = ApiDesc.Message.CREATES_NEW_MESSAGE_DESC)
  @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(value = HttpStatus.OK)
  public void create(@RequestBody final MessageSavingRequestDto dto,
      final OAuth2Authentication authed)
      throws MessageTimeOverlapException, CustomerNotFoundException {
    messageService.create(dto, Long.parseLong(authed.getPrincipal().toString()));
  }

  @ApiOperation(value = ApiDesc.Message.GET_MESSAGE_MASTER_DATA_NOTE,
      notes = ApiDesc.Message.GET_MESSGAE_MASTER_DATA_DESC)
  @GetMapping(value = "/master-data", produces = MediaType.APPLICATION_JSON_VALUE)
  public MessageMasterDataDto getMasterData() {
    return messageService.getMasterData();
  }

  @ApiOperation(value = ApiDesc.Message.SEARCH_MESSAGE_NOTE,
      notes = ApiDesc.Message.SEARCH_MESSAGE_DESC)
  @PostMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
  public Page<MessageSearchResultDto> searchMessage(
      @RequestBody final VMessageSearchRequestCriteria criteria,
      @PageableDefault final Pageable pageable) {
    return messageService.searchMessage(criteria, pageable);
  }

  @ApiOperation(
      value = ApiDesc.Message.GET_FILTERING_OPTIONS_MASTER_DATA_FOR_SEARCHING_MESSAGE_NOTE,
      notes = ApiDesc.Message.GET_FILTERING_OPTIONS_MASTER_DATA_FOR_SEARCHING_MESSAGE_DESC)
  @GetMapping(value = "/search/master-data", produces = MediaType.APPLICATION_JSON_VALUE)
  public MessageFilterOptionsMasterData getFilteringMessageOptionsMasterData() {
    return messageService.getFilterOptionsMasterData();
  }

  @ApiOperation(value = ApiDesc.Message.DELETE_MESSAGE_NOTE,
      notes = ApiDesc.Message.DELETE_MESSAGE_DESC)
  @DeleteMapping(value = "/{messageId}/delete", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(value = HttpStatus.OK)
  public void delete(@PathVariable("messageId") final Long messageId) {
    messageService.deleteMessage(messageId);
  }

  @ApiOperation(value = ApiDesc.Message.FIND_MESSAGE_NOTE,
      notes = ApiDesc.Message.FIND_MESSAGE_DESC)
  @GetMapping(value = "/{messageId}", produces = MediaType.APPLICATION_JSON_VALUE)
  public MessageResultDto findById(@PathVariable("messageId") final Long messageId) {
    return messageService.findById(messageId);
  }

  @ApiOperation(value = ApiDesc.Message.UPDATES_NEW_MESSAGE_NOTE,
      notes = ApiDesc.Message.UPDATES_NEW_MESSAGE_DESC)
  @PutMapping(value = "/{messageId}/update", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(value = HttpStatus.OK)
  public void update(@RequestBody final MessageSavingRequestDto dto,
      final OAuth2Authentication authed, @PathVariable("messageId") final Long messageId)
      throws MessageTimeOverlapException, CustomerNotFoundException {
    messageService.update(dto, messageId, Long.parseLong(authed.getPrincipal().toString()));
  }
}
