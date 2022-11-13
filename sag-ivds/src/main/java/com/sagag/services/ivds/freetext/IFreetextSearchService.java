package com.sagag.services.ivds.freetext;

import com.sagag.services.elasticsearch.enums.FreetextSearchOption;
import com.sagag.services.ivds.request.FreetextSearchRequest;
import com.sagag.services.ivds.response.FreetextResponseDto;

import java.util.List;

public interface IFreetextSearchService {

  void search(FreetextSearchRequest request, FreetextResponseDto response);

  boolean support(List<String> options);

  default FreetextSearchOption getSearchOption(List<String> options) {
    if (options.contains(FreetextSearchOption.ARTICLES.lowerCase())) {
      return FreetextSearchOption.ARTICLES;
    }
    if (options.contains(FreetextSearchOption.ARTICLES_DESC.lowerCase())) {
      return FreetextSearchOption.ARTICLES_DESC;
    }
    if (options.contains(FreetextSearchOption.VEHICLES.lowerCase())) {
      return FreetextSearchOption.VEHICLES;
    }
    if (options.contains(FreetextSearchOption.VEHICLES_MOTOR.lowerCase())) {
      return FreetextSearchOption.VEHICLES_MOTOR;
    }
    throw new IllegalArgumentException("Not supported the search options");
  }
}
