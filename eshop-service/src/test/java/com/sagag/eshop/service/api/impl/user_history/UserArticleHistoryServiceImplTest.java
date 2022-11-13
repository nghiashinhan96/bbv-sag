package com.sagag.eshop.service.api.impl.user_history;

import com.google.common.collect.Lists;
import com.sagag.eshop.repo.api.ArticleHistoryRepository;
import com.sagag.eshop.repo.api.UserArticleHistoryRepository;
import com.sagag.eshop.repo.api.userhistory.VUserArticleHistoryRepository;
import com.sagag.eshop.repo.entity.ArticleHistory;
import com.sagag.eshop.repo.entity.UserArticleHistory;
import com.sagag.eshop.service.api.impl.userhistory.UserArticleHistoryServiceImpl;
import com.sagag.services.common.enums.ArticleSearchMode;
import com.sagag.services.common.enums.UserHistoryFromSource;
import com.sagag.services.common.utils.PageUtils;
import com.sagag.services.domain.eshop.dto.ArticleHistoryDto;
import java.util.Optional;
import java.util.Random;
import org.hamcrest.Matchers;
import org.hamcrest.core.Is;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class UserArticleHistoryServiceImplTest {

  @InjectMocks
  private UserArticleHistoryServiceImpl service;

  @Mock
  private UserArticleHistoryRepository userArtHistoryRepo;

  @Mock
  private ArticleHistoryRepository articleHistoryRepo;

  @Mock
  private VUserArticleHistoryRepository vUserArtHistoryRepo;

  @Test
  public void testAddArtHistory() {
    String articleId = "1000055887";
    String articleNumber = "OC 90";
    final ArticleHistory articleHistory = initArticleHistory(articleId, articleNumber);
    String searchTerm = "oc90";
    String rawSearchTerm = "oc90";
    int userId = 340609;
    final UserHistoryFromSource fromSource = UserHistoryFromSource.C4C;
    ArticleSearchMode searchMode = ArticleSearchMode.FREE_TEXT;
    final UserArticleHistory userArtHistory =
        initUserArtHistory(articleHistory, searchTerm, userId, fromSource, searchMode);

    Mockito.when(articleHistoryRepo.save(Mockito.any())).thenReturn(articleHistory);
    Mockito.when(userArtHistoryRepo.findById(Mockito.any()))
      .thenReturn(Optional.of(userArtHistory));
    Mockito.when(userArtHistoryRepo.save(Mockito.any())).thenReturn(userArtHistory);

    service.addArticleHistory(userId, articleId, articleNumber, searchTerm, searchMode, fromSource,
        rawSearchTerm);

    Mockito.verify(articleHistoryRepo, times(ArticleSearchMode.ARTICLE_ID == searchMode ? 1 : 0))
      .save(Mockito.any());
    Mockito.verify(userArtHistoryRepo, times(1)).findById(Mockito.any());
    Mockito.verify(userArtHistoryRepo, times(1)).save(Mockito.any());
  }

  @Test
  public void testGetLastestArticleHistory() {
    String articleId = "1000055887";
    String articleNumber = "OC 90";
    String searchTerm = "oc90";
    long userId = 340609;
    ArticleSearchMode searchMode = ArticleSearchMode.FREE_TEXT;
    ArticleHistoryDto articleHistoryDto = ArticleHistoryDto.builder().articleId(articleId)
        .searchTerm(searchTerm).artnrDisplay(articleNumber).searchMode(searchMode.name()).build();
    Page<ArticleHistoryDto> userArtHistory =
        new PageImpl<ArticleHistoryDto>(Lists.newArrayList(articleHistoryDto));

    Mockito.when(vUserArtHistoryRepo.findTopArticleHistories(Mockito.anyLong(), Mockito.any()))
        .thenReturn(userArtHistory);
    Page<ArticleHistoryDto> lastestArticleHistory = service.getLastestArticleHistory(userId);

    Mockito.verify(vUserArtHistoryRepo, times(1)).findTopArticleHistories(userId,
        PageUtils.DEF_PAGE);
    Assert.assertNotNull(lastestArticleHistory);
    Assert.assertThat(lastestArticleHistory.getContent(), Matchers.notNullValue());
    Assert.assertThat(lastestArticleHistory.getContent().size(), Is.is(1));
  }

  @Test
  public void testUpdateArticleHistorySelectDate() {
    final Long userId = new Random().nextLong();
    final Long userArticleHistoryId = new Random().nextLong();
    when(userArtHistoryRepo.findByIdAndUserId(Mockito.anyLong(), Mockito.anyLong()))
        .thenReturn(Optional.of(new UserArticleHistory()));
    when(userArtHistoryRepo.save(Mockito.any(UserArticleHistory.class)))
        .thenAnswer(i -> i.getArguments()[0]);
    service.updateArticleHistorySelectDate(userId, userArticleHistoryId);

    verify(userArtHistoryRepo, Mockito.times(1)).findByIdAndUserId(Mockito.anyLong(),
        Mockito.anyLong());
    verify(userArtHistoryRepo, Mockito.times(1)).save(Mockito.any(UserArticleHistory.class));
  }

  private UserArticleHistory initUserArtHistory(final ArticleHistory articleHistory,
      String searchTerm, int userId, final UserHistoryFromSource fromSource,
      ArticleSearchMode searchMode) {
    return UserArticleHistory.builder().userId(userId).articleHistory(articleHistory)
        .searchTerm(searchTerm).searchMode(searchMode).fromSource(fromSource).build();
  }

  private ArticleHistory initArticleHistory(String articleId, String articleNumber) {
    return ArticleHistory.builder().articleId(articleId).articleNumber(articleNumber).build();
  }

}
