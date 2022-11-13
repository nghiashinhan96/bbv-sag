package com.sagag.services.elasticsearch.parser;

import com.sagag.services.elasticsearch.query.vehicles.builder.VehicleFreetextQueryStrParser;
import com.sagag.services.elasticsearch.utils.QueryUtils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

@Component
public class VehicleFreetextStringParser implements ISearchStringParser {

  @Override
  public String apply(String text, Object[] objects) {
    List<Function<String, String>> parsers = new LinkedList<>();
    parsers.add(t -> StringUtils.replacePattern(t, QueryUtils.SLASH_BACKSLASH_PATTERN,
            StringUtils.SPACE));
    parsers.addAll(QueryUtils.getEsSpecialCharacterParsers());
    parsers.add(VehicleFreetextQueryStrParser::parseQueryContainEngineCode);
    return applyParsers(text, parsers);
  }

}
