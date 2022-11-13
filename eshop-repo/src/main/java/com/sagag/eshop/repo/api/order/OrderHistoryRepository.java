package com.sagag.eshop.repo.api.order;


import com.sagag.eshop.repo.entity.order.OrderHistory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Order history JPA repository interface.
 */
public interface OrderHistoryRepository extends JpaRepository<OrderHistory, Long> {

  /**
   * Updates closed date by order id.
   *
   * @param orderId the order history id
   * @param closedDate the closed date to update
   */
  @Modifying
  @Query(value = "update OrderHistory o set o.closedDate = :closedDate where o.id=:orderId")
  @Transactional
  void updateClosedDateByOrderId(@Param("orderId") long orderId,
      @Param("closedDate") Date closedDate);

  /**
   * Gets the latest order state for specified user.
   *
   * @param userId the user id
   * @return the latest order state for specified user
   */
  @Query(
      value = "SELECT TOP(1) ORDER_STATE FROM ORDER_HISTORY WHERE USER_ID = :userId ORDER BY ID DESC",
      nativeQuery = true
      )
  String findLatestOrderStateByUserId(@Param("userId") Long userId);

  /**
   * Returns the available order numbers of user id.
   *
   * @param orderNumbers the order number list to search
   * @param userId the user id
   * @return the list of available order numbers
   */
  @Query("select o.orderNumber from OrderHistory o "
      + "where o.orderNumber IN :orderNumbers "
      + "and o.orderNumber is not null and o.userId = :userId")
  List<String> findAvailableOrderNrs(@Param("orderNumbers") List<String> orderNumbers,
      @Param("userId") Long userId);

  /**
   * Returns the order history by order number.
   *
   * @param orderNumber the order number selected
   * @return the optional of {@link OrderHistory}
   */
  @Query("select o from OrderHistory o where o.orderNumber = :orderNumber")
  Optional<OrderHistory> findByOrderNumber(@Param("orderNumber") String orderNumber);

  /**
   * Searches order histories by userIds.
   *
   * @param userIds the user id to search
   * @return The matched list of OrderHistory
   */
  @Query("select o from OrderHistory o where o.userId IN :userIds")
  List<OrderHistory> findByUserIds(@Param("userIds") List<Long> userIds);
}
