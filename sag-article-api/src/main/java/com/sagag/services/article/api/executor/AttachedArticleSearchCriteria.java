package com.sagag.services.article.api.executor;

import com.sagag.services.article.api.request.AttachedArticleRequest;
import com.sagag.services.common.enums.SupportedAffiliate;
import com.sagag.services.domain.eshop.dto.VehicleDto;
import com.sagag.services.domain.eshop.dto.WssDeliveryProfileDto;
import com.sagag.services.domain.sag.external.Customer;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Optional;

/**
 * Class to provide the articles info request when
 * execute update depot, recycle, voc, vrg articles info from ERP.
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class AttachedArticleSearchCriteria extends ArticleSearchCriteria {

  private static final long serialVersionUID = -6647063943459161192L;

  private List<AttachedArticleRequest> attachedArticleRequestList;

  private boolean callShoppingCart = true;

  private boolean calculateVatPriceRequired;

  private double vatRate;

  public Optional<AttachedArticleRequest> getAttachedArticleRequestById(String id) {
    if (CollectionUtils.isEmpty(attachedArticleRequestList)) {
      return Optional.empty();
    }
    return attachedArticleRequestList.stream()
        .filter(request -> StringUtils.equalsIgnoreCase(id, request.getAttachedArticleId()))
        .findFirst();
  }

  public static AttachedArticleSearchCriteria createcreateArticleRequest(
      SupportedAffiliate affiliate, Customer customer,
      List<AttachedArticleRequest> attachedArticleRequestList, VehicleDto vehicle,
      WssDeliveryProfileDto wssDeliveryProfile) {

    final AttachedArticleSearchCriteria criteria = new AttachedArticleSearchCriteria();
    criteria.setAffiliate(affiliate);
    criteria.setCustNr(customer.getNr());
    criteria.setGrossPrice(true);
    criteria.setSpecialNetPriceArticleGroup(true);
    criteria.setAttachedArticleRequestList(attachedArticleRequestList);
    criteria.setUpdatePrice(true);
    criteria.setCompanyName(affiliate.getCompanyName());
    criteria.setVehicle(vehicle);
    criteria.setWssDeliveryProfile(wssDeliveryProfile);
    return criteria;
  }

  public static AttachedArticleSearchCriteria createArticleRequestForFilter(
      SupportedAffiliate affiliate, String customerNr,
      boolean calculateVatPriceRequired, double vatRate,
      List<AttachedArticleRequest> attachedArticleRequestList) {
    final AttachedArticleSearchCriteria criteria = new AttachedArticleSearchCriteria();
    criteria.setAffiliate(affiliate);
    criteria.setCustNr(customerNr);
    criteria.setCalculateVatPriceRequired(calculateVatPriceRequired);
    criteria.setVatRate(vatRate);
    criteria.setGrossPrice(true);
    criteria.setSpecialNetPriceArticleGroup(true);
    criteria.setAttachedArticleRequestList(attachedArticleRequestList);
    criteria.setUpdatePrice(true);
    criteria.setCompanyName(affiliate.getCompanyName());
    criteria.setCallShoppingCart(false);
    return criteria;
  }

}
