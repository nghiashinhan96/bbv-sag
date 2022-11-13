package com.sagag.eshop.repo.api;

import com.sagag.eshop.repo.entity.VUserExport;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface VUserExportRepository extends JpaRepository<VUserExport, Long>, JpaSpecificationExecutor<VUserExport> {

  List<VUserExport> findByOrgParentShortName(@Param("orgParentShortName") String orgParentShortName);

}
