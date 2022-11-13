package com.sagag.eshop.repo.api;

import com.sagag.eshop.repo.entity.VinLogging;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface VinLoggingRepository extends JpaRepository<VinLogging, Integer> {

  /**
   * Returns the estimate used VIN log.
   *
   * @param vin the vin code
   * @param customerId the customer id
   * @return the found estimateId {@link java.lang.String}
   */
  @Query(value = "SELECT TOP 1 ESTIMATE_ID FROM VIN_LOGGING "
      + "WHERE CUSTOMER_ID = :customerId "
      + "AND ESTIMATE_ID IS NOT NULL "
      + "AND VIN = :vin", nativeQuery = true)
  Optional<String> findByEstimateUsed(@Param(value = "vin") String vin,
      @Param(value = "customerId") Long customerId);

  /**
   * Searches VinLogging by userIds.
   *
   * @param userIds the user id to search
   * @return The matched list of VinLogging
   */
  @Query("select v from VinLogging v " + "where v.userId IN :userIds")
  List<VinLogging> findByUserIds(@Param("userIds") List<Long> userIds);


  @Query(value = "SELECT TOP 1 * FROM VIN_LOGGING "
      + "WHERE CUSTOMER_ID = :customerId "
      + "AND ESTIMATE_ID IS NOT NULL "
      + "AND VIN = :vin", nativeQuery = true)
  Optional<VinLogging> findVinLogByEstimateUsed(@Param(value = "vin") String vin,
      @Param(value = "customerId") Long customerId);

  @Query(value = "SELECT * FROM VIN_LOGGING "
      + "WHERE CUSTOMER_ID = :customerId "
      + "AND ESTIMATE_ID IS NOT NULL "
      + "AND VIN = :vin "
      + "AND ESTIMATE_ID = :estimateId", nativeQuery = true)
  List<VinLogging> findAllByEstimateId(@Param(value = "vin") String vin,
      @Param(value = "customerId") Long customerId, @Param(value = "estimateId") String estimateId);
}
