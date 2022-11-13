package com.sagag.eshop.repo.api.legal_term;

import com.sagag.eshop.repo.entity.legal_term.LegalDoctype;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface LegalDoctypeRepository extends JpaRepository<LegalDoctype, Long>, JpaSpecificationExecutor<LegalDoctype> {
}
