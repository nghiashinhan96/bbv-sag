package com.sagag.services.tools.repository.target;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sagag.services.tools.domain.target.DeliveryType;

import java.util.Optional;

public interface DeliveryTypesRepository extends JpaRepository<DeliveryType, Integer> {

  Optional<DeliveryType> findOneById(int id);

  Optional<DeliveryType> findOneByDescCode(String descCode);

  @Query(value = "select d.id from DeliveryType d where d.descCode = :descCode")
  Optional<Integer> findIdByDescCode(@Param("descCode") String descCode);

  @Query(value = "select d.id from DeliveryType d where d.descCode = :descCode")
  Integer findIdByType(@Param("descCode") String descCode);
}
