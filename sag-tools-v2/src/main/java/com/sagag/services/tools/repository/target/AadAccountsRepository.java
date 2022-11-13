package com.sagag.services.tools.repository.target;


import com.sagag.services.tools.domain.target.AadAccounts;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AadAccountsRepository extends JpaRepository<AadAccounts, Integer> {

  List<AadAccounts> findByPrimaryContactEmail(String email);

}
