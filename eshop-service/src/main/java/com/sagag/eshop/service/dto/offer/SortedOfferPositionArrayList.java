package com.sagag.eshop.service.dto.offer;

import com.sagag.services.common.utils.SagStringUtils;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.ArrayList;
import java.util.Collections;

public class SortedOfferPositionArrayList extends ArrayList<OfferPositionDto> {

  private static final long serialVersionUID = 5696955325739852720L;

  @Override
  public boolean add(final OfferPositionDto offerPosition) {
    super.add(offerPosition);
    Collections.sort(this, (position1, position2) -> {
      final String artDesc1 = position1.getArticleDescription();
      final String artDesc2 = position2.getArticleDescription();
      if (StringUtils.isBlank(artDesc1) || StringUtils.isBlank(artDesc2)) {
        return NumberUtils.INTEGER_ZERO;
      }
      return SagStringUtils.prepareForCompare(artDesc1)
          .compareTo(SagStringUtils.prepareForCompare(artDesc2));
    });
    return true;
  }

}
