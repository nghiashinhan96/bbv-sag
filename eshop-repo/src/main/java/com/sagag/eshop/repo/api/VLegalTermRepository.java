package com.sagag.eshop.repo.api;

import com.sagag.eshop.repo.cache.RepoCacheMaps;
import com.sagag.eshop.repo.entity.VLegalTerm;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * Jpa repository for legal term view.
 */
public interface VLegalTermRepository
    extends JpaRepository<VLegalTerm, Long>, JpaSpecificationExecutor<VLegalTerm> {

    @Cacheable(cacheNames = RepoCacheMaps.LEGAL_DOC_BY_CUSTOMER_ID_AND_LANGUGE_AND_STATUS,
            unless = RepoCacheMaps.RESULT_IS_NULL)
    List<VLegalTerm> findAllByCustomerIdAndLanguageAndStatus(Long customerId, String lang, int status);
}
