package com.sagag.eshop.repo.api;

import com.sagag.eshop.repo.entity.Branch;
import com.sagag.eshop.repo.entity.DeliveryProfile;
import com.sagag.services.domain.eshop.dto.externalvendor.DeliveryProfileDto;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DeliveryProfileRepository
    extends JpaRepository<DeliveryProfile, Integer>, JpaSpecificationExecutor<DeliveryProfile> {

  @Query(value = "select distinct new com.sagag.services.domain.eshop.dto.externalvendor.DeliveryProfileDto("
      + "de.deliveryProfileId,de.deliveryProfileName) " 
      + "from DeliveryProfile de "
      + "where deliveryProfileName is not null")
  List<DeliveryProfileDto> findDeliveryProfileName();

  @Query(value = "select distinct new com.sagag.eshop.repo.entity.Branch("
      + "de.deliveryBranchId,br.branchCode) " 
      + "from DeliveryProfile de " + "left join Branch br "
      + "on de.deliveryBranchId = br.branchNr")
  List<Branch> findBranchCodeByDeliveryBranchId();


  @Query(value = "select distinct new com.sagag.eshop.repo.entity.Branch("
      + "de.distributionBranchId,br.branchCode) " 
      + "from DeliveryProfile de "
      + "left join Branch br " + "on de.distributionBranchId = br.branchNr")
  List<Branch> findBranchCodeByDistributionBranchId();
}
