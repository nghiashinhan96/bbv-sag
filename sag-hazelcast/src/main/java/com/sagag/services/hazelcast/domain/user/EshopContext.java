package com.sagag.services.hazelcast.domain.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sagag.services.common.enums.ErpSendMethodEnum;
import com.sagag.services.common.enums.order.OrderType;
import com.sagag.services.common.utils.SagJSONUtil;
import com.sagag.services.domain.article.ArticleFilteringResponseDto;
import com.sagag.services.domain.eshop.dto.DeliveryTypeDto;
import com.sagag.services.domain.eshop.dto.EshopBasketDto;
import com.sagag.services.domain.eshop.dto.UserPriceContextDto;
import com.sagag.services.domain.eshop.dto.VehicleDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.ObjectUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Data
@Builder
@AllArgsConstructor
@Slf4j
public class EshopContext implements Serializable {

  private static final long serialVersionUID = -3988605039427561581L;
  private static final String SEARCH_BY_VEHICLE = "SEARCH_BY_VEHICLE";
  private static final String SEARCH_BY_ARTICLE_NUMBER = "SEARCH_BY_ARTICLE_NUMBER";

  private String siteArea;
  private String section;
  private String component;
  private String searchKey;
  private String searchValue;
  private List<VehicleDto> selectedVehicleDocs;
  private EshopBasketContext eshopBasketContext;

  private UserPriceContext userPriceContext;

  // Keep tyre search results per user login
  @JsonIgnore
  private ArticleFilteringResponseDto cachedArticlesResult;

  /**
   * Default constructor.
   */
  public EshopContext() {
    this.siteArea = "MAIN_SEARCH_AREA";
    this.section = "PARTS";
    this.component = "PART_LIST_SEARCH_KEY";
    this.eshopBasketContext = new EshopBasketContext();
  }

  /**
   * Updates article number to search context info.
   */
  public void updateArticleNumber(final String articleNumber) {
    this.searchKey = SEARCH_BY_ARTICLE_NUMBER;
    this.searchValue = articleNumber;
    this.selectedVehicleDocs = new ArrayList<>();
  }

  /**
   * Update selected vehicle doc to search context info.
   *
   */
  public void updateSelectedVehicleDocs(final VehicleDto vehicleDoc) {
    this.selectedVehicleDocs = (vehicleDoc == null ? new ArrayList<>() : Arrays.asList(vehicleDoc));
    this.searchKey = SEARCH_BY_VEHICLE;
    this.searchValue = (vehicleDoc == null ? null : vehicleDoc.getVehId());
  }

  public void updateBasketContext(final EshopBasketDto eshopBasketDto) {
    // Avoid NPE
    if (Objects.isNull(eshopBasketDto)) {
      return;
    }
    if (!ObjectUtils.isEmpty(eshopBasketDto.getAllocation())) {
      this.eshopBasketContext.setAllocation(eshopBasketDto.getAllocation());
    }
    if (!ObjectUtils.isEmpty(eshopBasketDto.getInvoiceType())) {
      this.eshopBasketContext.setInvoiceType(eshopBasketDto.getInvoiceType());
    }
    if (!ObjectUtils.isEmpty(eshopBasketDto.getPaymentMethod())) {
      this.eshopBasketContext.setPaymentMethod(eshopBasketDto.getPaymentMethod());
    }
    if (!ObjectUtils.isEmpty(eshopBasketDto.getDeliveryType())) {
      this.eshopBasketContext.setDeliveryType(eshopBasketDto.getDeliveryType());
    }
    if (!ObjectUtils.isEmpty(eshopBasketDto.getCollectionDelivery())) {
      this.eshopBasketContext.setCollectionDelivery(eshopBasketDto.getCollectionDelivery());
    }
    if (!ObjectUtils.isEmpty(eshopBasketDto.getPickupBranch())) {
      this.eshopBasketContext.setPickupBranch(eshopBasketDto.getPickupBranch());
    }
    log.debug("EshopBasketContext: BillingAddress {}", eshopBasketDto.getBillingAddress());
    if (!ObjectUtils.isEmpty(eshopBasketDto.getBillingAddress())) {
      this.eshopBasketContext.setBillingAddress(eshopBasketDto.getBillingAddress());
    }
    log.debug("EshopBasketContext: DeliveryAddress {}", eshopBasketDto.getDeliveryAddress());
    if (!ObjectUtils.isEmpty(eshopBasketDto.getDeliveryAddress())) {
      this.eshopBasketContext.setDeliveryAddress(eshopBasketDto.getDeliveryAddress());
    }
    if (!ObjectUtils.isEmpty(eshopBasketDto.getOrderType())) {
      this.eshopBasketContext
          .setOrderType(OrderType.getNullSafeType(eshopBasketDto.getOrderType()));
    }
    if (!ObjectUtils.isEmpty(eshopBasketDto.getLocation())) {
      this.eshopBasketContext.setLocation(eshopBasketDto.getLocation());
    }
    if (!ObjectUtils.isEmpty(eshopBasketDto.getCourierService())) {
      this.eshopBasketContext.setCourierService(eshopBasketDto.getCourierService());
    }
    if (!ObjectUtils.isEmpty(eshopBasketDto.getMessageToBranch())) {
      this.eshopBasketContext.setMessageToBranch(eshopBasketDto.getMessageToBranch());
    }
    if (!ObjectUtils.isEmpty(eshopBasketDto.getReferenceTextByLocation())) {
      this.eshopBasketContext
          .setReferenceTextByLocation(eshopBasketDto.getReferenceTextByLocation());
    }
    if (!ObjectUtils.isEmpty(eshopBasketDto.getPickupLocation())) {
      this.eshopBasketContext.setPickupLocation(eshopBasketDto.getPickupLocation());
    }
    this.eshopBasketContext.setShowKSLMode(eshopBasketDto.getShowKSLMode());
    if (CollectionUtils.isNotEmpty(eshopBasketDto.getTourTimes())) {
      this.eshopBasketContext.setTourTimes(eshopBasketDto.getTourTimes());
    }

    log.debug("EshopContext: Updates Eshop Basket Context complete {}",
        SagJSONUtil.convertObjectToPrettyJson(eshopBasketContext));
  }

  public boolean hasPickupBranchId() {
    return Objects.nonNull(eshopBasketContext)
        && Objects.nonNull(eshopBasketContext.getPickupBranch())
        && StringUtils.isNotBlank(eshopBasketContext.getPickupBranch().getBranchId());
  }

  public void updateUserPriceContext(UserPriceContextDto userPriceContextDto) {
    if (userPriceContextDto == null) {
      return;
    }

    if (userPriceContext == null) {
      userPriceContext = new UserPriceContext();
    }
    userPriceContext.setCurrentStateNetPriceView(userPriceContextDto.isCurrentStateNetPriceView());
  }

  public String getInvoiceTypeDescCode() {
    if (eshopBasketContext == null || eshopBasketContext.getInvoiceType() == null) {
      return StringUtils.EMPTY;
    }
    return StringUtils.defaultIfBlank(eshopBasketContext.getInvoiceType().getDescCode(),
        StringUtils.EMPTY);
  }

  public String getPickupBranchId() {
    if (!hasPickupBranchId()) {
      return StringUtils.EMPTY;
    }
    return eshopBasketContext.getPickupBranch().getBranchId();
  }

  /**
   * Returns the delivery type value.
   *
   * @return the delivery type.
   */
  @JsonIgnore
  public ErpSendMethodEnum getDeliveryType() {
    final DeliveryTypeDto deliveryType = this.getEshopBasketContext().getDeliveryType();
    if (Objects.isNull(deliveryType)) {
      throw new IllegalArgumentException("Delivery type must not be null.");
    }
    return ErpSendMethodEnum.valueOf(deliveryType.getDescCode());
  }

}
