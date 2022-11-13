package com.sagag.services.hazelcast.repo.api;

import com.sagag.eshop.repo.hz.entity.EshopCartItem;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

/**
 * Shopping cart item interface repository to define the basic APIs on EshopCartItem.
 */
public interface ShoppingCartItemRepository extends JpaRepository<EshopCartItem, Long> {

  /**
   * Returns a shopping cart item from the key.
   *
   * @param cartKey the key for the added item
   * @return the cart item in the basket
   */
  @Query("SELECT c FROM EshopCartItem c WHERE c.cartKey =:cartKey")
  EshopCartItem findOneByCartKey(@Param("cartKey") String cartKey);

  /**
   * Returns all cart item keys.
   *
   * @return all cart item keys
   */
  @Query("SELECT cartKey FROM EshopCartItem")
  List<String> findAllCartKeys();

  /**
   * Returns all items having its keys in the <code>cartKeys</code>.
   *
   * @param cartKeys all cart item keys
   * @return all cart items
   */
  @Query("SELECT c FROM EshopCartItem c WHERE c.cartKey IN :cartKeys")
  List<EshopCartItem> findItemsInCartKeys(@Param("cartKeys") final Collection<String> cartKeys);

  /**
   * Returns a shopping cart item id from the key.
   *
   * @param cartKey the key for the added item
   * @return the cart item id in the basket
   */
  @Query("SELECT id FROM EshopCartItem WHERE cartKey =:cartKey")
  Long findIdByCartKey(@Param("cartKey") String cartKey);

  List<EshopCartItem> findByUserIdInAndCustomerNrIn(List<String> userIds, List<String> customerNrs);

  @Query("SELECT eci FROM EshopCartItem eci WHERE eci.userId IN :userIds")
  List<EshopCartItem> findByUserIds(@Param("userIds")  Collection<String> userIds);

  @Query("SELECT cartKey FROM EshopCartItem WHERE userId =:userId AND customerNr =:customerNr AND shopType =:shopType")
  List<String> findCartKeyByUserIdAndCustomerNrAndShopType(@Param("userId") String userId,
      @Param("customerNr") String customerNr, @Param("shopType") String shopType);

}
