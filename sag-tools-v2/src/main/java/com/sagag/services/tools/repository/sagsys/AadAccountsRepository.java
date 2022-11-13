package com.sagag.services.tools.repository.sagsys;


import com.sagag.services.tools.domain.sagsys.AadAccounts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AadAccountsRepository extends JpaRepository<AadAccounts, Integer> {

}
