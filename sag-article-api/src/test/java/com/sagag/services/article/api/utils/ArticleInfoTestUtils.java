package com.sagag.services.article.api.utils;

import com.sagag.services.common.utils.SagJSONUtil;
import com.sagag.services.domain.article.ArticleDocDto;

import lombok.experimental.UtilityClass;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;

@UtilityClass
public final class ArticleInfoTestUtils {

  public static ArticleDocDto initArticleDocDto() throws IOException {
    final File file =
        new File(FilenameUtils.separatorsToSystem("src/test/resources/article/1000082238.json"));
    final ArticleDocDto article = SagJSONUtil
        .convertJsonToObject(FileUtils.readFileToString(file, "UTF-8"), ArticleDocDto.class);
    return article;
  }
}
