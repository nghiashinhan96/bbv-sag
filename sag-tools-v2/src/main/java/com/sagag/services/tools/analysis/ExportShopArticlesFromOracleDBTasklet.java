package com.sagag.services.tools.analysis;

import com.sagag.services.tools.batch.AbstractTasklet;
import com.sagag.services.tools.config.OracleProfile;
import com.sagag.services.tools.domain.csv.CsvShopArticle;
import com.sagag.services.tools.domain.source.SourceShopArticle;
import com.sagag.services.tools.exception.BatchJobException;
import com.sagag.services.tools.repository.source.SourceShopArticleRepository;
import com.sagag.services.tools.support.Folders;
import com.sagag.services.tools.utils.CsvUtils;
import com.sagag.services.tools.utils.ToolConstants;

import lombok.extern.slf4j.Slf4j;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 *
 */
@Component
@OracleProfile
@Slf4j
public class ExportShopArticlesFromOracleDBTasklet extends AbstractTasklet {

  private static final String FILE_NAME = "SHOP_ARTICLE_";

  @Autowired(required = false)
  private SourceShopArticleRepository sourceShopArticleRepo;

  @Override
  public RepeatStatus execute(StepContribution contribution,
    ChunkContext chunkContext) throws BatchJobException {

    int size = ToolConstants.MAX_SIZE;
    long total = sourceShopArticleRepo.count();
    int totalPage = (int) total / size;

    log.info("Total record = {} - total page = {}", total, totalPage);
    for (int curPage = 0; curPage < totalPage; curPage++) {
      Page<SourceShopArticle> shopArticles = sourceShopArticleRepo.findAll(PageRequest.of(curPage, size));
      if (!shopArticles.hasContent()) {
        continue;
      }

      final List<CsvShopArticle> csvShopArticleList = shopArticles.getContent().stream().map(entity -> {
        CsvShopArticle csvShopArticle = new CsvShopArticle();
        BeanUtils.copyProperties(entity, csvShopArticle);
        return csvShopArticle;
      }).collect(Collectors.toList());

      String filePath = Folders.EXPORT_SHOP_ARTICLE_DIR_PATH.getPath()
        + FILE_NAME + curPage + ToolConstants.CSV_SUFFIX;
      CsvUtils.write(filePath, csvShopArticleList);
    }

    return finish(contribution);
  }
}
