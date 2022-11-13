package com.sagag.services.tools.repository.target;

import com.sagag.services.tools.domain.target.SupportedAffiliate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * Repository interfacing for {@link SupportedAffiliate}.
 */
public interface SupportedAffiliateRepository extends JpaRepository<SupportedAffiliate, Integer> {

  /**
   * Finds supported affiliates within country by short name.
   *
   * @param shortName the affiliate short name
   * @return the list of {@link SupportedAffiliate}
   */
  @Query("SELECT sa FROM SupportedAffiliate sa WHERE sa.countryId = "
      + "(SELECT ssa.countryId FROM SupportedAffiliate ssa WHERE ssa.shortName = :shortName)")
  List<SupportedAffiliate> findByWithinCountryOfShortName(@Param("shortName") String shortName);

  /**
   * Finds supported affiliates by short name.
   *
   * @param shortName the affiliate short name
   * @return the optional of {@link SupportedAffiliate}
   */
  Optional<SupportedAffiliate> findFirstByShortName(@Param("shortName") String shortName);

  @Query("SELECT sa.showPfandArticle FROM SupportedAffiliate sa WHERE sa.shortName = :shortName")
  Boolean isShowPfandArticleByShortName(@Param("shortName") String shortName);

  @Query(
      value = "select aff.noReplyEmail from SupportedAffiliate aff where aff.shortName= :shortName")
  Optional<String> findNoReplyEmailByShortName(@Param("shortName") String shortName);

  @Query("select c.shortName from SupportedAffiliate sa, Country c "
      + "where sa.countryId = c.id and sa.shortName = :shortName")
  String findCountryShortNameByAffShortName(@Param("shortName") String shortName);

  @Query(
      value = "select aff.shortName from SupportedAffiliate aff where aff.esShortName= :esShortName")
  Optional<String> findShortNameByEsShortName(@Param("esShortName") String esShortName);
}
