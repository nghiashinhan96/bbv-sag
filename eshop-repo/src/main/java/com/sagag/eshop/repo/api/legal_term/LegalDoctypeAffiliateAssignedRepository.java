package com.sagag.eshop.repo.api.legal_term;

import com.sagag.eshop.repo.entity.legal_term.LegalDoctypeAffiliateAssigned;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface LegalDoctypeAffiliateAssignedRepository extends JpaRepository<LegalDoctypeAffiliateAssigned, Long>, JpaSpecificationExecutor<LegalDoctypeAffiliateAssigned> {
}
