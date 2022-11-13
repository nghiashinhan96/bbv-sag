package com.sagag.services.hazelcast.domain.cart;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sagag.services.common.contants.SagConstants;
import com.sagag.services.domain.article.ArticleDocDto;

import lombok.AllArgsConstructor;
import lombok.Data;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.data.annotation.Transient;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Data
@AllArgsConstructor
public class ShoppingCart implements Serializable {
  private static final long serialVersionUID = 1258220781365475673L;

  private List<ShoppingCartItem> items;
  private int numberOfItems;
  private double vatValue;
  private double totalWithDiscount;
  private double newTotal;
  private double discount;
  private String couponCode;

  private Integer finalCustomerOrgId;

  @JsonIgnore
  private boolean finalCustomerNetPriceRequired;

  @Transient
  private List<String> notFoundPartNumbers;

  public ShoppingCart() {
    items = new ArrayList<>();
    numberOfItems = 0;
    vatValue = 0;
    totalWithDiscount = 0;
    newTotal = 0;
    discount = 0;
    couponCode = StringUtils.EMPTY;
  }

  public ShoppingCart(final List<ShoppingCartItem> items) {
    this();
    this.items.addAll(items);
    this.sumOfQuantities();

    // Update final customer org id from first cart item.
    CollectionUtils.emptyIfNull(items).stream().findFirst()
    .map(ShoppingCartItem::getFinalCustomerOrgId).ifPresent(this::setFinalCustomerOrgId);
  }

  /**
   * Updates the <code>CartItem</code> of the specified <code>article</code> to the specified
   * quantity. If '<code>0</code>' is the given quantity, the <code>CartItem</code> is removed from
   * the <code>ShoppingCart</code>'s <code>items</code> list.
   *
   * @param article the <code>Article</code> that defines the type of shopping cart item
   * @param quantity the number which the <code>CartItem</code> is updated to
   * @see ShoppingCartItem
   */
  public synchronized void update(ArticleDocDto article, int quantity) {
    for (ShoppingCartItem scItem : items) {
      if (scItem.getArticleItem().getId().equalsIgnoreCase(article.getId())) {
        if (quantity != 0) {
          // set item quantity to new value
          scItem.setQuantity(quantity);
        } else {
          // if quantity equals 0, save item and break
          items.remove(scItem);
          break;
        }
      }
    }
  }

  /**
   * Remove the selected <code>CartItem</code> of the specified <code>article id</code>.
   *
   * @param idsSagSys the <code>idsSagSys</code> that defines the article id
   * @see ShoppingCartItem
   */
  public synchronized int remove(Set<String> idsSagSys) {
    int totalRemovedItems = 0;
    Iterator<ShoppingCartItem> it = items.iterator();
    while (it.hasNext()) {
      ShoppingCartItem scItem = it.next();
      for (String artNumber : idsSagSys) {
        if (artNumber.equalsIgnoreCase(scItem.getArticleItem().getIdSagsys())) {
          it.remove();
          totalRemovedItems++;
        }
      }
    }
    return totalRemovedItems;
  }

  /**
   * Returns the list of <code>CartItems</code>.
   *
   * @return the <code>items</code> list
   * @see ShoppingCartItem
   */
  public List<ShoppingCartItem> getItems() {
    return items;
  }

  /**
   * Returns the sum of quantities for all items maintained in shopping cart <code>items</code>
   * list.
   *
   * @return the number of items in shopping cart
   * @see ShoppingCartItem
   */
  // it will be the quantitySum. it will be miss leading others
  // and it kill performance if hit so many times with this loop
  // and lamda should be used for clean code please.
  // Just comment here since I got urgent state to spend time to do
  // the test as i spend time to refactor it
  public int sumOfQuantities() {
    numberOfItems = 0;
    for (ShoppingCartItem scItem : items) {
      numberOfItems += scItem.getQuantity();
    }
    return numberOfItems;
  }

  /**
   * Returns sub total cost of the order for the given <code>ShoppingCart</code> instance without
   * net and without vat.
   *
   * @return sub total
   */
  public double getSubTotal() {
    return items.parallelStream()
        .mapToDouble(item -> item.getTotalGrossPrice() + item.getAttachedArticleTotalGrossPrice()).sum();
  }

  /**
   * Returns sub total cost of the order for the given <code>ShoppingCart</code> instance with net
   * and vat.
   *
   * @return sub total
   */
  public double getSubTotalWithNetAndVat() {
    return items.parallelStream().mapToDouble(
        item -> item.getTotalNetPriceWithVat() + item.getAttachedArticleTotalNetPriceInclVat()).sum();
  }

  /**
   * Returns sub total cost of the order for the given <code>ShoppingCart</code> instance with net.
   *
   * @return sub total
   */
  public double getSubTotalWithNet() {
    return items.parallelStream()
        .mapToDouble(item -> item.getTotalNetPrice() + item.getAttachedArticleTotalNetPrice()).sum();
  }

  /**
   * Returns sub total cost of the order for the given <code>ShoppingCart</code> instance
   * with net 1
   *
   * @return sub total net 1
   */
  public double getSubTotalWithNet1Price() {
    return items.parallelStream()
        .mapToDouble(item -> item.getTotalNet1Price() + item.getAttachedArticleTotalNet1Price()).sum();
  }

  /**
   * Returns sub total cost of the order for the given <code>ShoppingCart</code> instance with vat.
   *
   * @return sub total
   */
  public double getSubTotalWithVat() {
    return items.parallelStream()
        .mapToDouble(
            item -> item.getTotalGrossPriceWithVat() + item.getAttachedArticleTotalGrossPriceInclVat())
        .sum();
  }

  /**
   * Returns the total gross price excluding VAT of the order for the given
   * <code>ShoppingCart</code> instance. Data for 'Total Price ohne MwSt' field
   *
   * @return gross total price excluding VAT
   */
  public synchronized double getGrossTotalExclVat() {
    return getSubTotal() + discount;
  }

  /**
   * Returns the total net price excluding VAT of the order for the given <code>ShoppingCart</code>
   * instance. Data for 'Total Price ohne MwSt' field
   *
   * @return net total price excluding VAT
   */
  public double getNetTotalExclVat() {
    return getSubTotalWithNet() + discount;
  }

  /**
   * Returns the total net 1 price excluding VAT of the order for the given <code>ShoppingCart</code>
   * instance. Data for 'Total Price ohne MwSt' field
   *
   * @return total net 1 price excluding VAT
   */
  public double getTotalNet1Price() {
    return getSubTotalWithNet1Price();
  }

  /**
   * Get Vat total cost of the order for the given <code>ShoppingCart</code> instance.
   *
   * @return vat total
   */
  public synchronized double getVatTotal() {
    return items
        .parallelStream().mapToDouble(item -> item.getPriceItem().getVatInPercent()
            * SagConstants.PERCENT * item.getTotalGrossPrice() + item.getAttachedArticleVatTotalOnGross())
        .sum();
  }

  /**
   * Return vat total cost of the order for the given <code>ShoppingCart</code> instance with net.
   *
   * @return vat total
   */
  public double getVatTotalWithNet() {
    return items
        .parallelStream().mapToDouble(item -> item.getPriceItem().getVatInPercent()
            * SagConstants.PERCENT * item.getTotalNetPrice() + item.getAttachedArticleVatTotalOnNet())
        .sum();
  }

  /**
   * Return vat total cost of the order for the given <code>ShoppingCart</code> instance with net 1.
   *
   * @return vat total of net 1
   */
  public Double getVatTotalWithNet1Price() {
    return items.parallelStream()
        .filter(item -> Objects.nonNull(item.getTotalNet1Price()))
        .mapToDouble(item -> item.getPriceItem().getVatInPercent() * SagConstants.PERCENT
            * item.getTotalNet1Price() + item.getAttachedArticleVatTotalOnNet1())
        .sum();
  }

  /**
   * Returns the total cost of the order for the given <code>ShoppingCart</code> instance with VAT.
   *
   * @return the total cost
   */
  public double getNewTotalWithVat() {
    // Always include vat
    return getSubTotalWithVat() + discount;
  }

  /**
   * Returns the total cost of the order for the given <code>ShoppingCart</code> instance with NET
   * and VAT.
   *
   * @return the total cost
   */
  public double getNewTotalWithNetAndVat() {
    // Always include vat
    return getSubTotalWithNetAndVat() + discount;
  }

  /**
   * Returns the total VAT of the order for the given <code>ShoppingCart</code> instance.
   *
   * @return VAT value
   */
  public double getVatValue() {
    return vatValue;
  }


  /**
   * Empties the shopping cart. All items are removed from the shopping cart <code>items</code>
   * list, <code>numberOfItems</code> and <code>total</code> are reset to '<code>0</code>'.
   *
   * @see ShoppingCartItem
   */
  public void clear() {
    items.clear();
    numberOfItems = 0;
    newTotal = 0;
  }

  public int getNumberOfOrderPos() {
    if (CollectionUtils.isEmpty(items)) {
      return NumberUtils.INTEGER_ZERO;
    }
    return (int) items.stream()
        .filter(item -> !CartItemType.DVSE_NON_REF_ARTICLE.equals(item.getItemType())).count();
  }

  /**
   * Returns total final customer net price of the order for the given <code>ShoppingCart</code> instance.
   *
   * @return total final customer net price
   */
  public double getTotalFinalCustomerNetPrice() {
    return items.parallelStream().mapToDouble(item -> item.getTotalFinalCustomerNetPrice()
        + item.getAttachedArticleTotalFinalCustomerNetPrice()).sum();
  }

  /**
   * Return vat total cost of the order for the given <code>ShoppingCart</code> instance with final customer net.
   *
   * @return vat total of final customer net
   */
  public double getVatTotalWithFinalCustomerNet() {
    return items.parallelStream()
        .mapToDouble(item -> item.getPriceItem().getVatInPercent() * SagConstants.PERCENT
            * (item.getTotalFinalCustomerNetPrice()
            + item.getAttachedArticleTotalFinalCustomerNetPrice()))
        .sum();
  }

  /**
   * Returns sub total cost of the order for the given <code>ShoppingCart</code> instance with final
   * customer net and vat.
   *
   * @return sub total of final customer net and vat
   */
  public double getSubTotalWithFinalCustomerNetAndVat() {
    return getVatTotalWithFinalCustomerNet() + getTotalFinalCustomerNetPrice();
  }

  /**
   * Returns the total cost of the order for the given <code>ShoppingCart</code> instance with final customer NET
   * and VAT.
   *
   * @return the total cost with final customer net
   */
  public double getNewTotalWithFinalCustomerNetAndVat() {
    // Always include vat
    return getSubTotalWithFinalCustomerNetAndVat() + discount;
  }

  /**
   * Returns the total final customer net price excluding VAT of the order for the given <code>ShoppingCart</code>
   * instance. Data for 'Total Price ohne MwSt' field
   *
   * @return final customer net total price excluding VAT
   */
  public double getFinalCustomerNetTotalExclVat() {
    return getTotalFinalCustomerNetPrice() + discount;
  }
}
