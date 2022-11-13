package com.sagag.services.copydb.repo.src;

import com.sagag.services.copydb.domain.src.InvoiceType;

import org.springframework.data.jpa.repository.JpaRepository;

public interface InvoiceTypeRepository extends JpaRepository<InvoiceType, Integer> {
}
