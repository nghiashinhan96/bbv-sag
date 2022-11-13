package com.sagag.eshop.repo.api.order;

import com.sagag.eshop.repo.entity.order.VCustomerOrderHistory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Temporal;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

import javax.persistence.TemporalType;

/**
 * VCustomerOrderHistory JPA repository interface.
 */
public interface VCustomerOrderHistoryRepository extends JpaRepository<VCustomerOrderHistory, Long> {

  /**
   * Searches order history by customerNumber, dateFrom, and dateTo.
   *
   * @param customerNumber the customer number to search
   * @param dateFrom the dateFrom for matching orders
   * @param dateTo the e dateTo for matching orders
   * @return The matched list of VCustomerOrderHistory
   */
  @Query("select o from VCustomerOrderHistory o "
        + "where (o.customerNumber = :customerNumber) "
        + "and (:dateFrom is null or o.createdDate > :dateFrom) "
        + "and (:dateTo is null or o.createdDate < :dateTo) "
      )
  List<VCustomerOrderHistory> findOrderByCustomerAndDate(
      @Param("customerNumber") Long customerNumber,
      @Param("dateFrom") @Temporal(TemporalType.DATE) Date dateFrom,
      @Param("dateTo") @Temporal(TemporalType.DATE) Date dateTo);

  /**
   * Searches order history by userId, dateFrom, and dateTo.
   *
   * @param userId the user id to search
   * @param dateFrom the dateFrom for matching orders
   * @param dateTo the e dateTo for matching orders
   * @return The matched list of VCustomerOrderHistory
   */
  @Query("select o from VCustomerOrderHistory o "
      + "where (o.userId = :userId) "
      + "and (:dateFrom is null or o.createdDate > :dateFrom) "
      + "and (:dateTo is null or o.createdDate < :dateTo) "
    )
  List<VCustomerOrderHistory> findOrderByUserAndDate(
      @Param("userId") Long userId,
      @Param("dateFrom") @Temporal(TemporalType.DATE) Date dateFrom,
      @Param("dateTo") @Temporal(TemporalType.DATE) Date dateTo);
}
