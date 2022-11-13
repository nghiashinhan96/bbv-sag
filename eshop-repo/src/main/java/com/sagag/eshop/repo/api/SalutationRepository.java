package com.sagag.eshop.repo.api;

import com.sagag.eshop.repo.entity.Salutation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SalutationRepository extends JpaRepository<Salutation, Integer> {

  Optional<Salutation> findOneByCode(String salutationCode);

  @Query(value = "select e from Salutation e where e.type = :type")
  List<Salutation> findAllByType(@Param("type") final String type);

  List<Salutation> findByCodeIn(List<String> codes);
}
