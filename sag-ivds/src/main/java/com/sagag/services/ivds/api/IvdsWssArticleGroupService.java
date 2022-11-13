package com.sagag.services.ivds.api;

import com.sagag.services.domain.article.WssArticleGroupDocDto;
import com.sagag.services.domain.article.WssArticleGroupDto;
import com.sagag.services.ivds.request.WssArticleGroupSearchRequest;

import org.springframework.data.domain.Page;

import java.util.Optional;

/**
 * Interface class for IVDS WSS Article group search services.
 */
public interface IvdsWssArticleGroupService {

  /**
   * Returns the WSS article groups from search request.
   *
   * @param request the WSS article group search request
   * @return a list of {@link Page<WssArticleGroupDto>}
   */
  Page<WssArticleGroupDto> searchWssArticleGroup(WssArticleGroupSearchRequest request);

  /**
   * Returns article group leaf id by leaf id.
   *
   * @param leafId
   * @return the optional of {@link WssArticleGroupDocDto}
   */
  Optional<WssArticleGroupDocDto> findExactByArticleGroupLeafId(String leafId);

  /**
   * Returns the article group by id.
   *
   * @param articleGroupId
   * @return the optional of {@link WssArticleGroupDocDto}
   */
  Optional<WssArticleGroupDocDto> findExactByArticleGroupId(String articleGroupId);
}
