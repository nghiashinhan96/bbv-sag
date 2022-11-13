package com.sagag.eshop.repo.api;

import com.sagag.eshop.repo.entity.SupportedAffiliate;
import com.sagag.services.domain.eshop.message.dto.SupportedAffiliateDto;

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

  @Query(value = "select new com.sagag.services.domain.eshop.message.dto.SupportedAffiliateDto"
      + "(aff.id, aff.companyName, aff.shortName) from SupportedAffiliate aff")
  List<SupportedAffiliateDto> findAllSupportedAffiliate();

  @Query(
      value = "select aff.noReplyEmail from SupportedAffiliate aff where aff.shortName= :shortName")
  Optional<String> findNoReplyEmailByShortName(@Param("shortName") String shortName);

  @Query("select c.shortName from SupportedAffiliate sa, Country c "
      + "where sa.countryId = c.id and sa.shortName = :shortName")
  String findCountryShortNameByAffShortName(@Param("shortName") String shortName);

  @Query(
      value = "select aff.shortName from SupportedAffiliate aff where aff.esShortName= :esShortName")
  Optional<String> findShortNameByEsShortName(@Param("esShortName") String esShortName);

  /**
   * Finds supported affiliates within country by short name.
   *
   * @param shortCode the country short code
   * @return the list of {@link SupportedAffiliate}
   */
  @Query("SELECT sa.companyName FROM SupportedAffiliate sa WHERE sa.countryId = "
    + "(SELECT c.id FROM Country c WHERE c.shortCode = :shortCode)")
  List<String> findCompanyNameByCountryShortCode(@Param("shortCode") String shortCode);


  @Query("SELECT sa.shortName FROM SupportedAffiliate sa WHERE sa.companyName = :companyName")
  Optional<String> findShortNameByCompanyName(@Param("companyName") String companyName);
}
