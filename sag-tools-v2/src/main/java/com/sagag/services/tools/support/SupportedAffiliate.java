package com.sagag.services.tools.support;

import lombok.AllArgsConstructor;
import lombok.Getter;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public enum SupportedAffiliate {

  DERENDINGER_CH("derendinger-ch", "Derendinger-Switzerland", "dch", "eshop-dch"),
  TECHNOMAG("technomag", "Technomag-Switzerland", "tm", "eshop-tm"),
  DERENDINGER_AT("derendinger-at", "Derendinger-Austria", "dat", "eshop-dat"),
  MATIK_AT("matik-at", "Matik-Austria", "mat", "eshop-mat"),
  MATIK_CH("matik-ch", "Matik-Switzerland", "mch", "eshop-mch"),
  WBB("wbb", "Walchli-Bollier-Bulach", "wb", "eshop-wbb"),
  RBE("rbe", "Remco-Belgium", "rbe", "eshop-rbe"),
  KLAUS("klaus", "Klaus-Switzerland", "kl", "eshop-klaus");

  private String affiliate;

  private String companyName;

  private String esShortName;

  private String salesOriginId;

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
    return isDch() || isTnm() || isMatikCh() || isWbb() || isRbe();
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
   * Constructs the supported affiliate from its description.
   *
   * @param desc the affiliate description
   * @return the supported affiliate
   */
  public static Optional<SupportedAffiliate> fromDesc(final String desc) {
    return Arrays.asList(values()).stream().filter(val -> StringUtils.equals(desc, val.getAffiliate())).findFirst();
  }

  public static SupportedAffiliate fromCompanyName(final String companyName) {
    return Arrays.asList(values()).stream().filter(val -> StringUtils.equalsIgnoreCase(companyName, val.companyName)).findFirst()
        .orElseThrow(() -> new NoSuchElementException("No support the given company name"));
  }

  public static List<SupportedAffiliate> getAtAffiliates() {
    return Arrays.asList(values()).stream().filter(SupportedAffiliate::isAtAffiliate).collect(Collectors.toList());
  }

  public static SupportedAffiliate fromCustomerNr(final String customerNr, final CountryCode countryCode) {
    switch (countryCode) {
      case AT:
        return customerNr.startsWith("1") ? SupportedAffiliate.DERENDINGER_AT : SupportedAffiliate.MATIK_AT;
      case CH:
        return getCHAffiliateByCustNr(customerNr);
      default:
        throw new NoSuchElementException("No support the given country name");
    }
  }

  private static SupportedAffiliate getCHAffiliateByCustNr(final String customerNr) {
    final Long custNr = Long.valueOf(customerNr);
    // #2473
    if (custNr < 4000000) {
      return SupportedAffiliate.DERENDINGER_CH;
    }
    if (custNr >= 4000000 && custNr < 5000000) {
      return SupportedAffiliate.TECHNOMAG;
    }
    if (custNr >= 5000000 && custNr < 6000000) {
      return SupportedAffiliate.WBB;
    }
    if (custNr >= 9000000) {
      return SupportedAffiliate.MATIK_CH;
    }
    throw new NoSuchElementException("No support the given customer nr");
  }
}
