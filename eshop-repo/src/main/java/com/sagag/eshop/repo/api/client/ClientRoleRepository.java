package com.sagag.eshop.repo.api.client;

import com.sagag.eshop.repo.entity.client.ClientRole;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ClientRoleRepository extends JpaRepository<ClientRole, Long> {

  @Query("select cr.roleId from ClientRole cr where cr.clientId = :clientId")
  List<Integer> findRoleIdByClientId(@Param("clientId") int clientId);

}
