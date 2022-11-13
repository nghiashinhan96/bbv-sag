package com.sagag.eshop.service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.sagag.eshop.repo.entity.BasketHistory;
import com.sagag.eshop.repo.entity.VBasketHistory;
import com.sagag.services.common.contants.SagConstants;
import com.sagag.services.common.utils.SagJSONUtil;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
  "id", "basketName", "customerNumber", "customerName",
  "createdUserId", "createdLastName", "createdFirstName",
  "salesUserId", "salesLastName", "salesFirstName",
  "updatedDate", "grandTotalExcludeVat", "items",
  "salesFullName", "createdFullName" })
public class BasketHistoryDto implements Serializable {

  private static final long serialVersionUID = 6337272098365425102L;

  private Long id;

  private String basketName;

  private String customerNumber;

  private String customerName;

  private Long createdUserId;

  private String createdLastName;

  private String createdFirstName;

  private Long salesUserId;

  private String salesLastName;

  private String salesFirstName;

  @JsonFormat(shape = JsonFormat.Shape.NUMBER)
  private Date updatedDate;

  private Double grandTotalExcludeVat;

  private List<BasketHistoryItemDto> items;

  private String customerRefText;

  public BasketHistoryDto(VBasketHistory basketHistory, boolean ignoreBasketItem) {

    this.id = basketHistory.getBasketHistoryId();
    this.basketName = basketHistory.getBasketName();
    this.updatedDate = basketHistory.getUpdatedDate();
    this.grandTotalExcludeVat = basketHistory.getGrandTotalExcludeVat();
    this.customerNumber = basketHistory.getOrgCode();
    this.customerName = basketHistory.getCustomerName();

    this.createdUserId = basketHistory.getCreatedUserId();
    this.createdLastName = basketHistory.getCreatedLastName();
    this.createdFirstName = basketHistory.getCreatedFirstName();

    this.salesUserId = basketHistory.getSalesUserId();
    this.salesLastName = basketHistory.getSalesLastName();
    this.salesFirstName = basketHistory.getSalesFirstName();

    if (!ignoreBasketItem && StringUtils.isNotBlank(basketHistory.getBasketJson())) {
      this.items = SagJSONUtil.convertArrayJsonToList(basketHistory.getBasketJson(),
          BasketHistoryItemDto.class);
    }

    this.customerRefText = basketHistory.getCustomerRefText();
  }

  public String getSalesFullName() {
    return StringUtils.trim(StringUtils.defaultString(salesFirstName) + StringUtils.SPACE
        + StringUtils.defaultString(salesLastName));
  }

  public String getCreatedFullName() {
    return StringUtils.trim(StringUtils.defaultString(createdFirstName) + StringUtils.SPACE
        + StringUtils.defaultString(createdLastName));
  }

  public BasketHistory toEntity(Integer orgId, boolean isActive) {

    BasketHistory.BasketHistoryBuilder builder =
        BasketHistory.builder()
        .organisationId(orgId)
        .createdUserId(createdUserId)
        .basketJson(SagJSONUtil.convertObjectToJson(this.items))
        .grandTotalExcludeVat(this.grandTotalExcludeVat)
        .active(isActive)
        .customerRefText(customerRefText);

    if (StringUtils.isNotBlank(this.basketName)) {
      builder.basketName(this.basketName);
    } else {
      builder.basketName(generateBasketName(createdUserId));
    }

    if (Objects.nonNull(salesUserId)) { // Optional
      builder.salesUserId(salesUserId);
    }
    builder.updatedDate(DateTime.now().toDate());

    return builder.build();
  }

  /**
   * <p>
   * The logic to generate the default basket name if no input from end-user
   * </p>
   *
   * <pre>
   * User_id + timestamp is perfect.
   * </pre>
   * @param userId the index of user
   * @return the basket name result
   * @see <a href = "https://app.assembla.com/spaces/sag-eshop/tickets/1462/details?comment=1252759893">#1462</a>
   *
   */
  private static String generateBasketName(Long userId) {
    return userId + SagConstants.UNDERSCORE +
        DateTime.now(DateTimeZone.UTC).getMillis();
  }

}
