package com.sagag.services.ivds.utils;

import com.sagag.services.common.utils.SagBeanUtils;
import com.sagag.services.domain.article.GenArtTxtDto;
import com.sagag.services.elasticsearch.domain.GenArtTxt;

import lombok.experimental.UtilityClass;

import java.util.Objects;

@UtilityClass
public class ArticlesGenArtUtils {

  public static GenArtTxtDto defaultGenArtTxtIfNull(GenArtTxt genArtTxt) {
    return Objects.isNull(genArtTxt) ? new GenArtTxtDto()
        : SagBeanUtils.map(genArtTxt, GenArtTxtDto.class);
  }
}
