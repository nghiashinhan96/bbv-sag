package com.sagag.services.dvse.api.impl;

import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.article.api.executor.ArticleSearchCriteria;
import com.sagag.services.article.api.executor.ArticleSearchExternalExecutor;
import com.sagag.services.common.enums.SupportedAffiliate;
import com.sagag.services.common.profiles.CzProfile;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.domain.eshop.dto.NextWorkingDates;
import com.sagag.services.dvse.api.UnicatArticleService;
import com.sagag.services.dvse.builder.UnicatArticleItemBuilder;
import com.sagag.services.dvse.wsdl.unicat.ArrayOfItem;
import com.sagag.services.dvse.wsdl.unicat.GetBackItems;
import com.sagag.services.dvse.wsdl.unicat.Item;
import com.sagag.services.dvse.wsdl.unicat.UnicatItem;
import com.sagag.services.hazelcast.api.ContextService;
import com.sagag.services.hazelcast.api.NextWorkingDateCacheService;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@CzProfile
public class UnicatArticleInformationImpl extends DvseProcessor implements UnicatArticleService {

  @Autowired
  private ArticleSearchExternalExecutor articleSearchExternalExecutor;

  @Autowired
  private UnicatArticleItemBuilder unicatArticleItemBuilder;

  @Autowired
  private NextWorkingDateCacheService nextWorkingDateCacheService;

  @Autowired
  private ContextService contextService;

  @Override
  public GetBackItems getArticleInformation(UserInfo userInfo, List<UnicatItem> itemsInOrder) {

    Map<String, Optional<Integer>> articleIdsAndQuantity = itemsInOrder.stream().collect(Collectors
        .toMap(UnicatItem::getArticleId, item -> Optional.of(item.getRequestedQuantity())));
    final SupportedAffiliate affiliate =
        SupportedAffiliate.fromCompanyName(userInfo.getCompanyName());
    final List<ArticleDocDto> articles = searchArticles(affiliate, articleIdsAndQuantity, userInfo.isSaleOnBehalf());

    ArticleSearchCriteria criteria = ArticleSearchCriteria.builder()
        .extCustomerId(userInfo.getExternalUserSession().getCustomerId())
        .language(userInfo.getLanguage()).extUsername(userInfo.getExternalUserSession().getUser())
        .extSecurityToken(userInfo.getExternalUserSession().getUid()).articles(articles)
        .updatePrice(true).updateAvailability(false).vatRate(userInfo.getSettings().getVatRate())
        .build();

    List<ArticleDocDto> updatedArticles = articleSearchExternalExecutor.execute(criteria);

    criteria.setUpdateAvailability(true);
    criteria.setUpdatePrice(false);
    List<ArticleDocDto> updatedArticlesWithAvail =
      articleSearchExternalExecutor.execute(criteria);

    updatedArticles.forEach(article -> {
      Optional<ArticleDocDto> updatedAvailArticle = updatedArticlesWithAvail.stream()
          .filter(art -> art.getId().equals(article.getId())).findFirst();
      if (updatedAvailArticle.isPresent()) {
        article.setAvailabilities(updatedAvailArticle.get().getAvailabilities());
      }
    });

    return buildUnicatGetBackItems(updatedArticles, userInfo, itemsInOrder);
  }

  private GetBackItems buildUnicatGetBackItems(List<ArticleDocDto> updatedArticles,
      UserInfo userInfo, List<UnicatItem> itemsInOrder) {
    final SupportedAffiliate affiliate =
        SupportedAffiliate.fromCompanyName(userInfo.getCompanyName());

    final String pickupBranchId = contextService.getPickupBranchId(userInfo);
    final NextWorkingDates nextWorkingDate =
        nextWorkingDateCacheService.get(userInfo, pickupBranchId);

    List<Item> responseItemInOrder = new ArrayList<>();
    itemsInOrder.forEach(it -> {
      Optional<ArticleDocDto> foundArt = updatedArticles.stream()
          .filter(art -> art.getArtid().equals(it.getArticleId())).findFirst();
      if (foundArt.isPresent()) {
        responseItemInOrder
            .add(unicatArticleItemBuilder.buildArticeItem(foundArt, affiliate, nextWorkingDate));
      } else {
        responseItemInOrder.add(unicatArticleItemBuilder.buildEmptyArticeItem(it.getArticleId(),
            affiliate, nextWorkingDate));
      }
    });
    final GetBackItems result = new GetBackItems();

    final ArrayOfItem resultItems = new ArrayOfItem();
    resultItems.getItem().addAll(responseItemInOrder);
    result.setItems(resultItems);

    return result;
  }

}
