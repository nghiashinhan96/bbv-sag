package com.sagag.services.tools.support.elasticsearch.indices;

public enum BranchIndexLang implements IIndexLang {

  DE;

  @Override
  public String index() {
    return "branches";
  }

  @Override
  public String lang() {
    return lang(name());
  }
}
