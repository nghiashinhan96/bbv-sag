package com.sagag.services.common.enums;

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

/**
 * <p>The enumerations to define affiliate info.</p>
 *
 * <pre>
 * About esShortName:
 * should refer ticket #1515
 * Mapping: (n/a = not applicable - no mapping)
 * dch Derendinger-Switzerland
 * dat Derendinger-Austria
 * mat Matik-Austria
 * wb Walchli-Bollier-Bulach
 * tm Technomag-Switzerland
 * ifr n/a
 * rbe Remco-Belgium
 * as n/a
 * tmoci n/a
 * rwb n/a
 * mch Matik-Switzerland
 * mchoci n/a
 * ehdch n/a
 * ehman n/a
 * ehwal n/a
 * ehaub n/a
 * ehwb n/a
 * ivt n/a
 * ehapa n/a
 * ehalc n/a
 * </pre>
 */
@Getter
@AllArgsConstructor
public enum SupportedAffiliate {

  DERENDINGER_CH("derendinger-ch", "Derendinger-Switzerland", "dch", "derendinger", "eshop-dch", "DD", "03"),
  TECHNOMAG("technomag", "Technomag-Switzerland", "tm", "technomag", "eshop-tm", "TM", "04"),
  DERENDINGER_AT("derendinger-at", "Derendinger-Austria", "dat", "derendinger", "eshop-dat", "DD", "01"),
  WINT_SB("wt-sb", "Wint-Serbia", "wt-sb", "", "", "", ""),
  MATIK_AT("matik-at", "Matik-Austria", "mat", "matik", "eshop-mat", "MA", "02"),
  MATIK_CH("matik-ch", "Matik-Switzerland", "mch", "matik", "eshop-mch", "MA", "05"),
  WBB("wbb", "Walchli-Bollier-Bulach", "wbb", "wbb", "eshop-wbb", "WB", "06"),
  RBE("rbe", "Remco-Belgium", "rbe", "rbe", "eshop-rbe", "RE", StringUtils.EMPTY),
  KLAUS("klaus", "Klaus-Switzerland", "kl", "kl", "eshop-klaus", "KL", "07"),
  AUTONET_HUNGARY("autonet-hungary", "Autonet-Hungary", "ahu", "", "", "", ""),
  AUTONET_ROMANIA("autonet-romania", "Autonet-Romania", "aro", "", "", "", ""),
  AUTONET_SLOVAKIA("autonet-slovakia", "Autonet-Slovakia", "asl", "", "", "", ""),
  AUTONET_SLOVENIA("autonet-slovenia", "Autonet-Slovenia", "aso", "", "", "", ""),
  STAKIS_CZECH("st-cz", "Stakis-Czech", "st-cz", "", "", "", ""),
  SAG_CZECH("ax-cz", "Sag-CzechRepublic", "ax-cz", "", "sag-cz", "", "97");

  private String affiliate;

  private String companyName;

  private String esShortName;

  private String generalAffiliateName;

  private String salesOriginId;

  private String affiliateInitials;

  private String salesOrderPool;

  /**
   * Checks if the affiliate is technomag.
   *
   * @return true if the affiliate is technomag, false otherwise.
   */
  public boolean isTnm() {
    return this == TECHNOMAG;
  }

  /**
   * Checks if the affiliate is derendinger-ch.
   *
   * @return true if the affiliate is derendinger-ch, false otherwise.
   */
  public boolean isDch() {
    return this == DERENDINGER_CH;
  }

  /**
   * Checks if the affiliate is derendinger-at.
   *
   * @return true if the affiliate is derendinger-at, false otherwise.
   */
  public boolean isDat() {
    return this == DERENDINGER_AT;
  }

  /**
   * Checks if the affiliate is matik-at.
   *
   * @return true if the affiliate is matik-at, false otherwise.
   */
  public boolean isMatikAt() {
    return this == MATIK_AT;
  }

  /**
   * Checks if the affiliate is matik-ch.
   *
   * @return true if the affiliate is matik-ch, false otherwise.
   */
  public boolean isMatikCh() {
    return this == MATIK_CH;
  }

  /**
   * Checks if the affiliate is wbb.
   *
   * @return true if the affiliate is wbb, false otherwise.
   */
  public boolean isWbb() {
    return this == WBB;
  }

  /**
   * Checks if the affiliate is rbe.
   *
   * @return true if the affiliate is rbe, false otherwise.
   */
  public boolean isRbe() {
    return this == RBE;
  }

  /**
   * Checks if the affiliate is Klaus.
   *
   * @return true if the affiliate is klaus, false otherwise.
   */
  public boolean isKlaus() {
    return this == KLAUS;
  }

  /**
   * Checks if the affiliate is belonging Austria.
   *
   * @return true if the affiliate is belonging Austria, false otherwise.
   */
  public boolean isAtAffiliate() {
    return isMatikAt() || isDat();
  }

  /**
   * Checks if the affiliate is belonging CH.
   *
   * @return true if the affiliate is belonging CH, false otherwise.
   */
  public boolean isChAffiliate() {
    return isDch() || isTnm() || isMatikCh() || isWbb() || isRbe() || isKlaus();
  }

  public boolean isCzBasedAffiliate() {
    return isCzAffiliate() || isSagCzAffiliate();
  }

  public boolean isCzAffiliate() {
    return STAKIS_CZECH == this;
  }


  public boolean isSbAffiliate() {
    return WINT_SB == this;
  }

  public boolean isSagCzAffiliate() {
    return SAG_CZECH == this;
  }

  /**
   * Checks if the affiliate is connecting to Dvse Catalog.
   *
   * @return true if the affiliate is connecting to Dvse Catalog.
   */
  public boolean isDvseAffiliate() {
    return isMatikAt() || isMatikCh();
  }

  /**
   * Checks if the affiliate is belonging Price Discount Promotion mode
   *
   * @return true if the affiliate is Price Discount Promotion mode
   */
 public boolean isPdpAffiliate() {
     return isChAffiliate() || isAtAffiliate() || isSagCzAffiliate();
 }

 /**
  * Checks if the affiliate is support Availability Display mode
  *
  * @return true if the affiliate is Availability Display mode
  */
 public boolean isSupportAvailDisplayAffiliate() {
   return isChAffiliate() || isAtAffiliate() || isSagCzAffiliate();
 }

 public boolean isAutonetAffiliate() {
   return Stream.of(AUTONET_HUNGARY, AUTONET_ROMANIA, AUTONET_SLOVAKIA, AUTONET_SLOVENIA)
     .anyMatch(affiliate -> this == affiliate);
 }

  /**
   * Constructs the supported affiliate from its description.
   *
   * @param desc the affiliate description
   * @return the supported affiliate
   */
  public static SupportedAffiliate fromDesc(final String desc) {
    return fromByFilter(val -> StringUtils.equals(desc, val.getAffiliate()));
  }

  public static Optional<SupportedAffiliate> fromDescSafely(final String desc) {
    return fromByFilterSafely(val -> StringUtils.equals(desc, val.getAffiliate()));
  }

  public static SupportedAffiliate fromCompanyName(final String companyName) {
    return fromByFilter(val -> StringUtils.equalsIgnoreCase(companyName, val.companyName));
  }

  public static Optional<SupportedAffiliate> fromCompanyNameSafely(final String companyName) {
    return fromByFilterSafely(val -> StringUtils.equalsIgnoreCase(companyName, val.companyName));
  }

  public static SupportedAffiliate fromEsShortName(final String esShortName) {
    return fromByFilter(val -> StringUtils.equalsIgnoreCase(esShortName, val.esShortName));
  }

  private static SupportedAffiliate fromByFilter(Predicate<SupportedAffiliate> filter) {
    return fromByFilterSafely(filter)
        .orElseThrow(() -> new NoSuchElementException("No support the given company name"));
  }

  private static Optional<SupportedAffiliate> fromByFilterSafely(
      Predicate<SupportedAffiliate> filter) {
    return Arrays.asList(values()).stream().filter(filter).findFirst();
  }

}