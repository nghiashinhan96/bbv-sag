package com.sagag.services.elasticsearch.parser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ArticleFreetextStringParser implements ISearchStringParser {

  @Autowired
  private FreetextStringParser freetextStringParser;
  
  @Override
  public String apply(String freetext, Object[] objs) {
    return freetextStringParser.apply(freetext, objs);
  }

}