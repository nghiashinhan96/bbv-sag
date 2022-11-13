package com.sagag.services.copydb.steps;

import com.sagag.services.copydb.config.CopyDbProfile;
import com.sagag.services.copydb.domain.src.ArticleHistory;
import com.sagag.services.copydb.domain.dest.DestArticleHistory;

import org.dozer.DozerBeanMapper;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@CopyDbProfile
public class ArticleHistoryProcessor implements ItemProcessor<ArticleHistory, DestArticleHistory> {

  @Autowired
  @Qualifier(value = "dozerBeanMapper")
  private DozerBeanMapper dozerBeanMapper;

  @Override
  public DestArticleHistory process(ArticleHistory item) throws Exception {
    return dozerBeanMapper.map(item, DestArticleHistory.class);
  }
}
