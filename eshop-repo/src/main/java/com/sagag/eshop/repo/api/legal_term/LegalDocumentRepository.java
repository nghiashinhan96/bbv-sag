package com.sagag.eshop.repo.api.legal_term;

import com.sagag.eshop.repo.entity.legal_term.LegalDocument;
import java.util.List;
import java.util.Map;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LegalDocumentRepository
  extends JpaRepository<LegalDocument, Long>, JpaSpecificationExecutor<LegalDocument> {

  @Query(value = "SELECT ld.DOCUMENT_NAME AS NAME, ld.SUMMARY AS SUMMARY, ld.DOCUMENT AS CONTENT, ld.PDF_URL AS PDF_URL, " +
    "CAST(ldt.ID as varchar) AS TERM_ID, CAST(ldcal.USER_ID as varchar) AS USER_ID, CONVERT(varchar(30), ldcal.TIME_ACCEPTED, 126) AS TIME_ACCEPTED, CONVERT(varchar(30), ldaal.DATE_VALID_FROM, 126)  AS DATE_VALID_FROM, " +
    "CAST(ldaal.ACCEPTANCE_PERIOD_DAYS as varchar) AS ACCEPTANCE_PERIOD_DAYS, CAST(ldaal.SORT as varchar) AS SORT   " +
    "FROM LEGAL_DOCUMENT ld  " +
    "INNER JOIN LEGAL_DOCTYPE ldt ON ld.LEGAL_DOCTYPE_ID = ldt.ID  " +
    "INNER JOIN LEGAL_DOCTYPE_CUSTOMER_ACCEPTED_LOG ldcal ON ldcal.LEGAL_DOCTYPE_ID = ldt.ID  " +
    "INNER JOIN LEGAL_DOCTYPE_AFFILIATE_ASSIGNED_LOG ldaal ON ldaal.AFFILIATE_ID = ldcal.AFFILIATE_ID AND ldaal.LEGAL_DOCTYPE_ID = ldcal.LEGAL_DOCTYPE_ID " +
    "WHERE ldcal.CUSTOMER_ID = :customerId AND ld.LANGUAGE = :lang AND ldaal.STATUS = :status"
    , nativeQuery = true)
  List<Map<String, Object>> findByCustomerIdAndLanguageAndStatus(@Param("customerId") Long customerId, @Param("lang") String lang, @Param("status") int status);
}
