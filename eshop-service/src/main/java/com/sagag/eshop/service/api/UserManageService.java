package com.sagag.eshop.service.api;

import com.sagag.services.domain.eshop.criteria.UserSearchCriteriaRequest;
import com.sagag.services.domain.eshop.dto.UserSearchResultItemDto;
import org.springframework.data.domain.Page;

public interface UserManageService {

  /**
   * Returns active users which matched searchCriteria.
   *
   * @param searchCriteria
   * @return UserSearchResultItemDto
   */
  Page<UserSearchResultItemDto> searchActiveUserProfile(UserSearchCriteriaRequest searchCriteria);

}
