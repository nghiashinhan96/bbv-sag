package com.sagag.eshop.service.api;

import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.eshop.service.exception.CustomerNotFoundException;
import com.sagag.eshop.service.exception.MessageTimeOverlapException;
import com.sagag.services.domain.eshop.criteria.VMessageSearchRequestCriteria;
import com.sagag.services.domain.eshop.message.dto.MessageDto;
import com.sagag.services.domain.eshop.message.dto.MessageFilterOptionsMasterData;
import com.sagag.services.domain.eshop.message.dto.MessageMasterDataDto;
import com.sagag.services.domain.eshop.message.dto.MessageResultDto;
import com.sagag.services.domain.eshop.message.dto.MessageSavingRequestDto;
import com.sagag.services.domain.eshop.message.dto.MessageSearchResultDto;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Interface that provides api services for message .
 *
 */
public interface MessageService {

  /**
   * Returns master data needed for create new message at back office.
   * @return message master data
   */
  MessageMasterDataDto getMasterData();

  /**
   * Creates new message.
   * @param model the input needed for creating new message
   * @param createdUserId user that create the message
   * @throws MessageTimeOverlapException throw when message is overlaped
   * @throws CustomerNotFoundException throw when can not find the customer
   */
  void create(MessageSavingRequestDto model, Long createdUserId)
      throws MessageTimeOverlapException, CustomerNotFoundException;

  /**
   * Returns messages for unauthorized pages.
   *
   * @param affiliate the requested affiliate
   * @param langIso the requested language
   * @return list of MessageDto
   */
  List<MessageDto> getUnauthorizedMessages(String affiliate, String langIso);

  /**
   * Returns messages for logged in user.
   *
   * @param affiliate the requested affiliate
   * @return list of MessageDto
   */
  List<MessageDto> getAuthorizedMessages(UserInfo user, String affiliate);

  /**
   * Hide messages for specified user.
   *
   * @param messageIds list of message id to hide
   */
  void hideMessages(Long userId, String messageIds);

  /**
   * Searching saved message base on criteria.
   * @param criteria search conditions
   * @param pageable
   * @return list of saved message that match criteria
   */
  Page<MessageSearchResultDto> searchMessage(VMessageSearchRequestCriteria criteria,
      Pageable pageable);

  /**
   * Get options support for search message.
   *
   * @return options for filtering
   */
  MessageFilterOptionsMasterData getFilterOptionsMasterData();

  /**
   * Delete message base on it's id.
   *
   * @param messageId id of message wanna delete
   */
  void deleteMessage(Long messageId);

  /**
   * Find message by id.
   *
   * @param id of message need to find
   * @return
   */
  MessageResultDto findById(Long id);

  /**
   * Update existed message.
   *
   * @param model the input needed for updating new message
   * @param messageId id of message need to update
   * @param updatedUserId user that update the message
   * @throws MessageTimeOverlapException throw this exception when message is overlaped
   * @throws CustomerNotFoundException throw when can not find the customer
   */
  void update(MessageSavingRequestDto model, Long messageId, Long updatedUserId)
      throws MessageTimeOverlapException, CustomerNotFoundException;
}
