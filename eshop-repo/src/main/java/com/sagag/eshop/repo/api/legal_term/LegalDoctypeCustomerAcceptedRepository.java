package com.sagag.eshop.repo.api.legal_term;

import com.sagag.eshop.repo.cache.RepoCacheMaps;
import com.sagag.eshop.repo.entity.legal_term.LegalDoctypeCustomerAccepted;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface LegalDoctypeCustomerAcceptedRepository extends JpaRepository<LegalDoctypeCustomerAccepted, Long>, JpaSpecificationExecutor<LegalDoctypeCustomerAccepted> {
    Optional<LegalDoctypeCustomerAccepted> findByLegalDoctypeIdAndCustomerId(@Param("legalDoctypeId") Long legalDoctypeId, @Param("customerId") Long customerId);

    @Override
    @CacheEvict(cacheNames = RepoCacheMaps.LEGAL_DOC_BY_CUSTOMER_ID_AND_LANGUGE_AND_STATUS)
    <S extends LegalDoctypeCustomerAccepted> S save(S entity);

    @Override
    @CacheEvict(cacheNames = RepoCacheMaps.LEGAL_DOC_BY_CUSTOMER_ID_AND_LANGUGE_AND_STATUS,
    allEntries = true)
    <S extends LegalDoctypeCustomerAccepted> List<S> saveAll(Iterable<S> entities);
    
    
}
