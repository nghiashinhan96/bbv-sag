package com.sagag.eshop.service.api.impl.userhistory;

import com.sagag.eshop.repo.api.ArticleHistoryRepository;
import com.sagag.eshop.repo.api.UserArticleHistoryRepository;
import com.sagag.eshop.repo.api.userhistory.VUserArticleHistoryRepository;
import com.sagag.eshop.repo.criteria.user_history.UserArticleHistorySearchCriteria;
import com.sagag.eshop.repo.entity.ArticleHistory;
import com.sagag.eshop.repo.entity.UserArticleHistory;
import com.sagag.eshop.repo.specification.userhistory.VUserArticleHistorySpecification;
import com.sagag.eshop.service.api.UserArticleHistoryService;
import com.sagag.services.common.enums.ArticleSearchMode;
import com.sagag.services.common.enums.UserHistoryFromSource;
import com.sagag.services.common.utils.PageUtils;
import com.sagag.services.domain.eshop.dto.ArticleHistoryDto;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Optional;

@Service
public class UserArticleHistoryServiceImpl implements UserArticleHistoryService {

  @Autowired
  private UserArticleHistoryRepository userArtHistoryRepo;

  @Autowired
  private ArticleHistoryRepository articleHistoryRepo;

  @Autowired
  private VUserArticleHistoryRepository vUserArtHistoryRepo;

  @Override
  public Page<ArticleHistoryDto> searchArticleHistories(UserArticleHistorySearchCriteria criteria,
      Pageable pageable) {
    final VUserArticleHistorySpecification spec = new VUserArticleHistorySpecification(criteria);
    return vUserArtHistoryRepo.findAll(spec, pageable)
        .map(entity -> new ArticleHistoryDto(entity.getId(), entity.getArticleId(),
            entity.getArtnrDisplay(), entity.getSelectDate(), entity.getSearchTerm(),
            entity.getSearchMode(), entity.getFullName(), entity.getFromSource(),
            entity.getSearchTermWithArtNr()));
  }

  @Override
  public Page<ArticleHistoryDto> getLastestArticleHistory(long userId) {
    return vUserArtHistoryRepo.findTopArticleHistories(userId, PageUtils.DEF_PAGE);
  }

  @Override
  @Transactional
  public void addArticleHistory(long userId, String articleId, String articleNumber,
      String searchTerm, ArticleSearchMode searchMode, UserHistoryFromSource fromSource,
      String rawSearchTerm) {
    Long latestArtHistoryId = null;

    if (ArticleSearchMode.ARTICLE_ID == searchMode) {
      latestArtHistoryId = vUserArtHistoryRepo.findLatestBySearchTermAndArticleId(userId,
          rawSearchTerm, articleId, fromSource.name());
    } else {
      latestArtHistoryId = vUserArtHistoryRepo.findLatestBySearchTermAndNotSearchMode(userId,
          rawSearchTerm, ArticleSearchMode.ARTICLE_ID.name(), fromSource.name());
    }
    if (latestArtHistoryId != null) {
      updateUserSearchHistory(searchMode, latestArtHistoryId, searchTerm);
      return;
    }

    final ArticleHistory createdArtHistory = createArticleHistory(articleId, articleNumber);
    createUserArtHistory(userId, fromSource, createdArtHistory, searchTerm, searchMode,
        rawSearchTerm);
  }

  private void updateUserSearchHistory(ArticleSearchMode searchMode, Long latestArtHistoryId,
      String searchTerm) {
    Optional<UserArticleHistory> userArtHistoryOpt =
        userArtHistoryRepo.findById(latestArtHistoryId);
    if (!userArtHistoryOpt.isPresent()) {
      return;
    }
    UserArticleHistory userArtHistory = userArtHistoryOpt.get();
    userArtHistory.setSearchMode(searchMode);
    userArtHistory.setSearchTerm(searchTerm);
    userArtHistory.setSelectDate(Calendar.getInstance().getTime());
    userArtHistoryRepo.save(userArtHistory);
  }

  @Override
  @Transactional
  public void updateArticleHistorySelectDate(long userId, long userArticleHistoryId) {
    Optional<UserArticleHistory> userArticleHistoryOpt =
        userArtHistoryRepo.findByIdAndUserId(userArticleHistoryId, userId);
    if (!userArticleHistoryOpt.isPresent()) {
      return;
    }
    UserArticleHistory userArtHistory = userArticleHistoryOpt.get();
    userArtHistory.setSelectDate(Calendar.getInstance().getTime());
    userArtHistoryRepo.save(userArtHistory);
  }

  private ArticleHistory createArticleHistory(String articleId, String articleNumber) {
    final ArticleHistory artHistory =
        ArticleHistory.builder().articleId(articleId).articleNumber(articleNumber).build();
    return articleHistoryRepo.save(artHistory);
  }

  private void createUserArtHistory(long userId, UserHistoryFromSource fromSource,
      ArticleHistory artHistory, String searchTerm, ArticleSearchMode searchMode,
      String rawSearchTerm) {
    final String updatedSearchTerm = StringUtils.defaultIfBlank(searchTerm, null);

    final UserArticleHistory userArtHistory = UserArticleHistory.builder().userId(userId)
        .articleHistory(artHistory).searchTerm(updatedSearchTerm).searchMode(searchMode)
        .fromSource(fromSource).rawSearchTerm(rawSearchTerm).build();
    userArtHistory.setSelectDate(Calendar.getInstance().getTime());

    userArtHistoryRepo.save(userArtHistory);
  }

}
