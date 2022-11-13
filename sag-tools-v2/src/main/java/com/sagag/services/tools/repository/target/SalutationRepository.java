package com.sagag.services.tools.repository.target;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sagag.services.tools.domain.target.Salutation;

import java.util.List;
import java.util.Optional;

public interface SalutationRepository extends JpaRepository<Salutation, Integer> {

  Optional<Salutation> findOneByCode(String salutationCode);

  @Query(value = "select e from Salutation e where e.type = :type")
  List<Salutation> findAllByType(@Param("type") final String type);
}
