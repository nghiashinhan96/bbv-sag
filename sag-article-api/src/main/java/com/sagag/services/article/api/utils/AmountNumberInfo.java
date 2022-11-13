package com.sagag.services.article.api.utils;

import lombok.Data;

import org.apache.commons.collections.CollectionUtils;

import java.util.List;

/**
 * Class to contain amount number info to calculate sales quantity.
 *
 */
@Data
public class AmountNumberInfo {

  private int defaultAmountNumber;

  private List<String> genArts;

  public AmountNumberInfo(List<String> genArts) {
    this.genArts = genArts;
  }

  public AmountNumberInfo(int defaultAmountNumber, List<String> genArts) {
    this.defaultAmountNumber = defaultAmountNumber;
    this.genArts = genArts;
  }

  public boolean containsGenArt(String genArt) {
    if (CollectionUtils.isEmpty(genArts)) {
      return false;
    }
    return genArts.contains(genArt);
  }

}
