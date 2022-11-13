package com.sagag.services.hazelcast.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hazelcast.core.PartitionAware;
import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.DataSerializable;
import com.sagag.eshop.repo.hz.entity.ShopType;
import com.sagag.services.common.contants.SagConstants;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.domain.eshop.dto.VehicleDto;
import com.sagag.services.hazelcast.domain.cart.CartItemType;
import com.sagag.services.hazelcast.domain.cart.CategoryDto;

import lombok.Data;
import lombok.NoArgsConstructor;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
public class CartItemDto implements Serializable, DataSerializable, PartitionAware<String> {

  private static final long serialVersionUID = 9032883719982365195L;

  private String cartKey;

  private String userId;

  private String userName;

  private String customerNr;

  private int quantity;

  private ArticleDocDto article;

  private CategoryDto category;

  private VehicleDto vehicle;

  private Date addedTime;

  private String itemDesc;

  private CartItemType itemType;

  private List<ArticleDocDto> attachedArticles;

  private ShopType shopType;

  private Integer finalCustomerOrgId;

  private String basketItemSourceId;

  private String basketItemSourceDesc;

  @JsonIgnore
  public String userKey() {
    return StringUtils.join(Arrays.asList(this.userId, this.customerNr), SagConstants.UNDERSCORE);
  }

  @Override
  public void writeData(ObjectDataOutput out) throws IOException {
    out.writeUTF(cartKey);
    out.writeUTF(userId);
    out.writeUTF(userName);
    out.writeUTF(customerNr);
    out.writeInt(quantity);
    out.writeObject(article);
    out.writeObject(addedTime);
    out.writeObject(category);
    out.writeObject(vehicle);
    out.writeObject(itemType);
    out.writeUTF(itemDesc);
    out.writeObject(attachedArticles);
    out.writeObject(shopType);
    out.writeObject(finalCustomerOrgId);
    out.writeUTF(basketItemSourceId);
    out.writeUTF(basketItemSourceDesc);
  }

  @Override
  public void readData(ObjectDataInput in) throws IOException {
    cartKey = in.readUTF();
    userId = in.readUTF();
    userName = in.readUTF();
    customerNr = in.readUTF();
    quantity = in.readInt();
    article = in.readObject();
    addedTime = in.readObject();
    category = in.readObject();
    vehicle = in.readObject();
    itemType = in.readObject();
    itemDesc = in.readUTF();
    attachedArticles = in.readObject();
    shopType = in.readObject();
    finalCustomerOrgId = in.readObject();
    basketItemSourceId = in.readUTF();
    basketItemSourceDesc = in.readUTF();
  }

  @Override
  public String getPartitionKey() {
    return this.cartKey;
  }
}
