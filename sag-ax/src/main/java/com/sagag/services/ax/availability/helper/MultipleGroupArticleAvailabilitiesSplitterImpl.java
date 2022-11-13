package com.sagag.services.ax.availability.helper;

import com.sagag.services.article.api.availability.MultipleGroupArticleAvailabilitiesSplitter;
import com.sagag.services.article.api.utils.AvailabilityFilterUtils;
import com.sagag.services.common.profiles.AxProfile;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.domain.sag.erp.Availability;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.SerializationUtils;
import org.springframework.stereotype.Component;

@Component
@AxProfile
public class MultipleGroupArticleAvailabilitiesSplitterImpl
    implements MultipleGroupArticleAvailabilitiesSplitter {

  @Override
  public void splitAvailabilities(List<ArticleDocDto> articles) {
    List<Availability> totalAvails = articles.stream().findAny()
        .map(ArticleDocDto::getAvailabilities).orElse(Collections.emptyList());

    totalAvails.sort(Comparator.comparing(Availability::isExternalSource));
    articles.forEach(art -> art.setAvailabilities(Collections.emptyList()));
    articles.forEach(art -> findAvailForArticle(art, totalAvails));
    articles.forEach(art -> Collections.sort(art.getAvailabilities(),
        AvailabilityFilterUtils.sortByArrivalTimeIfExist()));
  }

  private void findAvailForArticle(ArticleDocDto art, List<Availability> remainingAvails) {
    if (Objects.isNull(art) || CollectionUtils.isEmpty(remainingAvails)) {
      return;
    }
    List<Availability> clonedRemainingAvails =
        remainingAvails.stream().map(SerializationUtils::clone).collect(Collectors.toList());

    for (int i = 0; i <= clonedRemainingAvails.size() - 1; i++) {
      List<Availability> artAvails = art.getAvailabilities();
      if (CollectionUtils.isEmpty(artAvails)) {
        artAvails = new ArrayList<>();
      }
      int filledQuantity = getAvailTotalQuantity(artAvails);
      int neededQuantity = art.getAmountNumber() - filledQuantity;
      if (neededQuantity == 0) {
        return;
      }

      Availability avail = clonedRemainingAvails.get(i);
      int quantity = avail.getQuantity();
      if (quantity == neededQuantity) {
        artAvails.add(avail);
        art.setAvailabilities(artAvails);

        findAvailIndex(avail, remainingAvails)
            .ifPresent(usedAvailIndex -> remainingAvails.remove((int) usedAvailIndex));
        return;
      }

      if (quantity > neededQuantity) {
        Availability splitAvail = SerializationUtils.clone(avail);
        splitAvail.setQuantity(neededQuantity);
        artAvails.add(splitAvail);
        art.setAvailabilities(artAvails);

        findAvailIndex(avail, remainingAvails).ifPresent(usedAvailIndex -> {
          Availability usedAvail = remainingAvails.get(usedAvailIndex);
          usedAvail.setQuantity(quantity - neededQuantity);
        });
        return;
      }

      artAvails.add(avail);
      art.setAvailabilities(artAvails);
      findAvailIndex(avail, remainingAvails)
          .ifPresent(usedAvailIndex -> remainingAvails.remove((int) usedAvailIndex));
    }
  }

  private Optional<Integer> findAvailIndex(Availability avail, List<Availability> avails) {
    for (int i = 0; i <= avails.size(); i++) {
      if (equalAvail(avail, avails.get(i))) {
        return Optional.of(i);
      }
    }
    return Optional.empty();
  }

  private boolean equalAvail(Availability avail1, Availability avail2) {
    return avail1.getQuantity().equals(avail2.getQuantity())
        && avail1.getArrivalTime().equals(avail2.getArrivalTime())
        && avail1.isExternalSource() == avail2.isExternalSource()
        && avail1.isVenExternalSource() == avail2.isVenExternalSource();
  }

  private int getAvailTotalQuantity(List<Availability> avails) {
    if (CollectionUtils.isEmpty(avails)) {
      return 0;
    }
    return avails.stream().collect(Collectors.summingInt(Availability::getQuantity));
  }
}
