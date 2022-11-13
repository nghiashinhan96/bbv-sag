package com.sagag.services.tools.support.elasticsearch.indices;

import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.i18n.LocaleContextHolder;

@FunctionalInterface
public interface IMultilingualIndex {

  String index();

  default String findIndex(IIndexLang[] indices) {
    if (ArrayUtils.getLength(indices) == 1) {
      return findIndexVal(indices);
    }
    return findIndexVal(indices) + '_' +
      findLangOpt(indices).map(IIndexLang::lang).orElse(defaultLang());
  }

  default String defaultLang() {
    return "de";
  }

  static String findIndexVal(IIndexLang[] indices) {
    return indices[0].index();
  }

  static Optional<IIndexLang> findLangOpt(IIndexLang[] indices) {
    return Stream.of(indices).filter(langPredicate()).findFirst();
  }

  static Predicate<IIndexLang> langPredicate() {
    return index -> StringUtils.equalsIgnoreCase(index.lang(),
      LocaleContextHolder.getLocale().getLanguage());
  }

}
