package com.sagag.services.service.api;

import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.domain.eshop.dto.UserSearchHistoryDto;

public interface SearchHistoryService {

  /**
   * Return user search histories summary
   *
   * @param userInfo current login user
   * @param fromSource search by sale/customer
   * @return {@link UserSearchHistoryDto}
   */
  UserSearchHistoryDto getLatestHistories(UserInfo userInfo, String fromSource);}
