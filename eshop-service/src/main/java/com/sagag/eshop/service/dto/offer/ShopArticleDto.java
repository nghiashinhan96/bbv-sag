package com.sagag.eshop.service.dto.offer;

import com.sagag.eshop.repo.entity.offer.ShopArticle;
import com.sagag.services.common.enums.offer.ShopArticleType;
import com.sagag.services.common.utils.SagBeanUtils;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * DTO object for shop article.
 */
@Data
@NoArgsConstructor
public class ShopArticleDto implements Serializable {

  private static final long serialVersionUID = -2251203997010425747L;

  private Long id;

  private ShopArticleType type;

  private String articleNumber;

  private String name;

  private String description;

  private double amount;

  private double price;

  private String tecstate;

  private int version;

  private Integer organisationId;

  private Long createdUserId;

  private Date createdDate;

  private Long modifiedUserId;

  private Date modifiedDate;

  private CurrencyDto currency;

  public ShopArticleDto(ShopArticle entity) {
    super();
    this.id = entity.getId();
    this.type = ShopArticleType.valueOf(entity.getType());
    this.articleNumber = entity.getArticleNumber();
    this.name = entity.getName();
    this.description = entity.getDescription();
    this.amount = entity.getAmount();
    this.price = entity.getPrice();
    this.tecstate = entity.getTecstate();
    this.version = entity.getVersion();
    this.createdUserId = entity.getCreatedUserId();
    this.createdDate = entity.getCreatedDate();
    this.modifiedUserId = entity.getModifiedUserId();
    this.modifiedDate = entity.getModifiedDate();
    this.organisationId = entity.getOrganisationId();
    if (entity.getCurrency() != null) {
      this.currency = SagBeanUtils.map(entity.getCurrency(), CurrencyDto.class);
    }
  }
}
